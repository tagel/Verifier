package arithmetic.objects.arrays;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

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
	}
}
