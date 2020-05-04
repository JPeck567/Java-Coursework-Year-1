package uk.ac.aston.jpd.coursework.officebuilding.interfacer;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import uk.ac.aston.jpd.coursework.officebuilding.Launcher;
import uk.ac.aston.jpd.coursework.officebuilding.building.floor.Floor;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;

public class Interfacer {
	private static Scanner in;
	private int empNo;
	private int devNo;
	private int seed;
	private double p;
	private double q;

	public Interfacer() {
		in = new Scanner(System.in);
	}

	public void printSimulation(Simulator sim, int tick, Floor[] floors, List<String> elevatorQueue, String elevatorState, int elevatorCurrentFloor, int noComplaints) {
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
			List<String> onFloorStr = f.getOnFloorList().stream().map(pID -> sim.getPerson(pID).toString()).collect(Collectors.toList());
			List<String> waitingStr = f.getWaitingQueue().stream().map(pID -> sim.getPerson(pID).toString()).collect(Collectors.toList());
			//String.format("|%-20|", 93); // prints: |93                  |

			String floorPrint = "On Floor " + String.format("%-30s", onFloorStr) + "Waiting "
					+ String.format("%-30s|", waitingStr);

			if (elevatorCurrentFloor == floor) {
				floorPrint += elevatorQueue + " " + elevatorState.substring(0, 1).toUpperCase() + elevatorState.substring(1, elevatorState.length());
			}

			output.append(floorPrint + "\n");
		}

		System.out.println(output);
	}

	public void readVariables() {
		System.out.print("Enter the number of Employees: ");
		if (in.hasNext()) {
			empNo = in.nextInt();
		}
		
		System.out.print("Enter the number of Developers: ");
		if (in.hasNext()) {
			devNo = in.nextInt();
		}

		System.out.print("Enter the seed: ");
		if (in.hasNext()) {
			seed = in.nextInt();
		}

		System.out.print("Enter the probability of p: ");
		if (in.hasNext()) {
			p = in.nextDouble();
		}

		System.out.print("Enter the probability of q: ");
		if (in.hasNext()) {
			q = in.nextDouble();
		}
	}

	public int getEmpNo() {
		return empNo;
	}

	public int getDevNo() {
		return devNo;
	}

	public int getSeed() {
		return seed;
	}

	public double getP() {
		return p;
	}

	public double getQ() {
		return q;
	}
}