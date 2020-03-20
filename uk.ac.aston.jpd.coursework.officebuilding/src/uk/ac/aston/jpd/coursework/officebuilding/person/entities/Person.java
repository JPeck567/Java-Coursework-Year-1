package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

public class Person implements Comparable<Person> {
	public Person() {
		
	}
	
	
	public int compareTo(Person p, Person p2) {
		return p.getDestination() - p2.getDestination();
	}

}
