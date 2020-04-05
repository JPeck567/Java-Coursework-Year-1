package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class PersonHandler {
	
	public PersonHandler() {
	
	}
	
	public void changePeoplesStates(int[] people, int state) {
		for(int pID: people) {
			changePersonState(pID, state);
		}
	}

	private void changePersonState(int pID, int state) {
		// TODO Auto-generated method stub
		
	}

	public Person getPerson(int pID) {
		// TODO Auto-generated method stub
		return people.get(pID);
	}
}