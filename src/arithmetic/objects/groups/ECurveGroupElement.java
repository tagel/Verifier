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
	 * @param ecElem
	 *            another elliptic curve element
	 * @return The result of the multiplication of our element andecElem.
	 */
	@Override
	public ECurveGroupElement mult(IGroupElement ecElem) {

		if (ecElem.equals(group.one())) {
			return this;
		}
		if (this.equals(group.one())) {
			return (ECurveGroupElement) ecElem;
		}
		IntegerFieldElement xp = this.getElement().getX();
		IntegerFieldElement xq = ((ECurveGroupElement) ecElem).getElement().getX();
		IntegerFieldElement yp = this.getElement().getY();
		IntegerFieldElement yq = ((ECurveGroupElement) ecElem).getElement().getY();

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
	 * @param ecElem
	 *            another elliptic curve element
	 * @return the result of the multiplication of our element with the inverse
	 *         of ecElem (division).
	 */
	@Override
	public ECurveGroupElement divide(IGroupElement ecElem) {
		
		return mult(ecElem.inverse());
	}

	/**
	 * 
	 * @return the result of the multiplication of our element with itself.
	 */
	public ECurveGroupElement square() {
		
		if (element.getX().getElement()
				.equals(group.one().getElement().getX().getElement())
				&& element.getY().getElement()
						.equals(group.one().getElement().getY().getElement()))
			return this;

		if (getElement().getY().getElement().signum() == 0)
			return group.one();
		
		IField<IntegerFieldElement> field = new PrimeOrderField(getGroup()
				.getFieldOrder());
		
		IntegerFieldElement x1 = this.getElement().getX();
		IntegerFieldElement y1 = this.getElement().getY();
		IntegerFieldElement ifeTwo = new IntegerFieldElement(new LargeInteger("2"), field);
		IntegerFieldElement ifeThree = new IntegerFieldElement(new LargeInteger("3"), field);
		
		IntegerFieldElement a = new IntegerFieldElement(new LargeInteger(getGroup()
				.getXCoefficient()), field);

		IntegerFieldElement z = (x1.mult(x1)).mult(ifeThree).add(a).divide(y1.mult(ifeTwo));
		
      
		LargeInteger x3 = (z.getElement().power(TWO)).subtract(x1.getElement().multiply(new LargeInteger("2")));
		IntegerFieldElement X3 = new IntegerFieldElement(x3.mod(group.getFieldOrder()), field);
		
		LargeInteger y3 = z.getElement().multiply(x1.getElement().subtract(x3)).subtract(y1.getElement());
		IntegerFieldElement Y3 = new IntegerFieldElement(y3.mod(group.getFieldOrder()), field);
		
		Point p = new Point(X3, Y3);
		return new ECurveGroupElement(p, getGroup());
	}
	
	
	/**
	 * 
	 * @param exponent
	 *            a large non-negative integer which is the exponent. precondition: exponent is not
	 *            negative(may be zero).
	 * @return our element in the exponent'th power.
	 * 
	 *         Some of the logic for this method was taken from bouncy castle open
	 *         source code.
	 */
	@Override
	public ECurveGroupElement power(LargeInteger exponent) {

		if (element.getX().getElement()
				.equals(group.one().getElement().getX().getElement())
				&& element.getY().getElement()
						.equals(group.one().getElement().getY().getElement()))
			return this;

		if (exponent.signum() == 0)
			return group.one();

		LargeInteger e = exponent;
		LargeInteger h = e.multiply(new LargeInteger("3"));
		ECurveGroupElement inv = this.inverse();
		ECurveGroupElement R = this;
		for (int i = h.bitLength() - 2; i > 0; --i) {
			R = R.square();
			boolean hBit = h.testBit(i);
			boolean eBit = e.testBit(i);

			if (hBit != eBit) {
				R = R.mult(hBit ? this : inv);
			}
		}

		return R;

	}

	/**
	 * 
	 * @param ecElem
	 *            another elliptic curve element
	 * @return true if and only if our element and ecElem are equal. That means,
	 *         represent the same point and belong to the same curve.
	 */
	@Override
	public boolean equals(Object c) {
		
		if (!(c instanceof ECurveGroupElement)) {
			return false;
		}

		ECurveGroupElement ecElem = (ECurveGroupElement) c;
		
		
		if (getElement().getX().equals(
				(((ECurveGroupElement) ecElem).getElement()).getX())
				&& (getElement()).getY().equals(
						(((ECurveGroupElement) ecElem).getElement()).getY())) {
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
