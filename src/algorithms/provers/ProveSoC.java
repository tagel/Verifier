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
	 * @return true if the proof of shuffle of commitments was correct and false
	 *         otherwise.
	 */
	public static boolean prove(RandomOracle ROSeed, RandomOracle ROChallenge,
			byte[] ro, int N, int Ne, int Nr, int Nv,
			PseudoRandomGenerator prg, IGroup Gq,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply) {

		try {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			ArrayOfElements<IGroupElement> u = permutationCommitment;

			/**
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D')
			 */

			// creating B,A',B',C',D'
			@SuppressWarnings("unchecked")
			ArrayOfElements<IGroupElement> B = (ArrayOfElements<IGroupElement>) (PoSCommitment
					.getAt(0));
			@SuppressWarnings("unchecked")
			ArrayOfElements<IGroupElement> Btag = (ArrayOfElements<IGroupElement>) (PoSCommitment
					.getAt(2));
			IGroupElement Atag = (IGroupElement) PoSCommitment.getAt(1);
			IGroupElement Ctag = (IGroupElement) PoSCommitment.getAt(3);
			IGroupElement Dtag = (IGroupElement) PoSCommitment.getAt(4);

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke)
			 */
			IntegerRingElement Ka = (IntegerRingElement) PoSReply.getAt(0);
			IntegerRingElement Kc = (IntegerRingElement) PoSReply.getAt(2);
			IntegerRingElement Kd = (IntegerRingElement) PoSReply.getAt(3);
			@SuppressWarnings("unchecked")
			ArrayOfElements<IntegerRingElement> Kb = (ArrayOfElements<IntegerRingElement>) (PoSReply
					.getAt(1));
			@SuppressWarnings("unchecked")
			ArrayOfElements<IntegerRingElement> Ke = (ArrayOfElements<IntegerRingElement>) (PoSReply
					.getAt(4));

			/**
			 * 2 - computing the seed
			 */

			StringLeaf stringLeaf = new StringLeaf("generators");
			byte[] independentSeed = ROSeed
					.getRandomOracleOutput(ArrayGenerators.concatArrays(ro,
							stringLeaf.toByteArray()));
			ArrayOfElements<IGroupElement> h = Gq.createRandomArray(N, prg,
					independentSeed, Nr);

			IGroupElement g = Gq.getGenerator();
			Node nodeForSeed = new Node();
			nodeForSeed.add(g);
			nodeForSeed.add(h);
			nodeForSeed.add(u);
			byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

			/**
			 * 3 - Computation of A
			 */
			IGroupElement A = computeA(N, Ne, seed, prg, u, Gq);

			/**
			 * 4 - Computation of the challenge
			 */
			ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));

			Node nodeForChallenge = new Node();
			nodeForChallenge.add(leaf);
			nodeForChallenge.add(PoSCommitment);

			byte[] challenge = ROChallenge
					.getRandomOracleOutput(ArrayGenerators.concatArrays(ro,
							nodeForChallenge.toByteArray()));

			/* Computation of v: */
			LargeInteger v = new LargeInteger(challenge);
			LargeInteger twoNv = new LargeInteger("2").power(Nv);
			v = v.mod(twoNv);

			/**
			 * 5 - Compute C,D and verify equalities
			 */
			LargeInteger E = computeE(N, Ne, seed, prg);
			IGroupElement C = computeC(u, h, N);
			IGroupElement D = computeD(E, B, h, N);

			/*
			 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
			 */
			if (!verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka)) {
				return false;
			}

			/*
			 * Equation 2: (B[i]^v) * Btag[i] = (g^Kb[i]) * (B[i-1]^Ke[i]),
			 * where B[-1] = h[0]
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
			return true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
