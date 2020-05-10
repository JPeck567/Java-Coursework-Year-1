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
		super(stat, PersonHandler.MAINTENANCEWEIGHT, id, noFloors - 1, noFloors - 1, (stat.getRandomRangeNum(20, 40) * 60) / 10);
	}
}