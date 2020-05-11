package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

/**
*
*/
public class Person {
	/**
	 *
	 */
	protected final int id;
	protected final int destBoundL;
	protected final int destBoundR;
	protected int destination;
	protected int currentFloor;
	protected boolean isWaiting;
	private final int weight;
	private long timeStamp; // used for comparator to decide who is goes in front in queue. measured in nano seconds
	private int waitingTick; // tick when person started waiting
	private List<Integer> waitTimeList; // in ticks

	/**
	 *
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
	
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) {
		if(decideToChangeFloor(stat)) {
			changeFloor(sim, getRandomFloor(stat));
		}
	}
	
	private boolean decideToChangeFloor(Stats stat) {
		if(currentFloor != -1 && !isWaiting) {
			return stat.getDecisionProb();
		}
		return false;
	}
	
	protected void changeFloor(Simulator sim, int newDest) {
		destination = newDest;
		sim.removeFromFloor(currentFloor, id);
		pressButton(sim);
	}
	
	protected int getRandomFloor(Stats stat) {
		if(destBoundL == destBoundR) {
			return destBoundL;
		} else {
			while (true) {
				int randFloor = stat.getRandomRangeNum(destBoundL, destBoundR); // keeps trying for a random floor until it isn't the current floor of the person
				if (randFloor != currentFloor) {
					return randFloor;
				}
			}
		}
		
	}

	/**
	 *
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 *
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 *
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 *
	 */
	public void setCurrentFloor(int floorNo) {
		currentFloor = floorNo;
	}

	/**
	 *
	 */
	public void setDestination(int floorNo) {
		destination = floorNo;
	}

	/**
	 *
	 */
	public void pressButton(Simulator sim) {
		if(currentFloor != destination) {
			isWaiting = true;
			timeStamp = Simulator.getNewTimeStamp();
			waitingTick = sim.getTick();
			sim.getFloor(currentFloor).pressButton(id);
		} else {  // current floor same as dest
			addSelfToFloor(sim);
		}
	}
	
	public void arrive(Simulator sim) {
		pressButton(sim);
	}
	

	protected void addSelfToFloor(Simulator sim) {
		sim.addToOnFloor(id, Simulator.DEFAULTFLOOR);
	}

	/**
	 *
	 */
	public int getID() {
		return id;
	}

	/**
	 *
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 *
	 */
	public String toString() {
		if (this instanceof Developer) {
			return ((Developer) this).getCompany().substring(0, 1).toUpperCase() + ":" + id;
		}
		return this.getClass().getSimpleName().substring(0, 1).toUpperCase() + ":" + id;
	}

	/**
	 *
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 *
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

	public void moveOffElevator(int currentTick) {
		currentFloor = destination;
	}

	public void moveOnElevator(int currentTick) {
		isWaiting = false;
		currentFloor = -1; // sets person to travelling state which is -1 (meaning on elevator)
		waitTimeList.add(currentTick - waitingTick);
	}
}