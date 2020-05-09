package uk.ac.aston.jpd.coursework.officebuilding.stats;

public class ArrivalSimulator {
	private static final double PROBQMAIN = 0.005;
	private final double probQClient;

	public ArrivalSimulator(double q) {
		probQClient = q;
	}

	public boolean getProbCli(double randNum) {
		return randNum < probQClient;

	}

	public boolean getProbMain(double randNum) {
		return randNum < PROBQMAIN;
	}
}