package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Developer extends Person {
	public Developer(int id, int destination) {
		super(PersonHandler.DEFAULTWEIGHT, id, destination);
	}
}