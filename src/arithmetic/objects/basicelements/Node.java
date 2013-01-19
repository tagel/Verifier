package arithmetic.objects.basicelements;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayGenerators;

/**
 * This class represents a node in a byte tree. a node can contain only children
 * which are elements that implement the ByteTree interface, and are either
 * nodes or leaves.
 * 
 * @author Itay
 * 
 */
public class Node implements ByteTree {

	private List<ByteTree> children = new ArrayList<ByteTree>();
	private int numOfChildren;
	byte[] data; // if this node is actually a leaf, the byte array of this leaf
					// will be stored here. if the node is a "real" node (has at
					// least 1 child) "data" will be null.

	/**
	 * Constructor
	 * 
	 * @param bt
	 *            - a byte array representation (as a byte tree) of this node.
	 *            the constructor interprets the number of children of the node
	 *            and creates a list of the node's children.
	 */
	public Node(byte[] bt) {
		if (bt[0] == 0) { // it is a node with children
			numOfChildren = intFromByteArray(Arrays.copyOfRange(bt, 1, 5));
			data = null;
			bt = Arrays.copyOfRange(bt, 5, bt.length);
			for (int i = 0; i < this.numOfChildren; i++) {
				int endChildIndex = getEndIndex(bt, 0);
				ByteTree child = new RawElement(Arrays.copyOfRange(bt, 0,
						endChildIndex));
				children.add(child);
				bt = Arrays.copyOfRange(bt, endChildIndex, bt.length);
			}
		} else { // it is a leaf
			children = null;
			numOfChildren = 0;
			data = bt;
		}

	}

	/**
	 * Constructor. creates an empty node and now children can be added to it
	 * using the add function.
	 */
	public Node() {

	}

	/**
	 * Constructor
	 * 
	 * @param arr
	 *            an array of the children we want our node to have. this
	 *            constructor takes the array of children and creates a new node
	 *            with this children.
	 */
	public Node(ByteTree[] arr) {
		children = Arrays.asList(arr);
	}

	/**
	 * 
	 * @param b
	 *            a byte array (representing a byte tree)
	 * @param i
	 *            an index in the byte array which is the starting index of a
	 *            certain element.
	 * @return the index in b, which is the last index (the ending index) of the
	 *         element that its representation started in the index i. this
	 *         function helps the Node constructor from a byte array extract its
	 *         different children.
	 */
	private int getEndIndex(byte[] b, int i) {
		if (b[i] == 1) {
			int size = intFromByteArray(Arrays.copyOfRange(b, i + 1, i + 5));
			return (i + 4 + size + 1);
		} else {
			int numOfChildren = intFromByteArray(Arrays.copyOfRange(b, i + 1,
					i + 5));
			i = i + 5;
			for (int j = 0; j < numOfChildren; j++) {
				i = getEndIndex(b, i);
			}
			return i;
		}
	}
/**
 * 
 * @param a a byte array
 * @return the integer this byte array represents. 
 */
	public int intFromByteArray(byte[] a) {
		ByteBuffer b = ByteBuffer.wrap(a);
		return b.getInt();
	}
/**
 * 
 * @param index an index in the list of children of the node.
 * @return the node's child in the appropriate index.
 */
	public ByteTree getAt(int index) {
		return children.get(index);
	}
/**
 * 
 * @param index an index in the list of children we want to reset.
 * @param newValue the new children we want to put in that index
 */
	public void setAt(int index, ByteTree newValue) {
		children.set(index, newValue);
	}
/**
 * 
 * @param element an element we want to add to the children list
 */
	public void add(ByteTree element) {
		children.add(element);
	}
/**
 * 
 * @return the number of children this node has.
 */
	public int getChildrenSize() {
		return children.size();
	}
/**
 * returns the byte array representation (as a byte tree) of the node.
 */
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
				.putInt(children.size()).array();
		byte[] b = new byte[a.length + 1];
		System.arraycopy(a, 0, b, 1, a.length);
		b[0] = 0;
		for (int i = 0; i < children.size(); i++) {
			byte[] c = children.get(i).toByteArray();
			b = ArrayGenerators.concatArrays(b, c);
		}
		return b;
	}

}
