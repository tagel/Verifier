package arithmetic.objects.basicelements;

import arithmetic.objects.LargeInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;

/**
 * This class represent a leaf of a large integer.
 * 
 * @author Itay
 * 
 */

public class BigIntLeaf implements ByteTree {

	private LargeInteger num;

	/**
	 * 
	 * @param num
	 *            the large integer this leaf represents
	 */
	public BigIntLeaf(LargeInteger num) {
		this.num = num;
	}

	public LargeInteger getNum() {
		return num;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the large
	 * number.
	 */
	public byte[] toByteArray() {
		byte[] b = num.toByteArray();
		int numOfBytes = b.length;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfBytes).array();
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		byte[] ret = new byte[c.length + 1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}

}
