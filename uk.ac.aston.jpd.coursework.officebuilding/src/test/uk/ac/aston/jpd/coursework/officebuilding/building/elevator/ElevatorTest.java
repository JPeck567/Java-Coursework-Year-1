package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

import uk.ac.aston.jpd.coursework.officebuilding.TestResources;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class ElevatorTest extends TestResources {
	public ElevatorTest() {
		super();
	}
	
	@Test
	public void standardOffloadPeopleTest() { // offload onto floor 0, which is where elevator is at default
		Person p1 = makeEmployee(0);
		Person p2 = makeEmployee(0);

		loadElevatorAndPersonHandler(new Person[] { p1, p2 });

		buildingTickLoop(2); // 2 ticks, to open + close
		assertTrue(defaultElevator.getList().isEmpty()); // if queue is empty, offload happened correctly
		assertEquals(0, p1.getCurrentFloor());
		assertEquals(0, p2.getCurrentFloor());
	}

	@Test
	public void liftOffloadPeopleForSpecificPeopleTest() { // offload where only two people of four need to get off
		Person p1 = makeEmployee(0);
		Person p2 = makeEmployee(0);
		Person p3 = makeEmployee(1);
		Person p4 = makeEmployee(1);
		loadElevatorAndPersonHandler(new Person[] { p1, p2, p3, p4 });

		buildingTickLoop(2); // 2 ticks, to open + close

		List<Integer> l = defaultElevator.getList();
		assertEquals(2, l.size()); // verifies that there are two people, and the two people are the ones for floor 1, not 0
		assertTrue(l.contains(p3.getID()));
		assertTrue(l.contains(p4.getID()));
		assertEquals(0, p1.getCurrentFloor());
		assertEquals(0, p2.getCurrentFloor());
	}

	@Test
	public void standardOnloadPeopleTest() { // onload of two people
		Person p1 = makeEmployee(2);
		Person p2 = makeEmployee(2);
		loadGroundFloorAndPersonHandler(new Person[] { p1, p2 });

		buildingTickLoop(2);

		List<Integer> l = defaultElevator.getList();

		assertEquals(2, l.size());
		assertTrue(l.contains(p1.getID()));
		assertTrue(l.contains(p2.getID()));
	}

	@Test
	public void fullLiftOnloadPersonTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);
		Person p3 = makeEmployee(1);
		Person p4 = makeEmployee(1);

		Person p5 = makeEmployee(1);
		loadElevatorAndPersonHandler(new Person[] { p1, p2, p3, p4 });
		loadFloor(new Person[] { p5 }, p5.getCurrentFloor());

		buildingTickLoop(2);

		List<Integer> l = defaultElevator.getList();
		assertTrue(l.contains(p1.getID())); // people still on lift
		assertTrue(l.contains(p2.getID()));
		assertTrue(l.contains(p3.getID()));
		assertTrue(l.contains(p4.getID()));

		assertTrue(defaultBuilding.getFloor(0).getWaitingQueue().contains(p5.getID())); // person who cannot get on is still waiting
	}

	@Test
	public void rivalryOnloadDevTest() {
		Person g = makeDeveloper(0, 'g');
		Person m = makeDeveloper(1, 'm');

		loadGroundFloorAndPersonHandler(new Person[] { g });
		loadElevatorAndPersonHandler(new Person[] { m });

		buildingTickLoop(2);

		List<Integer> l = defaultElevator.getList();
		assertEquals(1, l.size());
		assertTrue(l.contains(m.getID()));
	}

	@Test
	public void floorReRequestWhenFullCapacityOnOnloadTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);
		Person p3 = makeEmployee(1);
		Person p4 = makeEmployee(1);

		Person p5 = makeEmployee(1);
		loadElevatorAndPersonHandler(new Person[] { p1, p2, p3, p4 });
		loadGroundFloorAndPersonHandler(new Person[] { p5 });

		buildingTickLoop(2); // allows open close on floor 0 (2 ticks) and move to floor 1. by this time rerequest occurs
		assertFalse(getElevatorRequest(0));
		buildingTickLoop(2);
		assertTrue(getElevatorRequest(0));
	}

	@Test
	public void combinedOpenClosePeopleTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);
		Person p3 = makeEmployee(0);
		Person p4 = makeEmployee(0);
		Person p5 = makeEmployee(1);
		Person p6 = makeEmployee(1);

		loadElevatorAndPersonHandler(new Person[] { p1, p2, p3, p4 });
		loadGroundFloorAndPersonHandler(new Person[] { p5, p6 });

		buildingTickLoop(1);
		assertTrue(defaultElevator.getDoorState().equals("open"));

		buildingTickLoop(1);
		assertTrue(defaultElevator.getDoorState().equals("close"));

		List<Integer> l = defaultElevator.getList();
		assertEquals(4, l.size());
		assertTrue(l.contains(p1.getID())); // verify p3 and p4 got off, and p5 and p6 got on
		assertTrue(l.contains(p2.getID()));
		assertTrue(l.contains(p5.getID()));
		assertTrue(l.contains(p6.getID()));
	}

	@Test
	public void movementBasedOnPeopleInLiftTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);

		loadElevatorAndPersonHandler(new Person[] { p1, p2 });

		buildingTickLoop(2); // 1 tick to assert new direction, the other for actual movement

		assertEquals(1, defaultElevator.getCurrentFloor());
	}

	@Test
	public void movementBasedOnWaitingQueueRequestTest() {
		defaultElevator.addRequest(1);

		buildingTickLoop(2);

		assertEquals(1, defaultElevator.getCurrentFloor());
	}

	@Test
	public void combinedPeopleOnLiftAndWaitingQueueRequestMovementTest() {
		Person p1 = makeEmployee(1);
		Person p2 = makeEmployee(1);

		loadElevatorAndPersonHandler(new Person[] { p1, p2 });
		defaultElevator.addRequest(2);

		buildingTickLoop(4); // 1 tick for asserting new dir, 1 for movement, and the other 2 for open/close
		assertEquals(1, defaultElevator.getCurrentFloor());

		buildingTickLoop(1); // 1 more tick for moving to floor 2. dir is already asserted from tick 4 above.
		assertEquals(2, defaultElevator.getCurrentFloor());

	}

	@Test
	public void idleStateMovementTest() {
		Person p1 = makeEmployee(1);
		loadElevatorAndPersonHandler(new Person[] { p1 });

		buildingTickLoop(4); // 1 tick for asserting new dir, 1 for movement, and the other 2 for open/close

		assertEquals('I', getElevatorDirection()); // tests that elevator know there is not req's

		buildingTickLoop(1);

		assertEquals(0, defaultElevator.getCurrentFloor()); // to test it goes to ground

		buildingTickLoop(10);

		assertEquals(0, defaultElevator.getCurrentFloor()); // to test it rests at ground
	}
	
	@Test 
	public void checkDownMethodForNothingTest(){
		// above, shouldn't check
		Person p1 = makeEmployee(5);
		defaultElevator.addRequest(4);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(-1, checkDownElevatorMethod());
	}

	@Test
	public void checkDownMethodForRequestTest() {
		Person p1 = makeEmployee(1);
		defaultElevator.addRequest(2);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(2, checkDownElevatorMethod());  // gets req, not person

	}

	@Test
	public void checkDownMethodForPersonTest() {
		Person p1 = makeEmployee(1);
		defaultElevator.addRequest(0);
		
		// above
		defaultElevator.addRequest(4);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(1, checkDownElevatorMethod());
	}
	
	
	@Test 
	public void checkUpMethodForNothingTest(){
		// below, shouldn't check
		Person p1 = makeEmployee(2);
		defaultElevator.addRequest(3);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(-1, checkUpElevatorMethod());
	}

	@Test
	public void checkUpMethodForRequestTest() {
		Person p1 = makeEmployee(5);
		defaultElevator.addRequest(4);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(4, checkUpElevatorMethod());  // should get req at floor 4
	}

	@Test
	public void checkUpMethodForPersonTest() {
		Person p1 = makeEmployee(4);
		defaultElevator.addRequest(5);

		setElevatorCurrentFloor(3);
		loadElevatorAndPersonHandler(new Person[] { p1 });
		
		assertEquals(4, checkUpElevatorMethod());  // should get person for floor 4
	}

	@Test
	public void setDirectionMethodForUpTest() {
		setElevatorCurrentFloor(3);
		
		setDirectionElevatorMethod(4);
		
		assertEquals('U', getElevatorDirection());
		
	}

	@Test
	public void setDirectionMethodForDownTest() {
		setElevatorCurrentFloor(3);
		
		setDirectionElevatorMethod(2);
		
		assertEquals('D', getElevatorDirection());
	}

	@Test
	public void setDirectionMethodForIdleTest() {
		setElevatorCurrentFloor(3);
		
		setDirectionElevatorMethod(-1);
		
		assertEquals('I', getElevatorDirection());
	}

	@Test
	public void tickMethodTest() {
	}

}