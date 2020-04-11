package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.util.Queue;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;


//simplify by making just a queue
//just let elevator go up and down.
public class PQueue {
	private List<Integer> queue;
	private final int SIZE;
	
	public PQueue(int size) {
		this.SIZE = size;
		queue = new ArrayList<Integer>(size);
	}
	
	public ArrayList<Integer> removePeople(int floor, Simulator sim) { // returns people who want to get off at floor id and removes from queue
		ArrayList<Integer> offload = new ArrayList<Integer>();
		
		for(int i = 0; i <= queue.size(); i++) {
			int pID = queue.get(i);
			
			if(sim.getPerson(pID).getDestination() == floor) {
				offload.add(queue.remove(i)); // remove from queue, and add to list to return
			}
		}
		return offload;
	}
	
	public void addPeople(ArrayList<Integer> pressed) {
		for(int pID: pressed) {
			queue.add(pID);
		}
	}
	
	public int getSpaces() {
		return SIZE - queue.size();
	}
	
	public void getNextDestination(int currentFloor) { }
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	private void nextFloor() { }
	
	
	
}
