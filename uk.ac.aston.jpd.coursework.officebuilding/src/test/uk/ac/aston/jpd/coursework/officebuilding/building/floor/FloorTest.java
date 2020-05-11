package uk.ac.aston.jpd.coursework.officebuilding.building.floor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.PriorityQueue;

import uk.ac.aston.jpd.coursework.officebuilding.TestResources;
import uk.ac.aston.jpd.coursework.officebuilding.building.Building;
import uk.ac.aston.jpd.coursework.officebuilding.building.Button;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.Elevator;
import uk.ac.aston.jpd.coursework.officebuilding.building.elevator.PList;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class FloorTest extends TestResources {
	public FloorTest() {
		super();
	}

	@Test
	public void checkButtonPressAddsPersonToWaitingQueue() {
		Person p1 = makeEmployee(1);
		p1.pressButton(defaultSim);
		setElevatorCurrentFloor(5);  // so doesn't immediately get picked up
		loadPersonHandler(new Person[] {p1});
		
		buildingTickLoop(1);
		
		Queue<Integer> q = defaultBuilding.getFloor(0).getWaitingQueue();
		
		assertEquals(1, q.size());
		assertTrue(q.contains(p1.getID()));
	}

	@Test
	public void checkClientIsFrontOfQueue() {
		Person p1 = makeEmployee(1);
		Person p2 = makeClient(1);
		p1.pressButton(defaultSim);
		p2.pressButton(defaultSim);
		setElevatorCurrentFloor(5);  // so doesn't immediately get picked up
		
		loadPersonHandler(new Person[] {p1, p2});
		
		buildingTickLoop(1);
		
		assertEquals(p2.getID(), defaultBuilding.getFloor(0).getWaitingQueue().poll().intValue());
	
	}

	@Test
	public void checkRivalriesGotoBackOfQueueTest() {
		Person m1 = makeDeveloper(1, 'm'); 
		Person g1 = makeDeveloper(1, 'g');
		m1.pressButton(defaultSim);  // m presses button first, so g1 will be still waiting
		g1.pressButton(defaultSim);
		
		loadPersonHandler(new Person[] {m1, g1});
		
		buildingTickLoop(1);
		
		List<Integer> l = defaultElevator.getList();
		
		assertEquals(g1.getID(), defaultBuilding.getFloor(0).getWaitingQueue().poll().intValue());
		assertEquals(1, l.size());
		assertTrue(l.contains(m1.getID()));
	}

	@Test
	public void checkPeopleWaitInOrderOfButtonPressTest() {
		Person p1 = makeEmployee(1); 
		Person p2 = makeEmployee(1);
		Person p3 = makeClient(1);
		Person p4 = makeClient(1);
		Person p5 = makeDeveloper(1, 'm');
		Person p6 = makeDeveloper(1, 'g');
		Person p7 = makeMaintenance(1);
		
		
		p1.pressButton(defaultSim);  // m presses button first, so g1 will be still waiting
		p2.pressButton(defaultSim);
		p3.pressButton(defaultSim);
		p4.pressButton(defaultSim);
		p5.pressButton(defaultSim);
		p6.pressButton(defaultSim);
		p7.pressButton(defaultSim);
		
		setElevatorCurrentFloor(5);  // so doesn't immediately get picked up
		
		
		
		loadPersonHandler(new Person[] {p1, p2, p3, p4, p5, p6, p7});
		
		buildingTickLoop(1);
		
		Queue<Integer> l = defaultBuilding.getFloor(0).getWaitingQueue();
		assertEquals(p3.getID(), l.poll().intValue());  // clients first
		assertEquals(p4.getID(), l.poll().intValue());
		assertEquals(p1.getID(), l.poll().intValue());  // then the rest
		assertEquals(p2.getID(), l.poll().intValue());
		assertEquals(p5.getID(), l.poll().intValue());
		assertEquals(p6.getID(), l.poll().intValue());
		assertEquals(p7.getID(), l.poll().intValue());
	}

	@Test
	public void checkPersonRemovedFromWaitingQueueAfterOnload() {
		Person p1 = makeEmployee(1); 
		p1.pressButton(defaultSim);  // m presses button first, so g1 will be still waiting

		
		loadPersonHandler(new Person[] {p1});
		
		buildingTickLoop(1);

		assertEquals(0, defaultBuilding.getFloor(0).getWaitingQueue().size());
	}

	@Test
	public void checkPersonGoesToOnFloorAfterOffload() {
		Person p1 = makeEmployee(0);

		loadElevatorAndPersonHandler(new Person[] { p1 });

		buildingTickLoop(2); // 2 ticks, to open + close
		
		List<Integer> l = defaultBuilding.getFloor(0).getOnFloorList();
		assertEquals(1, l.size());
		assertTrue(l.contains(p1.getID()));
	}

}