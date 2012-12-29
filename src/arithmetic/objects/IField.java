package arithmetic.objects;


/**
 * This interface is used to represent a Field.
 *
 * @param <E>
 */
public interface IField<E> extends IRing<E> {

	
	/**
	 * @param a
	 * @return the multiplicative inverse a^-1 of a field element a.
	 */
	public E inverse (E a); 
	
}