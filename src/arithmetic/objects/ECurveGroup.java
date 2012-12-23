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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECurveGroupElement one() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECurveGroupElement inverse(ECurveGroupElement a) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public ECurveGroupElement power(ECurveGroupElement a, BigInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean equal(ECurveGroupElement a, ECurveGroupElement b) {
		// TODO Auto-generated method stub
		return false;
	}


	public byte[] elementsArrayToByteArray(Point[] arr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the resulting ASCII string from converting the byte tree of the group into a byte array which is encoded onto hexadecimal and prepended with an ASCII comment.
	 */
	public String marshal() {
		// TODO Auto-generated method stub
		return null;
	}




	


}

