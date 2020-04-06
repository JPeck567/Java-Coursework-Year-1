package uk.ac.aston.jpd.coursework.officebuilding.person.handler;


import java.util.HashMap;
import java.util.Map;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

public class PersonHandler {
	private Map<Integer, Person> people;
	private Stats stat;
	
	public PersonHandler(int empNo, int devNo, int seed) {
		people = new HashMap<Integer, Person>();
		stat = new Stats();
		generateEmps(empNo);
		generateDevs(devNo);
		
	}
	
	public void tick(Simulator sim) {
		
	}
	
	private void generateDevs(int devNo) {
		// TODO Auto-generated method stub
		
	}

	private void generateEmps(int empNo) {
		// TODO Auto-generated method stub
		
	}

	public Person getPerson(int pID) {
		return people.get(pID);
	}
}