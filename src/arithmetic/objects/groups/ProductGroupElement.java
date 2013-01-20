package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;

/**
 * This class represents a product group element. In our project, our verifiers
 * and provers only use two kinds of product group elements: a "simple" product
 * element, where all of his coordinates are group elements (meaning it's not
 * recursive), and a product group element that has only two coordinates which
 * are both product group elements themselves, and have an equal number of group
 * elements as coordinates.
 * 
 * @author Itay
 * 
 */
public class ProductGroupElement implements ByteTree {

	/**
	 * if this is a "simple" product element, arr contains its internal
	 * coordinates and left and right are null. if it is complex, then arr is
	 * null and left and right contain the "simple" product elements.
	 * 
	 */
	private ArrayOfElements<IGroupElement> arr;
	private ProductGroupElement left;
	private ProductGroupElement right;

	/**
	 * Constructor for a "simple" product group element
	 * 
	 * @param arr
	 *            an array containing the group elements coordinates.
	 */
	public ProductGroupElement(ArrayOfElements<IGroupElement> arr) {
		this.arr = arr;
		left = null;
		right = null;
	}

	/**
	 * Constructor for a "complex" product group element.
	 * 
	 * @param left
	 *            the first coordinate of this product element which is a
	 *            product element itself.
	 * @param right
	 *            the second coordinate of this product element which is also a
	 *            product element itself.
	 */
	public ProductGroupElement(ProductGroupElement left,
			ProductGroupElement right) {
		this.arr = null;
		this.left = left;
		this.right = right;
	}

	/**
	 * 
	 * @return an array of group elements, the coordinates, when this is a
	 *         simple product element. if it is complex, null will be returned.
	 */
	public ArrayOfElements<IGroupElement> getElements() {
		return arr;
	}

	/**
	 * 
	 * @return the first coordinate of this complex product element. if it is a
	 *         simple one, null will be returned.
	 */
	public ProductGroupElement getLeft() {
		return left;
	}

	/**
	 * 
	 * @return the scond coordinate of this complex product element. if it is a
	 *         simple one, null will be returned.
	 */
	public ProductGroupElement getRight() {
		return right;
	}

	/**
	 * 
	 * @return the number of coordinates in this product element.
	 */
	public int getSize() {
		if (arr == null) {
			return 2;
		}
		return arr.getSize();
	}

	/**
	 * @param b
	 *            another product group element
	 * @return the result of the multiplication of the 2 product elements.That
	 *         is, a new product group element composed of the multiplication of
	 *         the coordinates of our two multiplication parameters.
	 */
	public ProductGroupElement mult(ProductGroupElement b) {
		if (arr != null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < arr.getSize(); i++)
				a.add(arr.getAt(i).mult(b.getElements().getAt(i)));
			return new ProductGroupElement(a);
		}
		return new ProductGroupElement(left.mult(b.left), right.mult(b.right));
	}

	/**
	 * @param b
	 *            a large integer representing the exponent.
	 * @return the result of product element in the b'th power. That is, a new
	 *         product group element composed of the coordinates of the original
	 *         element, each one to the b'th power.
	 */
	public ProductGroupElement power(LargeInteger b) {
		if (arr != null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < arr.getSize(); i++)
				a.add(arr.getAt(i).power(b));
			return new ProductGroupElement(a);
		}
		return new ProductGroupElement(left.power(b), right.power(b));
	}

	/**
	 * 
	 * @return the inverse of this product group element. That is, a new product
	 *         group element composed of the inverse of the coordinates of the
	 *         original element.
	 */
	public ProductGroupElement inverse() {
		if (arr != null) {
			ArrayOfElements<IGroupElement> a = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < arr.getSize(); i++)
				a.add(arr.getAt(i).inverse());
			return new ProductGroupElement(a);
		}
		return new ProductGroupElement(left.inverse(), right.inverse());
	}

	/**
	 * 
	 * @param b
	 *            another product group element
	 * @return true if and only if our element and b are equal. That means, all
	 *         their coordinates are equal.
	 */
	public boolean equals(ProductGroupElement b) {
		if (arr != null) {
			ArrayOfElements<IGroupElement> a = arr;
			for (int i = 0; i < arr.getSize(); i++)
				if (!(a.getAt(i).equals(b.getElements().getAt(i))))
					return false;
			return true;
		}
		return (left.equals(b.left) && right.equals(b.right));
	}

	/**
	 * returns the byte array representation (as a byte tree) of the product
	 * group element.
	 */
	@Override
	public byte[] toByteArray() {
		if (arr != null)
			return arr.toByteArray();
		else {
			byte[] b = new byte[5];
			b[0] = 0;
			b[1] = 0;
			b[2] = 0;
			b[3] = 0;
			b[4] = 2;
			b = ArrayGenerators.concatArrays(b, left.toByteArray());
			return ArrayGenerators.concatArrays(b, right.toByteArray());
		}
	}
}
