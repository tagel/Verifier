package arithmetic.objects;


public class ArrayOfElements<E> implements ByteTree{
	
	private E[] arr;
	
	public ArrayOfElements (E[] arr) {
		this.arr = arr;
	}
	
	public E[] getArray() {
		return arr;
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
