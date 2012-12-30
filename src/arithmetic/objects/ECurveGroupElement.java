package arithmetic.objects;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;



public class ECurveGroupElement extends GroupElement<Point> {

	public ECurveGroupElement(Point element, IGroup<GroupElement<Point>> group) {
		super(element, group);
	}
	
	@Override
	public ECurveGroupElement mult(GroupElement<Point> b) {
		BigInteger s = ((getElement().getY().getElement().subtract(b.getElement().getY().getElement())).divide(getElement().getX().getElement().subtract(b.getElement().getX().getElement()))).mod(getGroup().getFieldOrder());
		BigInteger x = (s.pow(2).subtract(getElement().getX().getElement()).subtract(b.getElement().getX().getElement())).mod(getGroup().getFieldOrder());
		BigInteger y = (BigInteger.ZERO.subtract(getElement().getY().getElement()).add(s.multiply(getElement().getX().getElement().subtract(b.getElement().getX().getElement())))).mod(getGroup().getFieldOrder());
		IntegerFieldElement newY = new IntegerFieldElement(y, null);
		IntegerFieldElement newX = new IntegerFieldElement(x, null);
		Point p = new Point(newX, newY);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	
	@Override
	public ECurveGroupElement inverse() {
		IntegerFieldElement y = new IntegerFieldElement(BigInteger.ZERO.subtract(getElement().getY().getElement()).mod(getGroup().getFieldOrder()), null);
		Point p = new Point(getElement().getX(), y);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	
	public ECurveGroupElement power(BigInteger b) {
		ECurveGroupElement result = this;
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b)<0; i = i.add(BigInteger.ONE))
	    	result = result.mult(this);
	    return result;
	}
	

	
	public boolean equal(GroupElement<Point> b) {
		if (getElement().getX().getElement()==b.getElement().getX().getElement() && getElement().getY().getElement()==b.getElement().getY().getElement())
			return true;
		else return false;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		IntegerFieldElement[] arr ={element.getX(), element.getY()};
		Node pointNode = new Node(arr);
		return pointNode.toByteArray();
	}

}
