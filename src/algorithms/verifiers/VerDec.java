package algorithms.verifiers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import algorithms.provers.ProveDec;
import algorithms.provers.Prover;
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

	private static ArrayOfElements<ArrayOfElements<ProductRingElement>> DecryptionFactors;
	private static ArrayOfElements<Node> DecrFactCommitments;
	private static ArrayOfElements<IntegerRingElement> DecrFactReplies;

	// TODO change the comment
	/**
	 * @param arrayOfElements2
	 * @param publicKeys
	 * @param width
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
	 * @throws Exception
	 */
	static public boolean verify(RandomOracle ROSeed, RandomOracle ROChallenge,
			String directory, byte[] prefixToRO, int lambda, int N, int ne,
			int nr, int nv, PseudoRandomGenerator prg, IGroup Gq,
			ProductGroupElement pk, ArrayOfElements<ProductGroupElement> L,
			ArrayOfElements<ProductRingElement> m,
			IRing<IntegerRingElement> Zq,
			ArrayOfElements<IGroupElement> publicKeys,
			ArrayOfElements<IntegerRingElement> secretKeys, int width)
			throws Exception {

		// ********Step 1 in the algorithm**********
		// First, we read the relevant arrays of proofs
		for (int i = 1; i <= lambda; i++) {
			// Create the arrays of the different factors
			if (!readDecriptionFactors(lambda, directory, Zq, i, N)
					|| !readDecrFactCommitment(lambda, directory, Gq, i)
					|| !readDecrFactReply(lambda, directory, Zq, i))
				return false;
		}

		// Extract g from the public key to send to the verifier:
		IGroupElement g = pk.getElements().getAt(0);

		// ********Step 2 in the algorithm**********
		// Now we try to do the combined proof
		// If this return true, we skip to step 4
		if (!ProveDec.prove(0, prefixToRO, N, ne, nr, nv, prg, Gq, g,
				publicKeys, secretKeys, L, m))

			// ********Step 3 in the algorithm**********
			for (int i = 1; i <= lambda; i++) {
				if (!ProveDec.prove(i - 1, prefixToRO, N, ne, nr, nv, prg, Gq,
						g, publicKeys, secretKeys, L, m)
						&& (secretKeys.getAt(i - 1) == null || !DecryptionFactors
								.getAt(i - 1).equals(Prover.PDecrypt(
								secretKeys.getAt(i - 1), L))))
					return false;
			}

		// ********Step 4 in the algorithm**********
		//Verify Plainexts:
		//TODO MULTIPLY ARRAYS?!
		ProductRingElement f = null;
		
		if (!m.equals(Prover.TDecrypt(L, f)))
			return false;

		return true;
	}

	private static boolean readDecrFactCommitment(int lambda, String directory,
			IGroup Gq, int i) throws UnsupportedEncodingException {

		byte[] bDecrFactCommitment;
		try {
			bDecrFactCommitment = ElementsExtractor.btFromFile(directory,
					"proofs", "DecrFactCommitment" + (i < 10 ? "0" : "") + i
							+ ".bt");
		} catch (IOException e) {
			return false;
		}
		if (bDecrFactCommitment == null)
			return false;

		// Now interpret the DecrFactCommitment as a Node with two children:

		Node DecrFactCommitment = new Node(bDecrFactCommitment);

		// Read ytag as GroupElement
		IGroupElement temp = ElementsExtractor.createGroupElement(
				DecrFactCommitment.getAt(0).toByteArray(), Gq);

		DecrFactCommitment.setAt(0, temp);

		// Read Btag as plaintext
		ProductGroupElement tempB = ElementsExtractor.createSimplePGE(
				DecrFactCommitment.getAt(0).toByteArray(), Gq);

		DecrFactCommitment.setAt(1, tempB);

		DecrFactCommitments.add(DecrFactCommitment);

		return true;
	}

	private static boolean readDecrFactReply(int lambda, String directory,
			IRing<IntegerRingElement> Zq, int i) {

		byte[] bDecrFactReply;
		try {
			bDecrFactReply = ElementsExtractor.btFromFile(directory, "proofs",
					"DecrFactReply" + (i < 10 ? "0" : "") + i + ".bt");
		} catch (IOException e) {
			return false;
		}
		if (bDecrFactReply == null)
			return false;

		IntegerRingElement temp = new IntegerRingElement(
				ElementsExtractor.leafToInt(bDecrFactReply), Zq);

		DecrFactReplies.add(temp);

		return true;
	}

	private static boolean readDecriptionFactors(int lambda, String directory,
			IRing<IntegerRingElement> Zq, int i, int N)
			throws UnsupportedEncodingException {

		byte[] bDecrFact;
		try {
			bDecrFact = ElementsExtractor.btFromFile(directory, "proofs",
					"DecriptionFactors" + (i < 10 ? "0" : "") + i + ".bt");
		} catch (IOException e) {
			return false;
		}
		if (bDecrFact == null)
			return false;

		ArrayOfElements<ProductRingElement> factor = ArrayGenerators
				.createArrayOfPlaintexts(bDecrFact, Zq);
		
		if (factor.getSize()!=N)
			return false;
		
		DecryptionFactors.add(factor);

		return true;
	}

}
