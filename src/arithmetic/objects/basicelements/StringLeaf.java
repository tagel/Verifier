package arithmetic.objects.basicelements;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;

/**
 * This class represent a leaf of an ASCII string.
 * 
 * @author Itay
 * 
 */
public class StringLeaf implements ByteTree {

	private String str;

	/**
	 * 
	 * @param str
	 *            the string this leaf represents.
	 */
	public StringLeaf(String str) {
		
		this.str = str;
	}

	public String getString() {
		
		return str;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the string.
	 */
	public byte[] toByteArray() {
		
		byte[] stringByteArr;
		try {
			stringByteArr = str.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		int numOfBytes = stringByteArr.length;
		byte[] helperArr1 = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfBytes).array();
		byte[] helperArr2 = new byte[helperArr1.length + stringByteArr.length];
		System.arraycopy(helperArr1, 0, helperArr2, 0, helperArr1.length);
		System.arraycopy(stringByteArr, 0, helperArr2, helperArr1.length, stringByteArr.length);
		byte[] ret = new byte[helperArr2.length + 1];
		System.arraycopy(helperArr2, 0, ret, 1, helperArr2.length);
		ret[0] = 1;
		
		return ret;
	}
}
