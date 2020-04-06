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
	private Map<Integer, Boolean> requestsList; // to know where people are to get in lift
	private char direction;
	private int destination;
	private int currentFloor;
	
	public Elevator(PQueue queue) {
		currentCapacity = 0;
		direction = 'I'; // meaning idle and not moving anywhere
		destination = 0;  // initially at ground floor
		currentFloor = 0;
		state = "ready"; // either ready, meaning already off/onloaded and ready to go, or open, where off/onloading is in process. way of knowing if door needs to be closed or opened
		isMovement = true; // true = moving, false = idle
		
		this.queue = queue;
		requestsList = requestsListSetup();
		
	}
	
	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		if(currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			
			if(direction == 'I') {  // can be at dest but because lift is idle, will not need to open etc
				if(requestsList.get(currentFloor)) {
					// if is req, set destination + direction
				}
			} else {  // at dest and needs to offload/onload
				openCloseMechanism(sim);
			}
			 			
		} else { // if not at dest, moves toward dest only in the case the currfloor has no one to pick up
			if(requestsList.get(currentFloor)) {  // before moving check floor if anyone wants to get on
				openCloseMechanism(sim);
			} else {
				moveFloor();
				System.out.println("Moving to floor " + currentFloor);
			}
		}
	}
	
	private void openCloseMechanism(Simulator sim) {
		if(state.equals("close")) { // stops movement and opens door
			isMovement = false;
			state = "open";
			System.out.println("Stopped at floor" + currentFloor);
		
		} else if(state.equals("open")) { // flow starts of people going out and possibly in. 'ready' state ensures next tick will close door
			offloadPeople(sim); // people in queue needing to go out will do.
			// TODO: people to get in will do by a floor give function, if enough room
			// people currFloor will = -1 (meaning on elevator)
			
			requestsList.put(currentFloor, false);
			
			state = "ready";
			System.out.println("Opening door");
		    
		} else if(state.equals("ready")) { // closes door, finds new nearest dest and goes to it
			isMovement = true;
			state = "close";
			System.out.println("Closing door");
			
			searchNewDestination();
			// TODO: find new destination, by checking request list for currentfloor+n if going up, or currentFloor-n if going down
			//		 if no people want to use lift, goto ground and set direction to 'I' for idle
			
			setDirection(); // then sets direction based on new dest
		}
	}
		
	private void moveFloor () {  // will go up/down to destination floor
		if(direction == 'U') {
			currentFloor += 1;
		} else {
			currentFloor -= 1;
		}
	}
	
	private void setDirection() {
		int dir = destination - currentFloor;  // if +ive, below dest floor. if -ive, above dest floor
		if(dir < 0) {
			direction = 'U';
		} else {  // implies lift needs to go down
			direction = 'D';
		}	
	}
	
	private int searchNewDestination() {  // return 
		return int;
		
	}
		
	private void offloadPeople(Simulator sim) {  // offloads people from lift if needed
		ArrayList<Integer> offloaded = queue.removePeople(currentFloor, sim);  // gets people who need to get to current floor + removes from lift queue
		if(!offloaded.isEmpty()) { // given there are people to get off
			// TODO: SIMULATOR: set their person object state/current location (sim - peopleHandle)
			// TODO: then put to list on the floor
		}
	}
	
	private void onloadPeople(Simulator sim, Building bld, int capacityAllowance) {
		if(requestsList.get(currentFloor) && queue.getSpaces() > 0) {  // checks if people want to get on and if there is space to do so
			Floor fl = bld.getFloor(currentFloor);
			
			// TODO: floor function to get remove ids from waiting, with specified amount.
			// add req's id to this queue
			// sets people to travelling state (sim - peopleHandle)
		}
	}
	
	private HashMap<Integer, Boolean> requestsListSetup() {
		HashMap<Integer, Boolean> reqList = new HashMap<Integer, Boolean>();
		for(int floorNo = 0; floorNo <= Simulator.FLOORNO; floorNo++) {
			reqList.put(floorNo, false);
		}
		return reqList;
	}
	
	public void addRequest(int floorNo) { // notifies lift the button is pressed at the floor given
		requestsList.put(floorNo, true);
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean isMoving() {
		return isMovement;
	}	
}
