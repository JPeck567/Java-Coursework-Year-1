package uk.ac.aston.jpd.coursework.officebuilding;

import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Launcher {
	
	private Simulator sim;
	
	public Launcher() {
		sim = new Simulator(10, 10, 0);  // empNo, devNo and the seed
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
