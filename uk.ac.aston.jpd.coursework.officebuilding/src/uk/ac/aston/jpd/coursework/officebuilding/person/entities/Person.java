package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

public class Person {
	protected final int ID;
	protected final int WEIGHT;
	protected int currentFloor;
	protected int destination;

	public Person(int weight, int id) {
		ID = id;
		WEIGHT = weight;
		currentFloor = 0;
		destination = 0;
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
}