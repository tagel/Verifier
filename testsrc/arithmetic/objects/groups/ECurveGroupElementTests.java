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
 * Tests for class ECurveGroupElement.
 * 
 * @author Daniel
 * 
 */
public class ECurveGroupElementTests {
	private ECurveGroup eCurveGroup192 = new ECurveGroup("P-192");
	private ECurveGroup eCurveGroup256 = new ECurveGroup("P-256");
	private IField<IntegerFieldElement> f192 = new PrimeOrderField(
			eCurveGroup192.getFieldOrder());
	private IField<IntegerFieldElement> f256 = new PrimeOrderField(
			eCurveGroup256.getFieldOrder());
	private Point point = new Point(new IntegerFieldElement(new LargeInteger(
			"0"), f192), new IntegerFieldElement(new LargeInteger("1"), f192));
	private ECurveGroupElement eCurveElement = new ECurveGroupElement(point,
			eCurveGroup192);

	@Test
	public void getElementTest() {
		Assert.assertTrue(point.equals(eCurveElement.getElement()));
	}

	@Test
	public void getGroupTest() {
		Assert.assertTrue(eCurveGroup192.equals(eCurveElement.getGroup()));
	}

	@Test
	public void mult_caseXequalsYnegTest() {
		Point point1 = new Point(new IntegerFieldElement(new LargeInteger("0"),
				f192), new IntegerFieldElement(new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(new LargeInteger("0"),
				f192), new IntegerFieldElement(new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		Point pointRet = new Point(new IntegerFieldElement(new LargeInteger(
				"-1"), f192), new IntegerFieldElement(new LargeInteger("-1"),
				f192));
		ECurveGroupElement eCurveElementRet = new ECurveGroupElement(pointRet,
				eCurveGroup192);

		Assert.assertTrue(eCurveElementRet.equals(eCurveElement1
				.mult(eCurveElement2)));
	}

	@Test
	public void mult_caseXequalsYequalTest() throws Exception {
		Point point1 = new Point(new IntegerFieldElement(new LargeInteger("0"),
				f192), new IntegerFieldElement(new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(new LargeInteger("0"),
				f192), new IntegerFieldElement(new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		LargeInteger p = eCurveGroup192.getXCoefficient();
		LargeInteger s = LargeInteger.ZERO.subtract(p).divide(
				new LargeInteger("2"));
		IntegerFieldElement xr = new IntegerFieldElement(LargeInteger.power(s,
				new LargeInteger("2")), f192);
		IntegerFieldElement yr = new IntegerFieldElement(
				LargeInteger.ZERO.subtract(LargeInteger.ONE.add(s.multiply(xr
						.getElement()))), f192);

		ECurveGroupElement eCurveElementRet = new ECurveGroupElement(new Point(
				xr, yr), eCurveGroup192);

		Assert.assertTrue(eCurveElementRet.equals(eCurveElement1
				.mult(eCurveElement2)));
	}

	@Test
	public void mult_caseXareDiffTest() {
		Point point1 = new Point(new IntegerFieldElement(new LargeInteger("2"),
				f192), new IntegerFieldElement(new LargeInteger("2"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(new LargeInteger("1"),
				f192), new IntegerFieldElement(new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		ECurveGroupElement eCurveElementOut = eCurveElement2
				.mult(eCurveElement1);
		Assert.assertEquals(
				new LargeInteger("-2").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getX().getElement());
		Assert.assertEquals(
				new LargeInteger("2").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getY().getElement());
	}

	@Test
	public void mult_withTheOneTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		ECurveGroupElement eCurveElementOut = eCurveElement2
				.mult(eCurveElement1);
		Assert.assertEquals(
				new LargeInteger("-1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getX().getElement());
		Assert.assertEquals(
				new LargeInteger("1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getY().getElement());
	}

	@Test
	public void mult_theOneTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		ECurveGroupElement eCurveElementOut = eCurveElement1
				.mult(eCurveElement2);
		Assert.assertEquals(
				new LargeInteger("-1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getX().getElement());
		Assert.assertEquals(
				new LargeInteger("1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getY().getElement());
	}

	@Test
	public void inverseTest() {
		Point point1 = new Point(new IntegerFieldElement(new LargeInteger("1"),
				f192), new IntegerFieldElement(new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(new LargeInteger("1"),
				f192), new IntegerFieldElement(new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);
		Assert.assertTrue(eCurveElement2.equals(eCurveElement1.inverse()));
	}

	@Test
	public void divideTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);

		ECurveGroupElement eCurveElementOut = eCurveElement2
				.divide(eCurveElement1.inverse());
		Assert.assertEquals(
				new LargeInteger("-1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getX().getElement());
		Assert.assertEquals(
				new LargeInteger("1").mod(eCurveGroup192.getFieldOrder()),
				eCurveElementOut.getElement().getY().getElement());
	}

	@Test
	public void power_oneTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);
		Assert.assertTrue(eCurveElement1.equals(eCurveElement1
				.power(new LargeInteger("5"))));
		
		//REMOVE
		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("27"), f192), new IntegerFieldElement(
				new LargeInteger("15"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);
		ECurveGroupElement res = eCurveElement2.power(new LargeInteger("23"));
		System.out.println(res.getElement().getX().getElement().intValue());
		System.out.println(res.getElement().getY().getElement());
	}

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		Point point1 = new Point(new IntegerFieldElement(new LargeInteger("0"),
				f192), new IntegerFieldElement(new LargeInteger("0"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);
		Assert.assertEquals(
				"0000000002"
						+ "010000001900000000000000000000000000000000000000000000000000"
						+ "010000001900000000000000000000000000000000000000000000000000",
				CryptoUtils.bytesToHexString(eCurveElement1.toByteArray()));

	}

	@Test
	public void equal_trueTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);
		Assert.assertTrue(eCurveElement1.equals(eCurveElement2));
	}

	@Test
	public void equal_falseTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-2"), f192));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup192);
		Assert.assertFalse(eCurveElement1.equals(eCurveElement2));
	}

	@Test
	public void equal_falseDiffInGroupTest() {
		Point point1 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f192), new IntegerFieldElement(
				new LargeInteger("-1"), f192));
		ECurveGroupElement eCurveElement1 = new ECurveGroupElement(point1,
				eCurveGroup192);

		Point point2 = new Point(new IntegerFieldElement(
				new LargeInteger("-1"), f256), new IntegerFieldElement(
				new LargeInteger("-1"), f256));
		ECurveGroupElement eCurveElement2 = new ECurveGroupElement(point2,
				eCurveGroup256);
		Assert.assertFalse(eCurveElement1.equals(eCurveElement2));
	}
}
