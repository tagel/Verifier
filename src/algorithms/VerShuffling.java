package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import algorithms.provers.ProveShuffling;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayGenerators;
import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import arithmetic.objects.Ring.IRing;
import arithmetic.objects.Ring.IntegerRingElement;
import arithmetic.objects.Ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of verifying the shuffling.
 * 
 * @author Tagel & Sofi
 */

public class VerShuffling {

	private static ArrayOfElements<ProductGroupElement> Liminus1;
	private static ArrayOfElements<ProductGroupElement> Li;
	private static Node PoSCommitment;
	private static Node PoSReply;
	private static ArrayOfElements<IGroupElement> PermutationCommitment;
	private static Node PoSCCommitment;
	private static Node PoSCReply;

	/**
	 * @param prefixToRO
	 * @param lambda
	 * @param N
	 * @param ne
	 * @param nr
	 * @param nv
	 * @param prg
	 * @param Gq
	 * @param pk
	 * @param L0
	 * @param Llambda
	 * @param posc
	 * @param ccpos
	 * @param Zq
	 * @return true if verification of shuffling was successful and false
	 *         otherwise.
	 * @throws IOException
	 */
	static public boolean verify(RandomOracle ROSeed, RandomOracle ROChallenge,
			String directory, byte[] prefixToRO, int lambda, int N, int ne,
			int nr, int nv, PseudoRandomGenerator prg, IGroup Gq,
			ProductGroupElement pk, ArrayOfElements<ProductGroupElement> L0,
			ArrayOfElements<ProductGroupElement> Llambda, boolean posc,
			boolean ccpos, IRing<IntegerRingElement> Zq, int width)
			throws IOException {

		/*
		 * This step sets the N0 size that indicates if there was a
		 * pre-computation step or not. If N0==0, we use the whole permutation
		 * commitment array. Else, we shrink it to use only the relevant ones.
		 */
		int maxciph = readMaxciph(directory);

		if (maxciph == 0) {
			/*
			 * The file maxciph doesn't exist, so we need to loop over the
			 * different shuffling mix-servers (from 1 to lambda), read the
			 * following files: proof of commitment, proof of reply and
			 * permutation commitments, and send them to the shuffling prover.
			 */

			// First we set Li-1 = L0:
			Liminus1 = L0;

			// We read lists of ciphertexts until i=lambda.
			boolean retValue = true; // We check it later

			for (int i = 1; i <= lambda; i++) {
				if (i == lambda) {
					// Here we send the Llambda to the prover.
					retValue = retValue
							&& ProveShuffling.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk, Li,
									Llambda, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				} else {
					if (!readFiles(i, directory, Gq, Zq))
						retValue = false;
					retValue = retValue
							&& ProveShuffling.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk,
									Liminus1, Li, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				}
				/*
				 * If retValue is false, it means that reading of elements
				 * failed or the prover rejected
				 */
				if (!retValue) // we need to check if Liminus1 == Li
					if (i == lambda)
						if (!Llambda.equals(Li))
							return false;
						else if (!Li.equals(Liminus1))
							return false;
				retValue = true;
			}

			return true;

		} else {
			/*
			 * Here maxciph exists, so we have the N0 - size of pre-computed
			 * arrays. In this case we use the proof of shuffle of commitments,
			 * shrink the permutation commitments and verify commitment-
			 * consistent proof of a shuffle.
			 */
			if (N>maxciph)
				return false;
			
			for (int i = 1; i <= lambda; i++) {
				//Step 1 in the algorithm
				verifyPOSC(i, directory, Gq, Zq);
				
				//Step 2: potential early abort
				if (!ccpos)
					return true;
				
				//Step 3: Shrink permutation commitment.
				//First, try to read the KeepList Array:
				byte[] keepListFile = 
				
				
			}
			

		}

		return true;
	}

	private static void verifyPOSC(int i, String directory, IGroup gq,
			IRing<IntegerRingElement> zq) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method reads the relevant files for the i'th mix server. It sets the
	 * global fields, and the main function will send them to the prover.
	 * 
	 * @param i
	 *            the relevant mix-server
	 * @param gq
	 * @return true if the reading succeded
	 * @throws IOException
	 */
	private static boolean readFiles(int i, String directory, IGroup Gq,
			IRing<IntegerRingElement> Zq) throws IOException {
		/*
		 * The following steps read the files as byte[]. These byte[] objects
		 * will be sent to the relevant constructors to make the objects (Node,
		 * ciphertexts, array of commitment...) that will be sent to the
		 * provers.
		 */
		byte[] bLi = ElementsExtractor.btFromFile(directory, "proofs",
				"Ciphertexts" + (i < 10 ? "0" : "") + i + ".bt");
		if (bLi == null)
			return false;

		byte[] bPoSCommitment = ElementsExtractor.btFromFile(directory,
				"proofs", "PoSCommitment" + (i < 10 ? "0" : "") + i + ".bt");
		if (bPoSCommitment == null)
			return false;

		byte[] bPoSReply = ElementsExtractor.btFromFile(directory, "proofs",
				"PoSReply" + (i < 10 ? "0" : "") + i + ".bt");
		if (bPoSReply == null)
			return false;

		byte[] bPermutationCommitment = ElementsExtractor.btFromFile(directory,
				"proofs", "PermutationCommitment" + (i < 10 ? "0" : "") + i
						+ ".bt");
		if (bPermutationCommitment == null)
			return false;

		/*
		 * The following steps create the objects from the byte[] and set the
		 * global fields, that will be sent to the provers.
		 */
		PermutationCommitment = ArrayGenerators.createGroupElementArray(
				bPermutationCommitment, Gq);

		// If i==1 it means that Liminus1 = L0, as we did in the main loop
		if (i != 1)
			Liminus1 = Li;
		Li = ArrayGenerators.createArrayOfCiphertexts(bLi, Gq);

		// TODO reject if the interpretation fails
		/*
		 * each NODE needs to know which type are his children - First we create
		 * the nodes like a generic one, and then we create the appropriate
		 * types from the byte[] data, according to prover Algorithm
		 */
		PoSCommitment = new Node(bPoSCommitment);

		// Read the A', C', D' GroupElements
		IGroupElement temp = ElementsExtractor.createGroupElement(PoSCommitment
				.getAt(1).toByteArray(), Gq);
		PoSCommitment.setAt(1, temp);
		temp = ElementsExtractor.createGroupElement(PoSCommitment.getAt(3)
				.toByteArray(), Gq);
		PoSCommitment.setAt(3, temp);
		temp = ElementsExtractor.createGroupElement(PoSCommitment.getAt(4)
				.toByteArray(), Gq);
		PoSCommitment.setAt(4, temp);

		// Read F' Ciphertext
		ProductGroupElement tempF = ElementsExtractor.createCiphertext(
				PoSCommitment.getAt(5).toByteArray(), Gq);
		PoSCommitment.setAt(5, tempF);

		// Read B and B' arrays of N elements in Gq
		ArrayOfElements<IGroupElement> tempB = ArrayGenerators
				.createGroupElementArray(PoSCommitment.getAt(0).toByteArray(),
						Gq);
		PoSCommitment.setAt(0, tempF);
		tempB = ArrayGenerators.createGroupElementArray(PoSCommitment.getAt(2)
				.toByteArray(), Gq);
		PoSCommitment.setAt(2, tempF);

		PoSReply = new Node(bPoSReply);

		// Read Ka, Kc, Kd as RingElements
		IntegerRingElement tempR = new IntegerRingElement(
				ElementsExtractor.leafToInt(PoSReply.getAt(0).toByteArray()),
				Zq);
		PoSReply.setAt(0, temp);
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSReply
				.getAt(2).toByteArray()), Zq);
		PoSReply.setAt(2, temp);
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSReply
				.getAt(3).toByteArray()), Zq);
		PoSReply.setAt(3, temp);

		// Read Kf as productRingElement
		ProductRingElement tempp = new ProductRingElement(PoSReply.getAt(5)
				.toByteArray(), Zq);
		PoSReply.setAt(5, tempp);

		// Read Kb and Ke as arrays of Ring Elements
		ArrayOfElements<IntegerRingElement> tempK = ArrayGenerators
				.createRingElementArray(PoSReply.getAt(1).toByteArray(), Zq);
		PoSCommitment.setAt(1, tempK);
		tempK = ArrayGenerators.createRingElementArray(PoSReply.getAt(4)
				.toByteArray(), Zq);
		PoSCommitment.setAt(4, tempK);

		return true;
	}

	/**
	 * This method tries to read maxciph - if the file doesn't exist it catches
	 * the exception and returns "0". If the file exists, returns the relevant
	 * integer.
	 * 
	 * @param directory
	 *            - the directory where the file should be.
	 */
	private static int readMaxciph(String directory) {
		Scanner text = null;
		try {
			text = new Scanner(new File(directory, "maxciph"));
		} catch (FileNotFoundException e) {
			return 0;
		}

		return text.nextInt();

	}

}
