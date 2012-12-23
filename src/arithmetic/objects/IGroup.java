package arithmetic.objects;
import java.math.BigInteger;


/**
 * This interface is used to represent a Group.
 *
 * @param <E>
 */
public interface IGroup <E> extends ByteTree {
	
	
	/**
	 * @param a
	 * @return the multiplicative inverse a^-1 of a group element a.
	 */
	public E inverse (E a); 
	
	
	/**
	 * @param a 
	 * @param b
	 * @return The multiplication a*b of two Group elements a and b.
	 */
	public E mult (E a, E b); 
	
	/**
	 * @param a group element
	 * @param b integer
	 * @return  a^b
	 */
	public E power (E a, BigInteger b);

	/**
	 * @return the 1 of the Group.
	 */
	public E one();
	
	/**
	 * @param a group element	
	 * @param b group element
	 * @return True if a == b
	 */
	public boolean equal(E a, E b);
	
	/**
	 * 
	 * @return the resulting ASCII string from converting the byte tree of the group into a byte array which is encoded onto hexadecimal and prepended with an ASCII comment.
	 */
	public String marshal();
		


	
}
