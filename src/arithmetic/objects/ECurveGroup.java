package arithmetic.objects;

import java.math.BigInteger;


/**
 * This class represents a standard elliptic curve over a prime order field.
 *
 */
public class ECurveGroup implements IGroup<ECurveGroupElement>{
	
	/**
	 * p = order of the underlying field.
	 */
	private BigInteger p;
	/**
	 * q = order of the group
	 */
	private BigInteger q; 
	/**
	 * a = first coefficient of the curve equation
	 */
	private int a;
	/**
	 * b = second coefficient of the curve equation
	 */
	private int b;
	/**
	 * g = standard generator
	 */
	private Point g;

	/**
	 * @param p
	 * @param q
	 * @param a
	 * @param b
	 * @param g
	 * Constructor.
	 */
	public ECurveGroup (BigInteger p, BigInteger q, int a, int b, Point g) {
		this.p=p;
		this.q=q;
		this.a=a;
		this.b=b;
		this.g=g;
	}
	
	/**
	 * @param s
	 * Constructor.
	 */
	public ECurveGroup (String s) {
		return;
	}
	
	
	public BigInteger getFieldOrder() {
		return p;
	}
	
	@Override
	public ECurveGroupElement mult(ECurveGroupElement a, ECurveGroupElement b) {
		BigInteger s = ((a.getElement().getY().getElement().subtract(b.getElement().getY().getElement())).divide(a.getElement().getX().getElement().subtract(b.getElement().getX().getElement()))).mod(p);
		BigInteger x = (s.pow(2).subtract(a.getElement().getX().getElement()).subtract(b.getElement().getX().getElement())).mod(p);
		BigInteger y = (BigInteger.ZERO.subtract(a.getElement().getY().getElement()).add(s.multiply(a.getElement().getX().getElement().subtract(b.getElement().getX().getElement())))).mod(p);
		IntegerFieldElement newY = new IntegerFieldElement(y, null);
		IntegerFieldElement newX = new IntegerFieldElement(x, null);
		Point p = new Point(newX, newY);
		ECurveGroupElement ret = new ECurveGroupElement(p, a.getGroup());
		return ret;
	}

	@Override
	public ECurveGroupElement one() {
		IntegerFieldElement minusOne = new IntegerFieldElement (BigInteger.valueOf(-1), null);
		Point infinity = new Point(minusOne, minusOne);
		ECurveGroupElement ret = new ECurveGroupElement(infinity, null);
		return ret;
	}

	@Override
	public ECurveGroupElement inverse(ECurveGroupElement a) {
		IntegerFieldElement y = new IntegerFieldElement(BigInteger.ZERO.subtract(a.getElement().getY().getElement()).mod(p), null);
		Point p = new Point(a.getElement().getX(), y);
		ECurveGroupElement ret = new ECurveGroupElement(p, a.getGroup());
		return ret;
	}
	
	
	public ECurveGroupElement power(ECurveGroupElement a, BigInteger b) {
		ECurveGroupElement result = a;
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b)<0; i = i.add(BigInteger.ONE))
	    	result = this.mult(result, a);
	    return result;
	}

	
	public boolean equal(ECurveGroupElement a, ECurveGroupElement b) {
		if (a.getElement().getX().getElement()==b.getElement().getX().getElement() && a.getElement().getY().getElement()==b.getElement().getY().getElement())
			return true;
		else return false;
	}


	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}

