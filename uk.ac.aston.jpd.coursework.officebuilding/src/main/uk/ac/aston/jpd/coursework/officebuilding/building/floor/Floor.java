package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;

import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;

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
 * @summary This Class provides a way of storing the people's id's who are on
 *          the floor and the people's id's who are waiting for the lift. It
 *          also provides a interface to operate on this data
 */

public class Floor {
	/**
	 * Declaring Fields
	 */
	private final int FLOORNO;
	// button and tell lift to stop when it next comes. button press occures after
							// two ticks (one to close door, one after lift moves away), meaning values is
							// at 3
	private final Queue<Integer> waitingQueue;
	private final List<Integer> onFloor;
	private int toPress; // a toggle, in the case lift is full + people still waiting, will re press
	public final Button button;

	/**
	 * This is the constructing method to create a button object as well as the a
	 * list for the people on the floor, and a priority queue for people who are
	 * waiting
	 * 
	 * @param floorNo initialising the field floorNo
	 * @param button  initialising the field button
	 * @param c       this is used for constructing the priority queue
	 */
	public Floor(int floorNo, Button button, WaitingQueueComparator c) {
		FLOORNO = floorNo;
		toPress = 0;//
		this.waitingQueue = new PriorityQueue<Integer>(c);
		onFloor = new ArrayList<Integer>();
		this.button = button;
	}

	/**
	 * This method is to run the operations relating to loading people onto the
	 * waiting queue and notifying the elevator that there are people waiting
	 * 
	 * @param e this is the elevator object used to tell it that there are people
	 *          waiting
	 */
	public void tick(Elevator e) {
		// System.out.println("Floor " + FLOORNO + ". onFloor: " + onFloor.size() + ".
		// waiting: " + waitingQueue.size());
		if (button.isPressed()) { // operations related to if people have arrived to wait for list
			addPressedToWaiting(e, button.getPressed());
		}
		reRequestCheck(e);
	}

	/**
	 * This method is to tell if the waiting queue is empty or not
	 * 
	 * @return this returns a boolean to tell if the queue is empty or not
	 */
	public boolean isWaitingEmpty() {
		return waitingQueue.isEmpty();
	}

	/**
	 * This method puts the person's id onto the onFloor list
	 * 
	 * @param pID the is the person's id to add to the list
	 */
	public void addToFloor(int pID) {
		onFloor.add(pID);
	}

	/**
	 * This method puts the person's id onto the waiting queue
	 * 
	 * @param pID the is the person's id to add to the queue
	 */

	private void addToWaiting(int pID) { // adds persons ID who pressed button to the waiting list
		waitingQueue.add(pID);
	}

	/**
	 * This method removes the person's id from the waiting queue in a non FIFO
	 * fashion
	 * 
	 * @param pID the is the person's id to remove from the queue
	 */
	public void removeFromWaiting(int pID) {
		waitingQueue.remove(pID); // finds index of id so we can remove it if they are a impatient client
	}

	/**
	 * This method returns the id of the person at the front of the queue without
	 * removing them
	 * 
	 * @return this returns the id of the person
	 */
	public int peekWaitingPerson() {
		return waitingQueue.peek();
	}

	/**
	 * This method returns the id of the person at the front of the queue and
	 * removes them
	 * 
	 * @return this returns the id of the person
	 */
	public int pollWaitingPerson() {
		return waitingQueue.poll();
	}

	/**
	 * This method removes the person's id from the head of the waiting queue and
	 * will add the element back on the queue in FIFO fashion
	 * 
	 */
	public void moveFrontToBack() {
		waitingQueue.add(waitingQueue.poll());
	}

	/**
	 * This method provides the person to press the button on it's current floor
	 * 
	 * @param pID this is the person id
	 */
	public void pressButton(int pID) {
		button.pressButton(pID);
	}

	/**
	 * This method checks if there needs to be a re-request for the elevator to stop
	 * at this floor, if the elevator was full during onload, and so not allowing
	 * people on. If so, it will count 2 ticks and by then, it will add a new
	 * request to the elevator.
	 * 
	 * @param e this is the elevator object used to tell it that there are people
	 *          waiting
	 */
	private void reRequestCheck(Elevator e) {
		if (toPress > 0) { // is an occasion where needs to re-request lift after 2 ticks (once to close
			// door, once so it can move away)
			if (toPress >= 3) { // toPress begins at 1, therefore is 3 (1 + 2 ticks)
				e.addRequest(FLOORNO);
				toPress = 0;
			} else {
				toPress += 1;
			}
		}
	}

	/**
	 * This method add the people id to who presses the button onto the waiting
	 * queue
	 * 
	 * @param e       this is the elevator object used to tell it that there are
	 *                people waiting
	 * @param pressed this is a list of people id's who pressed the button
	 */
	public void addPressedToWaiting(Elevator e, List<Integer> pressed) {
		for (int pID : pressed) {// will give floor pIDs of people waiting from button
			addToWaiting(pID);
		}

		if(toPress == 0) {  // if there isn't the need for a rerequest
			e.addRequest(FLOORNO); // tells elevator people are waiting
		}	
	}

	/**
	 * This method indicates that the floor needs to re-request the elevator
	 */
	public void reRequest() {
		toPress = 1;
	}

	/**
	 * This method removes the person's id from the onFloor list
	 * 
	 * @param pID this is the person's id
	 */
	public void removeFromOnFloor(int pID) {
		onFloor.remove(onFloor.indexOf(pID));
	}

	/**
	 * This method returns a copy of the waiting queue
	 * 
	 * @return this returns a new priority queue with the contents from the original
	 *         waiting queue
	 */
	public Queue<Integer> getWaitingQueue() {
		return new PriorityQueue<Integer>(waitingQueue);
	}

	/**
	 * This method returns a copy of the onFloor list
	 * 
	 * @return this returns a new list with the contents from the original onFloor
	 *         list
	 */
	public List<Integer> getOnFloorList() {
		return new ArrayList<Integer>(onFloor);
	}

	/**
	 * This method returns the size of the waiting queue
	 * 
	 * @return this is the number of people waiting
	 */
	public int getNumberWaiting() {
		return waitingQueue.size();
	}
}