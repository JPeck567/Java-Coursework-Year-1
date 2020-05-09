package uk.ac.aston.jpd.coursework.officebuilding.stats;

public class DecisionSimulator {
	private final double PROBP;

	public DecisionSimulator(double p) {
		this.PROBP = p;
	}

	public boolean getProb(double randNum) {
		return randNum < PROBP;
	}

}