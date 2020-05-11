package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.simulator.Simulator;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;
/**
*
*/
public class Client extends ArrivingPerson {
	/**
	 * Declaring fields
	 */
	private final int entranceTick;


	/**
	 * @param id 
	 * @param stat
	 * @param noFloors
	 * @param entranceTick
	 */
	public Client(int id, Stats stat, int noFloors, int entranceTick) {
		super(stat, PersonHandler.DEFAULTWEIGHT, id, 0, ((noFloors - 1) / 2), stat.getRandomRangeNum(10, 30) * 60 / 10);
		this.entranceTick = entranceTick;
	}
	
	/**
	 *
	 */
	@Override
	public void tick(Simulator sim, PersonHandler pHandle, Stats stat) {
		if(startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			if(isComplaining(sim.getTick()) && isWaiting) {
				sim.removeFromWaiting(currentFloor, id);
				pHandle.incrementComplaints();
				leaveFloor(sim, pHandle);
			}
		} else {
			super.tick(sim, pHandle, stat);
		}
	}
	
	/**
	 *
	 */
	@Override
	protected void leaveFloor(Simulator sim, PersonHandler pHandle) {
		if(startingTick == PersonHandler.DEFAULTSTARTINGTICK) {
			pHandle.addToRemove(id);
		} else {
			super.leaveFloor(sim, pHandle);
		}
		
	}


	/**
	 * @param currentTick
	 * @return
	 */
	public boolean isComplaining(int currentTick) {
		if(60 <= (currentTick - entranceTick)) {  // 60 is 10 mins in ticks (as 10 mins is 600 secs / 10 is 60 ticks
			return true;
		}
		return false;
	}
}