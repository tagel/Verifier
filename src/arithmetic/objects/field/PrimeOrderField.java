package arithmetic.objects.field;

import arithmetic.objects.LargeInteger;

/**
 * This class represents a prime order field.
 * 
 * @author: Itay
 * 
 */
public class PrimeOrderField implements IField<IntegerFieldElement> {

	/**
	 * the order of the field. has to be prime.
	 */
	private LargeInteger q;

	public PrimeOrderField(LargeInteger q) {
		this.q = q;
	}

	/**
	 * returns the order of the field.
	 */
	public LargeInteger getOrder() {
		return q;
	}

	/**
	 * returns the 1 of the field (the element which is indifferent to the
	 * multiplication operation).
	 */
	public IntegerFieldElement one() {
		return new IntegerFieldElement(LargeInteger.ONE, this);
	}

	/**
	 * returns the 0 of the field (the element which is indifferent to the
	 * addition operation).
	 */

	public IntegerFieldElement zero() {
		return new IntegerFieldElement(LargeInteger.ZERO, this);

	}
}