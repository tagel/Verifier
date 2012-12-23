package arithmetic.objects;

public class ProductElement implements ByteTree {

	/**
	 * arr contains the internal ring elements of the Product Ring Element.
	 */
	private ByteTree[] arr;
	
	/**
	 * @param arr
	 * Constructor
	 */
	public ProductElement(ByteTree[] arr) {
		this.arr = arr;
	}
	
	
	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductElement mult(ProductElement b) {
		return null;
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductElement power(int b) {
		return null;
	}
	
	public ProductElement inverse() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equal(ProductElement b) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
}



	

