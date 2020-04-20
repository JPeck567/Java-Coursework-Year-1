package uk.ac.aston.jpd.coursework.officebuilding.stats;

public class ArrivalSimulator {
	private static final double PROBQ_MAIN = 0.005;
	private final double probQClient;

	public ArrivalSimulator(double q) {
		probQClient = q;
	}

	public boolean getProbCli(double num) {
		if (num < probQClient) {
			return true;
		}
		return false;
	}

	public boolean getProbMain(double num) {
		// TODO Auto-generated method stub
		return false;
	}

}