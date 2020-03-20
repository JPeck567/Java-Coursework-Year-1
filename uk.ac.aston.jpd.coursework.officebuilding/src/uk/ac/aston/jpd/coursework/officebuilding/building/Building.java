package uk.ac.aston.jpd.coursework.officebuilding.building;

import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;

public class Building {
	//declaring fields
	private Floor[] floors;
	private int noOfFloors
	private Elevator elevator;
	
	
	//constuctor
	public Building (Elevator e, int noOfFloors){
		this.elevator = e;
		this.noOfFloors = noOfFloors;
		generateFloors();
	}
	
	public void tick(Simulator sim){
	elevator.tick();
		for (Floor f : floors){
			f.tick();
		}
	}
	private int generateFloors(){
		for(int i = 0; i < noOfFloors; i++){
			floors[i] = new Floor(i);
		}
	}
	public int getNumberOfFloor(){
		return floors.size();
	}
}
