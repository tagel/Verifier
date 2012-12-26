package cryptographic.primitives;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides the functionality of a message digest algorithm from the "SHA-2" 
 * family, using the java.security.MessageDigest class.
 */
public class SHA2HashFunction implements HashFunction {
	
	private final MessageDigest md;
	
	/**
	 * @param algorithm the name of the hash algorithm to use, must be in the "SHA-2" family.
	 * @throws IllegalArgumentException if the algorithm is not one of the "SHA-2" family.
	 */
	public SHA2HashFunction(String algorithm) throws IllegalArgumentException {
		try {
			this.md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {			
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public byte[] digest(byte[] input) {
		// TODO Daniel: complete 
		return null;
	}

	@Override
	public int outlen() {
		// TODO Daniel: complete 
		return 0;
	}

}
