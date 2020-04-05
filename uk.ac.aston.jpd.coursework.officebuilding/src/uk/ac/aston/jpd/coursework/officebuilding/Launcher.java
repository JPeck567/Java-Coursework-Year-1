package uk.ac.aston.jpd.coursework.officebuilding;

import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Launcher {
	
	private Simulator sim;
	
	public Launcher() {
		sim = new Simulator(0, 0);
	}
	
	private void start() throws InterruptedException {
		sim.run();
	}
	
	public static void main(String args[]) throws InterruptedException {
		Launcher launch = new Launcher();
		// where the whole programmes objects are all initialised
	
		launch.start();
		// where the simulator is ran
	}
}
