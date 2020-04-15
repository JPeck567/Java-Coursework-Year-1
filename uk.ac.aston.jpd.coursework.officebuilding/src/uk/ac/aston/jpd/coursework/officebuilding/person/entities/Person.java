package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Person {
	protected final int ID;
	protected final int WEIGHT;
	protected int currentFloor;
	protected int destination;

	public Person(int weight, int id, int destination) {
		ID = id;
		WEIGHT = weight;
		currentFloor = Simulator.DEFAULTFLOOR;
		this.destination = destination;
	}
	
	public int getWeight() {
		return WEIGHT;
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
		sim.getButton(currentFloor).pressButton(ID);
	}
}