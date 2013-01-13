package algorithms.verifiers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.arrays.ArrayGenerators;
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

	private static ArrayOfElements<ArrayOfElements<IGroupElement>> DecryptionFactors;
	private static ArrayOfElements<Node> DecrFactCommitment;
	private static ArrayOfElements<Node> DecrFactReply;

	// TODO change the comment
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
			String directory, byte[] prefixToRO, int lambda, int N, int ne,
			int nr, int nv, PseudoRandomGenerator prg, IGroup Gq,
			ProductGroupElement pk, ArrayOfElements<ProductGroupElement> L,
			ArrayOfElements<ProductRingElement> m,
			IRing<IntegerRingElement> Zq,
			ArrayOfElements<IGroupElement> publicKeys,
			ArrayOfElements<IntegerRingElement> secretKeys) {

		// Create the arrays of the different factors
		if (!readDecriptionFactors(lambda, directory, Gq)
				|| !readDecrFactCommitment(lambda, directory, Gq)
				|| !readDecrFactReply(lambda, directory, Gq))
			return false;

		return true;
	}

	private static boolean readDecrFactReply(int lambda, String directory,
			IGroup Gq) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean readDecrFactCommitment(int lambda, String directory,
			IGroup Gq) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean readDecriptionFactors(int lambda, String directory,
			IGroup Gq) {

		for (int i = 0; i < lambda; i++) {

			byte[] bDecrFact;
			try {
				bDecrFact = ElementsExtractor.btFromFile(directory, "proofs",
						"DecriptionFactors" + (i < 10 ? "0" : "") + i + ".bt");
			} catch (IOException e) {
				return false;
			}
			if (bDecrFact == null)
				return false;

			try {
				ArrayOfElements<IGroupElement> factor = ArrayGenerators
						.createGroupElementArray(bDecrFact, Gq);
				DecryptionFactors.add(factor);
			} catch (UnsupportedEncodingException e) {
				return false;
			}

		}
		return true;
	}

}
