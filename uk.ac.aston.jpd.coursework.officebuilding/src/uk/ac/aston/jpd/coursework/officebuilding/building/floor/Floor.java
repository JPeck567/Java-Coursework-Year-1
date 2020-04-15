package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;

public class Floor {

	private final int FLOORNO;
	private int toPress; // a toggle, in the case lift is full + people still waiting, will re press button and tell lift to stop when it next comes. button press occures after two ticks (one to close door, one after lift moves away), meaning values is at 3
	private PQueue waiting;
	private List<Integer> onFloor;
	public Button button;

	public Floor(int floorNo, PQueue waiting, Button button) {
		FLOORNO = floorNo;
		toPress = 0;
		this.waiting = waiting;
		onFloor = new ArrayList<Integer>();
		this.button = button;
	}

	public void tick(Building bld, Elevator e) {
		if (button.isPressed()) { // operations related to if people have arrived to wait for list
			button.tick(this); // will give floor pIDs of people waiting
			e.addRequest(FLOORNO); // tells elevator people are waiting
		}

		if (toPress > 0) { // is an occasion where needs to re-request lift after 2 ticks (once to close door, once so it can move away)
			if (toPress >= 3) {
				e.addRequest(FLOORNO);
				toPress = 0;
			} else {
				toPress += 1;
			}
		}
	}

	public boolean isWaiting() {
		return waiting.isEmpty();
	}

	public void addRequest(int pressed) { // adds peopleID who pressed button to the waiting list 
		waiting.addPerson(pressed);
	}

	public void addToFloor(ArrayList<Integer> offloaded) {
		for (int pID : offloaded) {
			onFloor.add(pID);
		}
	}

	public void removeFromWaiting(ArrayList<Integer> onloaded) {
		for (int pID : onloaded) {
			waiting.removePerson(waiting.indexOf(pID));
		}
	}

	public void pressButton(int pID) {
		button.pressButton(pID);
	}

	public int getWaitingPerson(int index) {
		return waiting.getPerson(index);
	}

	public void reRequest() {
		toPress = 1;
	}

	public int getNumberWaiting() {
		return waiting.getSize();
	}

}