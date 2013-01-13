package arithmetic.objects.arrays;



import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import arithmetic.objects.ByteTree;


public class ArrayOfElements<E extends ByteTree> implements ByteTree {
	
	private List<E> elements = new ArrayList<E>();
	
	public ArrayOfElements () {
	}
	

	public E getAt (int index) {
		return elements.get(index);
	}
	
	public void setAt (int index, E newValue) {
		elements.set(index, newValue);
	}
	
	public void add(E element) {
	    elements.add(element);
	}
	
	public int getSize() {
		return elements.size();
	}
	
	public boolean equals (ArrayOfElements<E> b) {
		if (elements.size() != b.getSize()) return false;
		for (int i=0; i<elements.size(); i++) 
			if (!(elements.get(i).equals(b.getAt(i)))) return false;
		return true;
	}
	

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(elements.size()).array();
		byte[] b = new byte[a.length+1];
		System.arraycopy(a, 0, b, 1, a.length);
		b[0] = 0;
		for (int i=0; i<elements.size(); i++) {
			byte[] c = (elements.get(i)).toByteArray();
			b = ArrayGenerators.concatArrays(b, c);
		}
		return b;
	}



}
