package arithmetic.objects.ring;

import arithmetic.objects.LargeInteger;

/**
 * This generic interface is used to represent a ring over some kind of ring
 * element. in our project we will use integer ring elements.
 * 
 * @author Itay
 */
public interface IRing<E> {

	/**
	 * @return the order of the Ring
	 */
	public LargeInteger getOrder();

	/**
	 * @return the 0 of the ring. (the element which is indifferent to the
	 *         addition operation).
	 */
	public E zero();

	/**
	 * @return the 1 of the Ring. (the element which is indifferent to the
	 *         multiplication operation).
	 */
	public E one();
}
