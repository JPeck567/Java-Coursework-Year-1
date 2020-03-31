package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.PQueue;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

/**
 * 
 * @author Team 46
 *
 */
public class Simulator {
	
	protected int tick;
	private final int EMPNO;
	private final int DEVNO;
	private final int SIMTIME;
	private PersonHandler peopleHandle;
	private Building building;
	public final static int FLOORNO = 7;
	public final static int MAXCAPACITY = 4;
	
	/**
	 * Constructor that creates a simulator
	 * @param elevator The elevator within the building
	 */
	public Simulator(int empNo, int devNo, int simTime) {
		this.EMPNO = empNo;
		this.DEVNO = devNo;
		this.SIMTIME = simTime;  // 8hrs = 2880 ticks (as each tick is 10s)
		this.building = new Building(generateElevator());
		this.peopleHandle = new PersonHandler();
		
		// other stuff to do with people etc
	}
	
	public void run() throws InterruptedException { // called by launcher
		while(tick < SIMTIME) {
			tick();
			Thread.sleep(1000); // 1000ms = 1 second. Therefore in real life, each tick execution is ~ 1 second
		}
	}

	/**
	 * Is the clock that propagates the program
	 */
	private void tick() {
		tick += 1;
		building.tick(this);
		// tick functions related to persons/stats etc
	}
	
	/**
	 * Creates the elevator
	 */
	private Elevator generateElevator() { 
		return new Elevator(new PQueue(Simulator.MAXCAPACITY));
	}
	
	/**
	 * This returns the building
	 * @return The building
	 */
	public Building getBuilding() {
		return building;
	}
	
	public Person getPerson(int pID) {
		return peopleHandle.getPerson(pID);
	}
	
	/**
	 * 
	 * @param people
	 */
	public void setOffloadPeople(List<Integer> people) { // offload = persons arrives at floor so their dest field is also the persons location
		
	}
	
	/**
	 * 
	 * @param people
	 */
	public void setOnloadPeople(int[] people) { // onload = persons current location is the elevator, set as -1?
		
		//changes state to travelling.
		peopleHandle.changePeoplesStates(people, -1);
	}
	
	
}