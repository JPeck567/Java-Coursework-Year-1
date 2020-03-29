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
	private PersonHandler peopleHandle;
	private Building building;
	public final static int FLOORNO = 7;
	public final static int MAXCAPACITY = 4;
	
	/**
	 * Constructor that creates a simulator
	 * @param elevator The elevator within the building
	 */
	public Simulator(int empNo, int devNo) {
		this.EMPNO = empNo;
		this.DEVNO = devNo;
		this.building = new Building(generateElevator());
		this.peopleHandle = new PersonHandler();
	}
	
	private void run() {
		// while loop to call tick() every second using java.lang.Thread.sleep(milliseconds)
	}

	/**
	 * Is the clock that propagates the program
	 */
	public void tick() {
		tick += 1;
		building.tick(this);
		// tick functions related to persons/stats etc
	}
	
	// May not be needed as final fields are initialised in the constructor.
	//
	//	**
	//	 * Sets the number of employees in the building
	//	 * @param employeeno The number of employees
	//	 */
	//	private void setNoEmployees(int employeeno) {
	//		this.EMPNO = employeeno;
	//	}
	//	
	//	/**
	//	 * Sets the number of developers in the building
	//	 * @param developerno The number of developers
	//	 */
	//	private void setNoDevs(int developerno) {
	//		this.DEVNO = developerno;
	//	}
	//	
	
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
	public void setOffloadPeople(List<Integer> people) { // offload = persons dest is also now persons location
		
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