package arithmetic.objects;



import java.io.UnsupportedEncodingException;
import java.math.BigInteger;



public class ECurveGroupElement implements IGroupElement {

	private Point element;
	private ECurveGroup group;
	
	public ECurveGroupElement(Point element, ECurveGroup group) {
		this.element = element;
		this.group = group;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Point getElement() {
		return element;
	}
	
	public ECurveGroup getGroup() {
		return group;
	}
	
	@Override
	public ECurveGroupElement mult(IGroupElement b) {
		BigInteger s = ((((Point) getElement()).getY().getElement().subtract(((Point) b.getElement()).getY().getElement())).divide(((Point) getElement()).getX().getElement().subtract(((Point) b.getElement()).getX().getElement()))).mod(getGroup().getFieldOrder());
		BigInteger x = (s.pow(2).subtract(((Point) getElement()).getX().getElement()).subtract(((Point) b.getElement()).getX().getElement())).mod(getGroup().getFieldOrder());
		BigInteger y = (BigInteger.ZERO.subtract(((Point) getElement()).getY().getElement()).add(s.multiply(((Point) getElement()).getX().getElement().subtract(((Point) b.getElement()).getX().getElement())))).mod(getGroup().getFieldOrder());
		IntegerFieldElement newY = new IntegerFieldElement(y, null);
		IntegerFieldElement newX = new IntegerFieldElement(x, null);
		Point p = new Point(newX, newY);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	

	@Override
	public ECurveGroupElement inverse() {
		IntegerFieldElement y = new IntegerFieldElement(BigInteger.ZERO.subtract(((Point) getElement()).getY().getElement()).mod(getGroup().getFieldOrder()), null);
		Point p = new Point(((Point) getElement()).getX(), y);
		ECurveGroupElement ret = new ECurveGroupElement(p, getGroup());
		return ret;
	}
	
	public ECurveGroupElement power(BigInteger b) {
		ECurveGroupElement result = this;
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b)<0; i = i.add(BigInteger.ONE))
	    	result = result.mult(this);
	    return result;
	}
	

	@Override
	public boolean equal(IGroupElement b) {
		if (getElement().getX().getElement()==((Point) b.getElement()).getX().getElement() && ((Point) getElement()).getY().getElement()==((Point) b.getElement()).getY().getElement())
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