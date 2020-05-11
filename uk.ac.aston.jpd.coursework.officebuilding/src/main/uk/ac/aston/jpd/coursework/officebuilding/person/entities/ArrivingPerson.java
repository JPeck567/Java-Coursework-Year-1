package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;

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
 * @summary This Class extends from the person class to add attributes and
 *          methods related to people arriving and leaving
 * 
 */

public class ArrivingPerson extends Person {
	/**
	 * Declaring fields
	 */
	protected int startingTick; // as opposed to waiting tick of people class, starting tick is updated once only.
	private final int timeAvailable;
	private boolean toExit;

	/**
	 * This constructor initialises all the fields and also calls the constructor of
	 * the person class
	 * 
	 *
	 * @param stat          this is used in the person constructor to get a random
	 *                      floor
	 * @param weight        this is used in the person constructor to signify the
	 *                      weight of the people, which the elevator uses
	 * @param id            this is used as a unique identifier of the person
	 * @param destBoundL    this is the lowest floor the person can go to
	 * @param destBoundR    this is the highest floor the person can go to
	 * @param timeAvailable this is used for getting how many ticks the person can
	 *                      stay at a floor before leaving
	 */
	public ArrivingPerson(Stats stat, int weight, int id, int destBoundL, int destBoundR, int timeAvailable) {
		super(stat, weight, id, destBoundL, destBoundR);
		this.startingTick = PersonHandler.DEFAULTSTARTINGTICK;
		this.timeAvailable = timeAvailable;
		this.toExit = false;
	}

	/**
	 * This method runs the function related to, if the person needs to exit the
	 * building or not
	 * 
	 * @param sim     this is used as an interfacer between the building class and
	 *                to get the current tick
	 * @param pHandle this is used in the leave method
	 * @param stat    this is used to provide people with their probabilities
	 */
	@Override
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) { // overloading method
		if (startingTick != PersonHandler.DEFAULTSTARTINGTICK) { // if gotten to req floor
			if (isTimeTaken(sim.getTick())) { // if time limit is up
				if (toExit) {
					if (currentFloor == 0) { // if at ground
						leaveFloor(sim, pHandle);
					}
				} else {
					changeFloor(sim);
					toExit = true;
				}

			}
		}
	}

	/**
	 * This method overloads the change floor method of the person class to ensure
	 * the current floor changes to the ground floor
	 * 
	 * @param sim this is passed into the change floor method of the person class
	 */
	public void changeFloor(Simulator sim) { // overload method
		changeFloor(sim, 0); // defaults to floor 0
	}

	/**
	 * This method removes the person's id from the floor and removes it from the
	 * person map in the person handler object
	 * 
	 * @param sim     this is used the interfacer of the floor object
	 * @param pHandle this is used to remove the person id from the person map
	 */
	public void leaveFloor(Simulator sim, PersonHandler pHandle) {
		sim.removeFromFloor(currentFloor, id);
		pHandle.addToRemove(id);
	}

	/**
	 * This method checks if the time elapsed is greater or equal to the time
	 * available
	 * 
	 * @param currentTick this is used to check how many ticks it has been since the
	 *                    person has entered their destination floor
	 * @return returns a boolean to represent if the client has exceeded their time
	 *         at their destination floor or not
	 * 
	 */
	public boolean isTimeTaken(int currentTick) { //time taken is set to 9999,  meaning time taken will always be false until it is set to an appropriate value, when person gets to floor
		if (timeAvailable <= (currentTick - startingTick)) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This will call the addSelfToFloor method of Person, and will set the
	 * startingTick field to signify the client has entered their destination floor
	 * 
	 * @param sim this is used as an interfacer for the floor class, and to get the current tick
	 *
	 */
	@Override
	protected void addSelfToFloor(Simulator sim) {
		super.addSelfToFloor(sim);
		startingTick = sim.getTick();
	}

	/**
	 * This method will call the persons moveOffElevator method, whilst also setting
	 * their startingTick if they are arriving at their destination floor
	 * 
	 * @param currentTick the current tick of the simulation, used to initialise the starting tick of the 
	 * client
	 */
	@Override
	public void moveOffElevator(int currentTick) { // overload method
		super.moveOffElevator(currentTick); // need to initialise starting tick
		if (startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			startingTick = currentTick;
		}

	}
}
