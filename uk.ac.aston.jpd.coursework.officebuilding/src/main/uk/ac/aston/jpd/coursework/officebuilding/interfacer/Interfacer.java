package uk.ac.aston.jpd.coursework.officebuilding.interfacer;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import uk.ac.aston.jpd.coursework.officebuilding.Launcher;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Interfacer {
	public Interfacer() {

	}

	public void printSimulation(Simulator sim, int tick, Floor[] floors, List<String> elevatorQueue,
			String elevatorState, int elevatorCurrentFloor, int noComplaints) {
		StringBuilder output = new StringBuilder();

		int tickSecs = tick * 10;
		String elevatorQueueStr = elevatorQueue.toString();

		output.append("Seconds: " + tickSecs + "\n");
		output.append("Number of complaints: " + noComplaints + "\n");
		if (elevatorState.equals("open")) {
			elevatorQueueStr = elevatorQueueStr.substring(1, elevatorQueueStr.length());
		}

		for (int floor = 0; floor < floors.length; floor++) {
			Floor f = floors[floor];
			List<String> onFloorStr = f.getOnFloorList().stream().map(pID -> sim.getPerson(pID).toString())
					.collect(Collectors.toList());
			List<String> waitingStr = f.getWaitingQueue().stream().map(pID -> sim.getPerson(pID).toString())
					.collect(Collectors.toList());
			//String.format("|%-20|", 93); // prints: |93                  |

			String floorPrint = "On Floor " + String.format("%-30s", onFloorStr) + "Waiting "
					+ String.format("%-30s|", waitingStr);

			if (elevatorCurrentFloor == floor) {
				floorPrint += elevatorQueue + " " + elevatorState.substring(0, 1).toUpperCase()
						+ elevatorState.substring(1, elevatorState.length());
			}

			output.append(floorPrint + "\n");
		}

		System.out.println(output);
	}

	public int readInt() {
		Scanner in = new Scanner(System.in);
		while (!in.hasNextInt()) {
			System.out.println("Value must be an integer: ");
			in.nextLine();
		}

		int input = in.nextInt();
		in.close();

		return input;
	}

	public double readDouble() {
		Scanner in = new Scanner(System.in);
		while (!in.hasNextDouble()) {
			System.out.println("Value must be a double: ");
			in.nextLine();
		}

		double input = in.nextDouble();
		in.close();

		return input;
	}

	public int getEmpNo() {
		System.out.print("Enter the number of Employees: ");
		return readInt();
	}

	public int getDevNo() {
		System.out.print("Enter the number of Developers: ");
		return readInt();
	}

	public int getSeed() {
		System.out.print("Enter the seed: ");
		return readInt();
	}

	public double getP() {
		System.out.print("Enter the probability of p: ");
		return readDouble();
	}

	public double getQ() {
		System.out.print("Enter the probability of q: ");
		return readDouble();
	}
}