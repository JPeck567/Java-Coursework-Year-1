package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

import uk.ac.aston.jpd.coursework.officebuilding.person.handler.PersonHandler;
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
	public Client(int id, int destination, int timeAvailable, int entranceTick) {
		super(PersonHandler.DEFAULTWEIGHT, id, destination, timeAvailable);
		this.entranceTick = entranceTick;
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