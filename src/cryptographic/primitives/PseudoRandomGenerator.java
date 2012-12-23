package cryptographic.primitives;

/**
 * An interface for Pseudo Random Generator, a deterministic procedure 
 * that maps a random seed to a longer pseudo-random string.
 */
public interface PseudoRandomGenerator {
	
	/**
	 * @return the number of seed bits needed as input by the prg function.
	 */
	public int seedlen();
	
	/**
	 * @param seed a byte[] which is used to initialize the pseudo-random generator. 
	 * @return an array of pseudo-random bytes derived from the seed. 
	 */
	public byte[] prg(byte[] seed);
	
}
