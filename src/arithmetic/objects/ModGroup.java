package arithmetic.objects;
import java.math.BigInteger;


/**
 * This class represents a multiplicative modulo prime Group.
 *
 */
public class ModGroup implements IGroup<ModGroupElement> {

	/**
	 * p = the order of the underlying field Z*p
	 */
	private BigInteger p;
	/**
	 * q = the group order
	 */
	private BigInteger q;
	/**
	 *  g = generator
	 */
	private BigInteger g;

	
	/**
	 * @param p
	 * @param q
	 * @param g
	 * Constructor.
	 */
	public ModGroup (BigInteger p, BigInteger q, BigInteger g) {
		this.p = p;
		this.q = q;
		this.g = g;
	}
	
	public ModGroup (byte[] arr) {
		return;
	}
	
	
	public BigInteger getFieldOrder() {
		return p;
	}
	

	@Override
	public ModGroupElement mult(ModGroupElement a, ModGroupElement b) {
		ModGroupElement ret = new ModGroupElement ((a.getElement().multiply(b.getElement())).mod(q), a.getGroup());
		return ret;
	}

	@Override
	public ModGroupElement one() {
		ModGroupElement ret = new ModGroupElement (BigInteger.ONE, null);
		return ret;
	}

	@Override
	public ModGroupElement inverse(ModGroupElement a) {
		ModGroupElement ret = new ModGroupElement (a.getElement().modInverse(q), a.getGroup());
		return ret;
	}


	@Override
	public ModGroupElement power(ModGroupElement a, BigInteger b) {
		BigInteger result = a.getElement();
	    for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
	    	result = result.multiply(a.getElement());
	    ModGroupElement ret = new ModGroupElement (result.mod(q), a.getGroup());
	    return ret;
	}


	@Override
	public boolean equal(ModGroupElement a, ModGroupElement b) {
		if (a.getElement().mod(q)==b.getElement().mod(q)) return true;
		else return false;
	}

	
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
	






}
