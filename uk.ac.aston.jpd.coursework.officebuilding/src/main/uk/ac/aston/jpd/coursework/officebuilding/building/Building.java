package uk.ac.aston.jpd.coursework.officebuilding.building;

/**
 * 
*  @summary The building class indicated what the building core contents
*  is currently the elevator
*
*  @version 1.0
*  @since 2020
*  
*/

import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.WaitingQueueComparator;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * @summary The building class holds the objects for the floors and the elevator
 *          and runs their functions accordingly.
 * 
 */
public class Building {
	/**
	 * declaring fields
	 * 
	 * @param floors   Variable type floor
	 * @param elevator Variable type elevator
	 */
	private final Floor[] floors;
	private final Elevator elevator;

	/**
	 * Class constructor constructing the elevator.
	 * 
	 * @param e        Passing in the elevator object
	 * @param noFloors Constructing and generating a number of floors based on the
	 *                 variable
	 * @param c        floor uses comparator c as part of it's priority queue.
	 * 
	 */
	public Building(Elevator e, int noFloors, WaitingQueueComparator c) {
		this.elevator = e;
		floors = new Floor[noFloors];
		generateFloors(noFloors, c);
	}

	/**
	 * Tick method calls tick for the floors and elevator
	 * 
	 * @param sim elevator uses simulator as part of it's functions
	 * 
	 */
	public void tick(Simulator sim) {
		for (Floor f : floors) {
			f.tick(elevator);
		}
		elevator.tick(sim, this);
	}

	/**
	 * This method generate floors passing in the simulator and a button object for
	 * the constructor of floor.
	 * 
	 * @param noFloors Uses this integer to know how many loops are required
	 * 
	 * @param c        Floor uses the comparator as part of it's priority queue
	 * 
	 */
	private void generateFloors(int noFloors, WaitingQueueComparator c) {
		for (int i = 0; i < noFloors; i++) {
			floors[i] = new Floor(i, new Button(i), c);
		}
	}

	/**
	 * This method get a specified floor
	 * 
	 * @param floorNumber this parameter get's the floor number
	 * @return this returns the specified floor object
	 * 
	 */
	public Floor getFloor(int floorNumber) {
		return floors[floorNumber];
	}

	/**
	 * This method returns a copy of the list of the people in the elevator
	 * 
	 * @return a list object with the Id's of each person in the elevator
	 */
	public List<Integer> getElevatorQueue() {
		return elevator.getList();
	}

	/**
	 * This method returns the current floor of the elevator
	 * 
	 * @return this returns the integer representing the current floor.
	 */
	public int getElevatorCurrentFloor() {
		return elevator.getCurrentFloor();
	}

	/**
	 * This method returns the state of the elevator
	 * 
	 * @return this returns the string stating if the elevator is open/close
	 */
	public String getElevatorState() {
		return elevator.getDoorState();
	}
}
