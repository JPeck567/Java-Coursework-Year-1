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

/**
 * 
 * @author Team 46
 * @author 190148289 Jennifer A. Appiah
 * @author 190095097 Hannah Elliman
 * @author 190055002 Jorge Peck
 * @author 190174923 Hongyi Wang
 * @version 1.0
 * @since 2020 Coursework
 * 
 * 
 * @summary This Class stores attributes related to the elevator and methods
 *          which operate on the attributes and data based on the simulation at
 *          each tick
 */
public class Elevator {

	/**
	 * Declaring Fields
	 */
	private String doorState;
	private final PList pList; // space to hold people to get in the elevator
	private final Map<Integer, Boolean> requestsMap; // to know where people are to get in lift
	private char direction;
	private int currentFloor;

	/**
	 * This method is the constructor which initialises the variables
	 * 
	 * @param list     this provides the elevator with a space to store the people
	 *                 inside
	 * @param noFloors used to create a map of request to each floor
	 */
	public Elevator(PList list, int noFloors) {
		direction = 'I'; // meaning idle and not moving anywhere
		currentFloor = 0;
		doorState = "close"; // initially closed. will change to open then ready then close.

		this.pList = list;
		requestsMap = requestsListSetup(noFloors);

	}

	/**
	 * This method will make the lift carry out it's functions based on the current
	 * state of the simulation
	 * 
	 * @param sim provide interface between the lift and other classes
	 * @param bld provide interface between the lift and other elements the building
	 *            has
	 * 
	 */
	public void tick(Simulator sim, Building bld) {
		Floor currentFloorObj = bld.getFloor(currentFloor);

		if (checkStop(sim) || doorState.equals("open")) {
			openCloseMechanism(sim, currentFloorObj);
		} else {
			moveFloor();
		}

		setDirection(updateDestination(sim));
	}

	/**
	 * This method will open or close the elevator and carry out the functions
	 * related to these
	 * 
	 * @param sim             this is used to interface with the people handler
	 *                        class
	 * @param currentFloorObj this is to interface with the current floor object the
	 *                        elevator is on
	 */
	private void openCloseMechanism(Simulator sim, Floor currentFloorObj) {
		if (doorState.equals("close")) { // stops movement and opens door
			doorState = "open";

			offloadPeople(sim, currentFloorObj, pList.getOffload(sim, currentFloor)); // people in queue needing to go
																						// out will do.
			onloadPeople(sim, currentFloorObj);
		} else if (doorState.equals("open")) { // closes door, finds new nearest dest and goes to it
			doorState = "close";
		}
	}

	/**
	 * This method will move the elevator up/down based on what direction it needs
	 * to move
	 */
	private void moveFloor() {
		switch (direction) {
		case ('U'): // going up
			currentFloor += 1;
			break;
		case ('I'): // when idle, do not move if at bottom, if not, will move to case D, as no break
					// to stop execution of case block
			if (currentFloor == 0)
				break;
		case ('D'): // going down
			currentFloor -= 1;
			break;
		}
	}

	/**
	 * This method will decide which direction the lift needs to go
	 * 
	 * @param destinationFloor this is used to compare with the current floor
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
	 * This method is to check if the lift is needed to stop at its currentFloor
	 * 
	 * @param sim this is used for the getOffload method of the pList
	 * @return boolean
	 */
	private boolean checkStop(Simulator sim) {
		if (!pList.getOffload(sim, currentFloor).isEmpty() || requestsMap.get(currentFloor)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is to check for the next request from either people in the lift,
	 * or people on the floor, based on the elevators direction. If no requests,
	 * assumes idle state
	 * 
	 * @param sim this is used to provide access to the people object based on their
	 *            ID's
	 * @return if -1 there isn't a request, otherwise it will return the floor that
	 *         the elevator needs to go to
	 */
	private int updateDestination(Simulator sim) {

		int nextUpDest = checkUp(sim);
		int nextDownDest = checkDown(sim);

		int destinationFloor = -1;

		if (direction == 'D') { // can only go down
			if (nextDownDest != -1) { // if there was the need to go down, will return next floor down
				destinationFloor = nextDownDest;
			} else if (nextUpDest != -1) { // if no more stops to do going down, will change direction and to check for
											// any stops going up
				destinationFloor = nextUpDest; // if there was the need to go up, will return highest floor
			}
		} else { // meaning going up or can only go up
			if (nextUpDest != -1) { // if there was the need to go up, will return next floor on the way up
				destinationFloor = nextUpDest;
			} else if (nextDownDest != -1) { // if no more stops to do going up, will change direction and to check for
												// any stops going down
				destinationFloor = nextDownDest; // if there was the need to go down, will return next floor on the way
													// down
			}
		}

		// if no more stops to go to either way, goto ground + idle state
		return destinationFloor;
	}

	/**
	 * This method checks for the closest person destination above the elevator's
	 * current floor, from only the people in the lift
	 * 
	 * @param sim this is used to provide access to the people object based on their
	 *            ID's
	 * @return if -1 there isn't a request, otherwise it will return the persons
	 *         destination above the elevator's current floor
	 */
	private int checkUp(Simulator sim) {
		int nextUpPerson = pList.getNextUpFloor(sim, currentFloor); // if is a floor, will return it, if not, returns
																	// noFloors
		int nextUpReq = getNextUpReq(sim); // if is a floor, will return it, if not, returns noFloors

		int highTest = Math.min(nextUpPerson, nextUpReq); // lowest (so closest when going up) between req's and peoples
															// floor req's

		return (highTest == sim.getNoFloors()) ? -1 : highTest; // if no floors from either, return -1. else return the
																// floor

		// requestsList.keySet().stream().filter(floor -> requestsList.get(floor)) //
		// filters the values of each floorNo key by if they map to true
		// .filter(floor -> floor > currentFloor) // gets floors above lift
		// .mapToInt(Integer::intValue) // convert each Integer to int, using method
		// reference as intValue is static and the usual lambda expression is
		// essentially just calling a method, not evaluating typed predicament.
		// .min().orElse(noFloors); // find min value from stream. if no value, return
		// noFloors + 1 (meaning other val will be chosen, as needs min num.)
		//// get that value, as min() returns OptionalInt, such that there may not be
		// any values in the stream, so not int, or vice versa

	}

	/**
	 * This method checks for the closest person destination below the elevator's
	 * current floor, from only the people in the lift
	 * 
	 * @param sim this is used to provide access to the people object based on their
	 *            ID's
	 * @return if -1 there isn't a request, otherwise it will return the persons
	 *         destination below the elevator's current floor
	 */
	private int checkDown(Simulator sim) {
		int nextDownPerson = pList.getNextDownFloor(sim, currentFloor);
		int nextDownReq = getNextDownReq(sim);

		int lowTest = Math.max(nextDownPerson, nextDownReq); // highest ( meaning closest when going down) out of either
																// req's or people in lift

		return lowTest; // either -1, or a floor to goto downwards. in any case, value is valid
	}

	/**
	 * This method checks for the closest floor request above the elevator's current
	 * floor, from only the people waiting in the building's floors
	 * 
	 * @param sim this is used to provide access to the people object based on their
	 *            ID's
	 * @return if -1 there isn't a request, otherwise it will return the closest
	 *         floor request above the elevator's current floor
	 */

	private int getNextUpReq(Simulator sim) { // returns next up req floor. if none, returns noFloors
		int noFloors = sim.getNoFloors();
		int nextFloor = noFloors;

		for (int floor = currentFloor + 1; floor <= noFloors - 1; floor++) { // checks for requests going down from
																				// current floor to bottom. terminates
																				// when found
			if (requestsMap.get(floor)) {
				nextFloor = floor;
				break;
			}
		}
		return nextFloor; // if no floors, return -1
	}

	/**
	 * This method checks for the closest floor request below the elevator's current
	 * floor, from only the people waiting in the building's floors
	 * 
	 * @param sim this is used to provide access to the people object based on their
	 *            ID's
	 * @return if -1 there isn't a request, otherwise it will return the closest
	 *         floor request below the elevator's current floor
	 */

	private int getNextDownReq(Simulator sim) { // return next down req. if none returns -1
		int nextFloor = -1;

		for (int floor = currentFloor - 1; floor >= 0; floor--) { // checks for requests going up from currentfloor to
																	// top floor. terminates when found
			if (requestsMap.get(floor)) {
				nextFloor = floor;
				break;
			}
		}
		return nextFloor; // if no floors, return -1
	}

	/**
	 * This method takes the people off the elevator onto the destination floor
	 * 
	 * @param sim             this provides an interface to the people handler
	 *                        object
	 * @param currentFloorObj this lets the elevator put the people's ID's onto the
	 *                        floor objects onFloor list
	 * @param offload         a list of the people id's who need to move off the
	 *                        elevator at it's current floor
	 */
	private void offloadPeople(Simulator sim, Floor currentFloorObj, List<Integer> offload) { // offloads people from
																								// lift if needed
		if (!offload.isEmpty()) { // given there are people in the offload list to get off
			for (int pID : offload) {
				Person p = sim.getPerson(pID);
				removeFromElevator(sim, currentFloorObj, p);

//				if (p instanceof ArrivingPerson) {
//					ArrivingPerson arrP = (ArrivingPerson) p;
//					if ((!arrP.getToExit())) {
//						arrP.setStartingTick(sim.getTick());
//					}
//				}
			}
		}
	}

	/**
	 * This method moves people onto the lift's currentFloor if there are people are
	 * waiting, and if they can fit or not, based on their weight
	 * 
	 * @param sim             this provides an interface to the people handler
	 *                        object
	 * @param currentFloorObj this lets the elevator put the people's ID's onto the
	 *                        lifts pList object
	 */
	private void onloadPeople(Simulator sim, Floor currentFloorObj) {
		if (requestsMap.get(currentFloor)) { // checks if people want to get on
			int waitingNum = currentFloorObj.getNumberWaiting(); // keeps track of people who have the capacity to get
																	// on. if not (meaning rivalry or too big), will
																	// decrement
																	// will only perform the onload operation if there is space on the elevator
																	// gets specified number of people from queue and adds it to current queue. gets
																	// indexes from 0 to the number of spaces on lift

			while (pList.getSpaces() > 0 && waitingNum > 0) {
				Person p = sim.getPerson(currentFloorObj.peekWaitingPerson());
				if (p.getWeight() <= pList.getSpaces()) {

					if (p instanceof Developer) { // checks if developer to move on
						List<String> companies = pList.getOffloadCompanies(sim);
						String devCompany = ((Developer) p).getCompany();
						String rivalCompany = PersonHandler.COMPANIES[devCompany == PersonHandler.COMPANIES[0] ? 1: 0];  // if first company, rival is 2nd, else rival is 1st
						if (companies.contains(rivalCompany)) { // if opposite company of
																						// developer d is on lift, shift
																						// d to back of queue
							p.setTimeStamp(Simulator.getNewTimeStamp());
							currentFloorObj.moveFrontToBack();
							waitingNum--;
							continue; // cuts code body early and moves to next iteration. don't want to add person if
										// moved to back of queue
						}
					}
					addToElevator(sim, currentFloorObj, p);
				}

				waitingNum--; // if cannot fit on lift queue, or has moved person to lift, will lowers number
								// waiting as a candidate to get on lift
			}

			requestsMap.put(currentFloor, false);

			if (!currentFloorObj.isWaitingEmpty()) { // checks if still people on waiting to get on, due to lift being
														// full and people cannot get on
				currentFloorObj.reRequest();
			}
		}
	}

	/**
	 * This method calls the functions related to adding a person to the elevator
	 * 
	 * @param sim             this is used in the addToElevator to provide the
	 *                        current tick of the simulation
	 * @param currentFloorObj this is used to provide a way of removing the person
	 *                        ID from the floor
	 * @param p               this gives the person we need to add
	 */
	private void addToElevator(Simulator sim, Floor currentFloorObj, Person p) {
		pList.addPerson(p.getID());
		p.moveOnElevator(sim.getTick());
		currentFloorObj.pollWaitingPerson(); // removes given person
	}

	/**
	 * 
	 * This method calls the functions related to removing a person on the elevator
	 * 
	 * @param sim             this is used in the removeFromElevator to provide the
	 *                        current tick of the simulation
	 * @param currentFloorObj this is used to provide a way of removing the person
	 *                        ID from the elevator
	 * @param p               this gives the person we need to remove
	 */
	private void removeFromElevator(Simulator sim, Floor currentFloorObj, Person p) {
		int pID = p.getID();
		pList.removePerson(pList.indexOf(pID)); // removes from queue
		p.moveOffElevator(sim.getTick());
		currentFloorObj.addToFloor(pID);
	}

	/**
	 * This method creates a mapping of each floor number to a boolean value
	 * 
	 * @param noFloors this indicates the number of floors in the building
	 * @return this returns a complete hashmap of floor numbers to boolean values.
	 */
	private HashMap<Integer, Boolean> requestsListSetup(int noFloors) {
		HashMap<Integer, Boolean> reqList = new HashMap<Integer, Boolean>();
		for (int floorNo = 0; floorNo <= noFloors - 1; floorNo++) {
			reqList.put(floorNo, false);
		}
		return reqList;
	}

	/**
	 * This method maps a true boolean to the floor number in the request map,
	 * meaning the a button at a floor had been pressed
	 * 
	 * @param floorNo the specified floor that a request is sent from
	 */
	public void addRequest(int floorNo) {
		requestsMap.put(floorNo, true);
	}

	/**
	 * This method return the current floor of the elevator
	 * 
	 * @return this returns an integer representing the current floor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * This method returns the list of people Id's travelling on the elevator
	 * 
	 * @return this return a list of integers, which pList gives from it's getList
	 *         function
	 */
	public List<Integer> getList() {
		return pList.getList();
	}

	/**
	 * This method returns the current state of the elevator's door
	 * 
	 * @return this returns a string representing the state of the door
	 */
	public String getDoorState() {
		return doorState;
	}
}
