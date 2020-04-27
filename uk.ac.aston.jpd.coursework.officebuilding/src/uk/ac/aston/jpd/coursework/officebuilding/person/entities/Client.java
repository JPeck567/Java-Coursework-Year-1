package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;

public class Client extends Person implements ArrivingPerson {
	private int startingTick;
	private final int timeAvailable;
	private boolean toExit;
	
	private int entranceTick;

	public Client(int id, int destination, int timeAvailable, int entranceTick) {
		super(PersonHandler.DEFAULTWEIGHT, id, destination);
		this.startingTick = 3000;  // meaning time taken will always be false until it is set to an appropriate value, when person gets to floor
		this.timeAvailable = timeAvailable;
		this.toExit = false;
		
		this.entranceTick = entranceTick;
	}

	private void complain() {
		
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