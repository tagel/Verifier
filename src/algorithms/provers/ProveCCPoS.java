package algorithms.provers;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of the commitment-consistent proof of a
 * shuffle and is used for a proof of a shuffle for a given permutation
 * commitment.
 * 
 * @author Tagel
 */
public class ProveCCPoS extends Prover {

	/**
	 * This is the main function of this class which executes the shuffling
	 * algorithm.
	 * 
	 * @param ROSeed
	 *            RandomOracle for computing the seed
	 * @param ROChallenge
	 *            RandomOracle for computing the challenge
	 * @param ro
	 *            prefix to random oracle
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
	 *            The group
	 * @param pk
	 *            The public key
	 * @param wInput
	 *            Array of input ciphertexts
	 * @param wOutput
	 *            Array of output ciphertexts
	 * @param width
	 *            width of plaintexts and ciphertexts
	 * @param permutationCommitment
	 *            commitment to a permutation
	 * @param PoSCommitment
	 *            “Proof commitment” of the proof of a shuffle
	 * @param PoSReply
	 *            “Proof reply” of the proof of a shuffle
	 * @param h
	 *            Random array
	 * @return true if the commitment-consistent proof of shuffle was correct
	 *         and false otherwise.
	 */
	public static boolean prove(RandomOracle ROSeed, RandomOracle ROChallenge,
			byte[] ro, int N, int Ne, int Nr, int Nv,
			PseudoRandomGenerator prg, IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> wOutput, int width,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply, ArrayOfElements<IGroupElement> h) {

		/*
		 * 1(a) - interpret permutationCommitment (miu) as an array of Pedersen
		 * commitments in Gq
		 */
		ArrayOfElements<IGroupElement> u = permutationCommitment;

		/*
		 * 1(b) - interpret Tpos as Node(A',B')
		 */
		ProductGroupElement Btag = (ProductGroupElement) PoSCommitment.getAt(5);
		IGroupElement Atag = (IGroupElement) PoSCommitment.getAt(0);

		/*
		 * 1(c) - interpret Opos as Node(Ka,Kb,Ke)
		 */
		IntegerRingElement Ka = (IntegerRingElement) PoSReply.getAt(0);
		ProductRingElement Kb = (ProductRingElement) PoSReply.getAt(1);
		@SuppressWarnings("unchecked")
		ArrayOfElements<IntegerRingElement> Ke = (ArrayOfElements<IntegerRingElement>) (PoSReply
				.getAt(2));

		/*
		 * 2 - computing the seed
		 */
		Node nodeForSeed = computeNodeForSeed(Gq, pk, wInput, wOutput, h, u);
		byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

		/*
		 * 3 - Computation of A
		 */
		IGroupElement A = computeA(N, Ne, seed, prg, u, Gq);

		/*
		 * 4 - Computation of the challenge
		 */
		ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));
		byte[] challenge = computeChallenge(ROChallenge, ro, PoSCommitment,
				leaf);

		LargeInteger v = computeV(Nv, challenge);

		/*
		 * 5 - Compute B and verify equalities
		 */
		ProductGroupElement B = computeF(N, Ne, seed, prg, wInput);

		/*
		 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
		 */
		if (!verifyAvAtag(A, Atag, v, Ke, Gq.getGenerator(), N, h, Ka)) {
			return false;
		}

		/*
		 * Equation 2: B^v*Btag = Enc(1,-Kb) * PI(wOutput[i]^Ke[i])
		 */
		if (!verifyFFtag(N, Gq, pk, wOutput, width, Btag, Kb, Ke, B, v)) {
			return false;
		}

		/* All equalities exist. */
		return true;
	}

	private static Node computeNodeForSeed(IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> wOutput,
			ArrayOfElements<IGroupElement> h, ArrayOfElements<IGroupElement> u) {
		Node nodeForSeed = new Node();
		nodeForSeed.add(Gq.getGenerator());
		nodeForSeed.add(h);
		nodeForSeed.add(u);
		nodeForSeed.add(pk);
		nodeForSeed.add(wInput);
		nodeForSeed.add(wOutput);
		return nodeForSeed;
	}
}
