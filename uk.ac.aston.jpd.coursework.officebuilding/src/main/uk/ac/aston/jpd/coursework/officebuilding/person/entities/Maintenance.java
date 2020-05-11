package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

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
 * @summary This Class extends from Person to specify the type of person, such
 *          as their specific weight and destination floor
 */
public class Maintenance extends ArrivingPerson {

	/**
	 * This initialises all the fields and also calls the constructor of the
	 * ArrivingPerson class
	 * 
	 * @param id       this is used as a unique identifier of the person
	 * @param stat     this is used in the person constructor to get a random floor
	 * @param noFloors this is the total number of floors, used for getting a random
	 *                 floor
	 */
	public Maintenance(int id, Stats stat, int noFloors) {
		super(stat, PersonHandler.MAINTENANCEWEIGHT, id, noFloors - 1, noFloors - 1,
				(stat.getRandomRangeNum(20, 40) * 60) / 10);
	}
}