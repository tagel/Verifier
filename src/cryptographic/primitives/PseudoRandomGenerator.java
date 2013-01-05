package cryptographic.primitives;

/**
 * An interface for Pseudo Random Generator, a deterministic procedure that maps
 * a random seed to a longer pseudo-random string.
 * 
 * @author Daniel
 */
public interface PseudoRandomGenerator {

	/**
	 * @return the number of seed bits needed as input by the prg function.
	 */
	public int seedlen();

	/**
	 * Initialize the PRG with the seed.
	 * 
	 * @param seed
	 *            a byte[] which is used to initialize the pseudo-random
	 *            generator.
	 */
	public void setSeed(byte[] seed);

	/**
	 * @param numOfBytes
	 *            the number of bytes should be in the output.
	 * @return the next numOfBytes bytes of the output of the prg running on the
	 *         seed given. Before running this method seed must be initialized
	 *         with setSeet() otherwise throws IllegalStateException.
	 * 
	 * @throws IllegalStateException
	 *             if the methods is called before seed was set using setSeet().
	 */
	public byte[] getNextPRGOutput(int numOfBytes) throws IllegalStateException;

}
