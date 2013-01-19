package arithmetic.objects.ring;

import arithmetic.objects.LargeInteger;

/**
 * This class represents a Ring.
 * @author Itay
 * 
 */
public class Ring implements IRing<IntegerRingElement> {

	private LargeInteger q; // the order of the ring

	public Ring(LargeInteger q) {
		this.q = q;
	}

	/**
	 * the order of the ring.
	 */
	public LargeInteger getOrder() {
		return q;
	}
	
	/**
	 * @return the 0 of the ring. (the element which is indifferent to the addition operation).
	 */
	@Override
	public IntegerRingElement zero() {
		return new IntegerRingElement(LargeInteger.ZERO, this);
	}

	/**
	 * @return the 1 of the Ring. (the element which is indifferent to the multiplication operation).
	 */
	@Override
	public IntegerRingElement one() {
		return new IntegerRingElement(LargeInteger.ONE, this);
	}
}
