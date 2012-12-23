package arithmetic.objects;
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
	
	
	public BigInteger getRingOrder() {
		return q;
	}
	
	
	public IntegerRingElement neg(IntegerRingElement a) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IntegerRingElement add(IntegerRingElement a, IntegerRingElement b) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public IntegerRingElement mult(IntegerRingElement a, IntegerRingElement b) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public IntegerRingElement power(IntegerRingElement a, BigInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IntegerRingElement zero() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IntegerRingElement one() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean equal(IntegerRingElement a, IntegerRingElement b) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}



}
