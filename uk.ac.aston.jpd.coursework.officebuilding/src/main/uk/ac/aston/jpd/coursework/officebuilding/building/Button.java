package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
/**
*
*/
public class Button {
	private final int FLOORNO;
	private final ArrayList<Integer> pressedList;
	
	/**
	 *
	 */
	public Button(int floorNo) {
		FLOORNO = floorNo;
		pressedList = new ArrayList<Integer>();
	}
	
	/**
	 *
	 */
	public void pressButton(int pID) {
		pressedList.add(pID);
	}
	
	/**
	 *
	 */
	public List<Integer> getPressed() {  // returns pressed list and clears it
		List<Integer> pressedCopy = new ArrayList<Integer>(pressedList);
		pressedList.clear();
		return pressedCopy;
	}
	
	/**
	 *
	 */
	public boolean isPressed() {
		return !pressedList.isEmpty();
	}
}