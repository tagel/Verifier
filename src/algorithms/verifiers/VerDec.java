package algorithms.verifiers;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of verifying the decryption.
 * 
 * @author Tagel & Sofi
 */

public class VerDec {

	private ArrayOfElements<ArrayOfElements<IGroupElement>> mixDecryptionFactors;
	private ArrayOfElements<Node> mixDecrFactCommitment;
	private ArrayOfElements<Node> mixDecrFactReply;
	
	/**
	 * @param arrayOfElements2
	 * @param publicKeys
	 * @param randomOracle2
	 * @param randomOracle
	 * @param arrayOfElements2
	 * @param arrayOfElements
	 * @param productGroupElement
	 * @param iGroup
	 * @param pseudoRandomGenerator
	 * @param l
	 * @param k
	 * @param j
	 * @param i
	 * @param bs
	 * @param iRing
	 * @return true if verification of decryption was successful and false
	 *         otherwise.
	 */
	static public boolean verify(RandomOracle ROSeed, RandomOracle ROChallenge,
			String directory, byte[] prefixToRO, int lambda, int N, int ne, int nr, int nv,
			PseudoRandomGenerator prg, IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> L,
			ArrayOfElements<ProductRingElement> m,
			IRing<IntegerRingElement> Zq,
			ArrayOfElements<IGroupElement> publicKeys,
			ArrayOfElements<IntegerRingElement> secretKeys) {
		
		
		//Create the arrays of the different factors

		
		
		return true;
	}

}
