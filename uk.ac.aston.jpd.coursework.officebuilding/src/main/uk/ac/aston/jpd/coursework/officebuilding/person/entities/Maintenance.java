package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;
/**
*
*/
public class Maintenance extends ArrivingPerson {

	/**
	 *
	 */
	public Maintenance(int id, Stats stat, int noFloors) { 
		super(PersonHandler.MAINTENANCEWEIGHT, id, (stat.getRandomRangeNum(20, 40) * 60) / 10);
		this.destination = noFloors - 1;
	}
}