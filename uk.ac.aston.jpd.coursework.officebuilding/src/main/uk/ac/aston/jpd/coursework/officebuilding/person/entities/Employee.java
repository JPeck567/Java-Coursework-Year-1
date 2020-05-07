package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

/**
*
*/
public class Employee extends Person {
	/**
	 *
	 */
	public Employee(int id, int destination) {
		super(PersonHandler.DEFAULTWEIGHT, id, destination);
	}
}