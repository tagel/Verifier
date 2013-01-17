package arithmetic.objects.groups;


import java.io.UnsupportedEncodingException;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.ring.IntegerRingElement;


public class ProductGroupElement implements ByteTree {

	/**
	 * arr contains the internal elements of the Product Group Element. left and right are null if this is a simple product element. if it is complex, then arr is null.
	 */
	private ArrayOfElements<IGroupElement> arr;
	private ProductGroupElement left;
	private ProductGroupElement right;
	
	/**
	 * @param arr
	 * Constructor
	 */
	public ProductGroupElement(ArrayOfElements<IGroupElement> arr) {
		this.arr = arr;
		left = null;
		right = null;
	}
	
	public ProductGroupElement(ProductGroupElement left, ProductGroupElement right) {
		this.arr = null;
		this.left = left;
		this.right = right;
	}
	
	
	public ArrayOfElements<IGroupElement> getElements() {
		return arr;
	}


	public ProductGroupElement getLeft() {
		return left;
	}


	public ProductGroupElement getRight() {
		return right;
	}

	public int getSize() {
		if (arr==null) return 2;
		else return arr.getSize();
	}
	

	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductGroupElement mult(ProductGroupElement b) {
		if (arr!=null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i=0; i<arr.getSize(); i++)
				a.add(arr.getAt(i).mult(b.getElements().getAt(i)));
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.mult(b.left), right.mult(b.right));
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductGroupElement power(LargeInteger b) {
		if (arr!=null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i=0; i<arr.getSize(); i++)
				a.add(arr.getAt(i).power(b));
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.power(b), right.power(b));
	}
	
	public ProductGroupElement inverse() {
		if (arr!=null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i=0; i<arr.getSize(); i++)
				a.add(arr.getAt(i).inverse());
			return new ProductGroupElement(a);
		}
		else return new ProductGroupElement(left.inverse(), right.inverse());
	}

	public boolean equal(ProductGroupElement b) {
		if (arr!=null) {
			ArrayOfElements<IGroupElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				if (!(a.getAt(i).equal(b.getElements().getAt(i)))) return false;
			return true;
		}
		else return (left.equal(b.left) && right.equal(b.right));
	}
	
	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		if (arr!=null) return arr.toByteArray();
		else {
			byte[] b = new byte[5];
			b[0]=0; b[1]=0; b[2]=0; b[3]=0; b[4]=2;
			b = ArrayGenerators.concatArrays(b, left.toByteArray());
			return ArrayGenerators.concatArrays(b, right.toByteArray());
		}
	}
}



	

