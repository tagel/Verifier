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
		
		arr = Arrays.copyOfRange(arr, FROM_INDEX, arr.length);
		boolean[] helperArr = new boolean[arr.length];

		for (int i = 0; i < helperArr.length; i++) {
			if (arr[i] == 1) {
				helperArr[i] = true;
			} else {
				helperArr[i] = false;
			}
		}
		this.arr = helperArr;
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

		byte[] byteArrToRet = new byte[arr.length + FROM_INDEX];
		byteArrToRet[0] = 1;
		
		byte[] helperArr = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(arr.length).array();

		for (int i = 0; i < CAPACITY; i++) {
			byteArrToRet[i + 1] = helperArr[i];
		}
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == true) {
				byteArrToRet[i + FROM_INDEX] = 1;
			} else {
				byteArrToRet[i + FROM_INDEX] = 0;
			}
		}
		return byteArrToRet;
	}
}
