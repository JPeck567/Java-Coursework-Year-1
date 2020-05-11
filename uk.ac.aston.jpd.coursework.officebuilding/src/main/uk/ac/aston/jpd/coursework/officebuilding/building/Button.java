package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Team 46
 * @author 190148289 Jennifer A. Appiah
 * @author 190095097 Hannah Elliman
 * @author 190055002 Jorge Peck
 * @author 190174923 Hongyi Wang
 * @version 1.0
 * @since 2020 Coursework
 * 
 * 
 * @summary This Class stores the ID of the people who request for the lift on
 *          the current floor.
 * 
 */
public class Button {
	/**
	 * Declaring fields
	 *
	 */
	private final int FLOORNO;
	private final ArrayList<Integer> pressedList;

	/**
	 * This method is the constructor which initialises the variables
	 * 
	 * @param floorNo identifies the floor the button is on
	 * 
	 */
	public Button(int floorNo) {
		FLOORNO = floorNo;
		pressedList = new ArrayList<Integer>();
	}

	/**
	 * This method identifies the user by their ID and adds the to the pressed list
	 * 
	 * @param pID Person object identification
	 */
	public void pressButton(int pID) {
		pressedList.add(pID);
	}

	/**
	 * This method returns pressed list and clears it
	 * 
	 * @return returns a list with people's ID
	 */
	public List<Integer> getPressed() { // returns pressed list and clears it
		List<Integer> pressedCopy = new ArrayList<Integer>(pressedList);
		pressedList.clear();
		return pressedCopy;
	}

	/**
	 * This Method is to check if the button is pressed
	 * 
	 * @return returns a boolean based on if the list has entries or not
	 */
	public boolean isPressed() {
		return !pressedList.isEmpty();
	}
}