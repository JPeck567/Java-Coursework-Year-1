package uk.ac.aston.jpd.coursework.officebuilding.building;

/*
*  The building class indicated what the building core contents
*  is currently the elevator
*
*  @author 	Jennifer A. Appiah
*  @version 1.0
*  @since 2020
*/
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Building {
	/*
	 *
	 * declaring fields
	 * 
	 */
	private final Floor[] floors;
	private final Elevator elevator;

	/*
	 *
	 * Class constructor constructing the elevator.
	 */
	public Building(Elevator e, int noFloors, Simulator sim) {
		this.elevator = e;
		floors = new Floor[noFloors];
		generateFloors(noFloors, sim);
	}

	public void tick(Simulator sim) {
		for (Floor f : floors) {
			f.tick(this, elevator);
		}
		elevator.tick(sim, this);
	}

	private void generateFloors(int noFloors, Simulator sim) {
		for (int i = 0; i < noFloors; i++) {
			floors[i] = new Floor(i, new Button(i), sim);
		}
	}

	public Floor getFloor(int floorNumber) {
		return floors[floorNumber];
	}

	public List<Integer> getElevatorQueue() {
		return elevator.getQueue();
	}

	public int getElevatorCurrentFloor() {
		return elevator.getCurrentFloor();
	}

	public String getElevatorState() {
		return elevator.getState();
	}
}
