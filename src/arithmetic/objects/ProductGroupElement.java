package arithmetic.objects;


import java.math.BigInteger;


public class ProductGroupElement implements ByteTree {

	/**
	 * arr contains the internal elements of the Product Ring Element. left and right are null if this is a simple product element. if it is complex, then arr is null.
	 */
	private IGroupElement[] arr;
	private ProductGroupElement left;
	private ProductGroupElement right;
	
	/**
	 * @param arr
	 * Constructor
	 */
	public ProductGroupElement(IGroupElement[] arr) {
		this.arr = arr;
		left = null;
		right = null;
	}
	
	public ProductGroupElement(ProductGroupElement left, ProductGroupElement right) {
		this.arr = null;
		this.left = left;
		this.right = right;
	}
	
	
	public IGroupElement[] getArr() {
		return arr;
	}


	public ProductGroupElement getLeft() {
		return left;
	}


	public ProductGroupElement getRight() {
		return right;
	}


	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductGroupElement mult(ProductGroupElement b) {
		if (arr!=null) {
			IGroupElement[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].mult(b.getArr()[i]);
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.mult(b.left), right.mult(b.right));
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductGroupElement power(BigInteger b) {
		if (arr!=null) {
			IGroupElement[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].power(b);
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.power(b), right.power(b));
	}
	
	public ProductGroupElement inverse() {
		if (arr!=null) {
			IGroupElement[] a = arr;
			for (int i=0; i<arr.length; i++)
				a[i] = a[i].inverse();
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.inverse(), right.inverse());
	}

	public boolean equal(ProductGroupElement b) {
		if (arr!=null) {
			IGroupElement[] a = arr;
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



	

