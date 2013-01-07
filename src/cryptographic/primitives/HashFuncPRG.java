package cryptographic.primitives;

import java.util.Arrays;

/**
 * This class provides the functionality of a pseudo-random generator based on a
 * hash function. The hash function is used in the prg function to create from
 * the seed an array of pseudo-random bytes.
 * 
 * @author Daniel
 */
public class HashFuncPRG implements PseudoRandomGenerator {

	private final HashFunction hashFunction;

	private byte[] seed;
	private byte[] lastData;
	private int lastDataIndex;
	private byte[] ret;
	private int retIndex;
	private int counter;

	/**
	 * @param function
	 *            the hash function to use in the PRG.
	 */
	public HashFuncPRG(HashFunction function) {
		this.hashFunction = function;
		this.counter = Integer.MAX_VALUE;
	}

	@Override
	public int seedlen() {
		return hashFunction.outlen();
	}

	@Override
	public void setSeed(byte[] seed) {
		if (seed.length != seedlen() / 8) {
			throw new IllegalArgumentException("Seed must be in the current length! use seedlen() to check the length expected.");
		}

		this.seed = Arrays.copyOf(seed, seed.length);
		this.counter = 0;
		this.lastDataIndex = -1;
	}

	@Override
	public byte[] getNextPRGOutput(int numOfBits) throws IllegalStateException {
		// if seed not set and there isn't enough data calculated throw
		// exception
		if (counter >= Integer.MAX_VALUE && getNumOfBytesInOutput() == 0) {
			throw new IllegalStateException(
					"Seed was not initialized! use setSeed() to initialize seed.");
		}
		
		int bytesLeft = numOfBits / 8;
		ret = new byte[bytesLeft];
		retIndex = 0;
		
		// use the bytes left from old calculation
		bytesLeft = fillBytesFromLastData(bytesLeft);

		if (bytesLeft <= 0) {
			return ret;
		}
		
		while(bytesLeft > 0) {
			bytesLeft -= fillRetWithNextData(ret, bytesLeft);
		}
		
		return ret;
	}

	// fill in ret the next data, return number of bytes added to ret
	private int fillRetWithNextData(byte[] ret, int bytesLeft) {
		int count = 0;
		
		// calculate more data for input
		calcNextData();

		for (int i = retIndex; i < ret.length && lastDataIndex < lastData.length && bytesLeft - count > 0; i++) {
			ret[i] = lastData[lastDataIndex];
			lastDataIndex++;
			count++;
		}
		retIndex += count;
		return count;
	}

	private void calcNextData() {

		// create hash function input
		byte[] counterBytes = intToByteArray(counter);
		byte[] input = new byte[seed.length + 4];

		for (int i = 0; i < seed.length; i++) {
			input[i] = seed[i];
		}

		for (int i = 0; i < 4; i++) {
			input[seed.length + i] = counterBytes[i];
		}
		counter++;

		// run hash function on input
		lastData = hashFunction.digest(input);
		lastDataIndex = 0;
	}

	private int fillBytesFromLastData(int bytesLeft) {
		if (getNumOfBytesInOutput() == 0) {
			return bytesLeft;
		}

		int count = 0;
		for (int i = 0; i < bytesLeft && lastDataIndex + i < lastData.length; i++) {
			ret[i] = lastData[lastDataIndex + i];
			count++;
		}

		if (lastDataIndex + bytesLeft > lastData.length - 1) {
			lastDataIndex = -1;
		} else {
			lastDataIndex += bytesLeft;
		}
		
		retIndex += count;
		return bytesLeft - count;

	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}

	// return the num of bytes that are left to use from the last calculation
	private int getNumOfBytesInOutput() {
		if (lastDataIndex == -1 || lastData == null) {
			return 0;
		}
		
		return lastData.length - lastDataIndex;
	}
}
