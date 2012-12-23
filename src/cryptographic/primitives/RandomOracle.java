package cryptographic.primitives;

/**
 * Interface for Random Oracle. Random Oracle is a mathematical function 
 * mapping every possible query to a random response from its output domain.
 */
public interface RandomOracle {
	
	/**
	 * @param data the input to run the random oracle on.
	 * @return the output of the random oracle on the input byte[] data. 
	 */
	public byte[] getRandomOracleOutput(byte[] data);
}
