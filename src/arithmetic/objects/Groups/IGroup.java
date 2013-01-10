package arithmetic.objects.Groups;



import java.math.BigInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.Arrays.ArrayOfElements;

import cryptographic.primitives.PseudoRandomGenerator;


/**
 * This interface is used to represent a Group.
 *
 * @param <E>
 */
public interface IGroup extends ByteTree {
	
	

	public BigInteger getFieldOrder();
	
	public BigInteger getOrder();
	
	public IGroupElement getGenerator();

	public String getGroupType();

	/**
	 * @return the 1 of the Group.
	 */
	public IGroupElement one();
	
	public ArrayOfElements<IGroupElement> createRandomArray(int N, PseudoRandomGenerator prg, byte[] seed, int nr);

		


	
}
