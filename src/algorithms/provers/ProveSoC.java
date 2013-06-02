package algorithms.provers;

import main.Logger;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of the
 * shuffle of commitments.
 * 
 * @author Tagel
 */
public class ProveSoC extends Prover {

	/**
	 * This is the main function of this class which executes the shuffle of
	 * commitments algorithm.
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
	 * @param permutationCommitment
	 *            commitment to a permutation
	 * @param PoSCommitment
	 *            “Proof commitment” of the proof of a shuffle
	 * @param PoSReply
	 *            “Proof reply” of the proof of a shuffle
	 * @param h
	 *            Random array
	 * @return true if the proof of shuffle of commitments was correct and false
	 *         otherwise.
	 */
	public static boolean prove(RandomOracle ROSeed, RandomOracle ROChallenge,
			byte[] ro, int N, int Ne, int Nr, int Nv,
			PseudoRandomGenerator prg, IGroup Gq,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply, ArrayOfElements<IGroupElement> h, Logger logger) {

		logger.sendLog("Starting to prove the shuffling of commitment",Logger.Severity.NORMAL);
		
		/*
		 * 1(a) - interpret permutationCommitment (miu) as an array of Pedersen
		 * commitments in Gq
		 */
		ArrayOfElements<IGroupElement> u = permutationCommitment;

		/*
		 * 1(b) - interpret Tpos as Node(B,A',B',C',D')
		 */
		// creating B,A',B',C',D'
		@SuppressWarnings("unchecked")
		ArrayOfElements<IGroupElement> B = (ArrayOfElements<IGroupElement>) (PoSCommitment
				.getAt(0));
		IGroupElement Atag = (IGroupElement) PoSCommitment.getAt(1);
		@SuppressWarnings("unchecked")
		ArrayOfElements<IGroupElement> Btag = (ArrayOfElements<IGroupElement>) (PoSCommitment
				.getAt(2));
		IGroupElement Ctag = (IGroupElement) PoSCommitment.getAt(3);
		IGroupElement Dtag = (IGroupElement) PoSCommitment.getAt(4);

		/*
		 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke)
		 */
		IntegerRingElement Ka = (IntegerRingElement) PoSReply.getAt(0);
		@SuppressWarnings("unchecked")
		ArrayOfElements<IntegerRingElement> Kb = (ArrayOfElements<IntegerRingElement>) (PoSReply
				.getAt(1));
		IntegerRingElement Kc = (IntegerRingElement) PoSReply.getAt(2);
		IntegerRingElement Kd = (IntegerRingElement) PoSReply.getAt(3);
		@SuppressWarnings("unchecked")
		ArrayOfElements<IntegerRingElement> Ke = (ArrayOfElements<IntegerRingElement>) (PoSReply
				.getAt(4));

		/*
		 * 2 - computing the seed
		 */
		IGroupElement g = Gq.getGenerator();
		Node nodeForSeed = new Node();
		nodeForSeed.add(g);
		nodeForSeed.add(h);
		nodeForSeed.add(u);
		byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

		/*
		 * 3 - Computation of A
		 */
		IGroupElement A = computeA(N, Ne, seed, prg, u, Gq);

		/*
		 * 4 - Computation of the challenge
		 */
		ByteTree leaf = new BigIntLeaf(new LargeInteger(seed));
		//ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));

		byte[] challenge = computeChallenge(ROChallenge, ro, PoSCommitment,
				leaf);

		LargeInteger v = computeV(Nv, challenge);

		/*
		 * 5 - Compute C,D and verify equalities
		 */
		// TODO : LargeInteger E = computeE(N, Ne, seed, prg);
		IGroupElement C = computeC(u, h, N);
		IGroupElement D = computeD(B, h, N, Ne, seed, prg);
		
		//TODO: printouts
		
		System.out.println("B : "+B);
		System.out.println("A' : "+Atag);
		System.out.println("B' : "+Btag);
		System.out.println("C': "+Ctag);
		System.out.println("D' : "+Dtag);
		System.out.println("Ka : "+Ka);
		System.out.println("Kb : "+Kb);
		System.out.println("Kc : "+Kc);
		System.out.println("Kd : "+Kd);
		System.out.println("Ke : "+Ke);
		System.out.println("g : "+g);
		System.out.println("bt(g) : "+bytArrayToHex(g.toByteArray()));
		System.out.println("h : "+h);
		System.out.println("bt(h) : "+bytArrayToHex(h.toByteArray()));
		System.out.println("u : "+u);
		System.out.println("bt(u) : "+bytArrayToHex(u.toByteArray()));
		System.out.println("node(g,h,u) : "+bytArrayToHex(nodeForSeed.toByteArray()));
		System.out.println("ro : "+bytArrayToHex(ro));
		System.out.println("seed : "+bytArrayToHex(seed));
		System.out.println("A : "+A);
		System.out.println("v "+v);
		System.out.println("C : " + C);
		System.out.println("D : " + D);
		

		/*
		 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
		 */
		if (!verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka)) {
			return false;
		}

		/*
		 * Equation 2: (B[i]^v) * Btag[i] = (g^Kb[i]) * (B[i-1]^Ke[i]), where
		 * B[-1] = h[0]
		 */
		if (!verifyBvBtag(B, Btag, Kb, Ke, g, v, h, N)) {
			return false;
		}

		/*
		 * Equation 3: (C^v)*Ctag = g^Kc
		 */
		if (!verifyCvCtag(C, Ctag, v, Kc, g)) {
			return false;
		}

		/*
		 * Equation 4: (D^v)*Dtag = g^Kd
		 */
		if (!verifyDvDtag(D, Dtag, v, Kd, g)) {
			return false;
		}

		/* All equalities exist. */
		logger.sendLog("Shuffle of commitments proof succeeded",
				Logger.Severity.NORMAL);
		return true;
	}
	
	// TODO printout method - delete?
		static String bytArrayToHex(byte[] a) {
			StringBuilder sb = new StringBuilder();
			for (byte b : a)
				sb.append(String.format("%02x", b & 0xff));
			return sb.toString();
		}
}
