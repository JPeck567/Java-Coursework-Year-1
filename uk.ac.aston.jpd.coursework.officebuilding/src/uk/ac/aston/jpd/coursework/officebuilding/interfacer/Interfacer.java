package uk.ac.aston.jpd.coursework.officebuilding.interfacer;

import java.util.List;
import java.util.Scanner;

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

	public void printSimulation(int tick, Floor[] floors, List<Integer> elevatorQueue, String elevatorState,
			int elevatorCurrentFloor) {
		StringBuilder output = new StringBuilder();

		int tickSecs = tick * 10;
		String elevatorQueueStr = elevatorQueue.toString();

		output.append(tickSecs + "\n");

		if (elevatorState.equals("open")) {
			elevatorQueueStr = elevatorQueueStr.substring(1, elevatorQueueStr.length());
		}

		for (int floor = 0; floor < floors.length; floor++) {
			Floor f = floors[floor];
			//String.format("|%-20|", 93); // prints: |93                  |

			String floorPrint = "On Floor " + String.format("%-30s", f.getOnFloorList().toString()) + "Waiting "
					+ String.format("%-30s|", f.getWaitingQueue().toString());

			if (elevatorCurrentFloor == floor) {
				floorPrint += elevatorQueue;
			}

			output.append(floorPrint + "\n");
		}

		System.out.println(output);
	}

	public void readVariables() {
		System.out.println("Enter the number of Employees: \n");
		if (in.hasNext()) {
			empNo = in.nextInt();
		}
		
		System.out.println("Enter the number of Developers: \n");
		if (in.hasNext()) {
			devNo = in.nextInt();
		}

		System.out.println("Enter the seed: \n");
		if (in.hasNext()) {
			seed = in.nextInt();
		}

		System.out.println("Enter the probability of p: \n");
		if (in.hasNext()) {
			p = in.nextDouble();
		}

		System.out.println("Enter the probability of q: \n");
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