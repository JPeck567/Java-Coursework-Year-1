package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Maintenance extends ArrivingPerson {

	public Maintenance(int id, int destination, int timeAvailable) {
		super(PersonHandler.MAINTENANCEWEIGHT, id, destination, timeAvailable);
	}
}