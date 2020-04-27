package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import java.util.ArrayList;
import java.util.HashMap;
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

public class PersonHandler {
	private Map<Integer, Person> people;
	//private Map<Integer, ArrivingPerson> arrivals;
	private int idCounter;
	public static final int DEFAULTWEIGHT = 1;
	public static final int MAINTENANCEWEIGHT = 4;

	private Stats stat;

	public PersonHandler(int empNo, int devNo, int seed, Simulator sim, double p, double q) {
		people = new HashMap<Integer, Person>();
		idCounter = 0;
		stat = new Stats(seed, p, q);
		generatePeople(empNo, devNo, sim.getNoFloors());

		setupPeopleEntry(sim);
	}

	public void tick(Simulator sim) {
		randomDecisionTick(sim);
		randomArrivalTick(sim);
		
		arrivalsTimeCheck(sim);

		stat.tick(this, sim);
	}

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
					p.setIsWaiting(true);
					//System.out.println("Change floor! " + p.getClass().getSimpleName() + " ID:" + p.getID()
					//		+ " on floor: " + p.getCurrentFloor() + " to: " + p.getDestination());

				}
			}
		}
	}

	private void randomArrivalTick(Simulator sim) {
		int noFloors = sim.getNoFloors();

		if (stat.getMArrivalProb()) {
			int timeAvailable = (stat.getRandomRangeNum(20, 40) * 60) / 10; // number of ticks to stay in building. random amount of time from 20 to 40 mins 
			Person p = new Maintenance(idCounter, noFloors - 1, timeAvailable, sim.getTick()); // maintenance goto top floor

			addPerson(p);
			pressButton(sim, p);

			//System.out.println("Maintenance crew " + p.getID() + " has arrived!");
		}

		if (stat.getCArrivalProb()) {
			int timeAvailable = (stat.getRandomRangeNum(10, 30) * 60) / 10; // number of ticks to stay in building. random amount of time from 10 to 30 mins
			Person p = new Client(idCounter, getRandomCliFloor(noFloors), timeAvailable, sim.getTick()); // integer division, so whole num. clients goto bottom half

			addPerson(p);
			pressButton(sim, p);

			//System.out.println("Client " + p.getID() + " has arrived!");
		}

	}

	private void arrivalsTimeCheck(Simulator sim) {
		List<Integer> toRemove = new ArrayList<Integer>();

		for (Person p : people.values()) {
			if (p instanceof ArrivingPerson) {
				ArrivingPerson arrP = (ArrivingPerson) p;
					
				if (arrP.isTimeTaken(sim.getTick())) { // if time taken in building is up
					//System.out.println();
					if (arrP.getToExit()) { // if on way to exit
						if(p.getCurrentFloor() == 0) {  // and has gotten to ground
							toRemove.add(p.getID());
						}
					} else {  // not on way to exit, so we make them go on their way
						pressButton(sim, p);
						p.setDestination(0);
						arrP.setToExit(true);
						sim.removeFromFloor(p.getCurrentFloor(), p.getID());
					}
				}
			}
		}

		for (int pID : toRemove) {
			sim.removeFromFloor(0, pID);
			removePerson(pID);
		}
	}

	private void generatePeople(int empNo, int devNo, int noFloors) {
		while (idCounter < devNo) { // creates developers and maps them to an id.
			addPerson(new Developer(idCounter, getRandomDevFloor(0, noFloors))); // -1 is used as initial random floor can be 
		}

		while (idCounter < devNo + empNo) { // creates employers and maps them to an id. the first emp's id is the next one from the last dev's id
			addPerson(new Employee(idCounter, stat.getRandomFloor(noFloors)));
		}
	}

	private void addPerson(Person p) {
		int id = p.getID();
		people.put(id, p);
		idCounter++;
	}

	private void setupPeopleEntry(Simulator sim) {
		for (Integer pID : people.keySet()) {
			pressButton(sim, getPerson(pID));
		}
	}

	private void pressButton(Simulator sim, Person p) {
		if (p.getDestination() == 0 && p.getCurrentFloor() == 0) {
			sim.addToOnFloor(p.getID(), Simulator.DEFAULTFLOOR);
			
			if(p instanceof ArrivingPerson) {
				((ArrivingPerson) p).setStartingTick(sim.getTick());
			}
		} else {
			p.pressButton(sim);
			p.setIsWaiting(true);
			
		}
	}

	public void setCurrentFloor(int pID, int floorNo) {
		getPerson(pID).setCurrentFloor(floorNo);
	}

	public int getRandomDevFloor(int currentFloor, int noFloors) {
		while (true) {
			int randFloor = stat.getRandomRangeNum(noFloors / 2, noFloors); // keeps trying for a random floor until it isn't the current floor of the person
			if (randFloor != currentFloor) {
				return randFloor;
			}
		}
	}

	public int getRandomEmpFloor(int currentFloor, int noFloors) {
		while (true) {
			int randFloor = stat.getRandomFloor(noFloors); // keeps trying for a random floor until it isn't the current floor of the person
			if (randFloor != currentFloor) {
				return randFloor;
			}
		}
	}

	public int getRandomCliFloor(int noFloors) {
		return stat.getRandomRangeNum(0, (noFloors / 2));
	}

	public Person getPerson(int pID) {
		return people.get(pID);
	}

	public void removePerson(int pID) {
		people.remove(pID);

	}
}