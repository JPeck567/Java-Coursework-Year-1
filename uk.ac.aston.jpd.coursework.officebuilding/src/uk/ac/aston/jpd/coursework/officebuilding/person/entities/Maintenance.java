package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Maintenance extends Person {
	public Maintenance(int id, int destination) {
		super(PersonHandler.MAINTENANCEWEIGHT, id, destination);
	}
}