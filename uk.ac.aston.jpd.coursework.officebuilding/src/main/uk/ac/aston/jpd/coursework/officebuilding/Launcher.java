package uk.ac.aston.jpd.coursework.officebuilding;

import uk.ac.aston.jpd.coursework.officebuilding.interfacer.Interfacer;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

/**
 * 
 * @author Team 46
 * @author 190148289 Jennifer A. Appiah
 * @author 190095097 Hannah Elliman
 * @author 190055002 Jorge Peck
 * @author 190174923 Hongyi Wang
 * @version 1.0
 * @since 2020 Coursework
 * 
 * 
 * @summary This Class Creates instances of the simulator and the interfacer.
 * 
 */
public class Launcher {
	/**
	 * Declaring variables
	 * 
	 * @param sim       to initialise the simulation
	 * @param interface grabs parameters needed and displays the simulation
	 * 
	 */

	private final Simulator sim;
	private final Interfacer interfacer;

	/**
	 * This is the constructor class that initialise the variable
	 */
	public Launcher() {
		interfacer = new Interfacer();

		sim = createSimulator();
	}

	/**
	 * This main method creates a new launcher
	 * 
	 * @param args parameter is unused
	 * @throws InterruptedException This method throws and interrupted exception if
	 *                              the program is interrupted
	 */
	public static void main(String args[]) throws InterruptedException {
		Launcher l = new Launcher();
		l.launch();
	}

	/**
	 * This method constructs a new simulator object based on the values the
	 * interfacer gets from the user
	 * 
	 * @return this returns a new simulator object
	 */
	private Simulator createSimulator() {
		return new Simulator(7, 4, interfacer.getEmpNo(), interfacer.getDevNo(), interfacer.getSeed(),
				interfacer.getP(), interfacer.getQ());
	}

	/**
	 * The launch method will start the simulator's tick loop
	 * 
	 * @throws InterruptedException This method throws and interrupted exception if
	 *                              the program is interrupted
	 */
	private void launch() throws InterruptedException {
		sim.run(interfacer);
	}
}
