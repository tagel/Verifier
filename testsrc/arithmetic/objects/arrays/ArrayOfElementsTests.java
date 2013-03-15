package arithmetic.objects.arrays;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import arithmetic.objects.ring.Ring;

/**
 * Tests for ArrayOfElements class.
 * 
 * @author Daniel
 * 
 */
public class ArrayOfElementsTests {

	private ArrayOfElements<IntegerFieldElement> array = new ArrayOfElements<IntegerFieldElement>();
	private PrimeOrderField field = new PrimeOrderField(new LargeInteger("4"));
	private IntegerFieldElement element3 = new IntegerFieldElement(
			new LargeInteger("3"), field);
	private IntegerFieldElement element4 = new IntegerFieldElement(
			new LargeInteger("4"), field);

	@Test
	public void getAtTest() {
		array.add(element3);
		array.add(element4);
		Assert.assertTrue(element3.equals(array.getAt(0)));
		Assert.assertTrue(element4.equals(array.getAt(1)));
	}

	@Test
	public void setAtTest() {
		array.add(element3);
		Assert.assertTrue(element3.equals(array.getAt(0)));
		array.setAt(0, element4);
		Assert.assertTrue(element4.equals(array.getAt(0)));
	}

	@Test
	public void addTest() {
		array.add(element3);
		Assert.assertEquals(1, array.getSize());
	}

	@Test
	public void getSizeTest() {
		Assert.assertEquals(0, array.getSize());
		array.add(element3);
		Assert.assertEquals(1, array.getSize());
		array.add(element4);
		Assert.assertEquals(2, array.getSize());
	}

	@Test
	public void equalsTest() {
		ArrayOfElements<IntegerFieldElement> newArray = new ArrayOfElements<IntegerFieldElement>();
		Assert.assertTrue(newArray.equals(array));
		array.add(element3);
		Assert.assertFalse(newArray.equals(array));
		newArray.add(element3);
		Assert.assertTrue(newArray.equals(array));
	}

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		array.add(element3);
		array.add(element4);
		Assert.assertEquals("0000000002" + "010000000103" + "010000000100",
				CryptoUtils.bytesToHexString(array.toByteArray()));
		
		
		Ring ring = new Ring(new LargeInteger("263"));
		IntegerRingElement one = new IntegerRingElement(
				new LargeInteger("1"), ring);
		IntegerRingElement two = new IntegerRingElement(
				new LargeInteger("2"), ring);
		IntegerRingElement three = new IntegerRingElement(
				new LargeInteger("3"), ring);
		IntegerRingElement four = new IntegerRingElement(
				new LargeInteger("4"), ring);
		IntegerRingElement five = new IntegerRingElement(
				new LargeInteger("5"), ring);
		IntegerRingElement six = new IntegerRingElement(
				new LargeInteger("6"), ring);
		ArrayOfElements<IntegerRingElement> arr1 = new ArrayOfElements<IntegerRingElement>();
		ArrayOfElements<IntegerRingElement> arr2 = new ArrayOfElements<IntegerRingElement>();
		ArrayOfElements<IntegerRingElement> arr3 = new ArrayOfElements<IntegerRingElement>();
		arr1.add(one); arr1.add(four);
		arr2.add(two); arr2.add(five);
		arr3.add(three); arr3.add(six);
		
		ProductRingElement pre1 = new ProductRingElement(arr1);
		ProductRingElement pre2 = new ProductRingElement(arr2);
		ProductRingElement pre3 = new ProductRingElement(arr3);
		
		ArrayOfElements<ProductRingElement> arr = new ArrayOfElements<ProductRingElement>();
		arr.add(pre1); arr.add(pre2); arr.add(pre3);
		Assert.assertEquals("000000000200000000030100000002000101000000020002010000000200030000000003010000000200040100000002000501000000020006",
				CryptoUtils.bytesToHexString(arr.toByteArray()));
	}
}
