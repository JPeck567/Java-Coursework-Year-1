package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;
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
	private Queue<Integer> queue;
	private final int SIZE;
	
	public PQueue(int size) {
		this.SIZE = size;
		queue = new LinkedList<Integer>();
	}
	
	public ArrayList<Integer> getOffloadPeople(int floor) {
		ArrayList<Integer> offload = new ArrayList<Integer>();
		for(int pID: queue) {
			if(pID == floor) {
				offload.add(pID);
			}
		}
		return offload;
	}
	
	public void addOnloadPeople(int[] people) {
		for(int pID: people) {
			queue.add(pID);
		}
	}
	
	public int getSize() {
		return SIZE - queue.size();
	}
	
	public int getNextDestination(Simulator sim, int direction) {
		return Collections.max(queue); // the highest floor is returned
	}
	
	private void nextFloor(){
	}
	
}
