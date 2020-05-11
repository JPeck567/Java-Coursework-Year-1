package uk.ac.aston.jpd.coursework.officebuilding.stats;

import java.util.Random;

public class Stats {
	private final Random rnd;
	private final ArrivalSimulator arrSim;
	private final DecisionSimulator decSim;

	public Stats(int seed, double p, double q) {
		//make random object
		rnd = new Random(seed);

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
