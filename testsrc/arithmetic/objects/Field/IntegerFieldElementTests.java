package arithmetic.objects.Field;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class IntegerFieldElement.
 * 
 * @author Daniel
 * 
 */
public class IntegerFieldElementTests {

	private PrimeOrderField pof_263 = new PrimeOrderField(
			BigInteger.valueOf(263));
	private PrimeOrderField pof_4 = new PrimeOrderField(BigInteger.valueOf(4));

	@Test
	public void toByteArrayTest() {
		Assert.assertEquals("01000000020102", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(
						new BigInteger("258"), pof_263).toByteArray()));
		Assert.assertEquals("01000000020005", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(new BigInteger("5"),
						pof_263).toByteArray()));
	}

	@Test
	public void getElementTest() {
		IntegerFieldElement ife = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		Assert.assertEquals(new BigInteger("1"), ife.getElement());
	}

	@Test
	public void equalTest() {
		IntegerFieldElement ife = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		Assert.assertTrue(ife.equal(new IntegerFieldElement(
				new BigInteger("1"), pof_4)));
		Assert.assertTrue(ife.equal(new IntegerFieldElement(
				new BigInteger("5"), pof_4)));
	}

	@Test
	public void getFieldTest() {
		IntegerFieldElement ife = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		Assert.assertTrue(pof_4.equals(ife.getField()));

		ife = new IntegerFieldElement(new BigInteger("1"), pof_263);
		Assert.assertTrue(pof_263.equals(ife.getField()));
	}

	@Test
	public void addTest() {
		IntegerFieldElement ife1 = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		IntegerFieldElement ife2 = new IntegerFieldElement(new BigInteger("2"),
				pof_4);
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);
		Assert.assertTrue(ife3.equal(ife2.add(ife1)));
		Assert.assertTrue(ife1.equal(ife2.add(ife3)));
	}

	@Test
	public void addZeroTest() {
		IntegerFieldElement ife1 = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		Assert.assertTrue(ife1.equal(ife1.add(ife1.getField().zero())));
	}

	@Test
	public void negTest() {
		IntegerFieldElement ife = new IntegerFieldElement(new BigInteger("2"),
				pof_4);

		Assert.assertTrue(ife.neg().equal(ife));

		ife = new IntegerFieldElement(new BigInteger("1"), pof_4);
		IntegerFieldElement ifeneg = new IntegerFieldElement(
				new BigInteger("3"), pof_4);
		Assert.assertTrue(ife.neg().equal(ifeneg));

		ife = new IntegerFieldElement(new BigInteger("5"), pof_4);
		ifeneg = new IntegerFieldElement(new BigInteger("3"), pof_4);
		
		Assert.assertTrue(ife.neg().equal(ifeneg));
	}

	@Test
	public void mult1_Test() {
		IntegerFieldElement ife2 = new IntegerFieldElement(new BigInteger("2"),
				pof_4);
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);

		Assert.assertTrue(ife2.equal(ife2.mult(ife3)));
	}

	@Test
	public void mult2_Test() {
		IntegerFieldElement ife1 = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);

		Assert.assertTrue(ife1.equal(ife3.mult(ife3)));
		Assert.assertTrue(ife3.equal(ife3.mult(ife1)));
	}

	@Test
	public void multWithOneTest() {
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);

		Assert.assertTrue(ife3.equal(ife3.mult(ife3.getField().one())));
	}

	@Test
	public void multWithZeroTest() {
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);

		Assert.assertTrue(ife3.getField().zero()
				.equal(ife3.mult(ife3.getField().zero())));
	}

	@Test
	public void powerTest() {
		IntegerFieldElement ife2 = new IntegerFieldElement(new BigInteger("2"),
				pof_4);
		IntegerFieldElement ife3 = new IntegerFieldElement(new BigInteger("3"),
				pof_4);
		
		Assert.assertTrue(ife3.equal(ife3.power(new BigInteger("3"))));
		Assert.assertTrue(ife2.getField().zero().equal(ife2.power(new BigInteger("5"))));
	}

	@Test
	public void inverseTest() {
		// TODO
	}
}
