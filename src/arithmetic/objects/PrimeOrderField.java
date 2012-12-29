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
	
	public IntegerFieldElement neg(IntegerFieldElement a) {
		IntegerFieldElement ret = new IntegerFieldElement(q.min(a.getElement().mod(q)), a.getField());
		return ret;
	}
	
	
	public IntegerFieldElement add(IntegerFieldElement a, IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement ((a.getElement().add(b.getElement())).mod(q), a.getField());
		return ret;
	}
	
	
	@Override
	public IntegerFieldElement mult(IntegerFieldElement a, IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement ((a.getElement().multiply(b.getElement())).mod(q), a.getField());
		return ret;
	}

	public IntegerFieldElement power(IntegerFieldElement a, BigInteger b) {
		BigInteger result = a.getElement();
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
	    	result = result.multiply(a.getElement());
	    IntegerFieldElement ret = new IntegerFieldElement (result.mod(q), a.getField());
	    return ret;

	}
	
	public IntegerFieldElement one() {
		IntegerFieldElement ret = new IntegerFieldElement (BigInteger.ONE, null);
		return ret;
	}

	
	public IntegerFieldElement zero() {
		IntegerFieldElement ret = new IntegerFieldElement (BigInteger.ZERO, null);
		return ret;
	}
	
	public IntegerFieldElement inverse(IntegerFieldElement a) {
		IntegerFieldElement ret = new IntegerFieldElement (a.getElement().modInverse(q), a.getField());
		return ret;
	}
	

	public boolean equal(IntegerFieldElement a, IntegerFieldElement b) {
		if (a.getElement().mod(q)==b.getElement().mod(q)) return true;
		else return false;
	}

}