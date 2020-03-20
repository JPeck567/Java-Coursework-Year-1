package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;

public class Building {
	//declaring fields
	private List <Floor> floors = new ArrayList <Floor>();;
	private Elevator elevator;
	
	//constuctor
	public Building (Elevator e, int floors){
	this.floors = floors; 
	}
	
	public void tick(){
	elevator.tick();
		for (Floor f : floors){
		f.tick();
		}
	}
	private int generateFloors(){
		for(int i = 0; i <8; i++){
			Floor f = new Floor(i);
			floor.add(f);
		}
	}
	public int getNumberOfFloor(){
	return floors.size();
	}
}
