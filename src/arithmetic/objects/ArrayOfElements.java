package arithmetic.objects;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public abstract class ArrayOfElements<E> implements ByteTree{
	
	private E[] arr;
	
	public ArrayOfElements (E[] arr) {
		this.arr = arr;
	}
	
	public E[] getArray() {
		return arr;
	}
	
	public void addElement(E element) {
	    arr = Arrays.copyOf(arr, arr.length+1);
	    arr[arr.length] = element;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		Node arrNode = new Node((ByteTree[]) arr);
		return arrNode.toByteArray();
	}

}
