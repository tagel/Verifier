package cryptographic.primitives;

import javax.xml.bind.DatatypeConverter;

/**
 * Contains methods providing common utilities.
 * @author Daniel
 *
 */
public class CryptoUtils {
	
	/**
	 * Creates byte[] from a given integer.
	 * @param value integer to make byte[].
	 * @return byte[] representing the value given. 
	 */
	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}
	
	/**
	 * Creates a hex string representing the byte[] given. 
	 * @param bytes to cast to hex string.
	 * @return hex string representing the bytes.
	 */
	public static String bytesToHexString(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes).toLowerCase();
	}
	
	/**
	 * Creates a byte[] from the given hex string.
	 * @param hexStr string to cast to bytes.
	 * @return the byte[] representing the hex string.
	 */
	public static byte[] hexStringToBytes(String hexStr) {
		return DatatypeConverter.parseHexBinary(hexStr);
	}
}
