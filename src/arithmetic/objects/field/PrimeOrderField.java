package arithmetic.objects.field;



import arithmetic.objects.LargeInteger;



/**
 * This class represents a prime order Field.
 *
 */
public class PrimeOrderField implements IField<IntegerFieldElement> {

	/**
	 * the order of the field
	 */
	private LargeInteger q; 
	
	public PrimeOrderField (LargeInteger q) {
		this.q = q;
	}
	
	
	public LargeInteger getOrder() {
		return q;
	}
	

	
	public IntegerFieldElement one() {
		return new IntegerFieldElement (LargeInteger.ONE, this);
	}

	
	public IntegerFieldElement zero() {
		 return new IntegerFieldElement (LargeInteger.ZERO, this);
		
	}
}