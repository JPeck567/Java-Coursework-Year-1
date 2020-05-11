package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PList;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.WaitingQueueComparator;
import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

/**
 * 
 * @author Team 46
 * @author 190148289 Jennifer A. Appiah
 * @author 190095097 Hannah Elliman
 * @author 190055002 Jorge Peck
 * @author 190174923 Hongyi Wang
 * @version 1.0
 * @since 2020 Coursework
 * 
 * @summary This Class provides an interface for all the other classes so that
 *          they can communicate with each other.
 */
public class Simulator {
	/**
	 * Declaring fields
	 */
	private static final int SIMTIME = 2880; // 8hrs = 2880 ticks (as each tick is 10s)
	private final PersonHandler peopleHandler;
	private final Building building;
	private int tick;
	public static final int DEFAULTFLOOR = 0;
	public final int noFloors; // are 7 floors. represented as 0 to 6 in the floor array of building
	public final int maxCapacity;

	/**
	 * This method is the constructor that initialises the simulator
	 * 
	 * @param noFloors    this provides the number of floors created
	 * @param maxCapacity this is the maximum hold the elevator can take
	 * @param empNo       this is the number of employees to be generated
	 * @param devNo       this is the number of developers to be generated
	 * @param seed        this generate the same random number using the same random
	 *                    patterns when put in the constructor paramenter of the
	 *                    random object
	 * @param p           this is used to provide the probabilities of an employee
	 *                    or developers switching floors
	 * @param q           this is used to provide the probabilities of the client
	 *                    arriving
	 */
	public Simulator(int noFloors, int maxCapacity, int empNo, int devNo, int seed, double p, double q) {
		this.noFloors = noFloors;
		this.maxCapacity = maxCapacity;
		this.building = new Building(generateElevator(), noFloors, new WaitingQueueComparator(this));
		this.peopleHandler = new PersonHandler(empNo, devNo, seed, this, p, q);
	}

	/**
	 * This method runs the simulation which is called by the launcher
	 * 
	 * @param interfacer this provides the text interface
	 * @throws InterruptedException This method throws and interrupted exception if
	 *                              the program is interrupted
	 */
	public void run(Interfacer interfacer) throws InterruptedException {
		while (tick < SIMTIME) {
			tick();
			// Thread.sleep(250); // 1000ms = 1 second. Therefore in real life, each tick
			// execution is ~ 1 second
			// ( 1s + code execution time between loops)
			interfacer.printSimulation(this, getTick(), getFloorsWaitingQueue(), getFloorsOnFloorList(),
					getElevatorQueue().stream().map(pID -> getPerson(pID).toString()).collect(Collectors.toList()),
					getElevatorState(), getElevatorCurrentFloor(), peopleHandler.getComplaints());
		}
		System.out.println(peopleHandler.getAvgWaitingTime().toString());
	}

	/**
	 * This method is for propagating the program
	 */
	private void tick() {
		tick += 1;

		building.tick(this);
		peopleHandler.tick(this);
	}

	/**
	 * This method is used to creates the elevator
	 * 
	 * @return this returns a new elevator object
	 */
	private Elevator generateElevator() {
		return new Elevator(new PList(maxCapacity), noFloors);
	}

	/**
	 * This method gets the new time stamp
	 * 
	 * @return this returns the time stamp that has been stored
	 */
	public static long getNewTimeStamp() {
		return System.nanoTime();
	}

	/**
	 * This method gets the person from the person handler
	 * 
	 * @param pID this is the value that identifies the person
	 * @return this return the person that has been stored
	 */
	public Person getPerson(int pID) {
		return peopleHandler.getPerson(pID);
	}

	/**
	 * This method gets the tick
	 * 
	 * @return this method returns the tick that has been stored
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * This method adds a person to a floor after arrival or departure of elevator
	 * 
	 * @param pID     this is used to identify the person
	 * @param floorNo this is used to provide the floor the person is moving onto
	 */
	public void addToOnFloor(int pID, int floorNo) {
		building.getFloor(floorNo).addToFloor(pID);
	}

	/**
	 * This method removes a person from a floor after departure from elevator or
	 * building
	 * 
	 * @param pID          this is used to identify the person
	 * @param currentFloor this is used to provide the floor the person is leaving
	 */
	public void removeFromFloor(int currentFloor, int pID) {
		building.getFloor(currentFloor).removeFromOnFloor(pID);
	}

	/**
	 * This method removes a person from the waiting queue
	 * 
	 * @param pID          this is used to identify the person
	 * @param currentFloor this is used to provide the floor the person is currently
	 *                     on
	 */
	public void removeFromWaiting(int currentFloor, int pID) {
		building.getFloor(currentFloor).removeFromWaiting(pID);
	}

	/**
	 * This method gets the floor number
	 * 
	 * @return this method returns the floor number that has been stored
	 */
	public int getNoFloors() {
		return noFloors;
	}

	/**
	 * This method gets the floor from the building
	 * 
	 * @param currentFloor this is to identify the current floor
	 * @return this method returns the floor that has been stored
	 */
	public Floor getFloor(int currentFloor) {
		return building.getFloor(currentFloor);
	}

	/**
	 * This method gets the list of elevator queue
	 * 
	 * @return this returns the elevator queue that has been stored in the building
	 */
	public List<Integer> getElevatorQueue() {
		return building.getElevatorQueue();
	}

	/**
	 * This method gets all waiting queues and puts in list
	 * 
	 * @return this return a list of the waiting queue that has been stored
	 */
	public List<Queue<Integer>> getFloorsWaitingQueue() {
		List<Queue<Integer>> queueList = new ArrayList<Queue<Integer>>();
		for (int i = 0; i < noFloors; i++) {
			queueList.add(getFloor(i).getWaitingQueue());
		}
		return queueList;
	}

	/**
	 * This method gets all the list of people on the floor for each floor and puts
	 * in list
	 * 
	 * @return this return a list of every person on every floor
	 */

	public List<List<Integer>> getFloorsOnFloorList() {
		List<List<Integer>> listList = new ArrayList<List<Integer>>();
		for (int i = 0; i < noFloors; i++) {
			listList.add(getFloor(i).getOnFloorList());
		}
		return listList;
	}

	/**
	 * This method gets the elevators current floor
	 * 
	 * @return this method returns the current floor that has been stored that the
	 *         elevator is on
	 * 
	 */
	public int getElevatorCurrentFloor() {
		return building.getElevatorCurrentFloor();
	}

	/**
	 * This method gets the elevator state
	 * 
	 * @return this returns a string of the elevator's door state
	 */
	public String getElevatorState() {
		return building.getElevatorState();
	}
}