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
 * @summary This Class extends from Person to provide detail on the type of
 *          person and to provide a company field
 */
public class Developer extends Person {
	/**
	 * Declaring fields
	 */
	private final String company;

	/**
	 * This is the constructor of the developer class to initialise the fields of
	 * itself and the person class
	 * 
	 * @param id       this is used as a unique identifier of the person
	 * @param stat     this is used in the person constructor to get a random floor
	 * @param noFloors this is the total number of floors, used for getting a random
	 *                 floor and a random company
	 */
	public Developer(int id, Stats stat, int noFloors) {
		super(stat, PersonHandler.DEFAULTWEIGHT, id, (noFloors - 1) / 2, noFloors - 1);
		this.company = PersonHandler.COMPANIES[stat.getRandomRangeNum(0, 1)];
	}

	/**
	 * This method returns the company of the developer
	 * 
	 * @return this returns the developer's company
	 */
	public String getCompany() {
		return company;
	}
}