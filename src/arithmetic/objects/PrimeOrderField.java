package arithmetic.objects;


import java.math.BigInteger;


/**
 * This class represents a prime order Field.
 *
 */
public class PrimeOrderField implements IField<IntegerFieldElement> {

	/**
	 * the order of the field
	 */
	private BigInteger q; 
	
	public PrimeOrderField (BigInteger q) {
		this.q = q;
	}
	
	
	public BigInteger getOrder() {
		return q;
	}
	

	
	public IntegerFieldElement one() {
		IntegerFieldElement ret = new IntegerFieldElement (BigInteger.ONE, this);
		return ret;
	}

	
	public IntegerFieldElement zero() {
		IntegerFieldElement ret = new IntegerFieldElement (BigInteger.ZERO, this);
		return ret;
	}
}