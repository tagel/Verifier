package arithmetic.objects.field;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class IntegerFieldElement.
 * 
 * @author Daniel
 * 
 */
public class IntegerFieldElementTests {

	private PrimeOrderField pof_263 = new PrimeOrderField(new LargeInteger(
			"263"));
	private PrimeOrderField pof_4 = new PrimeOrderField(new LargeInteger("4"));

	IntegerFieldElement ife1_field4 = new IntegerFieldElement(new LargeInteger(
			"1"), pof_4);
	IntegerFieldElement ife2_field4 = new IntegerFieldElement(new LargeInteger(
			"2"), pof_4);
	IntegerFieldElement ife3_field4 = new IntegerFieldElement(new LargeInteger(
			"3"), pof_4);
	IntegerFieldElement ife5_field4 = new IntegerFieldElement(new LargeInteger(
			"5"), pof_4);

	@Test
	public void toByteArrayTest() {
		Assert.assertEquals("01000000020102", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(new LargeInteger(
						"258"), pof_263).toByteArray()));

		Assert.assertEquals("01000000020005", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(
						new LargeInteger("5"), pof_263).toByteArray()));
	}

	@Test
	public void getElementTest() {
		Assert.assertEquals(new LargeInteger("1"), ife1_field4.getElement());
	}

	@Test
	public void equalTest() {
		Assert.assertTrue(ife1_field4.equals(new IntegerFieldElement(
				new LargeInteger("1"), pof_4)));
		Assert.assertTrue(ife1_field4.equals(new IntegerFieldElement(
				new LargeInteger("5"), pof_4)));
	}

	@Test
	public void getFieldTest() {
		Assert.assertTrue(pof_4.equals(ife1_field4.getField()));

		IntegerFieldElement ife = new IntegerFieldElement(
				new LargeInteger("1"), pof_263);
		Assert.assertTrue(pof_263.equals(ife.getField()));
	}

	@Test
	public void addTest() {
		Assert.assertTrue(ife3_field4.equals(ife2_field4.add(ife1_field4)));
		Assert.assertTrue(ife1_field4.equals(ife2_field4.add(ife3_field4)));
	}

	@Test
	public void addZeroTest() {
		Assert.assertTrue(ife1_field4.equals(ife1_field4.add(ife1_field4
				.getField().zero())));
	}

	@Test
	public void negTest() {
		Assert.assertTrue(ife2_field4.neg().equals(ife2_field4));
		Assert.assertTrue(ife1_field4.neg().equals(ife3_field4));
		Assert.assertTrue(ife5_field4.neg().equals(ife3_field4));
	}

	@Test
	public void mult1_Test() {
		Assert.assertTrue(ife2_field4.equals(ife2_field4.mult(ife3_field4)));
	}

	@Test
	public void mult2_Test() {
		Assert.assertTrue(ife1_field4.equals(ife3_field4.mult(ife3_field4)));
		Assert.assertTrue(ife3_field4.equals(ife3_field4.mult(ife1_field4)));
	}

	@Test
	public void multWithOneTest() {
		Assert.assertTrue(ife3_field4.equals(ife3_field4.mult(ife3_field4
				.getField().one())));
	}

	@Test
	public void multWithZeroTest() {
		Assert.assertTrue(ife3_field4.getField().zero()
				.equals(ife3_field4.mult(ife3_field4.getField().zero())));
	}

	@Test
	public void powerTest() {
		Assert.assertTrue(ife3_field4.equals(ife3_field4.power(new LargeInteger(
				"3"))));
		Assert.assertTrue(ife2_field4.getField().zero()
				.equals(ife2_field4.power(new LargeInteger("5"))));
	}

	@Test
	public void inverse_pof4Test() {
		Assert.assertTrue(ife3_field4.equals(ife3_field4.inverse()));
	}
}
