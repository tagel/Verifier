package algorithms;

import algorithms.params.Parameters;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of re-encryption and permutation of the input ciphertexts.
 * @author Tagel & Sofi
 */
public class ProveShuffling {

	private RandomOracle ROSeed;
	private RandomOracle ROChallenge;
	
	/**
	 * @return true if the proof of shuffle was correct and false otherwise.
	 */
	public ProveShuffling(RandomOracle ROSeed, RandomOracle ROChallenge){
		this.ROSeed = ROSeed;
		this.ROChallenge = ROChallenge;
	}
	
	public boolean prove(Parameters params) {
		// TODO Auto-generated method stub
		return false;
	}

}
