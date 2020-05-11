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
 * @summary This Class extends from arriving person to provide functionality for
 *          complaining
 */
public class Client extends ArrivingPerson {
	/**
	 * Declaring fields
	 */
	private final int entranceTick;

	/**
	 * This initialises all the fields and also calls the constructor of the
	 * ArrivingPerson class
	 * 
	 * @param id           this is used as a unique identifier of the person
	 * @param stat         this is used in the person constructor to get a random
	 *                     floor
	 * @param noFloors     this is the total number of floors, used for getting a
	 *                     random floor
	 * @param entranceTick this is used to keep track of how long the client has
	 *                     been waiting for
	 */
	public Client(int id, Stats stat, int noFloors, int entranceTick) {
		super(stat, PersonHandler.DEFAULTWEIGHT, id, 0, ((noFloors - 1) / 2), stat.getRandomRangeNum(10, 30) * 60 / 10);
		this.entranceTick = entranceTick;
	}

	/**
	 * This method will test if the client has been waiting too long or not and if
	 * so will make the client leave the building {@inheritDoc}
	 * 
	 * @param sim     this is used as an interfacer for the other classes
	 * @param pHandle this is used in the parent method and also here to increment
	 *                the number of complaints
	 * @param stat    this is used in the parent method
	 */
	@Override
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) {
		if (startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			if (isComplaining(sim.getTick()) && isWaiting) {
				sim.removeFromWaiting(currentFloor, id);
				pHandle.incrementComplaints();
				leaveFloor(sim, pHandle);
			}
		} else {
			super.tick(sim, pHandle, stat);
		}
	}

	/**
	 * This method overrides the parent's method to allow the client to leave via
	 * complaint, or via normally by calling the parent method {@inheritDoc}
	 * 
	 * @param sim     this is used as an interfacer for the other classes
	 * @param pHandle this is used to remove itself from the person map
	 */
	@Override
	protected void leaveFloor(Simulator sim, PersonHandler pHandle) {
		if (startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			pHandle.addToRemove(id);
		} else {
			super.leaveFloor(sim, pHandle);
		}

	}

	/**
	 * This method is to check if the time elapsed from the clients waiting time for
	 * the elevator is equal or greater than 60 ticks, such that if so, they will
	 * complain
	 * 
	 * @param currentTick this is used to find the time elapsed
	 * @return this returns a boolean to signify if they are complaining or not
	 */
	public boolean isComplaining(int currentTick) {
		if (60 <= (currentTick - entranceTick)) { // 60 is 10 mins in ticks (as 10 mins is 600 secs / 10 is 60 ticks
			return true;
		}
		return false;
	}
}