package arithmetic.objects.basicelements;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import arithmetic.objects.ByteTree;

/**
 * This class represents an array of boolean values. we chose to implement a
 * stand-alone class for this element because its representation as a byte tree
 * is unique compared to arrays of other types of elements.
 * 
 * @author Itay
 * 
 */
public class BooleanArrayElement implements ByteTree {

	private boolean[] arr;

	/**
	 * Constructor
	 * 
	 * @param arr
	 *            the array of booleans we wish to represent.
	 */
	public BooleanArrayElement(boolean[] arr) {
		this.arr = arr;
	}

	/**
	 * Constructor
	 * 
	 * @param arr
	 *            - the byte array representation (as a byte tree) of the
	 *            boolean array we wish to represent.
	 */
	public BooleanArrayElement(byte[] arr) {
		arr = Arrays.copyOfRange(arr, 5, arr.length);
		boolean[] b = new boolean[arr.length];
		for (int i = 0; i < b.length; i++)
			if (arr[i] == 1)
				b[i] = true;
			else
				b[i] = false;
		this.arr = b;
	}

	public boolean[] getBooleanArray() {
		return arr;
	}

	/**
	 * 
	 * @param i
	 *            the index in the array
	 * @return the boolean value in the i'th index in the array.
	 */
	public boolean getAt(int i) {
		return arr[i];
	}

	/**
	 * returns the byte array representation (as a byte tree) of the boolean
	 * array.
	 */
	@Override
	public byte[] toByteArray() {
		byte[] b = new byte[arr.length + 5];
		b[0] = 1;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
				.putInt(arr.length).array();
		for (int i = 0; i < 4; i++)
			b[i + 1] = a[i];
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == true)
				b[i + 5] = 1;
			else
				b[i + 5] = 0;
		return b;

	}

}
