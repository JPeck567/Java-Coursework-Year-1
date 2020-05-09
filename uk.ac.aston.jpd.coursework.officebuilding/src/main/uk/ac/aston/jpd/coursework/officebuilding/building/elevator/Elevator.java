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
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.ArrivingPerson;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Elevator {

	/**
	 *
	 */
	private String state;
	private final PList queue; // space to hold people to get in the elevator
	private final Map<Integer, Boolean> requestsMap; // to know where people are to get in lift
	private char direction;
	private int currentFloor;

	/**
	 *
	 */
	public Elevator(PList queue, int noFloors) {
		direction = 'I'; // meaning idle and not moving anywhere
		currentFloor = 0;
		state = "close"; // initially closed. will change to open then ready then close.

		this.queue = queue;
		requestsMap = requestsListSetup(noFloors);

	}

	/**
	 *
	 */
	public void tick(Simulator sim, Building bld) {
		Floor currentFloorObj = bld.getFloor(currentFloor);

		if (checkStop(sim, currentFloorObj) || state.equals("open")) {
			openCloseMechanism(sim, currentFloorObj);
		} else {
			moveFloor();
		}

		setDirection(updateDestination(sim));
	}

	/**
	 *
	 */
	private void openCloseMechanism(Simulator sim, Floor currentFloorObj) {
		if (state.equals("close")) { // stops movement and opens door
			state = "open";
			
			offloadPeople(sim, currentFloorObj, queue.getOffload(sim, currentFloor)); // people in queue needing to go out will do.
			onloadPeople(sim, currentFloorObj);
		} else if (state.equals("open")) { // closes door, finds new nearest dest and goes to it
			state = "close";
		}
	}

	/**
	 *
	 */
	private void moveFloor() { // will go up/down to destination floor
		switch (direction) {
		case ('U'): // going up
			currentFloor += 1;
			break;
		case ('I'): // when idle, do not move if at bottom, if not, will move to case D, as no break to stop execution of case block
			if (currentFloor == 0)
				break;
		case ('D'): // going down
			currentFloor -= 1;
			break;
		}
	}

	/**
	 *
	 */
	private void setDirection(int destinationFloor) {
		if (destinationFloor != -1) {
			if (destinationFloor > currentFloor) { // lift needs to go up
				direction = 'U';
			} else { // implies lift needs to go down
				direction = 'D';
			}
		} else { // are no floors to go to so set to idle state
			direction = 'I';
		}
	}

	/**
	 *
	 */
	private boolean checkStop(Simulator sim, Floor currentFloorObj) { // checks if lift is needed to stop at its currentFloor.
		if (!queue.getOffload(sim, currentFloor).isEmpty() || requestsMap.get(currentFloor)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 */
	private int updateDestination(Simulator sim) { // checks up+down/down+up for reqs/ if not, assume idle state

		int nextUpDest = checkUp(sim);
		int nextDownDest = checkDown(sim);

		int destinationFloor = -1;

		if (direction == 'D') { // can only go down 
			if (nextDownDest != -1) { // if there was the need to go down, will return next floor down 
				destinationFloor = nextDownDest;
			} else if (nextUpDest != -1) { // if no more stops to do going down, will change direction and to check for any stops going up 
				destinationFloor = nextUpDest; // if there was the need to go up, will return highest floor
			}
		} else { // meaning going up or can only go up
			if (nextUpDest != -1) { // if there was the need to go up, will return next floor on the way up
				destinationFloor = nextUpDest;
			} else if (nextDownDest != -1) { // if no more stops to do going up, will change direction and to check for any stops going down
				destinationFloor = nextDownDest; // if there was the need to go down, will return next floor on the way down	
			}
		}

		// if no more stops to go to either way, goto ground + idle state
		return destinationFloor;
	}

	/**
	 *
	 */
	private int checkUp(Simulator sim) {
		int nextUpPerson = queue.getNextUpFloor(sim, currentFloor); // if is a floor, will return it, if not, returns noFloors
		int nextUpReq = getNextUpReq(sim); // if is a floor, will return it, if not, returns noFloors

		int highTest = Math.min(nextUpPerson, nextUpReq); // lowest (so closest when going up) between req's and peoples floor req's

		return (highTest == sim.getNoFloors()) ? -1 : highTest;  // if no floors from either, return -1. else return the floor

		//requestsList.keySet().stream().filter(floor -> requestsList.get(floor)) // filters the values of each floorNo key by if they map to true
		//.filter(floor -> floor > currentFloor) // gets floors above lift
		//.mapToInt(Integer::intValue) // convert each Integer to int, using method reference as intValue is static and the usual lambda expression is essentially just calling a method, not evaluating typed predicament.
		//.min().orElse(noFloors); // find min value from stream. if no value, return noFloors + 1 (meaning other val will be chosen, as needs min num.)
		//// get that value, as min() returns OptionalInt, such that there may not be any values in the stream, so not int, or vice versa 

	}

	/**
	 *
	 */
	private int checkDown(Simulator sim) {
		int nextDownPerson = queue.getNextDownFloor(sim, currentFloor);
		int nextDownReq = getNextDownReq(sim);

		int lowTest = Math.max(nextDownPerson, nextDownReq); // highest ( meaning closest when going down) out of either req's or people in lift

		return lowTest; // either -1, or a floor to goto downwards. in any case, value is valid
	}

	/**
	 *
	 */
	private int getNextUpReq(Simulator sim) {  // returns next up req floor. if none, returns noFloors
		int noFloors = sim.getNoFloors();
		int nextFloor = noFloors;

		for (int floor = currentFloor + 1; floor <= noFloors - 1; floor++) { // checks for requests going down from currentfloor to bottom. terminates when found
			if (requestsMap.get(floor)) {
				nextFloor = floor;
				break;
			}
		}
		return nextFloor;  // if no floors, return -1
	}

	/**
	 *
	 */
	private int getNextDownReq(Simulator sim) {  // return next down req. if none returns -1
		int nextFloor = -1;
		
		for(int floor = currentFloor - 1; floor >= 0; floor--) { // checks for requests going up from currentfloor to top floor. terminates when found
			if(requestsMap.get(floor)) {
				nextFloor = floor;
				break;
			}
		}
		return nextFloor;  // if no floors, return -1
	}

	/**
	 *
	 */
	private void offloadPeople(Simulator sim, Floor currentFloorObj, List<Integer> offload) { // offloads people from lift if needed
		if (!offload.isEmpty()) { // given there are people in the offload list to get off
			for (int pID : offload) {
				Person p = sim.getPerson(pID);
				removeFromLift(sim, currentFloorObj, p);

				if (p instanceof ArrivingPerson) {
					ArrivingPerson arrP = (ArrivingPerson) p;
					System.out.println();
					if ((!arrP.getToExit())) {
						arrP.setStartingTick(sim.getTick());
					}
				}
			}
		}
	}

	/**
	 *
	 */
	private void onloadPeople(Simulator sim, Floor currentFloorObj) { // operations to onload people on currentfloor if there are people are waiting
		if (requestsMap.get(currentFloor)) { // checks if people want to get on
			int waitingNum = currentFloorObj.getNumberWaiting(); // keeps track of people who have the capacity to get on. if not (meaning rivalry or too big), will decrement
			// will only perform the onload operation if there is space on the elevator
			// gets specified number of people from queue and adds it to current queue. gets indexes from 0 to the number of spaces on lift

			while (queue.getSpaces() > 0 && waitingNum > 0) {
				Person p = sim.getPerson(currentFloorObj.peekWaitingPerson());
				if (p.getWeight() <= queue.getSpaces()) {

					if (p instanceof Developer) { // checks if developer to move on
						List<String> companies = queue.getOffloadCompanies(sim);
						String devCompany = ((Developer) p).getCompany();
						if (companies.size() != 0 && !companies.contains(devCompany)) { // if opposite company of developer d is on lift, shift d to back of queue
							p.setTimeStamp(Simulator.getNewTimeStamp());
							currentFloorObj.moveFrontToBack();
							waitingNum--;
							continue; // cuts code body early and moves to next iteration. don't want to add person if moved to back of queue
						}
					}
					addToLift(sim, currentFloorObj, p);
				}

				waitingNum--; // if cannot fit on lift queue, or has moved person to lift, will lowers number waiting as a candidate to get on lift
			}

			requestsMap.put(currentFloor, false);

			if (!currentFloorObj.isWaitingEmpty()) { // checks if still people on waiting to get on, due to lift being full and people cannot get on
				currentFloorObj.reRequest();
			}
		}
	}
	
	private void addToLift(Simulator sim, Floor currentFloorObj, Person p) {
		queue.addPerson(p.getID());
		p.addToLift(sim.getTick());
		currentFloorObj.pollWaitingPerson(); // removes given person
	}
	
	
	private void removeFromLift(Simulator sim, Floor currentFloorObj, Person p) {
		int pID = p.getID();
		queue.removePerson(queue.indexOf(pID)); // removes from queue
		p.setCurrentFloor(currentFloor);
		currentFloorObj.addToFloor(pID);
	}

	/**
	 *
	 */
	private HashMap<Integer, Boolean> requestsListSetup(int noFloors) {
		HashMap<Integer, Boolean> reqList = new HashMap<Integer, Boolean>();
		for (int floorNo = 0; floorNo <= noFloors - 1; floorNo++) {
			reqList.put(floorNo, false);
		}
		return reqList;
	}

	/**
	 *
	 */
	public void addRequest(int floorNo) { // notifies lift the button is pressed at the floor given
		requestsMap.put(floorNo, true);
	}

	/**
	 *
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 *
	 */
	public List<Integer> getQueue() {
		return queue.getQueue();
	}

	/**
	 *
	 */
	public String getState() {
		return state;
	}
	
	
	public void addPersonTest(int pID) {  // for testing
		queue.addPerson(pID);
	}
	
	public boolean getRequestForTest(int floor) {
		return requestsMap.get(floor);
	}

	public char getDirectionForTest() {
		return direction;
	}

}
