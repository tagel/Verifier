package algorithms.verifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import algorithms.provers.ProveCCPoS;
import algorithms.provers.ProveShuffling;
import algorithms.provers.ProveSoC;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BooleanArrayElement;
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
 * This class provides the functionality of verifying the shuffling.
 * 
 * @author Sofi
 */

public class VerShuffling {

	private static final String MAXCIPH = "maxciph";
	private static final String CCPOS_REPLY = "CCPoSReply";
	private static final String CCPOS_COMMITMENT = "CCPoSCommitment";
	private static final String CIPHERTEXTS = "Ciphertexts";
	private static final String BT_FILE_EXT = ".bt";
	private static final String KEEP_LIST = "KeepList";
	private static final String PROOFS = "proofs";
	private static ArrayOfElements<ProductGroupElement> Liminus1;
	private static ArrayOfElements<ProductGroupElement> Li;
	private static ArrayOfElements<IGroupElement> PermutationCommitment;

	private static Node PoSCommitment;
	private static Node PoSReply;
	private static Node PoSCCommitment;
	private static Node PoSCReply;
	private static Node CCPoSCommitment;
	private static Node CCPoSReply;
	private static BooleanArrayElement keepList;

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
	 * @throws Exception
	 */
	static public boolean verify(RandomOracle ROSeed, RandomOracle ROChallenge,
			String directory, byte[] prefixToRO, int lambda, int N, int ne,
			int nr, int nv, PseudoRandomGenerator prg, IGroup Gq,
			ProductGroupElement pk, ArrayOfElements<ProductGroupElement> L0,
			ArrayOfElements<ProductGroupElement> Llambda, boolean posc,
			boolean ccpos, IRing<IntegerRingElement> Zq, int width,
			ArrayOfElements<IGroupElement> h) throws Exception {

		// set maxciph (indicates if there was pre-computation)
		int maxciph = readMaxciph(directory);

		// If maxciph == -1, we use the whole permutation commitment array
		// (otherwise, we use only the relevant ones).
		if (maxciph == -1) {

			Liminus1 = L0;

			// read lists of ciphertexts from 1 to lambda.
			boolean retValue;
			for (int i = 1; i <= lambda; i++) {
				retValue = true; // initialize

				if (!readFilesPoS(i, directory, Gq, Zq, N, width)) {
					retValue = false;
				}

				if (i == lambda) {
					// Here we send the Llambda to the prover.
					retValue = retValue
							&& ProveShuffling.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk, Li,
									Llambda, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				} else {
					retValue = retValue
							&& ProveShuffling.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk,
									Liminus1, Li, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				}

				// If !retValue, it means that reading the elements
				// failed or the prover rejected
				if (!retValue) {
					if (!compareLs(lambda, Llambda, i)) {
						return false;
					}
				}
			}
			return true;

		} else {

			// maxciph file exists (we have the size of pre-computed arrays)
			boolean retValue;
			if (N > maxciph) {
				return false;
			}

			for (int i = 1; i <= lambda; i++) {
				// Step 1 in the algorithm
				retValue = true; // initialize
				// TODO change the vars we send to the prover
				if (!readFilesPoSC(i, directory, Gq, Zq, N, width)
						|| (!ProveSoC.prove(ROSeed, ROChallenge, prefixToRO, N,
								ne, nr, nv, prg, Gq, PermutationCommitment,
								PoSCCommitment, PoSCCommitment))) {
					// if the algorithm rejects or reading failed set
					// permutation commitment to be h
					PermutationCommitment = h;
				}

				// Step 2: potential early abort
				if (!ccpos) {
					return true;
				}

				// Step 3: Shrink permutation commitment
				// trying to read the KeepList Array
				byte[] keepListFile = ElementsExtractor.btFromFile(directory,
						PROOFS, KEEP_LIST + (i < 10 ? "0" : "") + i
								+ BT_FILE_EXT);
				// if the file doesn't exist - fill the array with N truths and
				// the rest are false.
				if (keepListFile == null) {
					keepList = new BooleanArrayElement(
							createBooleanArrayWithNTrue(N, maxciph));
				} else {
					keepList = new BooleanArrayElement(keepListFile);
				}
				// shrink the permutation commitment according to keep list
				PermutationCommitment = shrinkPrmutatuonCommitment(maxciph);

				// Step 4 and 5 of the algorithm
				Liminus1 = L0; // initialize the list

				// read lists of ciphertexts until i=lambda.
				if (!readFilesCCPos(i, directory, Gq, Zq, N, width)) {
					retValue = false;
				}

				if (i == lambda) { // then send the Llambda to the prover
					retValue = retValue
							&& ProveCCPoS.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk, Li,
									Llambda, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				} else {
					retValue = retValue
							&& ProveCCPoS.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk,
									Liminus1, Li, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				}

				// If !retValue, it means that reading the elements
				// failed or the prover rejected
				if (!retValue) {
					if (!compareLs(lambda, Llambda, i)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static ArrayOfElements<IGroupElement> shrinkPrmutatuonCommitment(
			int maxciph) {
		ArrayOfElements<IGroupElement> ret = new ArrayOfElements<IGroupElement>();

		for (int j = 0; j < maxciph; j++) {
			if (keepList.getAt(j)) {
				ret.add(PermutationCommitment.getAt(j));
			}
		}
		return ret;
	}

	private static boolean[] createBooleanArrayWithNTrue(int N, int maxciph) {
		boolean[] tempArr = new boolean[maxciph];
		for (int j = 0; j < maxciph; j++) {
			if (j < N) {
				tempArr[j] = true;
			} else {
				tempArr[j] = false;
			}
		}
		return tempArr;
	}

	private static boolean compareLs(int lambda,
			ArrayOfElements<ProductGroupElement> Llambda, int i) {
		if (i == lambda) {
			if (!Llambda.equals(Li)) {
				return false;
			}
		} else if (!Li.equals(Liminus1)) {
			return false;
		}
		return true;
	}

	/*
	 * TODO Sofi - reject if the interpretation fails - in: readFilesPoSC
	 * readFilesCCPos readFilesPoS
	 */

	/**
	 * Read the files as byte[]. These byte[] objects will be sent to the
	 * relevant constructors to make the objects (Node, cipher-texts, array of
	 * commitment...) that will be sent to the provers.
	 * 
	 * @param i
	 *            the mix server
	 * @param directory
	 *            the directory
	 * @param Gq
	 *            the IGroup
	 * @param Zq
	 *            the IRing
	 * @param width
	 * @return true if the reading and extraction of variables succeeded
	 * @throws IOException
	 *             if bt from file fails or extraction fails
	 */
	private static boolean readFilesPoSC(int i, String directory, IGroup Gq,
			IRing<IntegerRingElement> Zq, int N, int width) throws IOException {

		byte[] bPoSCCommitment = ElementsExtractor.btFromFile(directory,
				PROOFS, "PoSCCommitment" + (i < 10 ? "0" : "") + i
						+ BT_FILE_EXT);
		if (bPoSCCommitment == null) {
			return false;
		}

		byte[] bPoSCReply = ElementsExtractor.btFromFile(directory, PROOFS,
				"PoSCReply" + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bPoSCReply == null) {
			return false;
		}

		byte[] bPermutationCommitment = ElementsExtractor.btFromFile(directory,
				PROOFS, "PermutationCommitment" + (i < 10 ? "0" : "") + i
						+ BT_FILE_EXT);
		if (bPermutationCommitment == null) {
			return false;
		}

		// create the objects from the byte[] and set the global fields, that
		// will be sent to the provers.
		PermutationCommitment = ArrayGenerators.createGroupElementArray(
				bPermutationCommitment, Gq);

		// first create the nodes as a generic node, then create the appropriate
		// types from the byte[] data, according to prover Algorithm
		PoSCCommitment = new Node(bPoSCCommitment);

		// Read the A', C', D' GroupElements
		PoSCCommitment.setAt(1, ElementsExtractor.createGroupElement(
				PoSCCommitment.getAt(1).toByteArray(), Gq)); // A'

		PoSCCommitment.setAt(3, ElementsExtractor.createGroupElement(
				PoSCCommitment.getAt(3).toByteArray(), Gq)); // C'

		PoSCCommitment.setAt(4, ElementsExtractor.createGroupElement(
				PoSCCommitment.getAt(4).toByteArray(), Gq)); // D'

		// Read B and B' arrays of N elements in Gq, and verify their size
		ArrayOfElements<IGroupElement> tempB = ArrayGenerators
				.createGroupElementArray(PoSCCommitment.getAt(0).toByteArray(),
						Gq);
		if (tempB.getSize() != N) {
			return false;
		}
		PoSCCommitment.setAt(0, tempB); // B

		tempB = ArrayGenerators.createGroupElementArray(PoSCCommitment.getAt(2)
				.toByteArray(), Gq);
		if (tempB.getSize() != N) {
			return false;
		}
		PoSCCommitment.setAt(2, tempB); // B'

		// Create the PoSCReply in the same way
		PoSCReply = new Node(bPoSCReply);

		// Read Ka, Kc, Kd as RingElements
		IntegerRingElement tempR = new IntegerRingElement(
				ElementsExtractor.leafToInt(PoSCReply.getAt(0).toByteArray()),
				Zq);
		PoSCReply.setAt(0, tempR); // Ka

		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSCReply
				.getAt(2).toByteArray()), Zq);
		PoSCReply.setAt(2, tempR); // Kc

		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSCReply
				.getAt(3).toByteArray()), Zq);
		PoSCReply.setAt(3, tempR); // Kd

		// Read Kb and Ke as arrays of Ring Elements, and verify they size == N
		ArrayOfElements<IntegerRingElement> tempK = ArrayGenerators
				.createRingElementArray(PoSCReply.getAt(1).toByteArray(), Zq);
		if (tempK.getSize() != N) {
			return false;
		}
		PoSCReply.setAt(1, tempK); // Kb

		tempK = ArrayGenerators.createRingElementArray(PoSCReply.getAt(4)
				.toByteArray(), Zq);
		if (tempK.getSize() != N) {
			return false;
		}
		PoSCReply.setAt(4, tempK); // Ke

		return true;
	}

	/**
	 * This method reads the relevant files for the i'th mix server. It sets the
	 * global fields, and the main function will send them to the Shuffling of
	 * commitments prover.
	 * 
	 * @param i
	 *            mix server index
	 * @param directory
	 *            the directory where the files are
	 * @param Gq
	 *            the IGroup
	 * @param Zq
	 *            the IRing
	 * @param width
	 * @return true if the reading and extraction of variables succeded
	 * @throws IOException
	 *             if bt from file fails or extraction fails
	 */
	private static boolean readFilesCCPos(int i, String directory, IGroup Gq,
			IRing<IntegerRingElement> Zq, int N, int width) throws IOException {

		// read the files as byte[]
		byte[] bLi = ElementsExtractor.btFromFile(directory, PROOFS,
				CIPHERTEXTS + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bLi == null) {
			return false;
		}

		byte[] bCCPoSCommitment = ElementsExtractor.btFromFile(directory,
				PROOFS, CCPOS_COMMITMENT + (i < 10 ? "0" : "") + i
						+ BT_FILE_EXT);
		if (bCCPoSCommitment == null) {
			return false;
		}

		byte[] bCCPoSReply = ElementsExtractor.btFromFile(directory, PROOFS,
				CCPOS_REPLY + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bCCPoSReply == null) {
			return false;
		}

		// create the objects from the byte[] and set the global fields 
		
		if (i != 1) { // If i == 1 then Liminus1 = L0, as in the main loop
			Liminus1 = Li;
		}
		Li = ArrayGenerators.createArrayOfCiphertexts(bLi, Gq, width);
		if (Li.getSize() != N) {
			return false;
		}

		/*
		 * each NODE needs to know which type are his children - First we create
		 * the nodes like a generic one, and then we create the appropriate
		 * types from the byte[] data, according to prover Algorithm
		 */
		CCPoSCommitment = new Node(bCCPoSCommitment);

		// A' As GroupElement
		IGroupElement temp = ElementsExtractor.createGroupElement(
				CCPoSCommitment.getAt(0).toByteArray(), Gq);
		CCPoSCommitment.setAt(0, temp);

		// Read B' as Ciphertext
		ProductGroupElement tempB = ElementsExtractor.createCiphertext(
				CCPoSCommitment.getAt(1).toByteArray(), Gq, width);
		CCPoSCommitment.setAt(1, tempB);

		// Create the CCPoSReply in the same way
		CCPoSReply = new Node(bCCPoSReply);

		// Read Ka, Kb, Ke as RingElements
		// Ka
		IntegerRingElement tempR = new IntegerRingElement(
				ElementsExtractor.leafToInt(CCPoSReply.getAt(0).toByteArray()),
				Zq);
		CCPoSReply.setAt(0, tempR);

		// Read Kb as productRingElement
		ProductRingElement tempp = new ProductRingElement(CCPoSReply.getAt(1)
				.toByteArray(), Zq, width);
		CCPoSReply.setAt(1, tempp);

		// Read Ke as arrays of Ring Elements, and verify if they are of size N
		ArrayOfElements<IntegerRingElement> tempK = ArrayGenerators
				.createRingElementArray(CCPoSReply.getAt(2).toByteArray(), Zq);
		if (tempK.getSize() != N) {
			return false;
		}
		CCPoSReply.setAt(2, tempK);

		return true;
	}

	/**
	 * This method reads the relevant files for the i'th mix server. It sets the
	 * global fields, and the main function will send them to the Suffling
	 * prover.
	 * 
	 * @param i
	 *            the relevant mix-server
	 * @param Gq
	 *            - The IGroup
	 * @param width
	 * @return true if the reading and extraction of variables succeded
	 * @throws IOException
	 *             - if bt from file fails or extraction fails
	 */
	private static boolean readFilesPoS(int i, String directory, IGroup Gq,
			IRing<IntegerRingElement> Zq, int N, int width) throws IOException {
		/*
		 * The following steps read the files as byte[]. These byte[] objects
		 * will be sent to the relevant constructors to make the objects (Node,
		 * ciphertexts, array of commitment...) that will be sent to the
		 * provers.
		 */
		byte[] bLi = ElementsExtractor.btFromFile(directory, PROOFS,
				CIPHERTEXTS + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bLi == null) {
			return false;
		}

		byte[] bPoSCommitment = ElementsExtractor.btFromFile(directory, PROOFS,
				"PoSCommitment" + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bPoSCommitment == null) {
			return false;
		}

		byte[] bPoSReply = ElementsExtractor.btFromFile(directory, PROOFS,
				"PoSReply" + (i < 10 ? "0" : "") + i + BT_FILE_EXT);
		if (bPoSReply == null) {
			return false;
		}

		byte[] bPermutationCommitment = ElementsExtractor.btFromFile(directory,
				PROOFS, "PermutationCommitment" + (i < 10 ? "0" : "") + i
						+ BT_FILE_EXT);
		if (bPermutationCommitment == null) {
			return false;
		}

		/*
		 * The following steps create the objects from the byte[] and set the
		 * global fields, that will be sent to the provers.
		 */
		PermutationCommitment = ArrayGenerators.createGroupElementArray(
				bPermutationCommitment, Gq);

		// If i==1 it means that Liminus1 = L0, as we did in the main loop
		if (i != 1) {
			Liminus1 = Li;
		}

		Li = ArrayGenerators.createArrayOfCiphertexts(bLi, Gq, width);
		if (Li.getSize() != N) {
			return false;
		}

		/*
		 * each NODE needs to know which type are his children - First we create
		 * the nodes like a generic one, and then we create the appropriate
		 * types from the byte[] data, according to prover Algorithm
		 */
		PoSCommitment = new Node(bPoSCommitment);

		// Read the A', C', D' GroupElements
		// A'
		IGroupElement temp = ElementsExtractor.createGroupElement(PoSCommitment
				.getAt(1).toByteArray(), Gq);
		PoSCommitment.setAt(1, temp);

		// C'
		temp = ElementsExtractor.createGroupElement(PoSCommitment.getAt(3)
				.toByteArray(), Gq);
		PoSCommitment.setAt(3, temp);

		// D'
		temp = ElementsExtractor.createGroupElement(PoSCommitment.getAt(4)
				.toByteArray(), Gq);
		PoSCommitment.setAt(4, temp);

		// Read F' Ciphertext
		ProductGroupElement tempF = ElementsExtractor.createCiphertext(
				PoSCommitment.getAt(5).toByteArray(), Gq, width);
		PoSCommitment.setAt(5, tempF);

		// Read B and B' arrays of N elements in Gq
		// B
		ArrayOfElements<IGroupElement> tempB = ArrayGenerators
				.createGroupElementArray(PoSCommitment.getAt(0).toByteArray(),
						Gq);
		// This array should be of size N
		if (tempB.getSize() != N) {
			return false;
		}
		PoSCommitment.setAt(0, tempB);

		// B'
		tempB = ArrayGenerators.createGroupElementArray(PoSCommitment.getAt(2)
				.toByteArray(), Gq);
		if (tempB.getSize() != N) {
			return false;
		}
		PoSCommitment.setAt(2, tempB);

		// Create the PoSReply in the same way
		PoSReply = new Node(bPoSReply);

		// Read Ka, Kc, Kd as RingElements
		// Ka
		IntegerRingElement tempR = new IntegerRingElement(
				ElementsExtractor.leafToInt(PoSReply.getAt(0).toByteArray()),
				Zq);
		PoSReply.setAt(0, tempR);

		// Kc
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSReply
				.getAt(2).toByteArray()), Zq);
		PoSReply.setAt(2, tempR);

		// Kd
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSReply
				.getAt(3).toByteArray()), Zq);
		PoSReply.setAt(3, tempR);

		// Read Kf as productRingElement
		ProductRingElement tempp = new ProductRingElement(PoSReply.getAt(5)
				.toByteArray(), Zq, width);
		PoSReply.setAt(5, tempp);

		// Read Kb and Ke as arrays of Ring Elements
		// Kb
		ArrayOfElements<IntegerRingElement> tempK = ArrayGenerators
				.createRingElementArray(PoSReply.getAt(1).toByteArray(), Zq);
		if (tempK.getSize() != N) {
			return false;
		}
		PoSReply.setAt(1, tempK);

		// Ke
		tempK = ArrayGenerators.createRingElementArray(PoSReply.getAt(4)
				.toByteArray(), Zq);
		if (tempK.getSize() != N) {
			return false;
		}

		PoSReply.setAt(4, tempK);
		return true;
	}

	/**
	 * Returns the maxciph read from file or -1 if the file doesn't exists.
	 * 
	 * @param directory
	 *            - the directory where the file should be.
	 */
	private static int readMaxciph(String directory) {
		Scanner text = null;
		try {
			text = new Scanner(new File(directory, MAXCIPH));
		} catch (FileNotFoundException e) {
			return -1;
		}

		return text.nextInt();
	}
}
