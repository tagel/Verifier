package algorithms.provers;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of the
 * decryption algorithm.
 * 
 * @author Tagel
 */
public class ProveDec extends Prover {

	/**
	 * @param pk
	 *            The public key
	 * @param wInput
	 *            Array of input ciphertexts
	 * @param wOutput
	 *            Array of output ciphertexts
	 * @param width
	 *            width of plaintexts and ciphertexts
	 */

	/**
	 * This is the main function of this class which executes the decryption
	 * algorithm.
	 * 
	 * @param ROSeed
	 *            RandomOracle for computing the seed
	 * @param ROChallenge
	 *            RandomOracle for computing the challenge
	 * @param j
	 *            index of proof to verify
	 * @param ro
	 *            prefix to random oracleF
	 * @param N
	 *            size of the arrays
	 * @param Ne
	 *            number of bits in each component
	 * @param Nr
	 *            Acceptable "statistical error" when deriving independent
	 *            generators
	 * @param Nv
	 *            Number of bits in the challenge
	 * @param prg
	 *            Pseudo-random generator used to derive random vectors for
	 *            batching
	 * @param Gq
	 *            group of prime order
	 * @param g
	 *            standard generator of the group
	 * @param y
	 *            Partial public keys
	 * @param wInput
	 * @param m
	 * @param decryptionFactors
	 * @param decrFactCommitments
	 * @param decrFactReplies
	 * @return true if we accept the proof and false otherwise
	 */
	public static boolean prove(
			RandomOracle ROSeed,
			RandomOracle ROChallenge,
			int j,
			byte[] ro,
			int N,
			int Ne,
			int Nr,
			int Nv,
			PseudoRandomGenerator prg,
			IGroup Gq,
			IGroupElement g,
			ArrayOfElements<IGroupElement> y,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> m,
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> decryptionFactors,
			ArrayOfElements<Node> decrFactCommitments,
			ArrayOfElements<IntegerRingElement> decrFactReplies) {

		try {

			/**
			 * 1(a) - interpret Tdec as Node(yl',B')
			 */
			Node decCommitment = decrFactCommitments.getAt(j);

			IGroupElement yltag = (IGroupElement) decCommitment.getAt(0);
			ProductRingElement Bltag = (ProductRingElement) decCommitment
					.getAt(1);

			/**
			 * 1(b) - interpret Odec as ByteTree(Klx)
			 */
			IntegerRingElement klx = decrFactReplies.getAt(j);

			/**
			 * 2 - computing the seed
			 */
			StringLeaf stringLeaf = new StringLeaf("generators");
			byte[] independentSeed = ROSeed
					.getRandomOracleOutput(ArrayGenerators.concatArrays(ro,
							stringLeaf.toByteArray()));

			Node leftNode = new Node();
			leftNode.add(g);
			leftNode.add(wInput);

			Node rightNode = new Node();
			Node a = new Node(y.toByteArray());
			Node b = new Node(decryptionFactors.toByteArray());
			rightNode.add(a);
			rightNode.add(b);

			Node nodeForSeed = new Node();
			nodeForSeed.add(leftNode);
			nodeForSeed.add(rightNode);

			byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

			/**
			 * 3 - Computation of e
			 */

			LargeInteger[] e = new LargeInteger[N];
			int length = 8 * ((int) Math.ceil((double) (Ne / 8)));
			prg.setSeed(seed);
			byte[] ByteArrToBigInt;
			LargeInteger t;

			for (int i = 0; i < N; i++) {
				ByteArrToBigInt = prg.getNextPRGOutput(length);
				t = ElementsExtractor.leafToInt(ByteArrToBigInt);
				e[i] = t.mod(new LargeInteger("2").power(Ne));
			}

			/**
			 * 4 - Computation of the challenge
			 */
			ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));
			Node decCommitmentsNode = new Node(decCommitment.toByteArray());
			Node nodeForChallenge = new Node();
			nodeForChallenge.add(leaf);
			nodeForChallenge.add(decCommitmentsNode);

			byte[] challenge = ROChallenge
					.getRandomOracleOutput(ArrayGenerators.concatArrays(ro,
							nodeForChallenge.toByteArray()));

			/* Computation of v: */
			LargeInteger v = new LargeInteger(challenge);
			LargeInteger twoNv = new LargeInteger("2").power(Nv);
			v = v.mod(twoNv);

			/**
			 * 5 - Compute A and B
			 */
			ArrayOfElements<ProductGroupElement> u = new ArrayOfElements<ProductGroupElement>();
			for (int i = 0; i < wInput.getSize(); i++) {
				u.add(wInput.getAt(i).getLeft());
			}

			ProductGroupElement left = u.getAt(0).power(e[0]);
			for (int i = 1; i < N; i++) {
				left = left.mult(u.getAt(i).power(e[i]));
			}

			ArrayOfElements<IGroupElement> arrOfOnes = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < wInput.getSize(); i++) {
				arrOfOnes.add(Gq.one());
			}
			ProductGroupElement ones = new ProductGroupElement(arrOfOnes);
			ProductGroupElement A = new ProductGroupElement(left, ones);

			if (j == 0) {
				/*
				 * compute B = PI((PI(fli)^ei) and accept if PI(yl)^v*PI(yltag)
				 * == g^SIGMA(klx) and B^v*PI(Bltag) == PDec(A)
				 */
				// TODO fl is an array, so we need to multiply fj's elements
				IGroupElement B = Gq.one();
				for (int i = 0; i < N; i++) {
					IGroupElement fl = Gq.one();
					for (int l = 0; i < decryptionFactors.getSize(); i++) {
						// fl = fl.mult(f.getAt(0).getAt(l));
					}
					B = B.mult(fl.power(e[i]));
				}

			} else if (j > 0) {

			} else {
				return false;
			}

			/* All equalities exist. */
			return true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
