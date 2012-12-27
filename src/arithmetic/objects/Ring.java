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
		IntegerRingElement ret = new IntegerRingElement(q.min(a.getElement().mod(q)), a.getRing());
		return ret;
	}

	
	public IntegerRingElement add(IntegerRingElement a, IntegerRingElement b) {
		IntegerRingElement ret = new IntegerRingElement ((a.getElement().add(b.getElement())).mod(q), a.getRing());
		return ret;
	}
	
	
	public IntegerRingElement mult(IntegerRingElement a, IntegerRingElement b) {
		IntegerRingElement ret = new IntegerRingElement ((a.getElement().multiply(b.getElement())).mod(q), a.getRing());
		return ret;
	}
	
	
	public IntegerRingElement power(IntegerRingElement a, BigInteger b) {
		BigInteger result = a.getElement();
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
	    	result = result.multiply(a.getElement());
	    IntegerRingElement ret = new IntegerRingElement (result.mod(q), a.getRing());
	    return ret;
	}

	
	public IntegerRingElement zero() {
		IntegerRingElement ret = new IntegerRingElement (BigInteger.ZERO, null);
		return ret;
	}
	
	@Override
	public IntegerRingElement one() {
		IntegerRingElement ret = new IntegerRingElement (BigInteger.ONE, null);
		return ret;
	}


	public boolean equal(IntegerRingElement a, IntegerRingElement b) {
		if (a.getElement().mod(q)==b.getElement().mod(q)) return true;
		else return false;
	}


	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}



}
