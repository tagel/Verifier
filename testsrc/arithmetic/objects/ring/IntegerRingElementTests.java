package arithmetic.objects.ring;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class IntegerRingElement.
 * 
 * @author Daniel
 * 
 */
public class IntegerRingElementTests {

	private Ring ring_263 = new Ring(new LargeInteger("263"));
	private Ring ring_4 = new Ring(new LargeInteger("4"));

	IntegerRingElement ire1_ring4 = new IntegerRingElement(new LargeInteger(
			"1"), ring_4);
	IntegerRingElement ire2_ring4 = new IntegerRingElement(new LargeInteger(
			"2"), ring_4);
	IntegerRingElement ire3_ring4 = new IntegerRingElement(new LargeInteger(
			"3"), ring_4);
	IntegerRingElement ire5_ring4 = new IntegerRingElement(new LargeInteger(
			"5"), ring_4);

	@Test
	public void toByteArrayTest() {
		Assert.assertEquals("01000000020102", CryptoUtils
				.bytesToHexString(new IntegerRingElement(
						new LargeInteger("258"), ring_263).toByteArray()));

		Assert.assertEquals("01000000020005", CryptoUtils
				.bytesToHexString(new IntegerRingElement(new LargeInteger("5"),
						ring_263).toByteArray()));
	}

	@Test
	public void getElementTest() {
		Assert.assertEquals(new LargeInteger("1"), ire1_ring4.getElement());
	}

	@Test
	public void equalTest() {
		Assert.assertTrue(ire1_ring4.equals(new IntegerRingElement(
				new LargeInteger("1"), ring_4)));
		Assert.assertTrue(ire1_ring4.equals(new IntegerRingElement(
				new LargeInteger("5"), ring_4)));
	}

	@Test
	public void getRingTest() {
		Assert.assertTrue(ring_4.equals(ire1_ring4.getRing()));

		IntegerRingElement ife = new IntegerRingElement(new LargeInteger("1"),
				ring_263);
		Assert.assertTrue(ring_263.equals(ife.getRing()));
	}

	@Test
	public void addTest() {
		Assert.assertTrue(ire3_ring4.equals(ire2_ring4.add(ire1_ring4)));
		Assert.assertTrue(ire1_ring4.equals(ire2_ring4.add(ire3_ring4)));
	}

	@Test
	public void addZeroTest() {
		Assert.assertTrue(ire1_ring4.equals(ire1_ring4.add(ire1_ring4
				.getRing().zero())));
	}

	@Test
	public void negTest() {
		Assert.assertTrue(ire2_ring4.neg().equals(ire2_ring4));
		Assert.assertTrue(ire1_ring4.neg().equals(ire3_ring4));
		Assert.assertTrue(ire5_ring4.neg().equals(ire3_ring4));
	}

	@Test
	public void mult1_Test() {
		Assert.assertTrue(ire2_ring4.equals(ire2_ring4.mult(ire3_ring4)));
	}

	@Test
	public void mult2_Test() {
		Assert.assertTrue(ire1_ring4.equals(ire3_ring4.mult(ire3_ring4)));
		Assert.assertTrue(ire3_ring4.equals(ire3_ring4.mult(ire1_ring4)));
	}

	@Test
	public void multWithOneTest() {
		Assert.assertTrue(ire3_ring4.equals(ire3_ring4.mult(ire3_ring4
				.getRing().one())));
	}

	@Test
	public void multWithZeroTest() {
		Assert.assertTrue(ire3_ring4.getRing().zero()
				.equals(ire3_ring4.mult(ire3_ring4.getRing().zero())));
	}

	@Test
	public void powerTest() {
		Assert.assertTrue(ire3_ring4.equals(ire3_ring4.power(new LargeInteger(
				"3"))));
		Assert.assertTrue(ire2_ring4.getRing().zero()
				.equals(ire2_ring4.power(new LargeInteger("5"))));
	}
}
