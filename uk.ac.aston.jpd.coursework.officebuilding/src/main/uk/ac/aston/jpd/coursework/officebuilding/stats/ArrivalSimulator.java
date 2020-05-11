package uk.ac.aston.jpd.coursework.officebuilding.stats;

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
 * @summary This Class will tell if the chance of a client/maintenance crew
 *          arrives occurs or not, based on the static probability q
 */
public class ArrivalSimulator {
	/*
	 * Declaring fields
	 */
	private static final double PROBQMAIN = 0.005;
	private final double probQClient;

	/**
	 * This constructor initialises the field q.
	 * 
	 * @param q the probability q
	 */
	public ArrivalSimulator(double q) {
		probQClient = q;
	}

	/**
	 * This method returns the boolean based on if the random number is smaller that
	 * the probablilty q for the client, to represent if a client arrives or not
	 * 
	 * @param randNum the random number, between 0 and 1, to test against the
	 *                probability q for a client to arrive
	 * @return a boolean to represent if a client has arrived or not
	 */
	public boolean getProbCli(double randNum) {
		return randNum < probQClient;

	}

	/**
	 * This method returns the boolean based on if the random number is smaller that
	 * the probablilty q for the maintenance crew, to represent if a maintenance
	 * crew arrives or not
	 * 
	 * @param randNum the random number, between 0 and 1, to test against the
	 *                probability q for a maintenance crew
	 * @return a boolean to represent if a maintenance crew has arrived or not
	 */
	public boolean getProbMain(double randNum) {
		return randNum < PROBQMAIN;
	}
}