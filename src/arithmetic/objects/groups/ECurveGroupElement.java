package arithmetic.objects.groups;

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

	/**
	 * 
	 * @param b
	 *            a large integer which is the exponent.
	 * @return our element in the b'th power.
	 */
	public ECurveGroupElement power(LargeInteger b) {

		ECurveGroupElement base = this;
		ECurveGroupElement result = this.getGroup().one();

		String str = b.toString(2);

		for (int i = str.length() - 1; i > -1; i--) {
			if (str.charAt(i) == '1')
				result = result.mult(base);
			base = base.mult(base);
		}
		return result;
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
