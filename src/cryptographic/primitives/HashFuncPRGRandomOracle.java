package cryptographic.primitives;


/**
 * This class provides the functionality of a random oracle, with a construction
 * based on hash function PRG. This random oracle (RO) implementation allows the
 * user to determine the output length of the RO in the construction of the
 * instance. In the construction a HashFuncPRGR (PRG) is created based on the
 * HashFunction given. The PRG will later be used in the getRandomOracleOutput
 * function, to expand the given data to the wanted length of the output.
 * 
 * @author Daniel
 */
public class HashFuncPRGRandomOracle implements RandomOracle {

	private final HashFunction hashFunc;
	private final PseudoRandomGenerator prg;
	private final int numOut;

	/**
	 * @param hashFunc
	 *            hash function to use in the construction of the random oracle.
	 * @param numOut
	 *            the length of the getRandomOracleOutput output in bits.
	 */
	public HashFuncPRGRandomOracle(HashFunction hashFunc, int numOut) {
		this.hashFunc = hashFunc;
		this.prg = new HashFuncPRG(hashFunc);
		this.numOut = numOut;
	}

	@Override
	public byte[] getRandomOracleOutput(byte[] data) {
		// calculate seed for the PRG
		byte[] ret;
		byte[] seed = calcSeed(data);

		// run PRG on seed
		prg.setSeed(seed);
		ret = prg.getNextPRGOutput(((numOut + 7) / 8) * 8);

		if (numOut % 8 != 0) {
			int bitPrefix = 8 - (numOut % 8);
			int prefix = 0xFF;
			prefix >>= bitPrefix;
			ret[0] = (byte) (prefix & ret[0]);
		}

		return ret;
	}

	private byte[] calcSeed(byte[] data) {
		byte[] input = new byte[data.length + 4];
		byte[] counterBytes = CryptoUtils.intToByteArray(numOut);

		for (int i = 0; i < 4; i++) {
			input[i] = counterBytes[i];
		}

		for (int i = 4; i < input.length; i++) {
			input[i] = data[i - 4];
		}

		return hashFunc.digest(input);
	}
}
