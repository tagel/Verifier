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
	
	
	public BigInteger getFieldOrder() {
		return q;
	}
	
	public IntegerFieldElement neg(IntegerFieldElement a) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public IntegerFieldElement add(IntegerFieldElement a, IntegerFieldElement b) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public IntegerFieldElement mult(IntegerFieldElement a, IntegerFieldElement b) {
		// TODO Auto-generated method stub
		return null;
	}

	public IntegerFieldElement power(IntegerFieldElement a, BigInteger b) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IntegerFieldElement one() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IntegerFieldElement zero() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IntegerFieldElement inverse(IntegerFieldElement a) {
		// TODO Auto-generated method stub
		return null;
	}
	

	public boolean equal(IntegerFieldElement a, IntegerFieldElement b) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}