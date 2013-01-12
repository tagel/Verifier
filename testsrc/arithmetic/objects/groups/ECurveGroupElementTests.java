package arithmetic.objects.groups;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

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

		Assert.assertTrue(eCurveElementRet.equal(eCurveElement1
				.mult(eCurveElement2)));
	}

	@Test
	public void mult_caseXequalsYequalTest() {
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
		IntegerFieldElement xr = new IntegerFieldElement(
				s.power(new LargeInteger("2")), f192);
		IntegerFieldElement yr = new IntegerFieldElement(LargeInteger.ZERO.subtract(LargeInteger.ONE.add(s.multiply(xr.getElement()))), f192);

		ECurveGroupElement eCurveElementRet = new ECurveGroupElement(new Point(
				xr, yr), eCurveGroup192);
		
		Assert.assertTrue(eCurveElementRet.equal(eCurveElement1
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
	public void inverseTest() {
		// TODO
	}

	@Test
	public void divideTest() {
		// TODO
	}

	@Test
	public void powerTest() {
		// TODO
	}

	@Test
	public void toByteArrayTest() {
		// TODO
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
		Assert.assertTrue(eCurveElement1.equal(eCurveElement2));
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
		Assert.assertFalse(eCurveElement1.equal(eCurveElement2));
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
		Assert.assertFalse(eCurveElement1.equal(eCurveElement2));
	}
}
