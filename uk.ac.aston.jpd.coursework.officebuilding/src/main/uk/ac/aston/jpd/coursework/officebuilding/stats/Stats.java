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
	private final Random rnd;
	private final int seed;
	private final ArrivalSimulator arrSim;
	private final DecisionSimulator decSim;

	public Stats(int seed, double p, double q) {
		//make random object
		rnd = new Random(seed);

		//seed field.
		this.seed = seed;

		//the two stats classes
		decSim = new DecisionSimulator(p);
		arrSim = new ArrivalSimulator(q);
	}
	public boolean getDecisionProb() {
		return decSim.getProb(rnd.nextDouble());
	}

	public boolean getMArrivalProb() {
		return arrSim.getProbMain(rnd.nextDouble());
	}

	public boolean getCArrivalProb() {
		return arrSim.getProbCli(rnd.nextDouble());
	}

	public int getRandomRangeNum(int boundL, int boundR) { // generates rand num from 0 to boundR.
		return rnd.nextInt(boundR - boundL + 1) + boundL; // rand num between 0 and range(diff of l to r) and then + boundL, therefore moving range of nums into correct range given in parameters
	}
}
