package uk.ac.aston.jpd.coursework.officebuilding.stats;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Stats {
	
	public Stats (int seed) {
		//make random object
		//seed field.
		//the two stats classes
	}
	// notes on:
	//   finding probablility --
	// 		is code to do so
	// 			rnd is java.util.Random object
	// 			double num = rnd.nextDouble(); // random number from 0.0 to 0.1
	//			if num is < than the probability set, event happening will be true.
	
	
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
