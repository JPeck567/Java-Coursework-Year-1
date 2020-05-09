package uk.ac.aston.jpd.coursework.officebuilding.interfacer;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;

import uk.ac.aston.jpd.coursework.officebuilding.Launcher;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
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
 * @summary This Class manages the inputs of the user and will also output a
 *          text UI of the current simulation
 * 
 */

public class Interfacer {
	/**
	 * Declaring fields
	 */
	public static Scanner sc;

	/**
	 * This is the constructor method which creates a new scanner object which reads
	 * the keyboard input from the user
	 */
	public Interfacer() {
		sc = new Scanner(System.in);
	}

	/**
	 * This method prints the simulation as text, using a string builder
	 * 
	 * @param sim                  providing interface to person handler
	 * @param tick                 gives us the current tick
	 * @param floorWaitingQueues   is a list for all the waiting queues for each
	 *                             floor
	 * @param floorOnFloorLists    is a list for all the onFloor list for each floor
	 * @param elevatorList         is a list of people id in the elevator
	 * @param elevatorState        is the current state of the elevator door
	 * @param elevatorCurrentFloor is the current floor of the elevator
	 * @param noComplaints         is the number of complaints from clients
	 */
	public void printSimulation(Simulator sim, int tick, List<Queue<Integer>> floorWaitingQueues,
			List<List<Integer>> floorOnFloorLists, List<String> elevatorList, String elevatorState,
			int elevatorCurrentFloor, int noComplaints) {
		StringBuilder output = new StringBuilder();

		int tickSecs = tick * 10;
		String elevatorQueueStr = elevatorList.toString();

		output.append("Seconds: " + tickSecs + "\n");
		output.append("Number of complaints: " + noComplaints + "\n");
		if (elevatorState.equals("open")) {
			elevatorQueueStr = elevatorQueueStr.substring(1, elevatorQueueStr.length());
		}

		for (int floor = 0; floor < floorWaitingQueues.size(); floor++) {
			List<String> onFloorStr = floorOnFloorLists.get(floor).stream().map(pID -> sim.getPerson(pID).toString())
					.collect(Collectors.toList());
			List<String> waitingStr = floorWaitingQueues.get(floor).stream().map(pID -> sim.getPerson(pID).toString())
					.collect(Collectors.toList());

			// String.format("|%-20|", 93); // prints: |93 |

			String floorPrint = "On Floor " + String.format("%-30s", onFloorStr) + "Waiting "
					+ String.format("%-30s|", waitingStr);

			if (elevatorCurrentFloor == floor) {
				floorPrint += elevatorList + " " + elevatorState.substring(0, 1).toUpperCase()
						+ elevatorState.substring(1, elevatorState.length());
			}

			output.append(floorPrint + "\n");
		}

		System.out.println(output);
	}

	/**
	 * This method takes an integer input from the user. If it is invalid, it will
	 * ask for a valid input
	 * 
	 * @return this returns an integer from the user
	 */
	public int readInt() {
		while (!sc.hasNextInt()) {
			System.out.println("Value must be an integer: ");
			sc.nextLine();
		}
		return sc.nextInt();
	}

	/**
	 * This method takes a double input from the user. If it is invalid, it will ask
	 * for a valid input
	 * 
	 * @return this returns a double from the user
	 */
	public double readDouble() {
		while (!sc.hasNextDouble()) {
			System.out.println("Value must be a double: ");
			sc.nextLine();
		}

		return sc.nextDouble();
	}

	/**
	 * This method tells the user to enter an integer of the number of employees
	 * 
	 * @return this returns an integer to represent the number of employees
	 */
	public int getEmpNo() {
		System.out.print("Enter the number of Employees: ");
		return readInt();
	}

	/**
	 * This method tells the user to enter an integer of the number of developers
	 * 
	 * @return this returns an integer to represent the number of developers
	 */
	public int getDevNo() {
		System.out.print("Enter the number of Developers: ");
		return readInt();
	}

	/**
	 * This method tells the user to enter an integer for the seed
	 * 
	 * @return this returns an integer to represent the seed
	 */

	public int getSeed() {
		System.out.print("Enter the seed: ");
		return readInt();
	}

	/**
	 * This method tells the user to enter a double for the probability of p
	 * 
	 * @return this returns a double to represent the probability of p
	 */
	public double getP() {
		System.out.print("Enter the probability of p: ");
		return readDouble();
	}

	/**
	 * This method tells the user to enter a double for the probability of q
	 * 
	 * @return this returns a double to represent the probability of q
	 */
	public double getQ() {
		System.out.print("Enter the probability of q: ");
		return readDouble();
	}
}