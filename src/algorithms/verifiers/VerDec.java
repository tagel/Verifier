package algorithms.verifiers;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;



/**
 * This class provides the functionality of verifying the decryption.
 * @author Tagel & Sofi
 */

public class VerDec {

	
	/**
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
	 * @return true if verification of decryption was successful and false otherwise.
	 */
	static public boolean verify(RandomOracle randomOracle, RandomOracle randomOracle2, String directory, byte[] bs, int i, int j, int k, int l, PseudoRandomGenerator pseudoRandomGenerator, IGroup iGroup, ProductGroupElement productGroupElement, ArrayOfElements<ProductGroupElement> arrayOfElements, ArrayOfElements<ProductRingElement> arrayOfElements2, IRing<IntegerRingElement> iRing) {
		// TODO Auto-generated method stub
		return false;
	}

}
