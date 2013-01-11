package arithmetic.objects.Ring;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayGenerators;
import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;



public class ProductRingElement implements ByteTree {

	
	private ArrayOfElements<IntegerRingElement> arr;
	
	
	public ProductRingElement(byte[] bt, IRing<IntegerRingElement> ring) throws UnsupportedEncodingException {
		arr = ArrayGenerators.createRingElementArray(bt, ring);
	}
	
	public ProductRingElement(ArrayOfElements<IntegerRingElement> arr) {
		this.arr = arr;
	}
	
	
	public ArrayOfElements<IntegerRingElement> getArr() {
		return arr;
	}
	
	/**
	 * @param b
	 * @return the result of the addition of the 2 product elements.
	 */
	public ProductRingElement add(ProductRingElement b) {
			ArrayOfElements<IntegerRingElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				a.setAt(i, a.getAt(i).add(b.getArr().getAt(i)));
			return new ProductRingElement(a);
	}
	
	/**
	 * @param b
	 * @return the result of the multiplication of the 2 product elements.
	 */
	public ProductRingElement mult(ProductRingElement b) {
			ArrayOfElements<IntegerRingElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				a.setAt(i, a.getAt(i).mult(b.getArr().getAt(i)));
			return new ProductRingElement(a);
	}
	
	/**
	 * @param integer b
	 * @return the result of product element in the b'th power.
	 */
	public ProductRingElement power(BigInteger b) {
			ArrayOfElements<IntegerRingElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				a.setAt(i, a.getAt(i).power(b));
			return new ProductRingElement(a);
	}
	
	/**
	 * 
	 * @return the negative of this product ring element.
	 */
	public ProductRingElement neg() {
			ArrayOfElements<IntegerRingElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				a.setAt(i, a.getAt(i).neg());
			return new ProductRingElement(a);
	}

	public boolean equal(ProductRingElement b) {
			ArrayOfElements<IntegerRingElement> a = arr;
			for (int i=0; i<arr.getSize(); i++)
				if (!(a.getAt(i).equal(b.getArr().getAt(i)))) return false;
			return true;
	}


	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return arr.toByteArray();
	}
	
	
}
