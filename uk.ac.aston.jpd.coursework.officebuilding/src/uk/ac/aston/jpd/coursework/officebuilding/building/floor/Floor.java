package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;

public class Floor {

	private final int FLOORNO;
	private PQueue waiting;
	private List<Integer> onFloor;
	private Button button;

	public Floor(int floorNo, PQueue waiting, Button button) {
		FLOORNO = floorNo;
		this.waiting = waiting;
		onFloor = new ArrayList<Integer>();
		this.button = button;
	}

	public void tick(Building bld) {
		button.tick(this);
	}

	public boolean isEmpty() {
		return waiting.isEmpty();
	}

	public void addRequests(ArrayList<Integer> pressed) {  // adds list of people who pressed button to waiting list 
		waiting.addPeople(pressed);
		// TODO Auto-generated method stub
		
	}
}