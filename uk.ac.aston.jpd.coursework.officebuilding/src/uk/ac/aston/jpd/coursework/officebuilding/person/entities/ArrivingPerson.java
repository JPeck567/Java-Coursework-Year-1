package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

public interface ArrivingPerson {
	boolean isTimeTaken(int currentTick);
	void setStartingTick(int tick);
	boolean getToExit();
	void setToExit(boolean b);
}
