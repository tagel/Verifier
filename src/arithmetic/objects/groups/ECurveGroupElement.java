package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

public class ECurveGroupElement implements IGroupElement {

	private Point element;
	private ECurveGroup group;

	public ECurveGroupElement(Point element, ECurveGroup group) {
		this.element = element;
		this.group = group;
	}

	public Point getElement() {
		return element;
	}

	public ECurveGroup getGroup() {
		return group;
	}

	@Override
	public ECurveGroupElement mult(IGroupElement b) {
		if (b.equal(group.one())) {
			return this;
		}

		IntegerFieldElement xp = this.getElement().getX();
		IntegerFieldElement xq = ((ECurveGroupElement) b).getElement().getX();
		IntegerFieldElement yp = this.getElement().getY();
		IntegerFieldElement yq = ((ECurveGroupElement) b).getElement().getY();

		IField<IntegerFieldElement> field = new PrimeOrderField(this.getGroup()
				.getFieldOrder());

		if (!xp.equal(xq)) {
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

		if (yp.equal(yq.neg())) {
			return group.one();
		}

		if (yp.equal(yq)) {
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

		// Not suppose to get here!
		System.out.println("Error.");
		return null;
	}

	@Override
	public ECurveGroupElement inverse() {
		IntegerFieldElement y = new IntegerFieldElement(LargeInteger.ZERO
				.subtract(((Point) getElement()).getY().getElement()).mod(
						getGroup().getFieldOrder()), null);
		Point p = new Point(getElement().getX(), y);
		return new ECurveGroupElement(p, getGroup());
	}

	@Override
	public ECurveGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}

	public ECurveGroupElement power(LargeInteger b) {
		ECurveGroupElement result = this;
		for (LargeInteger i = LargeInteger.ONE; i.compareTo(b) < 0; i = i
				.add(LargeInteger.ONE))
			result = result.mult(this);
		return result;
	}

	@Override
	public boolean equal(IGroupElement b) {
		if (getElement().getX().equal(
				(((ECurveGroupElement) b).getElement()).getX())
				&& (getElement()).getY().equal(
						(((ECurveGroupElement) b).getElement()).getY()))
			return true;
		else
			return false;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		Node pointNode = new Node();
		pointNode.add(element.getX());
		pointNode.add(element.getY());
		return pointNode.toByteArray();
	}

}