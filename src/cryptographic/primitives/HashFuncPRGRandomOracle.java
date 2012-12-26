package cryptographic.primitives;

/**
 * This class provides the functionality of a random oracle, with a  
 * construction based on hash function PRG. This random oracle (RO) implementation 
 * allows the user to determine the output length of the RO in the construction 
 * of the instance. 
 * In the construction a HashFuncPRGR (PRG) is created based on the HashFunction given. 
 * The PRG will later be used in the getRandomOracleOutput function, to expand the 
 * given data to the wanted length of the output.
 */
public class HashFuncPRGRandomOracle implements RandomOracle {
	
	private final PseudoRandomGenerator prg;
	private final int numOut;
	
	/**
	 * @param algorithm hash function to use in the construction of the random oracle.
	 * @param numOut the length of the getRandomOracleOutput output. 
	 */
	public HashFuncPRGRandomOracle(HashFunction function, int numOut) { 
		this.prg = new HashFuncPRG(function);
		this.numOut = numOut;
	}
	
	@Override
	public byte[] getRandomOracleOutput(byte[] data) {
		// TODO Daniel: complete 
		return null;
	}

}
