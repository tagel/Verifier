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
 * @author Sofi
 */

public class VerDec {
	private static final String BT_FILE_EXT = ".bt";
	private static final String PROOFS = "proofs";
	private static final String DECRIPTION_FACTORS = "DecriptionFactors";
	private static final String DECR_FACT_COMMITMENT = "DecrFactCommitment";
	private static final String DECR_FACT_REPLY = "DecrFactReply";

	private static ArrayOfElements<ArrayOfElements<ProductGroupElement>> DecryptionFactors;
	private static ArrayOfElements<Node> DecrFactCommitments;
	private static ArrayOfElements<IntegerRingElement> DecrFactReplies;

	// TODO Sofi - change the comment
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
			ArrayOfElements<ProductGroupElement> m,
			IRing<IntegerRingElement> Zq,
			ArrayOfElements<IGroupElement> publicKeys,
			ArrayOfElements<IntegerRingElement> secretKeys, int width, ArrayOfElements<IGroupElement> randArray)
			throws Exception {

		// ********Step 1 in the algorithm**********
		// First, we read the relevant arrays of proofs
		for (int i = 1; i <= lambda; i++) {
			// Create the arrays of the different factors
			if (!readDecriptionFactors(lambda, directory, Gq, i, N, width)
					|| !readDecrFactCommitment(lambda, directory, Gq, i)
					|| !readDecrFactReply(lambda, directory, Zq, i))
				return false;
		}

		// Extract g from the public key to send to the verifier:
		IGroupElement g = pk.getElements().getAt(0);

		// ********Step 2 in the algorithm**********
		// Now we try to do the combined proof
		// If this return true, we skip to step 4
		if (!ProveDec.prove(ROSeed, ROChallenge, 0, prefixToRO, N, ne, nr, nv,
				prg, Gq, g, publicKeys, secretKeys, L, m, DecryptionFactors,
				DecrFactCommitments, DecrFactReplies)) {

			// ********Step 3 in the algorithm**********
			for (int i = 1; i <= lambda; i++) {
				boolean proveDec = ProveDec.prove(ROSeed, ROChallenge, i - 1,
						prefixToRO, N, ne, nr, nv, prg, Gq, g, publicKeys,
						secretKeys, L, m, DecryptionFactors,
						DecrFactCommitments, DecrFactReplies);
				if (!proveDec
						&& (secretKeys.getAt(i - 1) == null || !DecryptionFactors
								.getAt(i - 1).equals(
										Prover.PDecrypt(
												secretKeys.getAt(i - 1), L)))) {
					return false;
				}
			}
		}

		// ********Step 4 in the algorithm**********
		// Verify Plaintexts:
		// TODO Sofi - MULTIPLY ARRAYS?!
		ArrayOfElements<ProductRingElement> f = multiplyArays(DecryptionFactors);
		if (!m.equals(Prover.TDecrypt(L, f))) {
			return false;
		}
		return true;
	}

	private static ArrayOfElements<ProductRingElement> multiplyArays(
			ArrayOfElements<ArrayOfElements<ProductRingElement>> arrays) {
		
		ArrayOfElements<ProductRingElement> retVal = new ArrayOfElements<ProductRingElement>();
		//i== number of arrays
		int i = arrays.getSize();
		
		//j== number of elements in the array
		int j = arrays.getAt(0).getSize();
		
		
		
		return null;
	}

	private static boolean readDecrFactCommitment(int lambda, String directory,
			IGroup Gq, int i) throws UnsupportedEncodingException {

		byte[] bDecrFactCommitment;
		try {
			bDecrFactCommitment = ElementsExtractor.btFromFile(directory,
					PROOFS, DECR_FACT_COMMITMENT + (i < 10 ? "0" : "") + i
							+ BT_FILE_EXT);
		} catch (IOException e) {
			return false;
		}

		if (bDecrFactCommitment == null) {
			return false;
		}

		// Now interpret the DecrFactCommitment as a Node with two children:
		Node DecrFactCommitment = new Node(bDecrFactCommitment);

		// Read ytag as GroupElement
		DecrFactCommitment.setAt(0, ElementsExtractor.createGroupElement(
				DecrFactCommitment.getAt(0).toByteArray(), Gq));

		// Read Btag as plaintext
		DecrFactCommitment.setAt(1, ElementsExtractor.createSimplePGE(
				DecrFactCommitment.getAt(0).toByteArray(), Gq));
		DecrFactCommitments.add(DecrFactCommitment);

		return true;
	}

	private static boolean readDecrFactReply(int lambda, String directory,
			IRing<IntegerRingElement> Zq, int i) {

		byte[] bDecrFactReply;
		try {
			bDecrFactReply = ElementsExtractor.btFromFile(directory, PROOFS,
					DECR_FACT_REPLY + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		} catch (IOException e) {
			return false;
		}

		if (bDecrFactReply == null) {
			return false;
		}

		DecrFactReplies.add(new IntegerRingElement(ElementsExtractor
				.leafToInt(bDecrFactReply), Zq));
		return true;
	}

	private static boolean readDecriptionFactors(int lambda, String directory,
			IGroup Gq, int i, int N, int width)
			throws UnsupportedEncodingException {

		byte[] bDecrFact;
		try {
			bDecrFact = ElementsExtractor.btFromFile(directory, PROOFS,
					DECRIPTION_FACTORS + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		} catch (IOException e) {
			return false;
		}

		if (bDecrFact == null) {
			return false;
		}

		ArrayOfElements<ProductGroupElement> factor = ArrayGenerators
				.createArrayOfPlaintexts(bDecrFact, Gq, width);

		if (factor.getSize() != N) {
			return false;
		}

		DecryptionFactors.add(factor);
		return true;
	}
}
