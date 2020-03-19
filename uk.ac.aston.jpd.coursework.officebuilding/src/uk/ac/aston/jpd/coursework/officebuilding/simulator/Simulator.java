package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PeopleHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

/**
 * 
 * @author Team 46
 *
 */
public class Simulator {
	
	protected int tick;
	private int employeeno;
	private int developerno;
	private final int floorno;
	private PeopleHandler peopleHandle;
	private Building building;
	
	/**
	 * Creates a simulator
	 * @param elevator The elevator within the building
	 */
	public Simulator(Elevator elevator) {
		this.employeeno = 0;
		this.developerno = 0;
		this.floorno = 7;
		this.peopleHandle = new PeopleHandler();
		this.building = new Building();
	}
	
	/**
	 * Is the clock that propagates the program
	 */
	private void tick() {
		
	}
	
	/**
	 * Sets the number of employees in the building
	 * @param employeeno The number of employees
	 */
	private void setNoEmployees(int employeeno) {
		this.employeeno = employeeno;
	}
	
	/**
	 * Sets the number of developers in the building
	 * @param developerno The number of developers
	 */
	private void setNoDevs(int developerno) {
		this.developerno = developerno;
	}
	
	/**
	 * Creates the elevator
	 */
	public void generateElevator() { 
		
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
	public void setOnloadPeople(Person[] people) {
		
	}
	
}