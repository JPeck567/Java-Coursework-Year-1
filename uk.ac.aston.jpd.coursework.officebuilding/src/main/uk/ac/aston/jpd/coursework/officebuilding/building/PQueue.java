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

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
/**
 * @author Team 46
 * @author 190148289
 * Jennifer A. Appiah
 * @author 190095097
 * Hannah Elliman
 * @author 190055002
 * Jorge Peck 
 * @author 190174923
 * Hongyi Wang
 * @version 1.0
 * @since 2020
 * Coursework
 * 
 * 
 * @summary This Class hold's the ID's of the people in the elevator and provides functions based on this data
 * 
 */
public class PQueue { 
	/**
	 * Declared variables used for the List
	 *
	 */
	private final List<Integer> queue;
	private final int SIZE;
	
	/**
	 * This method is the constructor which initialises the variables,
	 * @param size this limits the size of the list
	 */
	public PQueue(int size) {
		this.SIZE = size;
		queue = new ArrayList<Integer>(size);
	}
	
	/**
	 * This class removes the person from the list using their index
	 * @param index
	 * this indicates their position in the list
	 */
	public void removePerson(int index) {
		queue.remove(index);
	}

	/**
	 * This method adds the person to the list using their ID
	 * @param pID
	 * this identifies the person
	 */
	public void addPerson(int pID) {
		queue.add(pID);
	}

	/**
	 * This method returns list of people wanting to get off at a given floor
	 * @param sim
	 * @param floorNo
	 * @return
	 */
	public List<Integer> getOffload(Simulator sim, int floorNo) { 
		return queue.stream().filter(i -> sim.getPerson(i).getDestination() == floorNo).collect(Collectors.toList());
	}

	/**
	 * This method returns a list of the companies the elevator holds if there are devs onboard
	 * @param sim
	 * @return
	 */
	public List<String> getOffloadCompanies(Simulator sim) { 
		return queue.stream().map(pID -> sim.getPerson(pID)).filter(p -> (p instanceof Developer))
				.map(p -> ((Developer) p).getCompany()).distinct().collect(Collectors.toList());
	}

	/**
	 * This method indicates the direction: if there was a floor to go to, return it. else, return -1 to signal no floor
	 * @param sim
	 * @param currentFloor
	 * 
	 * @return
	 */
	public int getNextUpFloor(Simulator sim, int currentFloor) {
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
	 * @param sim
	 * @param currentFloor
	 * @return
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
	 * @return
	 */
	public int getSpaces() {
		return SIZE - queue.size();
	}

/**
 * 
 * @return
 */
	public List<Integer> getQueue() {
		return queue;
	}

/**
 * 
 * @param pID
 * @return
 */
	public int indexOf(int pID) {
		return queue.indexOf(pID);
	}

}
