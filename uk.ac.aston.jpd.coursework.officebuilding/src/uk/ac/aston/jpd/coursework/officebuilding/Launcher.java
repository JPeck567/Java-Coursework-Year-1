package uk.ac.aston.jpd.coursework.officebuilding;

import java.util.Random;

import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Launcher {
	
	private Simulator sim;
	private Interfacer interfacer;
	
	public Launcher() {
		interfacer = new Interfacer();
	}
	
	private void launch() throws InterruptedException {
		sim.run(interfacer);
	}
	
	private void build() {
		interfacer.readVariables();
		sim = new Simulator(7, 4, interfacer.getEmpNo(), interfacer.getDevNo(), interfacer.getSeed(), interfacer.getP(), interfacer.getQ());
	}
	
	public static void main(String args[]) throws InterruptedException {
		Launcher l = new Launcher();
		l.build();
		l.launch();
	}
}
