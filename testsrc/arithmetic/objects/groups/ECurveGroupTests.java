package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class ECurveGroup.
 * 
 * @author Daniel
 * 
 */
public class ECurveGroupTests {

	private ECurveGroup eCurve = new ECurveGroup("P-256");

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		Assert.assertEquals(
				"0100000005"
						+ CryptoUtils.bytesToHexString(("P-256")
								.getBytes("ASCII")),
				CryptoUtils.bytesToHexString(eCurve.toByteArray()));
	}

	@Test
	public void getFieldOrderTest() {
		Assert.assertEquals(
				new LargeInteger(
						"ffffffff00000001000000000000000000000000ffffffffffffffffffffffff",
						16), eCurve.getFieldOrder());
	}

	@Test
	public void getOrderTest() {
		Assert.assertEquals(
				new LargeInteger(
						"ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551",
						16), eCurve.getOrder());
	}

	@Test
	public void getXCoefficientTest() {
		Assert.assertEquals(
				new LargeInteger(
						"ffffffff00000001000000000000000000000000fffffffffffffffffffffffc",
						16), eCurve.getXCoefficient());

	}

	@Test
	public void getBTest() {
		Assert.assertEquals(
				new LargeInteger(
						"5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b",
						16), eCurve.getB());

	}

	@Test
	public void getGeneratorTest() {
		IField<IntegerFieldElement> f = new PrimeOrderField(
				eCurve.getFieldOrder());
		IntegerFieldElement gx = new IntegerFieldElement(
				new LargeInteger(
						"6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296",
						16), f);
		IntegerFieldElement gy = new IntegerFieldElement(
				new LargeInteger(
						"4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5",
						16), f);
		Point g = new Point(gx, gy);
		Assert.assertTrue(new ECurveGroupElement(g, eCurve).equals(eCurve
				.getGenerator()));
	}



	@Test
	public void oneTest() {
		IntegerFieldElement minusOne = new IntegerFieldElement(
				new LargeInteger("-1"), new PrimeOrderField(
						eCurve.getFieldOrder()));
		Point infinity = new Point(minusOne, minusOne);
		Assert.assertTrue(new ECurveGroupElement(infinity, eCurve).equals(eCurve
				.one()));
	}
}
