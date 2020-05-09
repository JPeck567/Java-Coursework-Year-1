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

public class Building {
	/**
	 * declaring fields
	 * @param floors
	 * @param elevator
	 */
	private final Floor[] floors;
	private final Elevator elevator;

	/**
	 * Class constructor constructing the elevator.
	 * @param e
	 * @param noFloors
	 * @param c
	 * 
	 */
	public Building(Elevator e, int noFloors, WaitingQueueComparator c) {
		this.elevator = e;
		floors = new Floor[noFloors];
		generateFloors(noFloors, c);
	}
/**
 *
 * @param sim
 * 
 */
	public void tick(Simulator sim) {
		for (Floor f : floors) {
			f.tick(this, elevator);
		}
		elevator.tick(sim, this);
	}
/**
 * 
 * @param noFloors
 * @param c
 * 
 */
	private void generateFloors(int noFloors, WaitingQueueComparator c) {
		for (int i = 0; i < noFloors; i++) {
			floors[i] = new Floor(i, new Button(i), c);
		}
	}
/**
 * @param floorNumber
 * 
 */
	public Floor getFloor(int floorNumber) {
		return floors[floorNumber];
	}
	/**
	 *
	 */
	public List<Integer> getElevatorQueue() {
		return elevator.getQueue();
	}
	/**
	 *
	 */
	public int getElevatorCurrentFloor() {
		return elevator.getCurrentFloor();
	}
	/**
	 *
	 */
	public String getElevatorState() {
		return elevator.getState();
		
		
	}
	public Elevator getElevatorForTest() {
		return elevator;
	}
}