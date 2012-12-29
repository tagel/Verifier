package arithmetic.objects;


import java.math.BigInteger;


/**
 * This interface is used to represent a Ring.
 *
 * @param <E>
 */
public interface IRing<E> {

	
	public BigInteger getOrder();
	/**
	 * @param a ring element
	 * @return the additive inverse -a of a ring element a.
	 */
	public E neg (E a); 
	
	/**
	 * @param a ring element
	 * @param b ring element
	 * @return The addition a+b of two ring elements a and b.
	 */
	public E add (E a, E b); 
	
	/**
	 * @param a 
	 * @param b
	 * @return The multiplication a*b of two Ring elements a and b.
	 */
	public E mult (E a, E b); 
	
	/**
	 * @param a ring element
	 * @param b integer
	 * @return  a^b
	 */

	public E power (E a, BigInteger b);

	/**
	 * @return the 0 of the ring.
	 */
	public E zero(); 
	
	/**
	 * @return the 1 of the Ring.
	 */
	public E one();
	
	/**
	 * @param a ring element	
	 * @param b ring element
	 * @return True if a == b
	 */
	public boolean equal(E a, E b);
	






	
}


