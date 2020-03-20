package uk.ac.aston.jpd.coursework.officebuilding.building.elevator;

import java.util.ArrayList;
import java.util.List;

import java.util.PriorityQueue;
import java.util.Queue;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;

public class ElevatorQueue {
	private Queue<Person> goingUp;
	private Queue<Person> goingDown;
	
	public ElevatorQueue() {
		goingUp = new PriorityQueue<Person>();
		goingDown = new PriorityQueue<Person>();
	}
	
	public getOffloadQueue
}