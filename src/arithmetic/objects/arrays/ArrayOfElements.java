package arithmetic.objects.arrays;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import arithmetic.objects.ByteTree;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.ProductRingElement;

/**
 * This generic class represents an array of elements. the elements in the array
 * are all of the same type, but this type has to be a type which implements the
 * ByteTree interface (field, ring, or group elements, product elements, arrays
 * of elements, etc...)
 * 
 * @author Itay
 */

public class ArrayOfElements<E extends ByteTree> implements ByteTree {

	private List<E> elements = new ArrayList<E>();

	/**
	 * Constructor: a new empty array is created and now elements can now be
	 * added to it using the add function.
	 */
	public ArrayOfElements() {
	}

	/**
	 * 
	 * @param index
	 *            - an int representing an index in the array.
	 * @return the element in the appropriate index in the array.
	 */
	public E getAt(int index) {
		
		return elements.get(index);
	}

	/**
	 * 
	 * @param index
	 *            the index in the array that we want to reset.
	 * @param newValue
	 *            - the new element we want to put in the appropriate index in
	 *            the array.
	 */
	public void setAt(int index, E newValue) {
		
		elements.set(index, newValue);
	}

	/**
	 * 
	 * @param element
	 *            - the element we want to add to the array. the element will be
	 *            added to the end of the array.
	 */
	public void add(E element) {
		
		elements.add(element);
	}

	/**
	 * 
	 * @return the size of the array.
	 */
	public int getSize() {
		
		return elements.size();
	}

	/**
	 * 
	 * @param byteArr
	 *            an array we want to compare to our array.
	 * @return true if and only if b contains the same element as our element in
	 *         each and every index of the array.
	 */
	@Override
	public boolean equals(Object arr) {
		
		if (!(arr instanceof ArrayOfElements)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		ArrayOfElements<E> byteArr = (ArrayOfElements<E>) arr;

		if (elements.size() != byteArr.getSize()) {
			return false;
		}

		for (int i = 0; i < elements.size(); i++) {
			if (!(elements.get(i).equals(byteArr.getAt(i)))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the array. in
	 * case this array contains product elements, its representation is a bit
	 * different (as explained in the document) and therefore there are two
	 * helper functions for that case right under this function.
	 */
	@Override
	public byte[] toByteArray() {
		
		if (getAt(0) instanceof ProductRingElement) {
			return ProductRingArrayToByteArray();
		}

		if (getAt(0) instanceof ProductGroupElement) {
			return ProductGroupArrayToByteArray();
		}

		byte[] helperArr1 = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(elements.size()).array();
		byte[] byteArrToRet = new byte[helperArr1.length + 1];
		System.arraycopy(helperArr1, 0, byteArrToRet, 1, helperArr1.length);
		byteArrToRet[0] = 0;

		for (int i = 0; i < elements.size(); i++) {
			byte[] helperArr2 = (elements.get(i)).toByteArray();
			byteArrToRet = ArrayGenerators.concatArrays(byteArrToRet,
					helperArr2);
		}
		return byteArrToRet;

	}

	/**
	 * @return the byte array representation of an array of product group
	 *         elements.
	 */
	public byte[] ProductGroupArrayToByteArray() {
		
		Node node = new Node();
		int productsSize = ((ProductGroupElement) getAt(0)).getSize();

		if (productsSize != 1) {
			byte[] helperArr1 = ByteBuffer.allocate(CAPACITY)
					.order(ByteOrder.BIG_ENDIAN).putInt(productsSize).array();
			byte[] helperArr2 = new byte[helperArr1.length + 1];
			System.arraycopy(helperArr1, 0, helperArr2, 1, helperArr1.length);
			helperArr2[0] = 0;

			if (productsSize == 2
					&& ((ProductGroupElement) getAt(0)).getLeft() != null
					&& ((ProductGroupElement) getAt(0)).getLeft().getSize() == 1) {

				handleProductSizeEq2(node, productsSize);
			}

			// product size != 2 or the left PGE is null or its size > 1
			else {
				handleProductSizeNE2(node, productsSize);
			}

		// product size == 1
		} else {

			for (int k = 0; k < getSize(); k++)
				node.add(((ProductGroupElement) getAt(k)).getElements()
						.getAt(0));
		}

		return node.toByteArray();
	}

	private void handleProductSizeNE2(Node node, int productsSize) {
		for (int i = 0; i < productsSize; i++) {
			ArrayOfElements<ByteTree> arr = new ArrayOfElements<ByteTree>();
			for (int j = 0; j < getSize(); j++) {
				if (((ProductGroupElement) getAt(j)).getElements() == null
						&& i == 0)
					arr.add(((ProductGroupElement) getAt(j)).getLeft());
				else if (((ProductGroupElement) getAt(j)).getElements() == null
						&& i == 1)
					arr.add(((ProductGroupElement) getAt(j)).getRight());
				else
					arr.add(((ProductGroupElement) getAt(j))
							.getElements().getAt(i));
			}
			node.add(arr);
		}
	}

	private void handleProductSizeEq2(Node node, int productsSize) {
		for (int i = 0; i < productsSize; i++) {
			ArrayOfElements<IGroupElement> arr = new ArrayOfElements<IGroupElement>();
			for (int j = 0; j < getSize(); j++) {
				if (((ProductGroupElement) getAt(j)).getElements() == null
						&& i == 0)
					arr.add(((ProductGroupElement) getAt(j)).getLeft()
							.getElements().getAt(0));
				else if (((ProductGroupElement) getAt(j)).getElements() == null
						&& i == 1)
					arr.add(((ProductGroupElement) getAt(j)).getRight()
							.getElements().getAt(0));
			}
			node.add(arr);
		}
	}

	/** 
	 * @return the byte array representation of an array of product ring
	 *         elements.
	 */
	public byte[] ProductRingArrayToByteArray() {
		
		int productsSize = ((ProductRingElement) getAt(0)).getSize();
		byte[] helperArr1 = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(productsSize).array();
		byte[] helperArr2 = new byte[helperArr1.length + 1];
		System.arraycopy(helperArr1, 0, helperArr2, 1, helperArr1.length);
		helperArr2[0] = 0;

		Node node = new Node();
		for (int i = 0; i < productsSize; i++) {
			ArrayOfElements<ByteTree> arr = new ArrayOfElements<ByteTree>();
			for (int j = 0; j < getSize(); j++) {
				arr.add(((ProductRingElement) getAt(j)).getElements().getAt(i));
			}
			node.add(arr);
		}

		return node.toByteArray();
	}

	/**
	 * overriding the toString Java's method
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String temp = "[";
		for (E elem : elements)
			temp = temp + elem.toString() + ",";

		return temp + "]";
	}
}
