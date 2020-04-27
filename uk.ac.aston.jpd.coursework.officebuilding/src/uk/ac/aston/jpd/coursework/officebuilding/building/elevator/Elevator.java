package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.ArrivingPerson;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {

	private String state;
	private boolean isMovement;
	private PQueue queue; // space to hold people to get in the elevator
	private Map<Integer, Boolean> requestsList; // to know where people are to get in lift
	private char direction;
	private int destination;
	private int currentFloor;

	public Elevator(PQueue queue, int noFloors) {
		direction = 'I'; // meaning idle and not moving anywhere
		destination = 0; // initially at ground floor
		currentFloor = 0;
		state = "close"; // initially closed. will change to open then ready then close.
		isMovement = false; // true = moving, false = idle

		this.queue = queue;
		requestsList = requestsListSetup(noFloors);

	}

	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		//System.out.println(
		//		"Tick No: " + sim.getTick() + "\nFloor No:" + currentFloor + " Current Capacity: " + queue.getSize());
		Floor currentFloorObj = bld.getFloor(currentFloor);

		if (currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			if (direction == 'I') { // if in idle state
				if (isMovement == false) { // continue the open/close cycle, as stopped
					openCloseMechanism(sim, currentFloorObj);
				} else { // if not needed to onload/offload on current floor
					updateDestination(sim, currentFloorObj); // finds new dest for lift
					setDirection(); // sets direction based upon if update above has found new dest or not

					if (checkStop(sim, currentFloorObj)) { // if new req, will either open if on same floor or move to it
						openCloseMechanism(sim, currentFloorObj);
					} else {
						moveFloor();
					}
				}

			} else { // open/close process as on dest floor and not idle.
				openCloseMechanism(sim, currentFloorObj);
			}
		} else { // no one needs to get on
			moveFloor();
			//System.out.println("Moving to floor " + currentFloor);
		}
	}

	private void openCloseMechanism(Simulator sim, Floor currentFloorObj) {
		if (state.equals("close")) { // stops movement and opens door
			//System.out.println("Stopped at floor " + currentFloor + " and opening door");

			state = "open";
			isMovement = false;

			offloadPeople(sim, currentFloorObj, queue.getOffload(sim, currentFloor)); // people in queue needing to go out will do.
			onloadPeople(sim, currentFloorObj);

		} else if (state.equals("open")) { // closes door, finds new nearest dest and goes to it
			//System.out.println("Closing door");

			state = "close";
			isMovement = true;

			updateDestination(sim, currentFloorObj); // find newest highest or lowest dest each tick if new people move onto lift or req for a floor in building
			setDirection(); // then sets direction based on new dest
		}
	}

	private void moveFloor() { // will go up/down to destination floor
		switch (direction) {
		case ('U'): // going up
			currentFloor += 1;
			break;
		case ('I'): // when idle, do not move unless not at bottom
			if (currentFloor == 0) { // if false, falls to next case, and moves down therefore
				break;
			}
		case ('D'): // going down
			currentFloor -= 1;
			break;
		default:
			// TODO exception
		}
	}

	private void changeDirection() {
		direction = (direction == 'U') ? 'D' : 'U';
	}

	private void setDirection() {
		int dir = destination - currentFloor; // if +ive, below dest floor. if -ive, above dest floor
		if (dir > 0) {
			direction = 'U';
		} else if (dir < 0) { // implies lift needs to go down
			direction = 'D';
		} else {
			direction = 'I';
		}
	}

	private boolean checkStop(Simulator sim, Floor currentFloorObj) { // checks if lift is needed to stop at its currentFloor.
		if (!queue.getOffload(sim, currentFloor).isEmpty() || requestsList.get(currentFloor)) {
			return true;
		} else {
			return false;
		}
	}

	private void updateDestination(Simulator sim, Floor currentFloorObj) { // checks up+down/down+up for reqs/ if not, assume idle state
		int nextUpDest = checkUp(sim);
		int nextDownDest = checkDown(sim);

		if (direction == 'U' || direction == 'I') { // meaning going up or can only go up
			if (nextUpDest != -1) { // if there was the need to go up, will return next floor on the way up
				destination = nextUpDest;
				return;
			} else if (nextDownDest != -1) { // if no more stops to do going up, will change direction and to check for any stops going down
				destination = nextDownDest; // if there was the need to go down, will return next floor on the way down	
				return;
			}
		} else { // can only go down
			if (nextDownDest != -1) { // if there was the need to go down, will return next floor down 
				destination = nextDownDest;
				return;
			} else if (nextUpDest != -1) { // if no more stops to do going down, will change direction and to check for any stops going up 
				destination = nextUpDest; // if there was the need to go up, will return highest floor
				return;
			}
		}
		// if no more stops to go to either way, goto ground and turn to idle state
		destination = 0;
		direction = 'I';
	}

	private int checkUp(Simulator sim) {
		Optional<Integer> nextUpPerson = queue.getNextUpFloor(sim, currentFloor);
		OptionalInt nextUpReq = requestsList.keySet().stream().filter(floor -> requestsList.get(floor)) // filters the values of each floorNo key by if they map to true
				.filter(floor -> floor > currentFloor) // gets floors above lift
				.mapToInt(Integer::intValue) // convert each Integer to int, using method reference as intValue is static and the usual lambda expression is essentially just calling a method, not evaluating typed predicament.
				.min(); // find min value from stream
		// get that value, as min() returns OptionalInt, such that there may not be any values in the stream, so not int, or vice versa 

		int noFloors = sim.getNoFloors();

		int highTest = Math.min(nextUpPerson.orElse(noFloors + 1), nextUpReq.orElse(noFloors + 1)); // lowest (so closest when going up) between req's and peoples floor req's
		// as type is optional, or else is used, where value is returned if there are any, or in the case there isn't (no req's or person on lift situation) substituing the parameter as the value.

		if (highTest != noFloors + 1) { // checks if there is an actual req to go up
			return highTest;
		} else {
			return -1; // returns if no more floors going up
		}

	}

	private int checkDown(Simulator sim) {
		Optional<Integer> nextDownPerson = queue.getNextDownFloor(sim, currentFloor);
		OptionalInt nextDownReq = requestsList.keySet().stream().filter(floor -> requestsList.get(floor))
				.filter(floor -> floor < currentFloor).mapToInt(Integer::intValue).max();

		int lowTest = Math.max(nextDownPerson.orElse(-1), nextDownReq.orElse(-1)); // highest ( meaning closest when going down) out of either req's or people in lift

		return lowTest; // either -1, or a floor to goto downwards. in any case, value is valid

	}

	private int searchNextRequest(Simulator sim) { // returns next closest request in direction of lift. If none, dest is furthest
		// floor in lifts direction
		int floorNo = -1;

		if (direction == 'D') { // when going down + stops at dest, checks for any more reqs below currentFloor,
			for (int num = currentFloor; num >= 0; num--) { // searches from top to bottom, so finds
															// closest req from currFloor (going down)
				if (requestsList.get(num)) {
					floorNo = num; // if req at floor, return.
				}
			}
		} else { // is going up or is idle
			for (int num = currentFloor; num <= 6; num++) { // searches from bottom to top, so finds
															// closest req from currFloor (going up)
				if (requestsList.get(floorNo)) {
					floorNo = num; // if req at floor, return.
				}
			}
		}

		direction = 'I'; // is no requests so assume idle state - allows for floor checking when moving
		return floorNo; // is no requests so go to bottom
	}

	private void offloadPeople(Simulator sim, Floor currentFloorObj, List<Integer> offload) { // offloads people from lift if needed
		if (!offload.isEmpty()) { // given there are people in the offload list to get off
			for (int pID : offload) {
				Person p = sim.getPerson(pID);
				queue.removePerson(queue.indexOf(pID)); // removes from queue
				p.setCurrentFloor(currentFloor);

				if (p instanceof ArrivingPerson) {
					
					System.out.println();
					if (((ArrivingPerson) p).getToExit()) {
						currentFloorObj.addToFloor(pID);
						//sim.removePerson(pID);
						//System.out.println("Person: " + p.getID() + " left the building");
						
					} else {  // arrivals code to add to floor
						currentFloorObj.addToFloor(pID);
						((ArrivingPerson) p).setStartingTick(sim.getTick());  // set starting tick so can start to time person in building
						
						//System.out.println("Person: " + sim.getPerson(pID).getID() + " left the elevator for floor "
						//		+ sim.getPerson(pID).getDestination());
					}
					
				} else {  // non arrivals code to add to floor
					currentFloorObj.addToFloor(pID);
					//System.out.println("Person: " + sim.getPerson(pID).getID() + " left the elevator for floor "
					//		+ sim.getPerson(pID).getDestination());

				}
			}
		}
	}

	private void onloadPeople(Simulator sim, Floor currentFloorObj) { // operations to onload people on currentfloor if there are people are waiting
		if (requestsList.get(currentFloor)) { // checks if people want to get on
			if (queue.getSpaces() > 0) { // will only perform the onload operation if there is space on the elevator
				int spaces = queue.getSpaces();

				// gets specified number of people from queue and adds it to current queue. gets indexes from 0 to the number of spaces on lift
				for (int index = 0; index <= spaces - 1; index++) {
					if (!currentFloorObj.waitingIsEmpty()) {
						int pID = currentFloorObj.getWaitingPerson();
						Person p = sim.getPerson(pID); // peeks at next person in queue
						if ((p.getWeight() <= queue.getSpaces())) { // given the selected person can fit within the spaces in the lift
							queue.addPerson(p.getID());

							p.setIsWaiting(false);
							p.setCurrentFloor(-1); // sets person to travelling state which is -1 (meaning on elevator)

							currentFloorObj.removeFromWaiting(); // removes given person

							//System.out.println(
							//		"Person: " + p.getID() + " entered the elevator for floor " + p.getDestination());
						}
					}
				}
				requestsList.put(currentFloor, false);
			}

			if (!currentFloorObj.isWaiting()) { // checks if still people on waiting to get on, due to lift being full and people cannot get on
				currentFloorObj.reRequest();
			}
		}
	}

	private HashMap<Integer, Boolean> requestsListSetup(int noFloors) {
		HashMap<Integer, Boolean> reqList = new HashMap<Integer, Boolean>();
		for (int floorNo = 0; floorNo <= noFloors - 1; floorNo++) {
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

	public List<Integer> getQueue() {
		return queue.getQueue();
	}

	public String getState() {
		return state;
	}
}
