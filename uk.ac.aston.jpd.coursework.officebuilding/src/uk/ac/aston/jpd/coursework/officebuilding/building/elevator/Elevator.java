package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {

	private int currentCapacity;
	private String state;
	private boolean isMovement;
	private PQueue queue; //space to hold people to get in the elevator
	private Map<Integer, ArrayList<Integer>> requestsList; // to know where people are to get in lift
	private int destination;
	private int currentFloor;
	
	public Elevator(PQueue queue) {
		currentCapacity = 0;
		destination = 0;
		currentFloor = 0;
		state = "ready"; // either ready, meaning already off/onloaded and ready to go, or open, where off/onloading is in process. way of knowing if door needs to be closed or opened
		isMovement = false; // true = moving, false = idle
		
		this.queue = queue;
		requestsList = new HashMap<Integer, ArrayList<Integer>>();
		
	}
	
	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		if(currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			
			if(isMovement) {
				isMovement = !isMovement; // stops movement and implies door is open
				state = "open";
				
			} else if (state.equals("open")) {
				offloadPeople(sim); // doors are open, so flow of people going in and out
				
			} else {  // door is to close and next dest is found
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
		if(currentFloor != 6)
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
	
	public void addRequests(int floorNo, ArrayList<Integer> pIDs) { // adds a list of people who pressed the button to a map of floors to people who want to get the lift
		for(int ID: pIDs) {
			if(!requestsList.containsKey(floorNo)) {
				ArrayList<Integer> req = requestsList.get(ID);
				req.add(ID);
				requestsList.put(floorNo, req);
			}
		}
		
	}
	
	public int getDirection() { 
		return destination - currentFloor;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean isMoving() {
		return isMovement;
	}	
}
