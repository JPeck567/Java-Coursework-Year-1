package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PList;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

/**
*
*/
public class Floor {
	/**
	 *
	 */
	private final int FLOORNO;
	private int toPress; // a toggle, in the case lift is full + people still waiting, will re press button and tell lift to stop when it next comes. button press occures after two ticks (one to close door, one after lift moves away), meaning values is at 3
	private final Queue<Integer> waitingQueue;
	private final List<Integer> onFloor;
	public final Button button;
	
	/**
	 *
	 */
	public Floor(int floorNo, Button button, WaitingQueueComparator c) {
		FLOORNO = floorNo;
		toPress = 0;//
		this.waitingQueue = new PriorityQueue<Integer>(c);
		onFloor = new ArrayList<Integer>();
		this.button = button;
	}

	/**
	 *
	 */
	public void tick(Building bld, Elevator e) {
		//System.out.println("Floor " + FLOORNO + ". onFloor: " + onFloor.size() + ". waiting: " + waitingQueue.size());
		if (button.isPressed()) { // operations related to if people have arrived to wait for list
			
			List<Integer> pressed = button.getPressed();
			for(int pID : pressed) {// will give floor pIDs of people waiting from button
				addToWaiting(pID);
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
	
	/**
	 *
	 */
	public boolean isWaitingEmpty() {
		return waitingQueue.isEmpty();
	}

	/**
	 *
	 */
	public void addToFloor(int pID) {
		onFloor.add(pID);
	}
	
	/**
	 *
	 */
	private void addToWaiting(int pID) { // adds persons ID who pressed button to the waiting list 
		waitingQueue.add(pID);
	}
	
	/**
	 *
	 */
	public void removeFromWaiting(int pID) {
		waitingQueue.remove(pID);  // finds index of id so we can remove it if they are a impatient client
	}
	
	/**
	 *
	 */
	public int peekWaitingPerson() {
		return waitingQueue.peek();
	}
	
	/**
	 *
	 */
	public int pollWaitingPerson() {
		return waitingQueue.poll();
	}
	
	/**
	 *
	 */
	public void moveFrontToBack() {
		waitingQueue.add(waitingQueue.poll());  // poll returns head and add will add the returned element to the queue in fifo fashion
	}
	
	/**
	 *
	 */
	public void pressButton(int pID) {
		button.pressButton(pID);
	}
	
	/**
	 *
	 */
	public void reRequest() {
		toPress = 1;
	}
	
	/**
	 *
	 */
	public void removeFromOnFloor(int pID) {
		onFloor.remove(onFloor.indexOf(pID));
	}
	
	/**
	 *
	 */
	public Queue<Integer> getWaitingQueue(){
		return new PriorityQueue<Integer>(waitingQueue);
	}
	
	/**
	 *
	 */
	public List<Integer> getOnFloorList() {
		return new ArrayList<Integer>(onFloor);
	}
	
	/**
	 *
	 */
	public int getNumberWaiting() {
		return waitingQueue.size();
	}
}