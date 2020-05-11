package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.ac.aston.jpd.coursework.officebuilding.TestResources;


public class ArrivingPersonTest extends TestResources{
	public ArrivingPersonTest() {
		super();
	}
	
	@Test
	public void movesFromFloorToWaitingAfterTimeTakenTest() {
		Person m = makeMaintenance(1);
		m.pressButton(defaultSim);

		loadPersonHandler(new Person[] {m});
		
		buildingTickLoop(4);  // to move person to floor
		
		setTimeAvailible((ArrivingPerson) m, 20);   // 20 ticks of time
		
		setSimTickField(20);

		
		m.tick(defaultSim, defaultPersonHandler, defaultStats);
		buildingTickLoop(1);  // to load person to waiting

		assertTrue(defaultBuilding.getFloor(m.getCurrentFloor()).getWaitingQueue().contains(m.getID()));	
	}
	
	@Test
	public void isTimeTakenMethodTrueTest() {
		Person m = makeMaintenance(1);
		
		m.pressButton(defaultSim);

		loadPersonHandler(new Person[] {m});
		
		buildingTickLoop(4);  // to move person to floor
		
		setTimeAvailible(((ArrivingPerson) m), 20);
		setSimTickField(20);
		assertTrue(isTimeTakenMethod((ArrivingPerson) m));
	}
	
	@Test
	public void isTimeTakenMethodFalseTest() {
		Person m = makeMaintenance(1);
		
		m.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {m});
		
		buildingTickLoop(4);  // to move person to floor
		
		assertFalse(isTimeTakenMethod((ArrivingPerson) m));  // just entered so should be false
	}
	
	
}
