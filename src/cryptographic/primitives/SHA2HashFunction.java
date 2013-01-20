package cryptographic.primitives;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides the functionality of a message digest algorithm from the
 * "SHA-2" family, using the java.security.MessageDigest class.
 * 
 * @author Daniel 
 */
public class SHA2HashFunction implements HashFunction {

	private final MessageDigest md;
	private final List<String> SHA2_FAMILY_ALGORITHMS = Arrays.asList(
			"SHA-256", "SHA-384", "SHA-512");

	/**
	 * @param algorithm
	 *            the name of the hash algorithm to use, must be in the "SHA-2"
	 *            family.
	 * @throws IllegalArgumentException
	 *             if the algorithm is not one of the "SHA-2" family.
	 */
	public SHA2HashFunction(String algorithm) throws IllegalArgumentException {
		try {
			if (SHA2_FAMILY_ALGORITHMS.contains(algorithm)) {
				this.md = MessageDigest.getInstance(algorithm);
			} else {
				throw new IllegalArgumentException(); // TODO Daniel - fix throw ex must catch
			}
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public byte[] digest(byte[] input) {
		return md.digest(input);
	}

	@Override
	public int outlen() {
		return (md.getDigestLength() * 8);
	}
}
