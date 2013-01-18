package arithmetic.objects.ring;

import arithmetic.objects.LargeInteger;

/**
 * This class represents a Ring.
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

	@Override
	public IntegerRingElement zero() {
		return new IntegerRingElement(LargeInteger.ZERO, this);
	}

	@Override
	public IntegerRingElement one() {
		return new IntegerRingElement(LargeInteger.ONE, this);
	}
}
