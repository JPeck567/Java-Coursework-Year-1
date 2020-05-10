package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

/**
*
*/
public class ArrivingPerson extends Person {
	protected int startingTick; // as opposed to waiting tick of people class, starting tick is updated once only.
	private final int timeAvailable;
	private boolean toExit;

	/**
	 *
	 */
	public ArrivingPerson(Stats stat, int weight, int id, int destBoundL, int destBoundR, int timeAvailable) {
		super(stat, weight, id, destBoundL, destBoundR);
		this.startingTick = PersonHandler.DEFAULTSTARTINGTICK;
		this.timeAvailable = timeAvailable;
		this.toExit = false;
	}

	@Override
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) {
		if (startingTick != PersonHandler.DEFAULTSTARTINGTICK) { // if gotten to req floor
			if (isTimeTaken(sim.getTick())) { // if time limit is up
				if(toExit) {
					if (currentFloor == 0) { // if at ground
						leave(sim, pHandle);
					}
				} else {
					changeFloor(sim);
					toExit = true;
			} 
				

			}
		}
	}

	public void changeFloor(Simulator sim) { // overload method
		changeFloor(sim, 0); // defaults to floor 0
	}

	public void leave(Simulator sim, PersonHandler pHandle) {
		sim.removeFromFloor(currentFloor, id);
		pHandle.addToRemove(id);
	}

	/**
	 *
	 */
	public boolean isTimeTaken(int currentTick) { //time taken is set to 9999,  meaning time taken will always be false until it is set to an appropriate value, when person gets to floor
		if (timeAvailable <= (currentTick - startingTick)) {
			return true;
		}
		return false;
	}

	@Override
	protected void addSelfToFloor(Simulator sim) {
		super.addSelfToFloor(sim);
		startingTick = sim.getTick();
	}

	/**
	 *
	 */
	public int getStartingTick() {
		return startingTick;
	}

	/**
	 *
	 */
	public void setStartingTick(int tick) {
		startingTick = tick;
	}

	@Override
	public void moveOffElevator(int currentTick) { // overload method
		super.moveOffElevator(currentTick); // need to initialise starting tick
		if (startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			startingTick = currentTick;
		}

	}
}
