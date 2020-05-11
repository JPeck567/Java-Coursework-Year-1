package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import uk.ac.aston.jpd.coursework.officebuilding.TestResources;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class WaitingQueueComparatorTest extends TestResources {
	private WaitingQueueComparator c;

	public WaitingQueueComparatorTest() {
		super();
		c = new WaitingQueueComparator(defaultSim);
	}

	@Test
	public void checkClientClientComparisonTest() {
		Person c1 = makeClient(1);
		Person c2 = makeClient(1);
		c1.pressButton(defaultSim);
		c2.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {c1, c2});
		
		assertTrue(c.compare(c1.getID(), c2.getID()) < 0);  // earlier client (c1) is first in queue
	}

	@Test
	public void checkClientPersonComparisonTest() {
		Person c1 = makeClient(1);
		Person p1 = makeEmployee(1);
		c1.pressButton(defaultSim);
		p1.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {c1, p1});
		
		assertTrue(c.compare(c1.getID(), p1.getID()) < 0);  // client is first in queue
	}

	@Test
	public void checkPersonClientComparisonTest() {
		Person p1 = makeEmployee(1);
		Person c1 = makeClient(1);
		p1.pressButton(defaultSim);
		c1.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {p1, c1});
		
		assertTrue(c.compare(p1.getID(), c1.getID()) > 0);  // client still first in queue
	}

	@Test
	public void checkPersonPersonComparisonTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);
		p2.pressButton(defaultSim);
		p1.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {p1, p2});
		
		assertTrue(c.compare(p1.getID(), p2.getID()) > 0);  // c2 pressed first so is in front
	}

}