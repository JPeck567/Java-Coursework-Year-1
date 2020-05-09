package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

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

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
/**
*
*/
public class PList { 
	/**
	 * used for the elevator
	 *
	 */
	private final List<Integer> queue;
	private final int SIZE;
	
	/**
	 *
	 */
	public PList(int size) {
		this.SIZE = size;
		queue = new ArrayList<Integer>(size);
	}
	
	/**
	 *
	 */
	public void removePerson(int index) {
		queue.remove(index);
	}

	/**
	 *
	 */
	public void addPerson(int pID) {
		queue.add(pID);
	}

	/**
	 *
	 */
	public List<Integer> getOffload(Simulator sim, int floorNo) { // returns list of people wanting to get off at a given floor
		return queue.stream().filter(i -> sim.getPerson(i).getDestination() == floorNo).collect(Collectors.toList());
	}

	/**
	 *
	 */
	public List<String> getOffloadCompanies(Simulator sim) { // returns a list of the companies the elevator holds if there are devs onboard
		return queue.stream().map(pID -> sim.getPerson(pID)).filter(p -> (p instanceof Developer))
				.map(p -> ((Developer) p).getCompany()).distinct().collect(Collectors.toList());
	}

	/**
	 *
	 */
	public int getNextUpFloor(Simulator sim, int currentFloor) { // if was a floor to go to, return it. else, return -1 to signal no floor
		int nextFloor = sim.getNoFloors();

		for (int pID : queue) { // for each person id
			int pDest = sim.getPerson(pID).getDestination();
			if (currentFloor < pDest && pDest < nextFloor) { // if pDest is inside range of currentFloor and nextFloor, set it as candidate for closest floor below
				nextFloor = pDest;
			}
		}

		return nextFloor;

		//return queue.stream().map(pID -> sim.getPerson(pID).getDestination()) // turns list of pids to list of their desinations
		//		.filter(floor -> floor < currentFloor) // gets dests which are below current floor, as we are going down
		//		.max(Integer::compare).orElse(-1); // gets highest floor below elevator, using method reference. if none exists, return -1
	}

	/**
	 *
	 */
	public int getNextDownFloor(Simulator sim, int currentFloor) {
		int nextFloor = -1;
		
		for (int pID : queue) { // for each person id
			int pDest = sim.getPerson(pID).getDestination();
			if (currentFloor > pDest && pDest > nextFloor) { // tests if given floor req higher than lift's current floor, but lower than the current most closest floor req
				nextFloor = pDest;
			}
		}
		
		return nextFloor;
	}

	//return queue.stream().map(pID -> sim.getPerson(pID).getDestination()).filter(floor -> floor > currentFloor)
	//		.min(Integer::compare).orElse(-1);

	/**
	 *
	 */
	public int getSpaces() {
		return SIZE - queue.size();
	}

	/**
	 *
	 */
	public List<Integer> getQueue() {
		return new ArrayList<Integer>(queue);
	}

	/**
	 *
	 */
	
	public int indexOf(int pID) {
		return queue.indexOf(pID);
	}

}
