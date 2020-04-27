package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Person {
	protected final int id;
	protected final int weight;
	protected int currentFloor;
	protected int destination;
	protected boolean isWaiting;

	public Person(int weight, int id, int destination) {
		this.id = id;
		this.weight = weight;
		this.currentFloor = Simulator.DEFAULTFLOOR;
		this.destination = destination;
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
		sim.getFloor(currentFloor).pressButton(id);
	}

	public boolean isWaiting() {
		return isWaiting;
	}

	public void setIsWaiting(boolean waiting) {
		isWaiting = waiting;
		
	}

	public int getID() {
		return id;
	}
}