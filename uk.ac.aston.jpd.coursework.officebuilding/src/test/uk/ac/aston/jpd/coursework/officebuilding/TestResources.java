package uk.ac.aston.jpd.coursework.officebuilding;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PList;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.ArrivingPerson;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Employee;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Maintenance;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

public class TestResources {
	protected Simulator defaultSim;
	protected Building defaultBuilding;
	protected Elevator defaultElevator;
	protected PersonHandler defaultPersonHandler;
	protected Stats defaultStats;
	protected int noFloors;
	protected int idCounter;

	public TestResources() {
		defaultSim = new Simulator(7, 4, 0, 0, 0, 0, 0);
		try {
			Field b = defaultSim.getClass().getDeclaredField("building");
			b.setAccessible(true);
			defaultBuilding = (Building) b.get(defaultSim);

			Field e = defaultBuilding.getClass().getDeclaredField("elevator");
			e.setAccessible(true);
			defaultElevator = (Elevator) e.get(defaultBuilding);

			Field pH = defaultSim.getClass().getDeclaredField("peopleHandler");
			pH.setAccessible(true);
			defaultPersonHandler = (PersonHandler) pH.get(defaultSim);

			Field s = defaultPersonHandler.getClass().getDeclaredField("stat");
			s.setAccessible(true);
			defaultStats = (Stats) s.get(defaultPersonHandler);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		noFloors = defaultSim.getNoFloors();
		idCounter = 0;
	}
	
	public void buildingTickLoop(int noTicks) {
		for (int i = 0; i < noTicks; i++) {
			defaultBuilding.tick(defaultSim);
		}
	}
	
	public void setSimTickField(int tick) {
		try {
			Field b = defaultSim.getClass().getDeclaredField("tick");
			b.setAccessible(true);
			b.set(defaultSim, tick);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadPersonHandler(Person[] toLoad) {
		try {
			HashMap<Integer, Person> people;
			Field ppl = defaultPersonHandler.getClass().getDeclaredField("people");
			ppl.setAccessible(true);
			people = (HashMap<Integer, Person>) ppl.get(defaultPersonHandler);

			for (Person p : toLoad) {
				people.put(p.getID(), p);
			}

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void loadElevatorAndPersonHandler(Person[] pArr) {
		try {
			PList pList;
			Field pL = defaultElevator.getClass().getDeclaredField("pList");
			pL.setAccessible(true);
			pList = (PList) pL.get(defaultElevator);

			HashMap<Integer, Person> people;
			Field ppl = defaultPersonHandler.getClass().getDeclaredField("people");
			ppl.setAccessible(true);
			people = (HashMap<Integer, Person>) ppl.get(defaultPersonHandler);

			for (Person p : pArr) {
				p.setCurrentFloor(-1);
				pList.addPerson(p.getID());
				people.put(p.getID(), p);
			}

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public void loadGroundFloorAndPersonHandler(Person[] pArr) {
		try {
			HashMap<Integer, Person> people;
			Field ppl;

			ppl = defaultPersonHandler.getClass().getDeclaredField("people");
			ppl.setAccessible(true);
			people = (HashMap<Integer, Person>) ppl.get(defaultPersonHandler);

			for (Person p : pArr) {
				p.pressButton(defaultSim);
				people.put(p.getID(), p);
			}

			defaultBuilding.getFloor(0).tick(defaultElevator);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void loadFloor(Person[] pArr, int floorNo) {
		for (Person p : pArr) {
			p.pressButton(defaultSim);
		}
		defaultBuilding.getFloor(floorNo).tick(defaultElevator);
	}

	public boolean getElevatorRequest(int floorNo) {
		HashMap<Integer, Boolean> reqList;
		try {
			Field r;
			r = defaultElevator.getClass().getDeclaredField("requestsMap");
			r.setAccessible(true);
			reqList = ((HashMap<Integer, Boolean>) r.get(defaultElevator));
			return reqList.get(floorNo);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public char getElevatorDirection() {
		try {
			Field dir;
			dir = defaultElevator.getClass().getDeclaredField("direction");
			dir.setAccessible(true);
			return (char) dir.get(defaultElevator);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return 'N';
		}

	}

	public void setElevatorCurrentFloor(int floorNo) {
		try {
			Field curr;
			curr = defaultElevator.getClass().getDeclaredField("currentFloor");
			curr.setAccessible(true);
			curr.set(defaultElevator, floorNo);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public Person makeEmployee(int floor) {
		Person p = new Employee(idCounter, defaultStats, noFloors);
		p.setDestination(floor);
		idCounter++;
		return p;
	}

	public Person makeDeveloper(int floor, char c) {
		Person p = new Developer(idCounter, defaultStats, noFloors);
		p.setDestination(floor);
		Field company;

		try {
			company = p.getClass().getDeclaredField("company");
			company.setAccessible(true);
			company.set(p, c == 'm' ? "Goggles" : "Mugtome");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		idCounter++;
		return p;
	}
	
	public void setTimeAvailible(ArrivingPerson m, int time) {
		try {
			Field tA;
			tA = ArrivingPerson.class.getDeclaredField("timeAvailable");
			tA.setAccessible(true);
			tA.set(m, time); // 20 ticks
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isTimeTakenMethod(ArrivingPerson p) {
		try {
			Method timeTaken;
			timeTaken = ArrivingPerson.class.getDeclaredMethod("isTimeTaken", int.class);
			return (boolean) timeTaken.invoke(p, 20);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	
	public Person makeClient(int floor) {
		Person p = new Client(idCounter, defaultStats, noFloors, defaultSim.getTick());
		p.setDestination(floor);
		idCounter++;
		return p;
	}
	
	public Person makeMaintenance(int floor) {
		Person p = new Maintenance(idCounter, defaultStats, noFloors);
		p.setDestination(floor);
		idCounter++;
		return p;
	}

	public int checkDownElevatorMethod() {
		try {
			Method chkD = defaultElevator.getClass().getDeclaredMethod("checkDown", Simulator.class);
			chkD.setAccessible(true);
			return (int) chkD.invoke(defaultElevator, defaultSim);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int checkUpElevatorMethod() {
		try {
			Method chkD = defaultElevator.getClass().getDeclaredMethod("checkUp", Simulator.class);
			chkD.setAccessible(true);
			return (int) chkD.invoke(defaultElevator, defaultSim);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setDirectionElevatorMethod(int dest) {
		try {
			Method setD = defaultElevator.getClass().getDeclaredMethod("setDirection", int.class);
			setD.setAccessible(true);
			setD.invoke(defaultElevator, dest);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public Map<Integer, Person> getPersonMap() {
		try {
			Map<Integer, Person> people;
			Field ppl;

			ppl = defaultPersonHandler.getClass().getDeclaredField("people");
			ppl.setAccessible(true);
			people = (Map<Integer, Person>) ppl.get(defaultPersonHandler);
			return people;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
