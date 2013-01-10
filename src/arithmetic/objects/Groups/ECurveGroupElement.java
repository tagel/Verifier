package arithmetic.objects.Groups;



import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.Field.IntegerFieldElement;



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
		BigInteger s = ((( getElement()).getY().getElement().subtract((((ECurveGroupElement) b).getElement()).getY().getElement())).divide(((Point) getElement()).getX().getElement().subtract(((Point) ((ECurveGroupElement) b).getElement()).getX().getElement()))).mod(getGroup().getFieldOrder());
		BigInteger x = (s.pow(2).subtract((getElement()).getX().getElement()).subtract((((ECurveGroupElement) b).getElement()).getX().getElement())).mod(getGroup().getFieldOrder());
		BigInteger y = (BigInteger.ZERO.subtract((getElement()).getY().getElement()).add(s.multiply((getElement()).getX().getElement().subtract((((ECurveGroupElement) b).getElement()).getX().getElement())))).mod(getGroup().getFieldOrder());
		IntegerFieldElement newY = new IntegerFieldElement(y, getElement().getY().getField());
		IntegerFieldElement newX = new IntegerFieldElement(x, getElement().getX().getField());
		Point p = new Point(newX, newY);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	

	@Override
	public ECurveGroupElement inverse() {
		IntegerFieldElement y = new IntegerFieldElement(BigInteger.ZERO.subtract(((Point) getElement()).getY().getElement()).mod(getGroup().getFieldOrder()), null);
		Point p = new Point(getElement().getX(), y);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	
	@Override
	public ECurveGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}
	
	public ECurveGroupElement power(BigInteger b) {
		ECurveGroupElement result = this;
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b)<0; i = i.add(BigInteger.ONE))
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