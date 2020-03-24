package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {
	private final int MAXCAPACITY;
	private int currentCapacity;
	private String state;
	private boolean isMovement;
	private PQueue queue; //space to hold people to get in the elevator to correct
	private int destination;
	private int currentFloor;
	
	public Elevator(PQueue queue) {
		MAXCAPACITY = Simulator.MAXCAPACITY; //
		currentCapacity = 0;
		state = "ready"; // either ready, meaning already off/onloaded and ready to go, or open, where off/onloading is in process. way of knowing if door needs to be closed or opened
		isMovement = false; // true = moving, false = idle
		this.queue = queue;
		destination = 0;
		currentFloor = 0;
	}
	
	public void tick(Simulator sim, Building bld) { // implement with switch case?
		if(currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			if(isMovement) {
				isMovement = !isMovement; // stops movement and implies door is open
				state = "open";
			} else if (state.equals("open")) {
				offloadPeople(sim); // doors are open, so flow of people going in and out
				onloadPeople(sim, bld, queue.getSize());
			} else {  // if not at destination, but ready to move to next dest. implies closing of door
				isMovement = !isMovement;
				state = "ready";
				queue.getNextDestination(sim, currentFloor);
			}
		} else {
			moveFloor(destination - currentFloor);
		}
		
	}
	
	private void moveFloor (int direction) {
		if(direction > 0) {
			currentFloor -= 1;
		} else {
			currentFloor += 1;
		}
	}
	
	private void offloadPeople(Simulator sim) {
		sim.setOffloadPeople(queue.getOffloadPeople(currentFloor)); // removes people from queue and set their person object state/current location
	}
	
	private void onloadPeople(Simulator sim, Building bld, int capacityAllowance) {
		Floor fl = bld.getFloor(currentFloor);
		int[] people = fl.removeFromQueue(capacityAllowance); // gets a certain number of people from the floor queue
		queue.addOnloadPeople(people);
		sim.setOnloadPeople(people); // sets people to travelling state
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
