package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {
	private final int MAXCAPACITY;
	private int currentCapacity;
	private String state;
	private boolean isMovement;
	private boolean isDoorOpen;
	private ElevatorQueue queue;
	private int destination;
	private int currentFloor;
	
	public Elevator(ElevatorQueue queue, int maxCapacity) {
		MAXCAPACITY = maxCapacity;
		currentCapacity = 0;
		state = "idle";
		isMovement = false; // true = moving, false = still
		isDoorOpen = false; // true = open, false = closed
		this.queue = queue;
		destination = 0;
		currentFloor = 0;
	}
	
	public void tick(Simulator sim, Floor currentFloorObj) { // implement with switch case?
		if(currentFloor != destination) {
			moveFloor(currentFloor - destination);
		} else if(!isDoorOpen) { // implies the elevator is at its destination + door is not open
			toggleDoor();
		} else if 
		// TODO: from here!
	}
	
	private void moveFloor (int direction) {
		if(direction > 0) {
			currentFloor -= 1;
		} else {
			currentFloor += 1;
		}
	}
	
	private void offloadPeople(Simulator sim) {
		sim.setOffloadPeople(queue.getOffloadPeople());
	}
	
	private void onloadPeople(Simulator sim, Building bld, int capacityAllowance) {
		Floor fl = bld.getFloor(currentFloor);
		Person[] people = fl.removeFromQueue(capacityAllowance);
		sim.setOnloadPeople(people);
	}
	
	private void toggleDoor() {
		isDoorOpen = !isDoorOpen;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean isMoving() {
		return isMovement;
	}

	public boolean isDoorOpen() {
		return isDoorOpen;
	}
	
}