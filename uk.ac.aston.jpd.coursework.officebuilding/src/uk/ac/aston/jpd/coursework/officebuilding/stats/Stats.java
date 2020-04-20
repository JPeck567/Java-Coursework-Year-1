package uk.ac.aston.jpd.coursework.officebuilding.stats;

import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Client;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Developer;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Employee;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Maintenance;
import uk.ac.aston.jpd.coursework.officebuilding.person.entities.Person;
import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import java.util.Random;

public class Stats {
	private Random rnd;
	private int seed;
	private ArrivalSimulator arrSim;
	private DecisionSimulator decSim;
	
	public Stats (int seed, double p) {
		//seed field.
		this.seed = seed;
		
		//make random object
		rnd = new Random (seed);
		
		//the two stats classes
		arrSim = new ArrivalSimulator(p);
		decSim = new DecisionSimulator ();
		
	}
	// notes on:
	//   finding probablility --
	// 		is code to do so
	public boolean getProb (Person p) {
		// if p is client or maintenance
		if(p instanceof Client) {
			return arrSim.getProbCli(rnd.nextDouble());
		} else if(p instanceof Maintenance) {
			return arrSim.getProbMain();
		} else { //  if (p instanceof Developer || p instanceof Employee)
			return decSim.getProb();
		}
		
		//double num = rnd.nextDouble(); // random number from 0.0 to 0.1
	}
	
	// 	the seed var
	//		pass the seed (a number you want) into the random constructor
	// 		the seed will specify what random numbers to generate
	//		so effectively it will simulate the same building randomness when a particular seed is given
	
	public void tick(PersonHandler pHandle, Simulator sim) {
		// TODO Auto-generated method stub
	}
	
	public int getRandomFloor() {
		// TODO
		return 4;
	}
}
