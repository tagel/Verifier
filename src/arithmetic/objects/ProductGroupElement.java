package arithmetic.objects;


import java.math.BigInteger;


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
			GroupElement<E>[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].mult(b.getArr()[i]);
			return new ProductGroupElement<E>(a);
		}
		else return new ProductGroupElement<E>(left.mult(b.left), right.mult(b.right));
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductGroupElement<E> power(BigInteger b) {
		if (arr!=null) {
			GroupElement<E>[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].power(b);
			return new ProductGroupElement<E>(a);
		}
		else return new ProductGroupElement<E>(left.power(b), right.power(b));
	}
	
	public ProductGroupElement<E> inverse() {
		if (arr!=null) {
			GroupElement<E>[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].inverse();
			return new ProductGroupElement<E>(a);
		}
		else return new ProductGroupElement<E>(left.inverse(), right.inverse());
	}

	public boolean equal(ProductGroupElement<E> b) {
		if (arr!=null) {
			GroupElement<E>[] a = arr;
			for (int i=0; i<arr.length; i++)
				if (!(a[i].equal(b.getArr()[i]))) return false;
			return true;
		}
		else return (left.equal(b.left) && right.equal(b.right));
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
}



	

