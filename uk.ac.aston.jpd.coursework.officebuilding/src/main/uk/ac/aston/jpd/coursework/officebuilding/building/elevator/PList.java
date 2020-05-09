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
 *          the elevator and methods which operate around this data
 */
public class PList {
	/**
	 * Declaring fields used for the elevator
	 *
	 */
	private final List<Integer> list;
	private final int SIZE;

	/**
	 * This method is constructing the arraylist for the people's id
	 * 
	 * @param size
	 */
	public PList(int size) {
		this.SIZE = size;
		list = new ArrayList<Integer>(size);
	}

	/**
	 * This method removes the id value given by the index
	 * 
	 * @param index this indicates the index of the person id to be removed
	 */
	public void removePerson(int index) {
		list.remove(index);
	}

	/**
	 * This method adds the id value to the list
	 * 
	 * @param pID this indicates the person id to be added
	 */
	public void addPerson(int pID) {
		list.add(pID);
	}

	/**
	 * This method returns list of people wanting to get off at a given floor
	 * 
	 * @param sim     this is used to interface with the people handler class
	 * @param floorNo this is used to check if the value given is the same as the
	 *                destination
	 * @return this returns a list of the people who wants to get off at the floor
	 *         number given
	 */
	public List<Integer> getOffload(Simulator sim, int floorNo) {
		return list.stream().filter(i -> sim.getPerson(i).getDestination() == floorNo).collect(Collectors.toList());
	}

	/**
	 * This method returns a list of the companies of developers the elevator holds
	 * if there are developers on board
	 * 
	 * @param sim 
	 * this is used to interface with the people handler class
	 * @return
	 * this returns a list of what companies are on board
	 */
	public List<String> getOffloadCompanies(Simulator sim) { 
		return list.stream().map(pID -> sim.getPerson(pID)).filter(p -> (p instanceof Developer))
				.map(p -> ((Developer) p).getCompany()).distinct().collect(Collectors.toList());
	}

	/** 
	 * This method returns the closest floor up from the current floor, by checking each person in the list for their destination.
	 * 
	 * @param sim
	 * this is used to interface with the people handler class
	 * @param currentFloor
	 * this indicated the current floor of the elevator
	 * @return
	 * If there is a floor upwards to go to, return it. If not, return -1
	 */
	public int getNextUpFloor(Simulator sim, int currentFloor) {
																	// to signal no floor
		int nextFloor = sim.getNoFloors();

		for (int pID : list) { // for each person id
			int pDest = sim.getPerson(pID).getDestination();
			if (currentFloor < pDest && pDest < nextFloor) { // if pDest is inside range of currentFloor and nextFloor,
																// set it as candidate for closest floor below
				nextFloor = pDest;
			}
		}

		return nextFloor;

		// return queue.stream().map(pID -> sim.getPerson(pID).getDestination()) //
		// turns list of pids to list of their desinations
		// .filter(floor -> floor < currentFloor) // gets dests which are below current
		// floor, as we are going down
		// .max(Integer::compare).orElse(-1); // gets highest floor below elevator,
		// using method reference. if none exists, return -1
	}

	/** 
	 *  
	 * This method returns the closest floor down from the current floor, by checking each person in the list for their destination.
	 * 
	 * @param sim
	 * this is used to interface with the people handler class
	 * @param currentFloor
	 * this indicated the current floor of the elevator
	 * @return
	 * If there is a floor downwards to go to, return it. If not, return -1
	 */
	
	public int getNextDownFloor(Simulator sim, int currentFloor) {
		int nextFloor = -1;

		for (int pID : list) { // for each person id
			int pDest = sim.getPerson(pID).getDestination();
			if (currentFloor > pDest && pDest > nextFloor) { // tests if given floor req higher than lift's current
																// floor, but lower than the current most closest floor
																// req
				nextFloor = pDest;
			}
		}

		return nextFloor;
	}

	// return queue.stream().map(pID ->
	// sim.getPerson(pID).getDestination()).filter(floor -> floor > currentFloor)
	// .min(Integer::compare).orElse(-1);

	/** 
	 * This method returns the remaining spaces in the list
	 * @return
	 * this returns the number of leftover spaces in the list
	 */
	public int getSpaces() {
		return SIZE - list.size();
	}

	/** 
	 * This method returns a copy of the content of the list
	 * @return
	 * this returns a new list with the copied content
	 */
	
	public List<Integer> getList() {
		return new ArrayList<Integer>(list);
	}

	/** 
	 * This method returns position of the given id in the list
	 * @param pID
	 * this indicates the given id which is inside the list
	 * @return
	 * this returns the index of the person's id in the list
	 */

	public int indexOf(int pID) {
		return list.indexOf(pID);
	}

}
