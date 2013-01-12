package arithmetic.objects.groups;



import java.io.UnsupportedEncodingException;
import arithmetic.objects.LargeInteger;

import arithmetic.objects.basicelements.Node;
import arithmetic.objects.field.IntegerFieldElement;



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
		if (b.equal(group.one())) return this;
		else {
			if (!(getElement().getX().getElement().equals(((ECurveGroupElement) b).getElement().getX().getElement()))) {
				LargeInteger s = (((getElement()).getY().getElement().subtract((((ECurveGroupElement) b).getElement()).getY().getElement())).divide(((Point) getElement()).getX().getElement().subtract(((Point) ((ECurveGroupElement) b).getElement()).getX().getElement()))).mod(getGroup().getFieldOrder());
				LargeInteger x = (s.power(2).subtract((getElement()).getX().getElement()).subtract((((ECurveGroupElement) b).getElement()).getX().getElement())).mod(getGroup().getFieldOrder());
				LargeInteger y = (LargeInteger.ZERO.subtract((getElement()).getY().getElement()).add(s.multiply((getElement()).getX().getElement().subtract((((ECurveGroupElement) b).getElement()).getX().getElement())))).mod(getGroup().getFieldOrder());
				IntegerFieldElement newY = new IntegerFieldElement(y, getElement().getY().getField());
				IntegerFieldElement newX = new IntegerFieldElement(x, getElement().getX().getField());
				Point p = new Point(newX, newY);
				ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
				return ret;
			}
			else {
				if (getElement().getY().getElement().equals(((ECurveGroupElement) b).getElement().getY().getElement().negate()))
					return group.one();
				else if (getElement().getY().getElement().equals(((ECurveGroupElement) b).getElement().getY().getElement())) {
					LargeInteger s = getElement().getX().getElement().power(2).multiply(new LargeInteger("3")).subtract(group.getXCoefficient()).divide(getElement().getY().getElement().multiply(new LargeInteger("2")));
					LargeInteger x = s.power(2).subtract(getElement().getX().getElement().multiply(new LargeInteger("2")));
					LargeInteger y = getElement().getY().getElement().add(s.multiply(x.subtract(getElement().getX().getElement())));
					IntegerFieldElement newY = new IntegerFieldElement(y, getElement().getY().getField());
					IntegerFieldElement newX = new IntegerFieldElement(x, getElement().getX().getField());
					Point p = new Point(newX, newY);
					ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
					return ret;
				}
				else {
					System.out.println("Error.");
					return null;
				}

			}
		}
	}


	@Override
	public ECurveGroupElement inverse() {
		IntegerFieldElement y = new IntegerFieldElement(LargeInteger.ZERO.subtract(((Point) getElement()).getY().getElement()).mod(getGroup().getFieldOrder()), null);
		Point p = new Point(getElement().getX(), y);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}

	@Override
	public ECurveGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}

	public ECurveGroupElement power(LargeInteger b) {
		ECurveGroupElement result = this;
		for (LargeInteger i = LargeInteger.ONE; i.compareTo(b)<0; i = i.add(LargeInteger.ONE))
			result = result.mult(this);
		return result;
	}


	@Override
	public boolean equal(IGroupElement b) {
		if (getElement().getX().equal((((ECurveGroupElement) b).getElement()).getX()) && (getElement()).getY().equal((((ECurveGroupElement) b).getElement()).getY()))
			return true;
		else return false;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		Node pointNode = new Node();
		pointNode.add(element.getX());
		pointNode.add(element.getY());
		return pointNode.toByteArray();
	}


}