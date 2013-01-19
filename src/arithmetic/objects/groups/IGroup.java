package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayOfElements;

import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This interface is used to represent a Group.
 * @author Itay
 */
public interface IGroup extends ByteTree {

	/**
	 * 
	 * @return the order of the underlying field Z*p
	 */
	public LargeInteger getFieldOrder();

	/**
	 * 
	 * @return the order of this group.
	 */
	public LargeInteger getOrder();

	/**
	 * 
	 * @return the generator of the group.
	 */
	public IGroupElement getGenerator();

	/**
	 * @return the 1 of the Group. (the element which is indifferent to the
	 *         multiplication operation).
	 */
	public IGroupElement one();

	/**
	 * 
	 * @param N
	 *            the size of the returned array
	 * @param prg
	 *            a pseudo-random generator
	 * @param seed
	 *            the seed used by the pseudo-random generator
	 * @param nr
	 *            auxiliary security parameter for the seed.
	 * @return an array of size N containing random group elements.
	 */
	public ArrayOfElements<IGroupElement> createRandomArray(int N,
			PseudoRandomGenerator prg, byte[] seed, int nr) throws Exception;

}
