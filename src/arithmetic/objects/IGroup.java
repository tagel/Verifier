package arithmetic.objects;



import java.math.BigInteger;

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
	
	public IGroupElement[] createRandomArray(int N, PseudoRandomGenerator prg, byte[] seed, int nr);

		


	
}
