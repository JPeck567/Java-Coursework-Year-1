package uk.ac.aston.jpd.coursework.officebuilding;

import java.util.Random;

import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Launcher {

	private final Simulator sim;
	private final Interfacer interfacer;

	public Launcher() {
		interfacer = new Interfacer();

		sim = createSimulator();
	}

	private Simulator createSimulator() {
		return new Simulator(7, 4, interfacer.getEmpNo(), interfacer.getDevNo(), interfacer.getSeed(),
				interfacer.getP(), interfacer.getQ());
	}

	private void launch() throws InterruptedException {
		sim.run(interfacer);
	}

	public static void main(String args[]) throws InterruptedException {
		Launcher l = new Launcher();
		l.launch();
	}
}
