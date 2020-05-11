package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.ArrivingPerson;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Employee;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Maintenance;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

/**
 * 
 * @author Team 46
 * @author 190148289 Jennifer A. Appiah
 * @author 190095097 Hannah Elliman
 * @author 190055002 Jorge Peck
 * @author 190174923 Hongyi Wang
 * @version 1.0
 * @since 2020 Coursework
 * 
 * 
 * @summary This Class handles all the person entities
 * 
 */

public class PersonHandler {
	/**
	 * Declaring fields
	 */
	private final Map<Integer, Person> people;
	private final Map<Integer, Person> departedPeople;
	private final List<Integer> toRemove;
	private final Stats stat;
	private int idCounter;
	private int noComplaints;
	public static final String[] COMPANIES = { "Goggles", "Mugtome" };
	public static final int DEFAULTWEIGHT = 1;
	public static final int MAINTENANCEWEIGHT = 4;
	public static final int DEFAULTSTARTINGTICK = 9999;

	/**
	 * This method is the constructor method that initialised all the fields and
	 * generates people
	 * 
	 * @param empNo this is used to set the amount of employees in the building
	 * @param devNo this is used to set the amount of developers in the building
	 * @param seed  this generate the same random number using the same random
	 *              patterns when put in the constructor parameter of the random
	 *              object
	 * @param sim   this is the interface of the person handler
	 * @param p     this is used to provide the probabilities of an employee or
	 *              developers switching floors
	 * @param q     this is used to provide the probabilities of the client arriving
	 */
	public PersonHandler(int empNo, int devNo, int seed, Simulator sim, double p, double q) {
		people = new HashMap<Integer, Person>();
		departedPeople = new HashMap<Integer, Person>();
		toRemove = new ArrayList<Integer>();
		idCounter = 0;
		stat = new Stats(seed, p, q);
		generatePeople(sim, empNo, devNo, sim.getNoFloors());

		// setupPeopleEntry(sim);
	}

	/**
	 * This method propagate the individuals arrival and departure
	 * 
	 * @param sim this provides the interface for the tick method
	 */

	public void tick(Simulator sim) {
		randomArrivalTick(sim);

		for (Person p : people.values()) {
			p.tick(sim, this, stat);
		}

		removePeople();
	}

	/**
	 * This method creates the developers and employee at the beginning
	 * 
	 * @param sim      this provides the interface to generate people
	 * @param empNo    this provides a number of employees to be generated
	 * @param devNo    this provides a number of developers to be generated
	 * @param noFloors this is used to identify the destination a person can go to
	 */
	private void generatePeople(Simulator sim, int empNo, int devNo, int noFloors) {
		while (idCounter < devNo) { // creates developers and maps them to an id. also has a random company from
									// static array
			Person p = new Developer(idCounter, stat, noFloors);
			addPerson(p);
			p.arrive(sim);
		}

		while (idCounter < devNo + empNo) { // creates employers and maps them to an id. the first emp's id is the next
											// one from the last dev's id
			Person p = new Employee(idCounter, stat, noFloors);
			addPerson(p); // randomfloor is exclusive to noFloors, so is fine to pass in noFloors w/o -1
			p.arrive(sim);
		}
	}

	/**
	 * This method check for arrivals at each tick
	 * 
	 * @param sim this is used as the interfacer of the arrival tick
	 */
	private void randomArrivalTick(Simulator sim) {
		int noFloors = sim.getNoFloors();

		if (stat.getMArrivalProb()) {
			ArrivingPerson p = new Maintenance(idCounter, stat, noFloors);
			addPerson((Person) p);
			p.arrive(sim);
		}

		if (stat.getCArrivalProb()) {
			ArrivingPerson p = new Client(idCounter, stat, noFloors, sim.getTick());
			addPerson((Person) p);
			p.arrive(sim);
		}
	}

	/**
	 * This method is used to add a person to the person list
	 * 
	 * @param p this is used to provide the information to be stored on each person
	 */
	private void addPerson(Person p) {
		people.put(p.getID(), p);
		idCounter++;
	}

	/**
	 * This method is used to remove people once the have left the building. The are
	 * added to a departedPeople list to store them so they can be accessed later.
	 */
	private void removePeople() {
		for (int pID : toRemove) {
			departedPeople.put(pID, getPerson(pID));
			people.remove(pID);
		}
		toRemove.clear();
	}

	/**
	 * This method is used to add the removed person to the remove list
	 * 
	 * @param pID this is used the identify the person stored
	 */
	public void addToRemove(int pID) {
		toRemove.add(pID);
	}

	/**
	 * This method get the person
	 * 
	 * @param pID this is used the identify the person stored
	 * @return this returns the person from the people list that has been stored
	 */
	public Person getPerson(int pID) {
		return people.get(pID);
	}

	/**
	 * This methods gets the number of complaints
	 * 
	 * @return this return the number of complaints made
	 */
	public int getComplaints() {
		return noComplaints;
	}

	/**
	 * This method get the overall waiting time from people in the building and
	 * those who have departed
	 * 
	 * @return this returns a hashmap containing each person' average wait time
	 *         value
	 */
	public HashMap<Integer, Double> getAvgWaitingTime() {
		HashMap<Integer, Person> allPeople = new HashMap<Integer, Person>();
		allPeople.putAll(people);
		allPeople.putAll(departedPeople);
		HashMap<Integer, Double> peopleAvgWaitTime = new HashMap<Integer, Double>();

		for (int pID : allPeople.keySet()) {
			double avgWait = allPeople.get(pID).getAverageWaitingTime();
			if (avgWait != -1) { // if there is actual data to record
				peopleAvgWaitTime.put(pID, avgWait);
			}
		}

		return peopleAvgWaitTime;
	}

	/**
	 * This method increases the number of complaint
	 */
	public void incrementComplaints() {
		noComplaints++;
	}
}