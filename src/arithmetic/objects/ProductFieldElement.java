package arithmetic.objects;

import java.io.UnsupportedEncodingException;

public class ProductFieldElement implements ByteTree {

	
	private IntegerFieldElement[] arr;
	
	
	public ProductFieldElement(IntegerFieldElement[] arr) {
		this.arr = arr;
	}
	
	
	public ProductFieldElement(ByteTree bt) {
		//TODO: IMPLEMENT
	}
	
	
	public IntegerFieldElement[] getArr() {
		return arr;
	}


	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
