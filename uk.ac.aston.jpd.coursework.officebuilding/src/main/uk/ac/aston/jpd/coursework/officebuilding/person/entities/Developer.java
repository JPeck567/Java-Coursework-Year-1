package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

/**
*
*/
public class Developer extends Person {
	/**
	 *
	 */
	private final String company;
	
	/**
	 *
	 */
	public Developer(int id, Stats stat, int noFloors) {
		super(PersonHandler.DEFAULTWEIGHT, id);
		this.destination = getRandomFloor(stat, (noFloors - 1) / 2, noFloors - 1);
		this.company = PersonHandler.COMPANIES[stat.getRandomRangeNum(0, 1)];
	}

	/**
	 *
	 */
	public String getCompany() {
		return company;
	}
}