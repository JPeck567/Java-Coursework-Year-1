package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;

public class Floor {
	
	private int floorNo;
	private PQueue waiting;
	private Button button;

	public Floor(int floorNo, PQueue waiting, Button button) {
		this.waiting = waiting;
		this.floorNo = floorNo;
		this.button = button;
		
	}

	public void tick(Building bld) {
		button.tick(bld);
		
	}
	
	public boolean isEmpty() {
		return waiting.isEmpty();
	}
	
}