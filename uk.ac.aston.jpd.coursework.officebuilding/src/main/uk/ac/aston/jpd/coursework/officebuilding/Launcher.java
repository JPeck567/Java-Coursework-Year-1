package uk.ac.aston.jpd.coursework.officebuilding;

import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

/**
 * 
 * @author Team 46
 * @author 190148289
 * @author 190095097
 * @author 190055002
 * @author 190174923
 * @version 1.0
 * @since 2020
 * 
 */
public class Launcher {
	/**
	 * 
	 * @param sim to initialise the simulation
	 * @param interface grabs parameters needed and displays the simulation
	 * 
	 */

	private final Simulator sim;
	private final Interfacer interfacer;
/**
 * @param Launcher constructor
 * 
 */
	public Launcher() {
		interfacer = new Interfacer();

		sim = createSimulator();
	}
/**
 * @
 * @throws InterruptedException
 * 
 */
	public static void main(String args[]) throws InterruptedException {
		Launcher l = new Launcher();
		l.launch();
	}
/**
 * @return an initialised simulator
 * 
 */
	private Simulator createSimulator() {
		return new Simulator(7, 4, interfacer.getEmpNo(), interfacer.getDevNo(), interfacer.getSeed(),
				interfacer.getP(), interfacer.getQ());
	}
/**
 * @throws an InterruptedException
 * 
 */
	private void launch() throws InterruptedException {
		sim.run(interfacer);
	}
}
