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
 * @summary This Class will tell if the chance of a decision to chance floor
 *          occurs or not, based on the probability p
 */
public class DecisionSimulator {
	/*
	 * Declaring fields
	 */
	private final double PROBP;

	/**
	 * This is the contructior which sets the final field of PROBP
	 * 
	 * @param p the probability of p
	 */
	public DecisionSimulator(double p) {
		this.PROBP = p;
	}

	/**
	 * This will return a boolean if the pseudo-random number is smaller than the
	 * prob p
	 * 
	 * @param randNum a random number between 0 and 1
	 * @return a boolean to represent if the chance of a decision happening
	 */
	public boolean getProb(double randNum) {
		return randNum < PROBP;
	}

}