package uk.ac.aston.jpd.coursework.officebuilding.person.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Employee;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

public class PersonHandler {
	private Map<Integer, Person> people;
	public static final int DEFAULTWEIGHT = 1;
	public static final int MAINTENANCEWEIGHT = 4;
	
	private Stats stat;

	public PersonHandler(int empNo, int devNo, int seed, Simulator sim) {
		people = new HashMap<Integer, Person>();
		stat = new Stats(seed);
		generatePeople(empNo, devNo);
		setupPeopleEntry(sim);
	}

	public void tick(Simulator sim) {
		// TODO people to leave building code
		stat.tick(this, sim);
	}

	private void generatePeople(int empNo, int devNo) {
		for (int id = 0; id < devNo; id++) { // creates developers and maps them to an id. id < devNo is used as 0 counts as a unique val, so id has to end -1 below devNo
			people.put(id, new Developer(id, stat.getRandomFloor()));
		}

		for (int id = devNo; id < empNo + devNo; id++) { // creates employers and maps them to an id. the first emp's id is the next one from the last dev's id, hence devNo
			people.put(id, new Employee(id, stat.getRandomFloor()));
		}
	}

	private void setupPeopleEntry(Simulator sim) {
		for (Integer p : people.keySet()) {
			people.get(p).pressButton(sim);
		}
	}

	public void setCurrentFloor(ArrayList<Integer> pIDs, int floorNo) {
		for (int pID : pIDs)
			people.get(pID).setCurrentFloor(floorNo);
	}

	public Person getPerson(int pID) {
		return people.get(pID);
	}
}