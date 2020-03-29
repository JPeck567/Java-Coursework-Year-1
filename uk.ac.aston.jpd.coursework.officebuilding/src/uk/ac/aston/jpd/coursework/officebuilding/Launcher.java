package uk.ac.aston.jpd.coursework.officebuilding;

import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Launcher {
	
	private Simulator sim;
	
	public Launcher() {
		sim = new Simulator(0, 0);
	}
	
	private void start() {
		while(true) {
			sim.tick();
		}
	}
	
	public static void main(String args[]) {
		Launcher launch = new Launcher();
		// where the whole programme begins
	}
}