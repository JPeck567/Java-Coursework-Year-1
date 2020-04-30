package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class ArrivingPerson extends Person{
	private int startingTick;
	private final int timeAvailable;
	private boolean toExit;
	
	public ArrivingPerson(int weight, int id, int destination, int timeAvailable) {
		super(weight, id, destination);
		this.startingTick = PersonHandler.DEFAULTSTARTINGTICK;  // meaning time taken will always be false until it is set to an appropriate value, when person gets to floor
		this.timeAvailable = timeAvailable;
		this.toExit = false;
	}
	
	public boolean isTimeTaken(int currentTick) {
		if (timeAvailable <= (currentTick - startingTick)) {
			return true;
		}
		return false;
	}
	
	public int getStartingTick() {
		return startingTick;
	}
	
	public void setStartingTick(int tick) {
		startingTick = tick;
	}

	public boolean getToExit() {
		return toExit;
	}

	public void setToExit(boolean b) {
		toExit = b;

	}
}
