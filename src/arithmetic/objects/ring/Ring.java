package arithmetic.objects.ring;



import java.math.BigInteger;







/**
 * This class represents a Ring.
 *
 */
public class Ring implements IRing<IntegerRingElement> {

	/**
	 * the order of the ring.
	 */
	private BigInteger q;
	
	public Ring (BigInteger q) {
		this.q = q;
	}
	
	
	public BigInteger getOrder() {
		return q;
	}
	
		
	public IntegerRingElement zero() {
		IntegerRingElement ret = new IntegerRingElement (BigInteger.ZERO, this);
		return ret;
	}
	
	@Override
	public IntegerRingElement one() {
		IntegerRingElement ret = new IntegerRingElement (BigInteger.ONE, this);
		return ret;
	}

}
