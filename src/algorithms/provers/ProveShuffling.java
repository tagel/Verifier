package algorithms.provers;

import main.Logger;
import arithmetic.objects.ByteTree;
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
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel
 */
public class ProveShuffling extends Prover {

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
	 * @return true if we accept the proof and false otherwise
	 */

	public static boolean prove(RandomOracle ROSeed, RandomOracle ROChallenge,
			byte[] ro, int N, int Ne, int Nr, int Nv,
			PseudoRandomGenerator prg, IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> wOutput, int width,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply,
			ArrayOfElements<IGroupElement> h, Logger logger) {

		logger.sendLog("Starting to prove the shuffling",
				Logger.Severity.NORMAL);

		/*
		 * 1(a) - interpret permutationCommitment (miu) as an array of Pedersen
		 * commitments in Gq
		 */
		ArrayOfElements<IGroupElement> u = permutationCommitment;

		/*
		 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
		 */
		@SuppressWarnings("unchecked")
		ArrayOfElements<IGroupElement> B = (ArrayOfElements<IGroupElement>) (PoSCommitment
				.getAt(0));
		IGroupElement Atag = (IGroupElement) PoSCommitment.getAt(1);
		@SuppressWarnings("unchecked")
		ArrayOfElements<IGroupElement> Btag = (ArrayOfElements<IGroupElement>) (PoSCommitment
				.getAt(2));
		IGroupElement Ctag = (IGroupElement) PoSCommitment.getAt(3);
		IGroupElement Dtag = (IGroupElement) PoSCommitment.getAt(4);
		ProductGroupElement Ftag = (ProductGroupElement) PoSCommitment.getAt(5);

		/*
		 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
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
		ProductRingElement Kf = (ProductRingElement) PoSReply.getAt(5);

		ProductGroupElement pkSeed;
		//Here we check if width > 1 -> if so, 
		//we interpret pk as pk = ((g,...,g),(y,...,y))
		if (width>1) {
			pkSeed = expandPk(pk, width);
		} else {
			pkSeed = pk;
		}
		
		/*
		 * 2 - computing the seed s=ROseed(...)
		 */
		IGroupElement g = Gq.getGenerator();

		ByteTree[] input = new ByteTree[6];
		input[0] = g;
		input[1] = h;
		input[2] = u;
		input[3] = pkSeed;
		input[4] = wInput;
		input[5] = wOutput;
		Node nodeForSeed = new Node(input);
		byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

		// TODO printouts
		 System.out.println("g : " + input[0]);
		 System.out.println("h : " + input[1]);
		 System.out.println("u : " + input[2]);
		 System.out.println("pk : " + input[3]);
		 System.out.println("w : " + input[4]);
		 System.out.println("w' : " + input[5]);
		
		 System.out.println("bt(g) : " +
		 bytArrayToHex(input[0].toByteArray()));
		 System.out.println("bt(h) : " +
		 bytArrayToHex(input[1].toByteArray()));
		 System.out.println("bt(u) : " +
		 bytArrayToHex(input[2].toByteArray()));
		 System.out.println("bt(pk) : " +
		 bytArrayToHex(input[3].toByteArray()));
		 System.out.println("bt(w) : " +
		 bytArrayToHex(input[4].toByteArray()));
		 System.out.println("bt(w') : " +
		 bytArrayToHex(input[5].toByteArray()));
		
		 System.out.println("node(g,q,...) : "+bytArrayToHex(nodeForSeed.toByteArray()));
		 System.out.println("Seed : "+bytArrayToHex(seed));
		
		/*
		 * 3 - Computation of A and F
		 */
		IGroupElement A = computeA(N, Ne, seed, prg, u, Gq);
		ProductGroupElement F = computeF(N, Ne, seed, prg, wInput);

		/*
		 * 4 - Computation of the challenge
		 */
		ByteTree leaf = new BigIntLeaf(new LargeInteger(seed));
		byte[] challenge = computeChallenge(ROChallenge, ro, PoSCommitment,
				leaf);
		LargeInteger v = computeV(Nv, challenge);

		/*
		 * 5 - Compute C,D and verify equalities
		 */
		IGroupElement C = computeC(u, h, N);
		IGroupElement D = computeD(B, h, N, Ne, seed, prg);

		// TODO printouts
		System.out.println("A : " + A);
		System.out.println("F : " + F);
		System.out.println("v : " + v);
		System.out.println("C : " + C);
		System.out.println("D : " + D);
		
		System.out.println("C^v : "+(C.power(v)).mult(Ctag));

		 /*
		 * Equation 1: (B[i]^v) * Btag[i] = (g^Kb[i]) * (B[i-1]^Ke[i]), where
		 * B[-1] = h[0]
		 */
		 if (!verifyBvBtag(B, Btag, Kb, Ke, g, v, h, N)) {
			 return false;
		 }
		
		 /*
		 * Equation 2: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
		 */
		 if (!verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka)) {
			 return false;
		 }
		
		 /*
		 * Equation 3: F^v*Ftag = Enc(1,-Kf) * PI(wOutput[i]^Ke[i])
		 */
		 if (!verifyFFtag(N, Gq, pk, wOutput, width, Ftag, Kf, Ke, F, v)) {
			 return false;
		 }
		
		 /*
		 * Equation 4: (C^v)*Ctag = g^Kc
		 */
		 if (!verifyCvCtag(C, Ctag, v, Kc, g)) {
			 return false;
		 }
		
		 /*
		 * Equation 5: (D^v)*Dtag = g^Kd
		 */
		 if (!verifyDvDtag(D, Dtag, v, Kd, g)) {
		 return false;
		 }

		/* All equalities exist. */
		return true;
	}
	
	/**
	 * 
	 * @param pk=(g,y)
	 * @param width
	 * @return the expanded pk = ((g,...,g),(y,...,y))
	 */
	private static ProductGroupElement expandPk(ProductGroupElement pk, int width) {
		
		ArrayOfElements<IGroupElement> left = new ArrayOfElements<IGroupElement>();
		ArrayOfElements<IGroupElement> right = new ArrayOfElements<IGroupElement>();

		IGroupElement y = pk.getElements().getAt(1);
		IGroupElement g = pk.getElements().getAt(0);
		
		for (int i=0; i<width; i++) {
			left.add(g);
			right.add(y);
		}
		
		ProductGroupElement expandedPk = new ProductGroupElement(
				new ProductGroupElement(left), new ProductGroupElement(right));
		return expandedPk;
	}


	// TODO printout method - delete?
	static String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

}
