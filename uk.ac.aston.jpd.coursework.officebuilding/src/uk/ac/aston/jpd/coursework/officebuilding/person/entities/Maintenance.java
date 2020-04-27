package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Maintenance extends Person implements ArrivingPerson {
	private int startingTick;
	private final int timeAvailable;
	private boolean toExit;
	
	private int entranceTick;

	public Maintenance(int id, int destination, int timeAvailable, int entranceTick) {
		super(PersonHandler.MAINTENANCEWEIGHT, id, destination);
		this.startingTick = 3000;  // meaning time taken will always be false until it is set to an appropriate value, when person gets to floor
		this.timeAvailable = timeAvailable;
		this.toExit = false;
		
		this.entranceTick = entranceTick;
	}

	@Override
	public boolean isTimeTaken(int currentTick) {
		if (timeAvailable <= (currentTick - startingTick)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void setStartingTick(int tick) {
		startingTick = tick;
	}

	@Override
	public boolean getToExit() {
		return toExit;
	}

	@Override
	public void setToExit(boolean b) {
		toExit = b;
	}
}