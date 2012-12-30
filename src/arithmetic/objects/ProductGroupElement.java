package arithmetic.objects;


public class ProductGroupElement<E> implements ByteTree {

	/**
	 * arr contains the internal elements of the Product Ring Element. left and right are null if this is a simple product element. if it is complex, then arr is null.
	 */
	private GroupElement<E>[] arr;
	private ProductGroupElement<E> left;
	private ProductGroupElement<E> right;
	
	/**
	 * @param arr
	 * Constructor
	 */
	public ProductGroupElement(GroupElement<E>[] arr) {
		this.arr = arr;
		left = null;
		right = null;
	}
	
	public ProductGroupElement(ProductGroupElement<E> left, ProductGroupElement<E> right) {
		this.arr = null;
		this.left = left;
		this.right = right;
	}
	
	
	public GroupElement<E>[] getArr() {
		return arr;
	}


	public ProductGroupElement<E> getLeft() {
		return left;
	}


	public ProductGroupElement<E> getRight() {
		return right;
	}


	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductGroupElement<E> mult(ProductGroupElement<E> b) {
		if (arr!=null) {
			for (int i=0; i<arr.length; i++)
				arr[i] = arr[i](arr[i], b.getArr()[i]);
		}
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductGroupElement<E> power(int b) {
		return null;
	}
	
	public ProductGroupElement<E> inverse() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equal(ProductGroupElement<E> b) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
}



	

