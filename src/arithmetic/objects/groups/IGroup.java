package arithmetic.objects.groups;



import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.arrays.ArrayOfElements;

import cryptographic.primitives.PseudoRandomGenerator;


/**
 * This interface is used to represent a Group.
 *
 * @param <E>
 */
public interface IGroup extends ByteTree {
	
	
	
	public LargeInteger getFieldOrder();
	
	public LargeInteger getOrder();
	
	public IGroupElement getGenerator();

	public String getGroupType();

	/**
	 * @return the 1 of the Group.
	 */
	public IGroupElement one();
	
	public ArrayOfElements<IGroupElement> createRandomArray(int N, PseudoRandomGenerator prg, byte[] seed, int nr) throws Exception;

		


	
}
