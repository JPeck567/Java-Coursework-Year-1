package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import java.util.ArrayList;
import java.util.List;
import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

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
 * @summary This Class provides all the attributes for a person and methods to
 *          work with the data
 */
public class Person {
	/**
	 * Declaring Fields
	 */
	protected final int id;
	protected final int destBoundL;
	protected final int destBoundR;
	protected int destination;
	protected int currentFloor;
	protected boolean isWaiting;
	private final int weight;
	private long timeStamp; // used for comparator to decide who is goes in front in queue. measured in nano
							// seconds
	private int waitingTick; // tick when person started waiting
	private List<Integer> waitTimeList; // in ticks

	/**
	 * This initialises all the fields and also calls the constructor of the
	 * ArrivingPerson class
	 * 
	 * @param stat       this is used in the person constructor to get a random
	 *                   floor
	 * @param weight     this is used to specify the weight of the person when they
	 *                   go on the lift
	 * @param id         this is used as a unique identifier of the person
	 * @param destBoundL this is the lowest floor the person can go to
	 * @param destBoundR this is the highest floor the person can go to
	 */

	public Person(Stats stat, int weight, int id, int destBoundL, int destBoundR) {
		this.weight = weight;
		this.id = id;
		this.destBoundL = destBoundL;
		this.destBoundR = destBoundR;
		this.currentFloor = Simulator.DEFAULTFLOOR;
		this.waitTimeList = new ArrayList<Integer>();
		this.destination = getRandomFloor(stat);
	}

	/**
	 * This method will decide if the person want to change floor or not. If so they
	 * will press the button to get the lift to a random floor
	 * 
	 * @param sim     this is used as an interfacer for the floor object
	 * @param pHandle this is used in overridden methods
	 * @param stat    this is used for getting a random floor
	 */
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) {
		if (decideToChangeFloor(stat)) {
			changeFloor(sim, getRandomFloor(stat));
		}
	}

	/**
	 * This method will decides to change floor or not, if they are not waiting for
	 * the lift or on it already
	 * 
	 * @param stat this is used for making a decision to change floor
	 * @return this is the boolean to represent if they want to change floor or not
	 */
	private boolean decideToChangeFloor(Stats stat) {
		if (currentFloor != -1 && !isWaiting) {
			return stat.getDecisionProb();
		}
		return false;
	}

	/**
	 * This method will carry out the methods for changing floor
	 * 
	 * @param sim     this is used as the interfacer for the floor object
	 * @param newDest this is used as a new floor to set as a destination
	 */
	protected void changeFloor(Simulator sim, int newDest) {
		destination = newDest;
		sim.removeFromFloor(currentFloor, id);
		pressButton(sim);
	}

	/**
	 * This method uses stat to get a random floor between destBoundL and the
	 * destBoundR fields.
	 * 
	 * @param stat this is used for finding a random number between the two
	 *             boundaries
	 * @return this returns a random floor number
	 */
	protected int getRandomFloor(Stats stat) {
		if (destBoundL == destBoundR) {
			return destBoundL;
		} else {
			while (true) {
				int randFloor = stat.getRandomRangeNum(destBoundL, destBoundR); // keeps trying for a random floor until
																				// it isn't the current floor of the
																				// person
				if (randFloor != currentFloor) {
					return randFloor;
				}
			}
		}

	}

	/**
	 * This method gets the weight
	 * 
	 * @return this returns the weight that has been stored
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * This method get the current floor
	 * 
	 * @return this return the current floor that has been stored
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * This method get the destination
	 * 
	 * @return this return the destination that has been stored
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * This method sets the current floor
	 * 
	 * @param floorNo this is a specified floor number to be set as the current
	 *                floor
	 */
	public void setCurrentFloor(int floorNo) {
		currentFloor = floorNo;
	}

	/**
	 * This method sets the current destination
	 * 
	 * @param floorNo this is a specified floor number to be set as the destination
	 */
	public void setDestination(int floorNo) {
		destination = floorNo;
	}

	/**
	 * This method notifies the button that a person is waiting, or if they are
	 * already at their destination, they will automatically move to the floor
	 * 
	 * @param sim this is the interfacer for a floor object
	 */
	public void pressButton(Simulator sim) {
		if (currentFloor != destination) {
			isWaiting = true;
			timeStamp = Simulator.getNewTimeStamp();
			waitingTick = sim.getTick();
			sim.getFloor(currentFloor).pressButton(id);
		} else { // current floor same as dest
			addSelfToFloor(sim);
		}
	}

	/**
	 * This method notifies that a person has arrived at a waiting queue
	 * 
	 * @param sim this is used in the press button method
	 */
	public void arrive(Simulator sim) {
		pressButton(sim);
	}

	/**
	 * This method add the person's id to the onFloor list
	 * 
	 * @param sim this is used in the press button method
	 * 
	 */
	protected void addSelfToFloor(Simulator sim) {
		sim.addToOnFloor(id, Simulator.DEFAULTFLOOR);
	}

	/**
	 * This method is used to get the Id of the person
	 * 
	 * @return this return the ID that has been stored
	 */
	public int getID() {
		return id;
	}

	/**
	 * This method is used to get the time stamp
	 * 
	 * @return this return the time stamp that has been stored
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * This method returns a string representation of the person
	 */
	public String toString() {
		if (this instanceof Developer) {
			return ((Developer) this).getCompany().substring(0, 1).toUpperCase() + ":" + id;
		}
		return this.getClass().getSimpleName().substring(0, 1).toUpperCase() + ":" + id;
	}

	/**
	 * This method sets the current destination
	 * 
	 * @param timeStamp this is a specified
	 */

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * This method is used to get the average waiting time
	 * 
	 * @return this returns the average waiting time that has been calculated
	 */
	public double getAverageWaitingTime() { // -1 is no times recorded
		if (!waitTimeList.isEmpty()) {
			double average = 0;

			for (int time : waitTimeList) {
				average += time;
			}
			return average / waitTimeList.size();
		}
		return -1;
	}

	/**
	 * This method is used to move the person onto the floor
	 * 
	 * @param currentTick this provides the current tick of the simulation
	 */
	public void moveOffElevator(int currentTick) {
		currentFloor = destination;
	}

	/**
	 * This method is used to move the person onto the elevator
	 * 
	 * @param currentTick this provides the current tick of the simulation
	 */
	public void moveOnElevator(int currentTick) {
		isWaiting = false;
		currentFloor = -1; // sets person to travelling state which is -1 (meaning on elevator)
		waitTimeList.add(currentTick - waitingTick);
	}
}