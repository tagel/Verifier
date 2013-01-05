package cryptographic.primitives;

/**
 * This class provides the functionality of a pseudo-random generator  
 * based on a hash function. The hash function is used in the prg 
 * function to create from the seed an array of pseudo-random bytes.
 * 
 * @author Daniel 
 */
public class HashFuncPRG implements PseudoRandomGenerator {
	
	private final HashFunction function;
	
	/**
	 * @param function the hash function to use in the PRG.
	 */
	public HashFuncPRG(HashFunction function) { 
		this.function = function;
	}
	
	@Override
	public int seedlen() { 
		return function.outlen();
	}

	@Override
	public void setSeed(byte[] seed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getNextPRGOutput(int numOfBytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
