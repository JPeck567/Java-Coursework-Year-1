package uk.ac.aston.jpd.coursework.officebuilding.stats;

import java.util.Random;

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
 * @summary This Class provides random generation of numbers using a
 *          pre-determined seed, through different probabilities for different
 *          uses.
 */

public class Stats {
	/**
	 * Declaring fields
	 */
	private final Random rnd;
	private final ArrivalSimulator arrSim;
	private final DecisionSimulator decSim;

	/**
	 * @param seed This is a inputed value passed in the constructor of the random
	 *             object to ensure the random object creates the same pattern of
	 *             pseudo-random numbers
	 * @param p    The probability p used to decide if a developer or employee
	 *             should change floor
	 * @param q    The probability q is used to decide if a client/maintenance crew
	 *             should arrive
	 */
	public Stats(int seed, double p, double q) {
		//make random object
		rnd = new Random(seed);

		//the two stats classes
		decSim = new DecisionSimulator(p);
		arrSim = new ArrivalSimulator(q);
	}

	/**
	 * This method gets a true or false value based on the output of decSim's
	 * getProb function
	 * 
	 * @return this returns a boolean to represent if a developer/employee will
	 *         change floor or not
	 * 
	 */
	public boolean getDecisionProb() {
		return decSim.getProb(rnd.nextDouble());
	}

	/**
	 * This method gets a true or false value based on the output of arrSim's
	 * getProb function
	 * 
	 * @return this returns a boolean value to represent if a client will arrive or
	 *         not
	 */
	public boolean getMArrivalProb() {
		return arrSim.getProbMain(rnd.nextDouble());
	}

	/**
	 * This method gets a boolean based on the output of the arrSim getProbCli
	 * function
	 * 
	 * @return this is a boolean to represent if a maintenance crew is to arrive or
	 *         not at the building
	 */
	public boolean getCArrivalProb() {
		return arrSim.getProbCli(rnd.nextDouble());
	}

	/**
	 * This method returns a random number between the two boundaries given,
	 * inclusive of boundR.
	 * 
	 * @param boundL this is the integer for which the lowest random number can be
	 * @param boundR this is the integer for which the highest random number can be
	 * @return a random integer between the two boundaries
	 */
	public int getRandomRangeNum(int boundL, int boundR) { // generates rand num from 0 to boundR.
		return rnd.nextInt(boundR - boundL + 1) + boundL; // rand num between 0 and range(diff of l to r) and then + boundL, therefore moving range of nums into correct range given in parameters
	}
}
