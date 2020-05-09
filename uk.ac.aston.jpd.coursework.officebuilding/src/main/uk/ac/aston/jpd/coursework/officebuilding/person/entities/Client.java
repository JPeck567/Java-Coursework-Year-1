package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
import uk.ac.aston.jpd.coursework.officebuilding.stats.Stats;
/**
*
*/
public class Client extends ArrivingPerson {
	/**
	 *
	 */
	private final int entranceTick;
	
	/**
	 *
	 */
	public Client(int id, Stats stat, int noFloors, int entranceTick) {
		super(PersonHandler.DEFAULTWEIGHT, id, stat.getRandomRangeNum(10, 30) * 60 / 10);
		this.destination = getRandomFloor(stat, 0, (noFloors - 1) / 2);
		this.entranceTick = entranceTick;
	}
	
	public void tick(Simulator sim) {
		if(isComplaining(sim.getTick()) && isWaiting) {
			
		}
	}

	/**
	 *
	 */
	public boolean isComplaining(int currentTick) {
		if(60 <= (currentTick - entranceTick)) {  // 60 is 10 mins in ticks (as 10 mins is 600 secs / 10 is 60 ticks
			return true;
		}
		return false;
	}
}