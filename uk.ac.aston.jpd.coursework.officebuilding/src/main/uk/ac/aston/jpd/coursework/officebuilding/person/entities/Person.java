package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Person {
	private final int id;
	private final int weight;
	private int currentFloor;
	private int destination;
	private boolean isWaiting;
	private long timeStamp;  // used for comparator to decide who is goes in front in queue. measured in nano seconds
	private int waitingTick;  // tick when person started waiting
	private List<Integer> waitTimeList;  // in ticks

	public Person(int weight, int id, int destination) {
		this.id = id;
		this.weight = weight;
		this.currentFloor = Simulator.DEFAULTFLOOR;
		this.destination = destination;
		this.waitTimeList = new ArrayList<Integer>();
	}

	public int getWeight() {
		return weight;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getDestination() {
		return destination;
	}

	public void setCurrentFloor(int floorNo) {
		currentFloor = floorNo;
	}
	
	public void setDestination(int floorNo) {
		destination = floorNo;
	}
	
	public void pressButton(Simulator sim) {
		isWaiting = true;
		timeStamp = Simulator.getNewTimeStamp();
		waitingTick = sim.getTick();
		sim.getFloor(currentFloor).pressButton(id);
	}

	public boolean isWaiting() {
		return isWaiting;
	}

	public void setIsWaiting(boolean waiting) {
		isWaiting = waiting;
	}
	
	public void addToLift(int currentTick) {
		isWaiting = false;
		currentFloor = -1;  // sets person to travelling state which is -1 (meaning on elevator)
		waitTimeList.add(currentTick - waitingTick);
	}

	public int getID() {
		return id;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public String toString() {
		if(this instanceof Developer) {
			return ((Developer) this).getCompany().substring(0, 1).toUpperCase() + ":" + id;
		}
		return this.getClass().getSimpleName().substring(0, 1).toUpperCase() + ":" + id;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public double getAverageWaitingTime() {  // -1 is no times recorded
		if(!waitTimeList.isEmpty()) {
			double average = 0;
			
			for(int time : waitTimeList) {
				average += time;
			}
			
			return average / waitTimeList.size();
		} else {
			return -1;
		}
		
	}
}