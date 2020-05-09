package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.Comparator;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
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
 * @summary This Class provides a comparator object to be used by the waiting
 *          queue, a priority queue, to order pID's by both the time they
 *          arrive, and if they are a client or not. If so, they are put in
 *          front of the other people.
 * 
 */

public class WaitingQueueComparator implements Comparator<Integer> {
	/**
	 * Declaring fields
	 */
	private final Simulator sim;

	/**
	 * This is the constructor method to initialise the field
	 */
	public WaitingQueueComparator(Simulator sim) {
		this.sim = sim;
	}

	@Override
	/**
	 * {@inheritDoc}
	 * 
	 * @param pID1 this is the id of the first person
	 * @param pID2 this is the id of the second person
	 * @return this returns -1 if the first person needs to be in front of the second person.
	 *  returns 1 if the second person needs to be in front of the first person.
	 *  returns 0 if both people are the same.
	 */
	public int compare(Integer pID1, Integer pID2) {
		Person p1 = sim.getPerson(pID1);
		Person p2 = sim.getPerson(pID2);
		if (p1 instanceof Client && !(p2 instanceof Client)) { // p1 should be in front of p2
			return -1;
		} else if (!(p1 instanceof Client) && p2 instanceof Client) { // p2 should be in front of p1
			return 1;
		} else {
			return (0 > (p2.getTimeStamp() - p1.getTimeStamp()) ? 1 : -1);
			// sort according to time of arrival. lower ids entered earlier than higher ones
			// if bigger than 0, positive so return 1, else -1
		}
	}
}
