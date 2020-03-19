package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorQueue {
	private List<Integer> goingUp;
	private List<Integer> goingDown;
	
	public ElevatorQueue() {
		goingUp = new ArrayList<Integer>();
		goingDown = new ArrayList<Integer>();
	}
}