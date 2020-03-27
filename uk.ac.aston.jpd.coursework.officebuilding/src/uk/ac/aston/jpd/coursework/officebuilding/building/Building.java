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
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;

public class Building {
/*
*
*  declaring fields 
* 
*/	private Floor[] floors;
	private Elevator elevator;
	// private final int NOOFFLOORS; constant and doesn't change. Taken out ask you can just ref simulators constant
	
	
 /*
 *
 * Class constructor constructing the elevator.
 */
	public Building (Elevator e){
		this.elevator = e;
		generateFloors();
	}
	
	public void tick(Simulator sim){
		elevator.tick(sim, this);
			for (Floor f : floors){
				f.tick();
			}
		}
	
	private void generateFloors(){
		
		for(int i = 0; i < Simulator.FLOORNO; i++){
			floors[i] = new Floor(i, new PQueue(100)); // queues on each floor are 
		}
	}
	
	public int getNumberOfFloor(){
		return floors.length;
	}
	
	public Floor getFloor(int floorNumber) {
		return floors[floorNumber];
	}
}
