package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.Thread;
import java.time.Instant;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

/**
 * 
 * @author Team 46
 *
 */
public class Simulator {

	private int tick;
	private static final int SIMTIME = 2880; // 8hrs = 2880 ticks (as each tick is 10s)
	private final PersonHandler peopleHandler;
	private final Building building;
	public final int noFloors; // are 7 floors. represented as 0 to 6 in the floor array of building
	public final int maxCapacity;
	public final static int DEFAULTFLOOR = 0;

	/**
	 * Constructor that creates a simulator
	 * @param this 
	 * 
	 * @param elevator The elevator within the building
	 */
	public Simulator(int noFloors, int maxCapacity, int empNo, int devNo, int seed, double p, double q) {
		this.noFloors = noFloors;
		this.maxCapacity = maxCapacity;
		this.building = new Building(generateElevator(), noFloors, this);
		this.peopleHandler = new PersonHandler(empNo, devNo, seed, this, p, q);
	}

	public void run(Interfacer interfacer) throws InterruptedException { // called by launcher
		while (tick < SIMTIME) {
			tick();
			Thread.sleep(250); // 1000ms = 1 second. Therefore in real life, each tick execution is ~ 1 second
								// ( 1s + code execution time between loops)
			interfacer.printSimulation(this, getTick(), getFloors(), 
					getElevatorQueue().stream().map(pID -> getPerson(pID).toString()).collect(Collectors.toList()), 
					getElevatorState(), getElevatorCurrentFloor());
		}
	}

	/**
	 * Is the clock that propagates the program
	 */
	private void tick() {
		tick += 1;
		
		building.tick(this);
		peopleHandler.tick(this);
	}

	/**
	 * Creates the elevator
	 */
	private Elevator generateElevator() {
		return new Elevator(new PQueue(maxCapacity), noFloors);
	}

	public Person getPerson(int pID) {
		return peopleHandler.getPerson(pID);
	}
	
	public int getTick() {
		return tick;
	}
	
	public void addToOnFloor(int pID, int floorNo) {
		building.getFloor(floorNo).addToFloor(pID);
	}
	
	public int getNoFloors() {
		return noFloors;
	}

	public Floor getFloor(int currentFloor) {
		return building.getFloor(currentFloor);
	}

	public List<Integer> getElevatorQueue() {
		return building.getElevatorQueue();
	}

	public Floor[] getFloors() {
		Floor[] f = new Floor[noFloors];
		for(int i = 0; i < noFloors; i++) {
			f[i] = getFloor(i);
		}
		return f;
	}

	public void removeFromFloor(int currentFloor, int pID) {
		building.getFloor(currentFloor).removeFromOnFloor(pID);
	}
	
	public void removeFromWaiting(int currentFloor, int pID) {
		building.getFloor(currentFloor).removeFromWaiting(pID);
	}

	public int getElevatorCurrentFloor() {
		return building.getElevatorCurrentFloor();
	}

	public String getElevatorState() {
		return building.getElevatorState();
	}
	
	public static long getNewTimeStamp() {
		return System.nanoTime();
	}
}