package arithmetic.objects.basicelements;




import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayGenerators;


public class Node implements ByteTree {

	private List<ByteTree> children = new ArrayList<ByteTree>();
	private int numOfChildren;
	byte[] data;
	
	

	public Node (byte[] bt) {
		if (bt[0]==0) { //it is a node with children
			numOfChildren = intFromByteArray(Arrays.copyOfRange(bt, 1, 5));
			data = null;
			bt = Arrays.copyOfRange(bt, 5, bt.length);
			for (int i = 0; i < this.numOfChildren; i++) {
				int endChildIndex = getEndIndex(bt, 0);
				ByteTree child = new RawElement (Arrays.copyOfRange(bt, 0, endChildIndex));
				children.add(child);
				bt = Arrays.copyOfRange(bt, endChildIndex, bt.length);
			}
		}
		else { // it is a leaf
			children = null;
			numOfChildren = 0;
			data = bt;
		}

	}

	public Node() {

	}
	
	public Node(ByteTree[] arr) {
		children = Arrays.asList(arr);
	}

	private int getEndIndex(byte[] b, int i) {
		if (b[i] == 1) {
			int size = intFromByteArray(Arrays.copyOfRange(b, i+1, i+5));
			return (i+4+size+1);
		}
		else {
			int numOfChildren = intFromByteArray(Arrays.copyOfRange(b, i+1, i+5));
			i=i+5;
			for (int j = 0; j < numOfChildren; j++) {
				i = getEndIndex(b, i);
			}
			return i;
		}
	}


	public int intFromByteArray (byte[] a) {
		ByteBuffer b = ByteBuffer.wrap(a);
		return b.getInt();
	}

	public ByteTree getAt (int index) {
		return children.get(index);
	}
	
	public void setAt (int index, ByteTree newValue) {
		children.set(index, newValue);
	}

	public void add(ByteTree element) {
		children.add(element);
	}

	public int getChildrenSize() {
		return children.size();
	}
	
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(children.size()).array();
		byte[] b = new byte[a.length+1];
		System.arraycopy(a, 0, b, 1, a.length);
		b[0] = 0;
		for (int i=0; i<children.size(); i++) {
			byte[] c = children.get(i).toByteArray();
			b = ArrayGenerators.concatArrays(b, c);
		}
		return b;
	}

}
