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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModGroupElement one() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModGroupElement inverse(ModGroupElement a) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ModGroupElement power(ModGroupElement a, BigInteger b) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean equal(ModGroupElement a, ModGroupElement b) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public byte[] elementsArrayToByteArray(ModGroupElement[] arr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the resulting ASCII string from converting the byte tree of the group into a byte array which is encoded onto hexadecimal and prepended with an ASCII comment.
	 */
	public String marshal() {
		// TODO Auto-generated method stub
		return null;
	}





}
