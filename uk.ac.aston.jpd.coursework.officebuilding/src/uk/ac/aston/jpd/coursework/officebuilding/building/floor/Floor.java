package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;

public class Floor {

	private final int FLOORNO;
	private int toPress; // a toggle, in the case lift is full + people still waiting, will re press button and tell lift to stop when it next comes. button press occures after two ticks (one to close door, one after lift moves away), meaning values is at 3
	private LinkedList<Integer> waitingQueue;
	private List<Integer> onFloor;
	public Button button;

	public Floor(int floorNo, Button button) {
		FLOORNO = floorNo;
		toPress = 0;
		this.waitingQueue = new LinkedList<Integer>();
		onFloor = new ArrayList<Integer>();
		this.button = button;
	}

	public void tick(Building bld, Elevator e) {
		//System.out.println("Floor " + FLOORNO + ". onFloor: " + onFloor.size() + ". waiting: " + waitingQueue.size());
		if (button.isPressed()) { // operations related to if people have arrived to wait for list
			
			List<Integer> pressed = button.getPressed();
			for(int pID : pressed) {// will give floor pIDs of people waiting from button
				addRequest(pID);
			}

			e.addRequest(FLOORNO); // tells elevator people are waiting
		}

		if (toPress > 0) { // is an occasion where needs to re-request lift after 2 ticks (once to close door, once so it can move away)
			if (toPress >= 3) {  // toPress begins at 1, therefore is 3 (1 + 2 ticks)
				e.addRequest(FLOORNO);
				toPress = 0;
			} else {
				toPress += 1;
			}
		}
	}

	public boolean isWaiting() {
		return waitingQueue.isEmpty();
	}

	public void addRequest(int pressed) { // adds persons ID who pressed button to the waiting list 
		waitingQueue.add(pressed);
	}

	public void addToFloor(int pID) {
		onFloor.add(pID);
	}

	public void removeFromWaiting() {
		waitingQueue.remove();  // finds index of id so we can remove it
	}

	public void pressButton(int pID) {
		button.pressButton(pID);
	}

	public int getWaitingPerson() {
		return waitingQueue.peek();
	}

	public void reRequest() {
		toPress = 1;
	}

	public int getNumberWaiting() {
		return waitingQueue.size();
	}

	public boolean waitingIsEmpty() {
		return waitingQueue.isEmpty();
	}

	public void removeFromOnFloor(int pID) {
		onFloor.remove(onFloor.indexOf(pID));
	}
	
	public Queue<Integer> getWaitingQueue(){
		return waitingQueue;
	}

	public List<Integer> getOnFloorList() {
		return onFloor;
	}

}