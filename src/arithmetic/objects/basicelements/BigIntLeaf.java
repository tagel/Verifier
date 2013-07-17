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
	private int numOfBytes = -1;

	/**
	 * 
	 * @param num
	 *            the large integer this leaf represents
	 */
	public BigIntLeaf(LargeInteger num) {
		
		this.num = num;
	}

	public BigIntLeaf(LargeInteger num, int numOfBytes) {
		
		this.num = num;
		this.numOfBytes = numOfBytes;
	}

	public LargeInteger getNum() {
		
		return num;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the large
	 * number.
	 */
	public byte[] toByteArray() {
		
		byte[] numArr = num.toByteArray();
		if (numOfBytes==-1)
			numOfBytes = numArr.length;
		byte[] helperArr = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfBytes).array();
		while (numArr.length < numOfBytes) {
			byte[] d = new byte[numArr.length + 1];
			System.arraycopy(numArr, 0, d, 1, numArr.length);
			d[0] = 0;
			numArr = d;
		}
		byte[] c = new byte[helperArr.length + numArr.length];
		System.arraycopy(helperArr, 0, c, 0, helperArr.length);
		System.arraycopy(numArr, 0, c, helperArr.length, numArr.length);
		
		byte[] ret = new byte[c.length + 1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}
}
