package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		state = "close"; // initially closed. will change to open then ready then close.
		isMovement = true; // true = moving, false = idle
		
		this.queue = queue;
		requestsList = requestsListSetup();
		
	}
	
	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		if(currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			
			if(direction == 'I') {  // can be at dest but because lift is idle, will not need to open etc
				if(requestsList.get(currentFloor)) {
					searchDestination();
					setDirection();
				}
			} else {  // at dest and needs to offload/onload
				openCloseMechanism(sim, bld.getFloor(currentFloor));
			}
			 			
		} else { // if not at dest, then still moving to destination
			if(requestsList.get(currentFloor) || isMovement) {  // before moving, checks current floor if anyone wants to get on, OR checks if in the process of such via checking if it has stopped
				openCloseMechanism(sim, bld.getFloor(currentFloor));
			} else {  // will move after opening and closing
				moveFloor();
				System.out.println("Moving to floor " + currentFloor);
			}
		}
	}
	
	private void openCloseMechanism(Simulator sim, Floor currentFloorObj) {
		if(state.equals("close")) { // stops movement and opens door
			System.out.println("Stopped at floor" + currentFloor);
			
			isMovement = false;
			state = "open";
			
		} else if(state.equals("open")) { // flow starts of people going out and possibly in. 'ready' state ensures next tick will close door
			System.out.println("Opening door");
			
			offloadPeople(sim, currentFloorObj); // people in queue needing to go out will do.
			onloadPeople(sim, currentFloorObj);
			state = "ready";
			  
		} else if(state.equals("ready")) { // closes door, finds new nearest dest and goes to it
			System.out.println("Closing door");
			
			isMovement = true;
			
			destination = searchDestination();
			setDirection(); // then sets direction based on new dest
			state = "close";
		}
}
		
	private void moveFloor () {  // will go up/down to destination floor
		switch(direction) {
			case('U'):  // going up
				currentFloor += 1;
				break;
			case('D'):  // going down
				currentFloor -= 1;
				break;
			default:  // when idle, do not move
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

	private int searchDestination() {  // returns next closest request in direction of lift. If none, dest is furthest floor in lifts direction
		Set<Integer> floorNums = requestsList.keySet();
		
		if(direction == 'D') {  // when going down + stops at dest, checks for any more reqs below currentFloor,
			for(int floorNo = currentFloor - 1; floorNo == 0; floorNo--) { // searches from top to bottom, so finds closest req from currFloor (going down)
				if(requestsList.get(floorNo)) {
					return floorNo;  // if req at floor, return.
				}
			}
		} else {  // is going up or is idle
			for(int floorNo = currentFloor + 1; floorNo == 6; floorNo++) { // searches from bottom to top, so finds closest req from currFloor (going up)
				if(requestsList.get(floorNo)) {
					return floorNo;  // if req at floor, return.
				}
			}
		}
		direction = 'I';  // is no requests so assume idle state - allows for floor checking when moving
		return 0; // is no requests so go to bottom
	}

	private void offloadPeople(Simulator sim, Floor currentFloorObj) {  // offloads people from lift if needed
		ArrayList<Integer> offloaded = queue.removePeople(currentFloor, sim);  // gets people who need to get to current floor + removes from lift queue
		if(!offloaded.isEmpty()) { // given there are people in the offload list to get off
			currentFloorObj.addToFloor(offloaded);
			sim.passNewCurrentFloor(offloaded, currentFloor);  
		}
	}
	
	private void onloadPeople(Simulator sim, Floor currentFloorObj) {  // operations to onload people on currentfloor if there are people are waiting
		if(requestsList.get(currentFloor)) {  // checks if people want to get on
			int spaces = queue.getSpaces();
			
			if(spaces > 0) {  // will only perform the onload operation if there is space on the elevator
				ArrayList<Integer> people = currentFloorObj.getPeople(spaces);
				
				requestsList.put(currentFloor, false); 
				queue.addPeople(people);
				
				sim.passNewCurrentFloor(people, -1); // sets people to travelling state meaning people currFloor will = -1 (meaning on elevator)
			}
			
			if(!currentFloorObj.isWaiting()) { // checks if still people on waiting to get on, due to lift being full and people cannot get on
				currentFloorObj.reRequest();
			}
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
