package arithmetic.objects.Ring;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ByteTree;



public class ProductRingElement implements ByteTree {

	
	private IntegerRingElement[] arr;
	
	
	public ProductRingElement(IntegerRingElement[] arr) {
		this.arr = arr;
	}
	
	
	public ProductRingElement(ByteTree bt) {
		//TODO: IMPLEMENT
	}
	
	
	public IntegerRingElement[] getArr() {
		return arr;
	}


	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
