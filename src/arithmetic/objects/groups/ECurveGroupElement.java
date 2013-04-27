package arithmetic.objects.groups;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.Fp1;
import org.bouncycastle.math.ec.FpNafMultiplier;
import org.bouncycastle.math.ec.ECCurve.Fp2;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

/**
 * This class is used to represent an element in an elliptic curve group.for
 * every such element, we will store both the point and the group that it
 * belongs to. the (x, y) coordinates are both integer field elements.
 * 
 * @author Itay
 * 
 */
public class ECurveGroupElement implements IGroupElement {

	private Point element;
	private ECurveGroup group;

	/**
	 * Constructor
	 * 
	 * @param element
	 *            - the elliptic curve point.
	 * @param group
	 *            - the group(elliptic curve) which the point belongs to.
	 */
	public ECurveGroupElement(Point element, ECurveGroup group) {
		this.element = element;
		this.group = group;
	}

	/**
	 * 
	 * @return the point
	 */
	public Point getElement() {
		return element;
	}

	/**
	 * @return the elliptic curve which this point belongs to.
	 */
	public ECurveGroup getGroup() {
		return group;
	}

	/**
	 * @param b
	 *            another elliptic curve element
	 * @return The result of the multiplication of our element and b.
	 */
	@Override
	public ECurveGroupElement mult(IGroupElement b) {
		if (b.equals(group.one())) {
			return this;
		}
		if (this.equals(group.one())) {
			return (ECurveGroupElement) b;
		}
		IntegerFieldElement xp = this.getElement().getX();
		IntegerFieldElement xq = ((ECurveGroupElement) b).getElement().getX();
		IntegerFieldElement yp = this.getElement().getY();
		IntegerFieldElement yq = ((ECurveGroupElement) b).getElement().getY();

		IField<IntegerFieldElement> field = new PrimeOrderField(this.getGroup()
				.getFieldOrder());

		if (!xp.equals(xq)) {
			IntegerFieldElement s = yp.subtract(yq).mult(
					xp.subtract(xq).inverse());
			IntegerFieldElement xr = s.power(new LargeInteger("2"))
					.subtract(xp).subtract(xq);
			IntegerFieldElement yr = field.zero().subtract(
					yp.add(s.mult(xr.subtract(xp))));
			ECurveGroupElement ret = new ECurveGroupElement(new Point(xr, yr),
					getGroup());
			return ret;
		}

		if (yp.equals(yq.neg())) {
			return group.one();
		}

		if (yp.equals(yq)) {
			IntegerFieldElement p = new IntegerFieldElement(this.getGroup()
					.getXCoefficient(), field);
			IntegerFieldElement three = new IntegerFieldElement(
					new LargeInteger("3"), field);
			IntegerFieldElement two = new IntegerFieldElement(new LargeInteger(
					"2"), field);

			IntegerFieldElement s = (xp.power(new LargeInteger("2"))
					.mult(three).subtract(p)).divide(yp.mult(two));
			IntegerFieldElement xr = s.power(new LargeInteger("2")).subtract(
					xp.mult(two));
			IntegerFieldElement yr = field.zero().subtract(
					yp.add(s.mult(xr.subtract(xp))));

			ECurveGroupElement ret = new ECurveGroupElement(new Point(xr, yr),
					getGroup());
			return ret;
		}

		// Not supposed to get here!
		return null;
	}

	/**
	 * 
	 * @return the multiplicative inverse of our element.
	 */
	@Override
	public ECurveGroupElement inverse() {
		if (element.equals(group.one())) {
			return this;
		}
		if (element.getY().equals(LargeInteger.ZERO)) {
			return this;
		}
		IField<IntegerFieldElement> field = new PrimeOrderField(getGroup()
				.getFieldOrder());
		IntegerFieldElement y = new IntegerFieldElement(LargeInteger.ZERO
				.subtract(((Point) getElement()).getY().getElement()).mod(
						getGroup().getFieldOrder()), field);
		Point p = new Point(getElement().getX(), y);
		return new ECurveGroupElement(p, getGroup());
	}

	/**
	 * 
	 * @param b
	 *            another elliptic curve element
	 * @return the result of the multiplication of our element with the inverse
	 *         of b (division).
	 */
	@Override
	public ECurveGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}

	public ECurveGroupElement square() {
		if (element.equals(group.one()))
			return this;
		if (getElement().getY().getElement().compareTo(LargeInteger.ZERO) == 0)
			return group.one();
		LargeInteger x1 = this.getElement().getX().getElement();
		LargeInteger y1 = this.getElement().getY().getElement();
		IField<IntegerFieldElement> field = new PrimeOrderField(this.getGroup()
				.getFieldOrder());
		LargeInteger x3 = (((((x1.multiply(x1)).multiply(new LargeInteger("3")))
				.add(getGroup().getXCoefficient())).divide(y1
				.multiply(new LargeInteger("2")))).power(2).subtract(x1
				.multiply(new LargeInteger("2")))).mod(getGroup()
				.getFieldOrder());
		IntegerFieldElement X3 = new IntegerFieldElement(x3, field);
		LargeInteger y3 = ((((((x1.multiply(x1))
				.multiply(new LargeInteger("3"))).add(getGroup()
				.getXCoefficient())).divide(y1.multiply(new LargeInteger("2"))))
				.multiply(x1.subtract(x3))).subtract(y1)).mod(getGroup()
				.getFieldOrder());
		IntegerFieldElement Y3 = new IntegerFieldElement(y3, field);
		Point p = new Point(X3, Y3);
		return new ECurveGroupElement(p, getGroup());
	}

	/**
	 * 
	 * @param b
	 *            a large integer which is the exponent. precondition: b is not
	 *            negative.
	 * @return our element in the b'th power.
	 * 
	 *         Some of the logic for this code was taken from bouncy castle open
	 *         source code.
	 */
	@Override
	public ECurveGroupElement power(LargeInteger b) {

		if (element.getX().getElement()
				.equals(group.one().getElement().getX().getElement())
				&& element.getY().getElement()
						.equals(group.one().getElement().getY().getElement()))
			return this;

		if (b.signum() == 0)
			return group.one();

		ECCurve curve = new Fp2(group.getFieldOrder(), group.getXCoefficient(), group.getB());
		ECMultiplier multiplier = new FpNafMultiplier();
		BigInteger B = new BigInteger(b.toByteArray());

		ECFieldElement X = new Fp(getElement().getX().getField().getOrder(),
				getElement().getX().getElement());
		ECFieldElement Y = new Fp(getElement().getY().getField().getOrder(),
				getElement().getY().getElement());
		ECPoint point = new Fp1(curve, X, Y);
		ECPoint result = multiplier.multiply(point, B, null);
		return new ECurveGroupElement(new Point(new IntegerFieldElement(
				new LargeInteger(result.getX().toBigInteger().toByteArray()),
				getElement().getX().getField()), new IntegerFieldElement(
				new LargeInteger(result.getY().toBigInteger().toByteArray()),
				getElement().getY().getField())), getGroup());

		/*
		 * if
		 * (element.getX().getElement().equals(group.one().getElement().getX()
		 * .getElement()) &&
		 * element.getY().getElement().equals(group.one().getElement
		 * ().getY().getElement())) return this;
		 * 
		 * if (b.signum()==0) return group.one();
		 * 
		 * 
		 * 
		 * 
		 * LargeInteger h = b.multiply(new LargeInteger("3"));
		 * ECurveGroupElement inv = this.inverse(); ECurveGroupElement p = this;
		 * ECurveGroupElement R = this; for (int i = h.bitLength() - 2; i > 0;
		 * i--) { R = R.square(); boolean hBit = h.testBit(i); boolean bBit =
		 * b.testBit(i);
		 * 
		 * if (hBit != bBit) { R = R.mult(hBit ? p : inv); } }
		 * 
		 * return R;
		 */
	}

	/**
	 * 
	 * @param b
	 *            another elliptic curve element
	 * @return true if and only if our element and b are equal. That means,
	 *         represent the same point and belong to the same curve.
	 */
	public boolean equals(IGroupElement b) {
		if (getElement().getX().equals(
				(((ECurveGroupElement) b).getElement()).getX())
				&& (getElement()).getY().equals(
						(((ECurveGroupElement) b).getElement()).getY())) {
			return true;
		}
		return false;
	}

	/**
	 * returns the byte array representation (as a byte tree) of this elliptic
	 * curve element.
	 */
	@Override
	public byte[] toByteArray() {
		Node pointNode = new Node();
		pointNode.add(element.getX());
		pointNode.add(element.getY());
		return pointNode.toByteArray();
	}

	@Override
	public String toString() {
		return element.toString();
	}
}
