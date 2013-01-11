package arithmetic.objects.ring;



import arithmetic.objects.LargeInteger;







/**
 * This class represents a Ring.
 *
 */
public class Ring implements IRing<IntegerRingElement> {

	/**
	 * the order of the ring.
	 */
	private LargeInteger q;
	
	public Ring (LargeInteger q) {
		this.q = q;
	}
	
	
	public LargeInteger getOrder() {
		return q;
	}
	
		
	public IntegerRingElement zero() {
		IntegerRingElement ret = new IntegerRingElement (LargeInteger.ZERO, this);
		return ret;
	}
	
	@Override
	public IntegerRingElement one() {
		IntegerRingElement ret = new IntegerRingElement (LargeInteger.ONE, this);
		return ret;
	}

}
