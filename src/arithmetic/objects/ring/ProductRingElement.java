package arithmetic.objects.ring;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;

/**
 * 
 * @author Itay
 * 
 */
public class ProductRingElement implements ByteTree {

	private ArrayOfElements<IntegerRingElement> arr;

	public ProductRingElement(byte[] bt, IRing<IntegerRingElement> ring, int w)
			throws UnsupportedEncodingException {
		if (w == 1) {
			ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();
			a.add(new IntegerRingElement(ElementsExtractor.leafToInt(bt), ring));
			arr = a;
		} else
			arr = ArrayGenerators.createRingElementArray(bt, ring);
	}

	public ProductRingElement(ArrayOfElements<IntegerRingElement> arr) {
		ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();
		for (int i = 0; i < arr.getSize(); i++) {
			a.add(arr.getAt(i));
		}
		this.arr = a;
	}

	/**
	 * 
	 * @return the array of elements
	 */
	public ArrayOfElements<IntegerRingElement> getElements() {
		return arr;
	}

	/**
	 * 
	 * @return the size of the array
	 */
	public int getSize() {
		return arr.getSize();
	}

	/**
	 * add element to product
	 * @param element the element to add
	 */
	public void addElement(IntegerRingElement element) {
		arr.add(element);
	}

	/**
	 * @param b
	 * @return the result of the addition of the 2 product elements.
	 */
	public ProductRingElement add(ProductRingElement b) {
		ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();
		for (int i = 0; i < arr.getSize(); i++) {
			a.add(arr.getAt(i).add(b.getElements().getAt(i)));
		}
		return new ProductRingElement(a);
	}

	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductRingElement mult(ProductRingElement b) {
		ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();
		for (int i = 0; i < arr.getSize(); i++) {
			a.add(arr.getAt(i).mult(b.getElements().getAt(i)));
		}
		return new ProductRingElement(a);
	}

	/**
	 * @param b
	 * @return the result of product element in the b'th power.
	 */
	public ProductRingElement power(LargeInteger b) {
		ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();

		for (int i = 0; i < arr.getSize(); i++) {
			a.add(arr.getAt(i).power(b));
		}
		return new ProductRingElement(a);
	}

	/**
	 * 
	 * @return the negative of this product ring element.
	 */
	public ProductRingElement neg() {
		ArrayOfElements<IntegerRingElement> a = new ArrayOfElements<IntegerRingElement>();

		for (int i = 0; i < arr.getSize(); i++) {
			a.add(arr.getAt(i).neg());
		}
		return new ProductRingElement(a);
	}

	public boolean equals(ProductRingElement b) {
		ArrayOfElements<IntegerRingElement> a = arr;
		for (int i = 0; i < arr.getSize(); i++) {
			if (!(a.getAt(i).equals(b.getElements().getAt(i)))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return arr.toByteArray();
	}
}
