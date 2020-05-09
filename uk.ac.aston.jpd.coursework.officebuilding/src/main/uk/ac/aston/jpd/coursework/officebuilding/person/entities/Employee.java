package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

/**
*
*/
public class Employee extends Person {
	/**
	 *
	 */
	public Employee(int id, Stats stat, int noFloors) {
		super(PersonHandler.DEFAULTWEIGHT, id);
		this.destination = getRandomFloor(stat, 0, noFloors - 1);
	}
}