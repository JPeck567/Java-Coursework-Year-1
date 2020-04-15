package uk.ac.aston.jpd.coursework.officebuilding.simulator;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
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
	private static final int SIMTIME = 2880; // 8hrs = 2880 ticks (as each tick is 10s)
	private PersonHandler peopleHandle;
	private Building building;
	public final static int FLOORNO = 7; // are 7 floors. represented as 0 to 6 in the floor array of building
	public final static int MAXCAPACITY = 4;
	public final static int DEFAULTFLOOR = 0;

	/**
	 * Constructor that creates a simulator
	 * 
	 * @param elevator The elevator within the building
	 */
	public Simulator(int empNo, int devNo, int seed) {
		this.EMPNO = empNo;
		this.DEVNO = devNo;
		this.building = new Building(generateElevator());

		this.peopleHandle = new PersonHandler(EMPNO, DEVNO, seed, this);

		// other stuff to do with people etc
	}

	public void run() throws InterruptedException { // called by launcher
		while (tick <= SIMTIME) {
			System.out.println("tick: " + tick);

			tick();
			Thread.sleep(125); // 1000ms = 1 second. Therefore in real life, each tick execution is ~ 1 second
								// ( 1s + code execution time between loops)
		}
	}

	/**
	 * Is the clock that propagates the program
	 */
	private void tick() {
		tick += 1;
		building.tick(this);
		// tick functions related to persons/stats etc
	}

	/**
	 * Creates the elevator
	 */
	private Elevator generateElevator() {
		return new Elevator(new PQueue(Simulator.MAXCAPACITY));
	}

	public Button getButton(int floorNo) {
		return building.getFloor(floorNo).button;
	}

	public Person getPerson(int pID) {
		return peopleHandle.getPerson(pID);
	}

	public void passNewCurrentFloor(ArrayList<Integer> offloaded, int floorNo) {
		peopleHandle.setCurrentFloor(offloaded, floorNo);
	}

}