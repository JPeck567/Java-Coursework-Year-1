package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class PersonHandler {
	public PersonHandler() {
		
	}
	
	public void changePeoplesStates(int[] people) {
		for(int pID: people) {
			changePersonState(pID);
		}
	}

	private void changePersonState(int pID) {
		// TODO Auto-generated method stub
		
	}
}