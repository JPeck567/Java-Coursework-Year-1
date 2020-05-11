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
*/
public class PersonHandler {
	/**
	 *
	 */
	private final Map<Integer, Person> people;
	private final Map<Integer, Person> departedPeople;
	private final List<Integer> toRemove;
	private final Stats stat;
	private int idCounter;
	private int noComplaints;
	public static final String[] COMPANIES = {"Goggles", "Mugtome"};
	public static final int DEFAULTWEIGHT = 1;
	public static final int MAINTENANCEWEIGHT = 4;
	public static final int DEFAULTSTARTINGTICK = 9999;
	
	/**
	 *
	 */
	public PersonHandler(int empNo, int devNo, int seed, Simulator sim, double p, double q) {
		people = new HashMap<Integer, Person>();
		departedPeople = new HashMap<Integer, Person>();
		toRemove = new ArrayList<Integer>();
		idCounter = 0;
		stat = new Stats(seed, p, q);
		generatePeople(sim, empNo, devNo, sim.getNoFloors());

		//setupPeopleEntry(sim);
	}
	
	/**
	 *
	 */

	public void tick(Simulator sim) {
		randomArrivalTick(sim);
		
		for(Person p : people.values()) {
			p.tick(sim, this, stat);
		}
		
		removePeople();
	}

	/**
	 *
	 */
	private void generatePeople(Simulator sim, int empNo, int devNo, int noFloors) {
		while (idCounter < devNo) { // creates developers and maps them to an id. also has a random company from static array
			Person p = new Developer(idCounter, stat, noFloors);
			addPerson(p);
			p.arrive(sim);
		}
	
		while (idCounter < devNo + empNo) { // creates employers and maps them to an id. the first emp's id is the next one from the last dev's id
			Person p = new Employee(idCounter, stat, noFloors);
			addPerson(p);  // randomfloor is exclusive to noFloors, so is fine to pass in noFloors w/o -1
			p.arrive(sim);
		}
	}

	/**
	 *
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
	 *
	 */
	private void addPerson(Person p) {
		people.put(p.getID(), p);
		idCounter++;
	}

	/**
	 *
	 */
	private void removePeople() {
		for (int pID : toRemove) {
			departedPeople.put(pID, getPerson(pID));
			removePerson(pID);
		}
		toRemove.clear();
	}

	/**
	 *
	 */
	private void removePerson(int pID) {
		people.remove(pID);
	}

	public void addToRemove(int pID) {
		toRemove.add(pID);
	}

	/**
	 *
	 */
	public Person getPerson(int pID) {
		return people.get(pID);
	}

	/**
	 *
	 */
	public int getComplaints() {
		return noComplaints;
	}
	
	/**
	 *
	 */
	public HashMap<Integer, Double> getAvgWaitingTime(){
		HashMap<Integer, Person> allPeople = new HashMap<Integer, Person>();
		allPeople.putAll(people);
		allPeople.putAll(departedPeople);
		HashMap<Integer, Double> peopleAvgWaitTime = new HashMap<Integer, Double>();
		
		for (int pID : allPeople.keySet()) {
			double avgWait = allPeople.get(pID).getAverageWaitingTime();
			if(avgWait != -1) {  // if there is actual data to record
				peopleAvgWaitTime.put(pID, avgWait);
			}
		}
		
		return peopleAvgWaitTime;
	}
	
	public void incrementComplaints() {
		noComplaints++;
	}
}