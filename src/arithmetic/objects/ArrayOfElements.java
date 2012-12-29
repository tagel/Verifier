package arithmetic.objects;

import java.io.UnsupportedEncodingException;


public class ArrayOfElements<E> implements ByteTree{
	
	private E[] arr;
	
	public ArrayOfElements (E[] arr) {
		this.arr = arr;
	}
	
	public E[] getArray() {
		return arr;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		Node arrNode = new Node((ByteTree[]) arr);
		return arrNode.toByteArray();
	}

}
