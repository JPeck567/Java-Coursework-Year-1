package uk.ac.aston.jpd.coursework.officebuilding.stats;

public class ArrivalSimulator {
	private static final double PROBQMAIN = 0.005;
	private final double probQClient;

	public ArrivalSimulator(double q) {
		probQClient = q;
	}

	public boolean getProbCli(double randNum) {
		if (randNum < probQClient) {
			return true;
		}
		return false;
	}

	public boolean getProbMain(double randNum) {
		if(PROBQMAIN > randNum) {
			return true;
		}
		return false;
	}

}