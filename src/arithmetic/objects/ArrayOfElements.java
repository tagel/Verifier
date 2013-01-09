package arithmetic.objects;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ArrayOfElements<E> implements ByteTree {
	
	private List<E> elements = new ArrayList<E>();
	
	public ArrayOfElements () {
	}
	
	
	public ArrayOfElements (E[] arr) {
		//TODO: implement
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

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		//TODO: implement
		return null;
	}



}
