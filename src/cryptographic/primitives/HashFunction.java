package cryptographic.primitives;

/**
 * An interface for Hash Function, which is an algorithm or subroutine that maps 
 * large data sets of variable length, called keys, to smaller data sets of a fixed 
 * length.
 * 
 * @author Daniel 
 */
public interface HashFunction {
	
	/**
	 * @param input the byte array to run the hash function on.
	 * @return the hash digest of the byte array input using the hash function. 
	 */
	public byte[] digest(byte[] input);
	
	/**
	 * @return the number of bits in the output of the hash function.
	 */
	public int outlen();
	
}
