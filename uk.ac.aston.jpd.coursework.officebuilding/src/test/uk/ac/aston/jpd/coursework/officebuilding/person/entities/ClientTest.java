package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.aston.jpd.coursework.officebuilding.TestResources;

public class ClientTest extends TestResources {
	public ClientTest() {
		super();
	}
	
	@Test
	public void isComplainingMethodTest() {
		Person p = makeClient(1);
		assertTrue(((Client) p).isComplaining(60));  // 60 ticks from beginning
	}
	
	@Test
	public void checkLeavesWaitingAndBuildingIfComplainingTest() {
		Person p = makeClient(1);
		p.pressButton(defaultSim);
		loadPersonHandler(new Person[] {p});
		
		for(int tick = 0; tick <= 60; tick++) {
			p.tick(defaultSim, defaultPersonHandler, defaultStats);
		}
		
		setSimTickField(60);
		
		defaultPersonHandler.tick(defaultSim); // to remove person
		
		assertFalse(getPersonMap().containsKey(p.getID()));
		assertFalse(defaultBuilding.getFloor(p.getCurrentFloor()).getWaitingQueue().contains(p.getID()));
	}
}