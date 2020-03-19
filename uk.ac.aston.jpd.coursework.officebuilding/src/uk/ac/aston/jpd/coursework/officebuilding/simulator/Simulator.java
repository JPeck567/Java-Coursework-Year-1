package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PeopleHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.ElevatorQueue;
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
	private final static int FLOORNO = 7;
	private PeopleHandler peopleHandle;
	private Building building;
	public static final int MAXCAPACITY = 4;
	
	/**
	 * Creates a simulator
	 * @param elevator The elevator within the building
	 */
	public Simulator(Building building) {
		this.EMPNO = 0;
		this.DEVNO = 0;
		this.building = new Building(generateElevator());
		this.peopleHandle = new PeopleHandler();
	}
	
	/**
	 * Is the clock that propagates the program
	 */
	private void tick() {
		
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
	public Elevator generateElevator() { 
		return new Elevator(new ElevatorQueue());
	}
	
	/**
	 * This returns the building
	 * @return The building
	 */
	public Building getBuilding() {
		return building;
	}
	
	/**
	 * 
	 * @param people
	 */
	public void setOffloadPeople(List<Integer> people) {
		
	}
	
	/**
	 * 
	 * @param people
	 */
	public void setOnloadPeople(Person[] people, int capacityAllowed) {
		
	}
	
}