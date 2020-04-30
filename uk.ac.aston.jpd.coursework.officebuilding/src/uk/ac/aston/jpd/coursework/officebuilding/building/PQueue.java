package uk.ac.aston.jpd.coursework.officebuilding.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	
	public int removePerson(int index) {
		return queue.remove(index);
	}
	
	public void addPerson(int pID) {
		queue.add(pID);
	}
	
	public List<Integer> getOffload(Simulator sim, int floorNo) {
		return queue.stream().filter(i -> sim.getPerson(i).getDestination() == floorNo).collect(Collectors.toList());
	}
	
	public Optional<Integer> getNextUpFloor(Simulator sim, int currentFloor) {
		return queue.stream()
				.map(pID -> sim.getPerson(pID).getDestination())
				.filter(floor -> floor > currentFloor)
				.min(Integer::compare);
	}
	
	public Optional<Integer> getNextDownFloor(Simulator sim, int currentFloor) {
		return queue.stream()
				.map(pID -> sim.getPerson(pID).getDestination())  // turns list of pids to list of their desinations
				.filter(floor -> floor < currentFloor)  // gets dests which are below current floor, as we are going down
				.max(Integer::compare);  // gets highest floor below elevator, using method reference
	}
	
	public int getSpaces() {
		return SIZE - queue.size();
	}
		
	public List<Integer> getQueue(){
		return queue;
	}
	
	public int indexOf(int pID) {
		return queue.indexOf(pID);
	}
	
}
