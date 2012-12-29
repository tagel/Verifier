package algorithms.params;

/**
 * Data structure that contains the protocol info.
 */
public interface ProtocolInfo {
	
	/** 
	 * @return the version id of the verificatum.
	 */
	public String getVersion();
	
	/**
	 * @return the globally unique session identifier tied to the generation 
	 * of a particular joint public key 
	 */
	public String getSessionId();
	
	/**
	 * @return the number of parties (k).
	 */
	public int getNumOfParties();
	
	/**
	 * @return the number of mix-servers that take part in the shuffling (lambda).
	 */
	public int getThreshold(); 
	
	/**
	 * @return the number of bits in each component of random vectors used for 
	 * batching in proofs of shuffles and proofs of correct decryption.
	 */
	public int getvbitlenro(); 

	/**
	 * @return the acceptable statistical error when sampling random values. 
	 */
	public int getStatDist(); 
	
	/** 
	 * @return the number of bits used in the challenge of the verifier in zero-knowledge proofs.
	 */
	public int getcbitlenro(); 
	 
	/**
	 * 
	 * @return the hash function used to create the random oracles.
	 */
	public String getHashFunction();
	
	/**
	 * @return the hash function used to create the PRG used to expand challenges into arrays.
	 */
	public String getPrg();   
	
	/**
	 * @return the String representation of the group Gp. 
	 */
	public String getPGroup();
	
	/**
	 * @return the default width of cipher-texts and plain-texts.
	 */
	public int getWidth();
}
