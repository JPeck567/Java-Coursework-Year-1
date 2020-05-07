package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

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
	public Developer(int id, int destination, String company) {
		super(PersonHandler.DEFAULTWEIGHT, id, destination);
		this.company = company;
	}
	
	/**
	 *
	 */
	public String getCompany() {
		return company;
	}
}