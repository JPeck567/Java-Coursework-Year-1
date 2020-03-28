package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.List;

public class Button {
	private final int FLOORNO;
	private ArrayList<Integer> pressedList;
	
	public Button(int floorNo) {
		FLOORNO = floorNo;
		pressedList = new ArrayList<Integer>();
	}
	
	public void pressButton(int pID) {
		pressedList.add(pID);
	}
	
	public ArrayList<Integer> getPressed() {
		ArrayList<Integer> copy = pressedList;
		pressedList.clear();
		return copy;
	}

	public void tick(Building bld) {
		if(!pressedList.isEmpty()) {
			bld.addRequests(FLOORNO, getPressed());
		}
	}
}