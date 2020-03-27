package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PQueue;

public class Floor {
	
	private PQueue waiting;

	public Floor(int i, PQueue waiting) {
		// TODO Auto-generated constructor stub
	}

	public void tick() {
		// TODO Auto-generated method stub
	}
	
	public boolean isEmpty() {
		return waiting.isEmpty();
	}
	
}