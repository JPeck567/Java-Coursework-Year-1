package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import java.util.Comparator;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
/**
*
*/
public class WaitingQueueComparator implements Comparator<Integer> {
	/**
	 *
	 */
	private final Simulator sim;

	/**
	 *
	 */
	public WaitingQueueComparator(Simulator sim) {
		this.sim = sim;
	}
	/**
	 *
	 */
	@Override
	public int compare(Integer pID1, Integer pID2) {
		Person p1 = sim.getPerson(pID1);
		Person p2 = sim.getPerson(pID2);
		if (p1 instanceof Client && !(p2 instanceof Client)) { // p1 should be in front of p2
			return -1;
		} else if (!(p1 instanceof Client) && p2 instanceof Client) { // p2 should be in front of p1
			return 1;
		} else {
			return (0 > (p2.getTimeStamp() - p1.getTimeStamp()) ? 1: -1);
			// sort according to time of arrival. lower ids entered earlier than higher ones 
			// if bigger than 0, positive so return 1, else -1
		}
	}
}
