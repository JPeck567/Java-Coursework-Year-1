package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {

	private int currentCapacity;
	private String state;
	private boolean isMovement;
	private PQueue queue; //space to hold people to get in the elevator to correct
	private int destination;
	private int currentFloor;
	
	public Elevator(PQueue queue) {
		currentCapacity = 0;
		state = "ready"; // either ready, meaning already off/onloaded and ready to go, or open, where off/onloading is in process. way of knowing if door needs to be closed or opened
		isMovement = false; // true = moving, false = idle
		this.queue = queue;
		destination = 0;
		currentFloor = 0;
	}
	
	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		if(currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			
			if(isMovement) {
				isMovement = !isMovement; // stops movement and implies door is open
				state = "open";
				
			} else if (state.equals("open")) {
				offloadPeople(sim); // doors are open, so flow of people going in and out
				checkFloor(sim, bld); // checks if anyone to move to lift, and does so if so.
				
			} else {  // if not at destination, but ready to move to next dest. implies closing of door
				isMovement = !isMovement;
				state = "ready";
				queue.getNextDestination(currentFloor);
			}
			
		} else { // not at dest, but will check if current floor has people to load on
			checkFloor(sim, bld);
			moveFloor(getDirection());
		}
	}
	
	private void checkFloor(Simulator sim, Building bld) {
		int spaces = queue.getSpaces();
		if (spaces != 0) {
			if(!bld.getFloor(currentFloor).isEmpty()) { // if waiting queue is not empty
				onloadPeople(sim, bld, spaces);
			}
		}
		
	}
	
	private void moveFloor (int direction) {
		
		
		// if(direction > 0) {  // going up
		// 	currentFloor -= 1;
		// } else if(direction < 0) { // going down
		// 	currentFloor += 1;
		// } else { // is idle, so no direction
		// 	return;
		// }
	}
	
	private void offloadPeople(Simulator sim) { 
		queue.getOffloadPeople(currentFloor, sim); // removes people from queue and set their person object state/current location
		// then put to list on the floor
	}
	
	private void onloadPeople(Simulator sim, Building bld, int capacityAllowance) {
		Floor fl = bld.getFloor(currentFloor);
		
		int[] people = fl.removeFromWaiting(capacityAllowance); // gets a certain number of people from the floor queue
		
		queue.addOnloadPeople(people);
		sim.setOnloadPeople(people); // sets people to travelling state
	}
	
	public boolean getDirection() { 
		return destination - currentFloor;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean isMoving() {
		return isMovement;
	}	
}
