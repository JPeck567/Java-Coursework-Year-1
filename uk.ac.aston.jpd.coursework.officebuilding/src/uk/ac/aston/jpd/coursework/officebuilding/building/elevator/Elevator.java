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
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class Elevator {

	private String state;
	private boolean isMovement;
	private PQueue queue; // space to hold people to get in the elevator
	private Map<Integer, Boolean> requestsList; // to know where people are to get in lift
	private char direction;
	private int destination;
	private int currentFloor;

	public Elevator(PQueue queue) {
		direction = 'I'; // meaning idle and not moving anywhere
		destination = 0; // initially at ground floor
		currentFloor = 0;
		state = "close"; // initially closed. will change to open then ready then close.
		isMovement = true; // true = moving, false = idle

		this.queue = queue;
		requestsList = requestsListSetup();

	}

	public void tick(Simulator sim, Building bld) { // each tick, elevator is on a floor
		System.out.println();
		if (currentFloor == destination) { // operations related to when elevator is at the floor it needs to be at
			Floor currentFloorObj = bld.getFloor(currentFloor);

			if (direction == 'I') {
				if (!isMovement || checkStop(sim, currentFloorObj)) {  // either continue the open/close cycle or start it, if needed.
					openCloseMechanism(sim, currentFloorObj);
				} else {  // if not needed to onload/offload on current floor
					updateDestination(sim, currentFloorObj); // finds new dest for lift
					setDirection(); // sets direction based upon if update above has found new dest or not
					
					if(checkStop(sim, currentFloorObj)) {  // if new req, will either open if on same floor or move to it
						openCloseMechanism(sim, currentFloorObj);
					} else {
						moveFloor();
					}
				}
			} else { // open/close process as on dest floor and not idle.
				openCloseMechanism(sim, currentFloorObj);
			}
		} else { // no one needs to get on
			System.out.println("Moving to floor " + currentFloor);
			moveFloor();
		}
	}

	private void openCloseMechanism(Simulator sim, Floor currentFloorObj) {
		if (state.equals("close")) { // stops movement and opens door
			System.out.println("Stopped at floor" + currentFloor);

			isMovement = false;
			state = "open";

		} else if (state.equals("open")) { // flow starts of people going out and possibly in. 'ready' state ensures
											// next tick will close door
			System.out.println("Opening door");

			offloadPeople(sim, currentFloorObj, queue.getOffload(sim, currentFloor)); // people in queue needing to go out will do.
			onloadPeople(sim, currentFloorObj);
			state = "ready";

		} else if (state.equals("ready")) { // closes door, finds new nearest dest and goes to it
			System.out.println("Closing door");

			isMovement = true;

			updateDestination(sim, currentFloorObj); // find newest highest or lowest dest each tick if new people move onto lift or req for a floor in building
			setDirection(); // then sets direction based on new dest
			state = "close";
		}
	}

	private void moveFloor() { // will go up/down to destination floor
		switch (direction) {
		case ('U'): // going up
			currentFloor += 1;
			break;
		case ('I'): // when idle, do not move unless not at bottom
			if (currentFloor == 0) {
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
		direction = (direction == 'U') ? 'D': 'U';
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

	private boolean checkStop(Simulator sim, Floor currentFloorObj) {  // checks if lift is needed to stop at its currentFloor.
		if (!queue.getOffload(sim, currentFloor).isEmpty() || requestsList.get(currentFloor)) {
			return true;
		} else {
			return false;
		}
	}

	private void updateDestination(Simulator sim, Floor currentFloorObj) {
		int nextDest;

		if (direction == 'U' || direction == 'I') { // can only go up
			nextDest = checkUp(sim);

			if (!(nextDest == -1)) { // if there was the need to go up, will return furthest floor
				destination = nextDest;
			} else { // if no more stops to do going up, will change direction and to check for any stops going down
				nextDest = checkDown(sim);

				if (!(nextDest == -1)) { // if there was the need to go down, will return lowest floor
					destination = nextDest;
					changeDirection();
				} else { // if no more stops to go to either way, goto ground and turn to idle state
					destination = 0;
					direction = 'I';
				}
			}
		} else { // can only go down
			nextDest = checkDown(sim);

			if (!(nextDest == -1)) { // if there was the need to go down, will return lowest floor
				destination = nextDest;
			} else { // if no more stops to do going up, will change direction and to check for any stops going down
				nextDest = checkUp(sim);

				if (!(nextDest == -1)) { // if there was the need to go up, will return highest floor
					destination = nextDest;
					changeDirection();
				} else { // if no more stops to go to either way, goto ground and turn to idle state
					destination = 0;
				}
			}
		}
	}

	private int checkUp(Simulator sim) {
		Optional<Integer> highestPerson = queue.getHighestFloor(sim);
		OptionalInt highestReq = requestsList.keySet().stream().filter(floor -> requestsList.get(floor)) // filters the values of each floorNo key by if they map to true
				.mapToInt(Integer::intValue) // convert each Integer to int, using method reference as intValue is static and lambda expression is essentially just calling a method, not evaluation a predicament.
				.max(); // find max value from stream
		// get that value, as max() returns OptionalInt, such that there may not be any values in the stream, so not int, or vice versa 

		int highTest = Math.max(highestPerson.orElse(-1), highestReq.orElse(-1));
		// as type is optional, or else is used, where value is returned if there are any, or in the case there isn't (no req's or person on lift situation) substituing the parameter as the value.
		if (destination < highTest) {
			return highTest;
		}

		return -1;
	}

	private int checkDown(Simulator sim) {
		Optional<Integer> lowestPerson = queue.getLowestFloor(sim);
		OptionalInt lowestReq = requestsList.keySet().stream().filter(floor -> requestsList.get(floor))
				.mapToInt(Integer::intValue).max();

		int lowTest = Math.min(lowestPerson.orElse(Simulator.FLOORNO + 1), lowestReq.orElse(Simulator.FLOORNO + 1)); // lowest out of either req's or people in lift

		if (destination > lowTest) {
			return lowTest;
		}

		return -1;

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
				queue.removePerson(queue.indexOf(pID)); // removes from queue
			}

			currentFloorObj.addToFloor((ArrayList<Integer>) offload);
			sim.passNewCurrentFloor((ArrayList<Integer>) offload, currentFloor);
		}
	}

	private void onloadPeople(Simulator sim, Floor currentFloorObj) { // operations to onload people on currentfloor if there are people are waiting
		if (requestsList.get(currentFloor)) { // checks if people want to get on
			if (queue.getSpaces() > 0) { // will only perform the onload operation if there is space on the elevator
				ArrayList<Integer> people = new ArrayList<Integer>(); // tracks what people are moving on
				int spaces = queue.getSpaces();
				
				for (int index = 0; index <= spaces - 1; index++) { // gets specified number of people from queue and adds it to current queue. gets indexes from 0 to the number of spaces on lift
					int personID = currentFloorObj.getWaitingPerson(index);
					if((sim.getPerson(personID).getWeight() <= queue.getSpaces())) {  // given the selected person can fit within the spaces in the lift
						queue.addPerson(personID);
						people.add(personID);
						System.out.println("Person:" + personID + " entered the building");
						
					}

				}

				currentFloorObj.removeFromWaiting(people);

				requestsList.put(currentFloor, false);

				sim.passNewCurrentFloor(people, -1); // sets people to travelling state meaning people currFloor will =
														// -1 (meaning on elevator)
			}

			if (!currentFloorObj.isWaiting()) { // checks if still people on waiting to get on, due to lift being full
												// and people cannot get on
				currentFloorObj.reRequest();
			}
		}
	}

	private HashMap<Integer, Boolean> requestsListSetup() {
		HashMap<Integer, Boolean> reqList = new HashMap<Integer, Boolean>();
		for (int floorNo = 0; floorNo <= Simulator.FLOORNO - 1; floorNo++) {
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
