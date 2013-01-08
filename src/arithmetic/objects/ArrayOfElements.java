package arithmetic.objects;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ArrayOfElements<E> implements ByteTree{
	
	private ArrayList<E> elements;
	
	public ArrayOfElements () {
	}
	
	
	
	public ArrayOfElements (ByteTree node){
		//TODO: implement
	}
	
	public E getAt (int index) {
		return elements.get(index);
	}
	
	public void add(E element) {
	    elements.add(element);
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		//TODO: implement
		return null;
	}



}
