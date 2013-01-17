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
import arithmetic.objects.basicelements.StringLeaf;
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
 * @author Tagel & Sofi
 */

public class VerShuffling {

	private static ArrayOfElements<ProductGroupElement> Liminus1;
	private static ArrayOfElements<ProductGroupElement> Li;

	private static ArrayOfElements<IGroupElement> PermutationCommitment;

	private static Node PoSCommitment;
	private static Node PoSReply;

	private static Node PoSCCommitment;
	private static Node PoSCReply;
	private static BooleanArrayElement keepList;

	private static Node CCPoSCommitment;
	private static Node CCPoSReply;

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
			boolean ccpos, IRing<IntegerRingElement> Zq, int width)
			throws Exception {

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
					if (!readFilesPoS(i, directory, Gq, Zq, N, width))
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
			if (N > maxciph)
				return false;

			for (int i = 1; i <= lambda; i++) {
				// Step 1 in the algorithm
				boolean retValue = true;
				// TODO change the vars we send to the prover
				if (!readFilesPoSC(i, directory, Gq, Zq, N, width)
						|| (!ProveSoC.prove(ROSeed, ROChallenge, prefixToRO, N,
								ne, nr, nv, prg, Gq, PermutationCommitment,
								PoSCCommitment, PoSCCommitment))) {
					/*
					 * If the algorithm rejected or the reading failed, set
					 * permutation commitment to be h - an array of random group
					 * elements
					 */
					StringLeaf stringLeaf = new StringLeaf("generators");
					byte[] independentSeed = ROSeed
							.getRandomOracleOutput(ArrayGenerators
									.concatArrays(prefixToRO,
											stringLeaf.toByteArray()));
					ArrayOfElements<IGroupElement> h = Gq.createRandomArray(N,
							prg, independentSeed, nr);
					PermutationCommitment = h;
				}

				// Step 2: potential early abort
				if (!ccpos)
					return true;

				// Step 3: Shrink permutation commitment.
				// First, try to read the KeepList Array:
				byte[] keepListFile = ElementsExtractor.btFromFile(directory,
						"proofs", "KeepList" + (i < 10 ? "0" : "") + i + ".bt");

				if (keepListFile == null) {// if the file doesn't exist - we
											// fill the array to be N truths and
											// the rest falses.
					boolean[] tempArr = new boolean[maxciph];
					for (int j = 0; i < maxciph; j++)
						if (j < N)
							tempArr[j] = true;
						else
							tempArr[j] = false;
					keepList = new BooleanArrayElement(tempArr);

				} else { // The file does exist, we read it.
					keepList = new BooleanArrayElement(keepListFile);
				}
				// Now we shrink the permutation commitment according to keep
				// list
				ArrayOfElements<IGroupElement> tempArray = new ArrayOfElements<IGroupElement>();
				for (int j = 0; i < maxciph; j++) {
					if (keepList.getAt(j))
						tempArray.add(PermutationCommitment.getAt(j));
				}
				PermutationCommitment = tempArray;

				// Step 4 and 5 of the algorithm
				Liminus1 = L0;// First we initialize the list

				// We read lists of ciphertexts until i=lambda.
				retValue = true; // We check it later

				if (i == lambda) {
					// TODO change the parameters we send to the prover
					// Here we send the Llambda to the prover.
					retValue = retValue
							&& ProveCCPoS.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk, Li,
									Llambda, width, PermutationCommitment,
									PoSCommitment, PoSReply);
				} else {
					if (!readFilesCCPos(i, directory, Gq, Zq, N, width))
						retValue = false;
					retValue = retValue
							&& ProveCCPoS.prove(ROSeed, ROChallenge,
									prefixToRO, N, ne, nr, nv, prg, Gq, pk, Liminus1,
									Li, width, PermutationCommitment,
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

		}

		return true;
	}

	/*
	 * TODO reject if the interpretation fails - in: readFilesPoSC
	 * readFilesCCPos readFilesPoS
	 */

	/**
	 * 
	 * @param i
	 *            - the mix server
	 * @param directory
	 * @param Gq
	 *            - the IGroup
	 * @param Zq
	 *            - the IRing
	 * @param width
	 * @return true if the reading and extraction of variables succeded
	 * @throws IOException
	 *             if bt from file fails or extraction fails
	 */
	private static boolean readFilesPoSC(int i, String directory, IGroup Gq,
			IRing<IntegerRingElement> Zq, int N, int width) throws IOException {
		/*
		 * The following steps read the files as byte[]. These byte[] objects
		 * will be sent to the relevant constructors to make the objects (Node,
		 * ciphertexts, array of commitment...) that will be sent to the
		 * provers.
		 */
		byte[] bPoSCCommitment = ElementsExtractor.btFromFile(directory,
				"proofs", "PoSCCommitment" + (i < 10 ? "0" : "") + i + ".bt");
		if (bPoSCCommitment == null)
			return false;

		byte[] bPoSCReply = ElementsExtractor.btFromFile(directory, "proofs",
				"PoSCReply" + (i < 10 ? "0" : "") + i + ".bt");
		if (bPoSCReply == null)
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

		/*
		 * each NODE needs to know which type are his children - First we create
		 * the nodes like a generic one, and then we create the appropriate
		 * types from the byte[] data, according to prover Algorithm
		 */
		PoSCCommitment = new Node(bPoSCCommitment);

		// Read the A', C', D' GroupElements
		// A'
		IGroupElement temp = ElementsExtractor.createGroupElement(
				PoSCCommitment.getAt(1).toByteArray(), Gq);
		PoSCCommitment.setAt(1, temp);

		// C'
		temp = ElementsExtractor.createGroupElement(PoSCCommitment.getAt(3)
				.toByteArray(), Gq);
		PoSCCommitment.setAt(3, temp);

		// D'
		temp = ElementsExtractor.createGroupElement(PoSCCommitment.getAt(4)
				.toByteArray(), Gq);
		PoSCCommitment.setAt(4, temp);

		// Read B and B' arrays of N elements in Gq, and verify if they are of
		// size N
		// B
		ArrayOfElements<IGroupElement> tempB = ArrayGenerators
				.createGroupElementArray(PoSCCommitment.getAt(0).toByteArray(),
						Gq);
		if (tempB.getSize() != N)
			return false;
		PoSCCommitment.setAt(0, tempB);

		// B'
		tempB = ArrayGenerators.createGroupElementArray(PoSCCommitment.getAt(2)
				.toByteArray(), Gq);
		if (tempB.getSize() != N)
			return false;
		PoSCCommitment.setAt(2, tempB);

		// Create the PoSCReply in the same way
		PoSCReply = new Node(bPoSCReply);

		// Read Ka, Kc, Kd as RingElements
		// Ka
		IntegerRingElement tempR = new IntegerRingElement(
				ElementsExtractor.leafToInt(PoSCReply.getAt(0).toByteArray()),
				Zq);
		PoSCReply.setAt(0, tempR);

		// Kc
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSCReply
				.getAt(2).toByteArray()), Zq);
		PoSCReply.setAt(2, tempR);

		// Kd
		tempR = new IntegerRingElement(ElementsExtractor.leafToInt(PoSCReply
				.getAt(3).toByteArray()), Zq);
		PoSCReply.setAt(3, tempR);

		// Read Kb and Ke as arrays of Ring Elements, and verify if they are of
		// size N
		// Kb
		ArrayOfElements<IntegerRingElement> tempK = ArrayGenerators
				.createRingElementArray(PoSCReply.getAt(1).toByteArray(), Zq);
		if (tempK.getSize() != N)
			return false;
		PoSCReply.setAt(1, tempK);

		// Ke
		tempK = ArrayGenerators.createRingElementArray(PoSCReply.getAt(4)
				.toByteArray(), Zq);
		if (tempK.getSize() != N)
			return false;
		PoSCReply.setAt(4, tempK);

		return true;
	}

	/**
	 * This method reads the relevant files for the i'th mix server. It sets the
	 * global fields, and the main function will send them to the Shuffling of
	 * commitments prover.
	 * 
	 * @param i
	 *            - mix server index
	 * @param directory
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

		byte[] bCCPoSCommitment = ElementsExtractor.btFromFile(directory,
				"proofs", "CCPoSCommitment" + (i < 10 ? "0" : "") + i + ".bt");
		if (bCCPoSCommitment == null)
			return false;

		byte[] bCCPoSReply = ElementsExtractor.btFromFile(directory, "proofs",
				"CCPoSReply" + (i < 10 ? "0" : "") + i + ".bt");
		if (bCCPoSReply == null)
			return false;

		/*
		 * The following steps create the objects from the byte[] and set the
		 * global fields, that will be sent to the provers.
		 */

		// If i==1 it means that Liminus1 = L0, as we did in the main loop
		if (i != 1)
			Liminus1 = Li;
		Li = ArrayGenerators.createArrayOfCiphertexts(bLi, Gq, width);
		if (Li.getSize() != N)
			return false;

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
		if (tempK.getSize() != N)
			return false;
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
		Li = ArrayGenerators.createArrayOfCiphertexts(bLi, Gq, width);
		if (Li.getSize() != N)
			return false;

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
		if (tempB.getSize() != N)
			return false;
		PoSCommitment.setAt(0, tempB);

		// B'
		tempB = ArrayGenerators.createGroupElementArray(PoSCommitment.getAt(2)
				.toByteArray(), Gq);
		if (tempB.getSize() != N)
			return false;
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
		if (tempK.getSize() != N)
			return false;
		PoSReply.setAt(1, tempK);

		// Ke
		tempK = ArrayGenerators.createRingElementArray(PoSReply.getAt(4)
				.toByteArray(), Zq);
		if (tempK.getSize() != N)
			return false;
		PoSReply.setAt(4, tempK);

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
