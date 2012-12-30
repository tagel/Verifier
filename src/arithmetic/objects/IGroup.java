package arithmetic.objects;


import java.math.BigInteger;


/**
 * This interface is used to represent a Group.
 *
 * @param <E>
 */
public interface IGroup<E> extends ByteTree {
	
	

	public BigInteger getFieldOrder();
	
	public BigInteger getOrder();



	/**
	 * @return the 1 of the Group.
	 */
	public E one();

		


	
}
