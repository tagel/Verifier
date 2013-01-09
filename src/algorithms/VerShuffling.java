package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Node;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This class provides the functionality of verifying the shuffling.
 * 
 * @author Tagel & Sofi
 */

public class VerShuffling {

	private static ArrayOfElements<ProductGroupElement> Li;
	private static Node PoSCommitment;
	private static Node PoSReply;
	private static ArrayOfElements<IGroupElement> PermutationCommitment;

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
	 * @return true if verification of shuffling was successful and false
	 *         otherwise.
	 * @throws IOException 
	 */
	static public boolean verify(String directory, byte[] prefixToRO,
			int lambda, int N, int ne, int nr, int nv,
			PseudoRandomGenerator prg, IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> L0,
			ArrayOfElements<ProductGroupElement> Llambda, boolean posc,
			boolean ccpos) throws IOException {

		int maxciph = readMaxciph(directory);

		if (maxciph == 0) {
			/*
			 * The file maxciph doesn't exist, so we need to loop over the
			 * different shuffling mix-servers (from 1 to lambda), read from
			 * file the proof of commitment, the proof of reply and the
			 * permutation commitments and send them to the shuffling prover.
			 */

			for (int i = 1; i <= lambda; i++) {
				if (!readFiles(i, directory, Gq))
					return false;
				
			}

		} else {

		}

		return true;
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
	private static boolean readFiles(int i, String directory, IGroup Gq)
			throws IOException {
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
		PermutationCommitment = ElementsExtractor.createArrayOfGroupElements(bPermutationCommitment,Gq);
		Li = ElementsExtractor.createArrayOfCiphertexts(bLi, Gq);
		PoSCommitment = new Node(bPoSCommitment);
		PoSReply = new Node(bPoSReply);
		
		return true;
	}

	/**
	 * This method tries to read maxciph - if the file doesn't exist it catches
	 * the exception and returns "0". If the file exists, etuns the relevent
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
