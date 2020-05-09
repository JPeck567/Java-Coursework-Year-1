package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
		idCounter = 0;
		stat = new Stats(seed, p, q);
		generatePeople(empNo, devNo, sim.getNoFloors());

		setupPeopleEntry(sim);
	}
	
	/**
	 *
	 */

	public void tick(Simulator sim) {
		randomDecisionTick(sim);
		randomArrivalTick(sim);
		arrivalsTimeCheck(sim);
	}

	/**
	 *
	 */
	private void randomDecisionTick(Simulator sim) {
		int noFloors = sim.getNoFloors();
		for (int pID : people.keySet()) {
			Person p = getPerson(pID);

			if (p.getCurrentFloor() != -1 && !p.isWaiting()) { // can only decide to change floor is not on lift, is not waiting already

				if (stat.getDecisionProb()) {
					if (p instanceof Developer) {
						p.setDestination(getRandomDevFloor(p.getCurrentFloor(), noFloors));
					} else if (p instanceof Employee) { // is employee
						p.setDestination(getRandomEmpFloor(p.getCurrentFloor(), noFloors));
					} else {  // a client or maintenance
						break;
					}

					sim.removeFromFloor(p.getCurrentFloor(), pID);
					p.pressButton(sim);
					//System.out.println("Change floor! " + p.getClass().getSimpleName() + " ID:" + p.getID()
					//		+ " on floor: " + p.getCurrentFloor() + " to: " + p.getDestination());

				}
			}
		}
	}

	/**
	 *
	 */
	private void randomArrivalTick(Simulator sim) {
		int noFloors = sim.getNoFloors();

		if (stat.getMArrivalProb()) {
			Person p = new Maintenance(idCounter, stat, noFloors);
			addPerson(p);
			p.pressButton(sim);
		}

		if (stat.getCArrivalProb()) {
			Person p = new Client(idCounter, stat, noFloors, sim.getTick());
			addPerson(p);
			p.pressButton(sim);
		}
	}

	/**
	 *
	 */
	private void arrivalsTimeCheck(Simulator sim) {
		List<Integer> toRemove = new ArrayList<Integer>();
		int currentTick = sim.getTick();

		for (Person p : people.values()) {
			if (p instanceof ArrivingPerson) {
				ArrivingPerson arrP = (ArrivingPerson) p;
				
				if(arrP.getStartingTick() == DEFAULTSTARTINGTICK) { // if arrival is not yet gotten to requested floor
					if(arrP instanceof Client) {  // and is client
						Client c = (Client) arrP;
						if(c.isComplaining(currentTick) && c.isWaiting()) {  // if taken too long to wait for elevator, and not waiting (as will still check even if on elevator)
							sim.removeFromWaiting(p.getCurrentFloor(), p.getID());
							toRemove.add(arrP.getID());
							noComplaints++;
						}
					}
				} else if (arrP.isTimeTaken(currentTick)) { // arrival is at it's req floor and is taken the alloted amount of time to stay in the building
					if (arrP.getToExit()) { // if on way to exit
						if(arrP.getCurrentFloor() == 0) {  // and has gotten to ground
							sim.removeFromFloor(p.getCurrentFloor(), p.getID());
							toRemove.add(arrP.getID());
						}
					} else {  // not on way to exit, so we make them go on their way
						p.pressButton(sim);
						p.setDestination(0);
						arrP.setToExit(true);
						sim.removeFromFloor(p.getCurrentFloor(), p.getID());
					}
				}
			}
		}
		removePeople(toRemove);
	}
	
	/**
	 *
	 */
	private void removePeople(List<Integer> toRemove) {
		for (int pID : toRemove) {
			departedPeople.put(pID, getPerson(pID));
			removePerson(pID);
		}
	}

	/**
	 *
	 */
	private void generatePeople(int empNo, int devNo, int noFloors) {
		while (idCounter < devNo) { // creates developers and maps them to an id. also has a random company from static array
			addPerson(new Developer(idCounter, stat, noFloors)); 
		}

		while (idCounter < devNo + empNo) { // creates employers and maps them to an id. the first emp's id is the next one from the last dev's id
			addPerson(new Employee(idCounter, stat, noFloors));  // randomfloor is exclusive to noFloors, so is fine to pass in noFloors w/o -1
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
	private void setupPeopleEntry(Simulator sim) {
		for (Integer pID : people.keySet()) {
			people.get(pID).pressButton(sim);
		}
	}

	/**
	 *
	 */
	private void pressButton(Simulator sim, Person p) {
		p.pressButton(sim);
	}	

	/**
	 *
	 */
	private int getRandomDevFloor(int currentFloor, int noFloors) {
		noFloors--;  // as is +1 too high for random funct, which treats boundR inclusive. as floor G as 0, not 1, noFloors needs -1
		while (true) {
			int randFloor = stat.getRandomRangeNum(noFloors / 2, noFloors); // keeps trying for a random floor until it isn't the current floor of the person
			if (randFloor != currentFloor) {
				return randFloor;
			}
		}
	}

	/**
	 *
	 */
	private int getRandomEmpFloor(int currentFloor, int noFloors) {
		while (true) {
			int randFloor = stat.getRandomFloor(noFloors); // keeps trying for a random floor until it isn't the current floor of the person
			if (randFloor != currentFloor) {
				return randFloor;
			}
		}
	}

	/**
	 *
	 */
	public int getRandomCliFloor(int noFloors) {
		return stat.getRandomRangeNum(0, ((noFloors - 1) / 2));
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
	public void removePerson(int pID) {
		people.remove(pID);
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
			double avgWait = getPerson(pID).getAverageWaitingTime();
			if(avgWait != -1) {  // if there is actual data to record
				peopleAvgWaitTime.put(pID, avgWait);
			}
		}
		
		return peopleAvgWaitTime;
	}
	
	public void addPersonForTest(Person p) {
		people.put(p.getID(), p);
	}
}