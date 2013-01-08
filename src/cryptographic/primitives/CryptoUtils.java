package cryptographic.primitives;

import javax.xml.bind.DatatypeConverter;

public class CryptoUtils {
	// TODO: document and test!
	
	/**
	 * creates byte[] fro m a given int.
	 * @param value int to make byte[]
	 * @return byte[] representing the value given 
	 */
	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}
	
	public static String bytesToHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array).toLowerCase();
	}

	public static byte[] hexStringToBytes(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
}
