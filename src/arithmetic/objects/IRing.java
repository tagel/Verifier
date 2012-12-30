package arithmetic.objects;


import java.math.BigInteger;


/**
 * This interface is used to represent a Ring.
 * 
 */
public interface IRing<E> {

	
	public BigInteger getOrder();
	

	/**
	 * @return the 0 of the ring.
	 */
	public E zero(); 
	
	/**
	 * @return the 1 of the Ring.
	 */
	public E one();
	

	






	
}


