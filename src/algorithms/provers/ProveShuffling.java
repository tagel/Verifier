package algorithms.provers;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BigIntLeaf;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.IField;
import arithmetic.objects.IGroup;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.Node;
import arithmetic.objects.PrimeOrderField;
import arithmetic.objects.ProductFieldElement;
import arithmetic.objects.ProductGroupElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel
 */
public class ProveShuffling extends Prover {

	public boolean prove(
			RandomOracle ROSeed,
			RandomOracle ROChallenge,
			byte[] ro,
			int N,
			int Ne,
			int Nr,
			int Nv,
			PseudoRandomGenerator prg,
			IGroup Gq,
			// TODO: what is the type of Rw,Cw??
			IGroup Rw,
			IGroup Cw,
			ProductGroupElement pk,
			ArrayOfElements<IGroupElement> wInput,
			ArrayOfElements<IGroupElement> wOutput,
			ArrayOfElements<ArrayOfElements<IGroupElement>> permutationCommitment,
			Node PoSCommitment, Node PoSReply,
			// TODO: what is h?
			ArrayOfElements<IGroupElement> h) {

		try {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			// TODO: what does u contains?
			ArrayOfElements<IGroupElement> u = new ArrayOfElements<IGroupElement>(
					permutationCommitment);

			/**
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
			 */

			// creating B,A',B',C',D',F'
			ArrayOfElements<IGroupElement> B = new ArrayOfElements<IGroupElement>(
					PoSCommitment.getAt(0));
			ArrayOfElements<IGroupElement> Btag = new ArrayOfElements<IGroupElement>(
					PoSCommitment.getAt(2));

			IGroupElement Atag = ElementsExtractor.createGroupElement(
					PoSCommitment.getAt(1).toByteArray(), Gq);
			IGroupElement Ctag = ElementsExtractor.createGroupElement(
					PoSCommitment.getAt(3).toByteArray(), Gq);
			IGroupElement Dtag = ElementsExtractor.createGroupElement(
					PoSCommitment.getAt(4).toByteArray(), Gq);
			ProductGroupElement Ftag = new ProductGroupElement(
					PoSCommitment.getAt(5));

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
			 */
			BigInteger q = Gq.getFieldOrder();
			IField<IntegerFieldElement> Zq = new PrimeOrderField(q);
			IntegerFieldElement Ka = new IntegerFieldElement(
					ElementsExtractor
							.leafToInt(PoSReply.getAt(0).toByteArray()),
					Zq);
			IntegerFieldElement Kc = new IntegerFieldElement(
					ElementsExtractor
							.leafToInt(PoSReply.getAt(2).toByteArray()),
					Zq);
			IntegerFieldElement Kd = new IntegerFieldElement(
					ElementsExtractor
							.leafToInt(PoSReply.getAt(3).toByteArray()),
					Zq);
			ProductFieldElement Kf = new ProductFieldElement(PoSReply.getAt(5));

			ArrayOfElements<IntegerFieldElement> Kb = new ArrayOfElements<IntegerFieldElement>(
					PoSReply.getAt(1));

			ArrayOfElements<IntegerFieldElement> Ke = new ArrayOfElements<IntegerFieldElement>(
					PoSReply.getAt(4));

			/**
			 * 2 - computing the seed
			 */

			// TODO: ask Tomer what are h,u
			IGroupElement g = Gq.getGenerator();
			Node nodeForSeed = new Node();
			nodeForSeed.add(g);
			nodeForSeed.add(h);
			nodeForSeed.add(u);
			nodeForSeed.add(pk);
			nodeForSeed.add(wInput);
			nodeForSeed.add(wOutput);
			byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

			/**
			 * 3 - Computation of A and F
			 */

			// Computation of e:
			int length = (int) Math.ceil((double) (Ne / 8));
			IntegerFieldElement pow = new IntegerFieldElement(
					BigInteger.valueOf(8 * length), Zq);
			ArrayOfElements<IntegerFieldElement> e = computeE(seed, Ne, prg, N,
					pow, BigInteger.valueOf(-1), BigInteger.valueOf(Ne));

			// Computation of A:
			IGroupElement A = u.getAt(0).power(e.getAt(0).getElement());
			for (int i = 1; i < N; i++) {
				A = A.mult(u.getAt(i).power(e.getAt(i).getElement()));
			}

			// Computation of F:
			IGroupElement F = wInput.getAt(0).power(e.getAt(0).getElement());
			for (int i = 1; i < N; i++) {
				F = F.mult(wInput.getAt(i).power(e.getAt(i).getElement()));
			}

			/**
			 * 4 - Computation of the challenge
			 */
			// TODO: where does s comes from?
			ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));

			Node nodeForChallenge = new Node(null);
			nodeForChallenge.add(leaf);
			nodeForChallenge.add(PoSCommitment);

			byte[] challenge = ROChallenge
					.getRandomOracleOutput(ElementsExtractor.concatArrays(ro,
							nodeForChallenge.toByteArray()));

			/* Computation of v: */
			BigInteger v = new BigInteger(challenge);
			BigInteger twoNv = BigInteger.valueOf(2).pow(Nv);
			v = v.mod(twoNv);

			/**
			 * 5 - Compute C,D and verify equalities
			 */
			IGroupElement C = computeC(u, h, N);
			IGroupElement D = computeD(e, B, h, N);

			/*
			 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
			 */
			// TODO: need a way to compare 2 IGroupElements
			verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka);

			/*
			 * Equation 2: (B[i]^v) * Btag[i] = (g^Kb[i]) * (B[i-1]^Ke[i]),
			 * where B[-1] = h[0]
			 */
			IGroupElement left = ((B.getAt(0)).power(v)).mult(Btag.getAt(0));
			IGroupElement right = g.power(Kb.getAt(0).getElement()).mult(
					h.getAt(0).power(Ke.getAt(0).getElement()));
			// if (left.compareTo(right) != 0) {
			// return false;
			// }

			for (int i = 1; i < N; i++) {
				left = (B.getAt(i)).power(v).mult(Btag.getAt(i));
				right = g.power(Kb.getAt(i).getElement()).mult(
						B.getAt(i - 1).power(Ke.getAt(i).getElement()));
			}
			// if (left.compareTo(right) != 0) {
			// return false;
			// }

			/*
			 * Equation 3: F^v*Ftag = Enc(1-Kf) * PI(wOutput[i]^Ke[i])
			 */

			/*
			 * Equation 4: (C^v)*Ctag = g^Kc
			 */
			left = (C.power(v)).mult(Ctag);
			right = g.power(Kc.getElement());
			// if (left.compareTo(right) != 0) {
			// return false;
			// }

			/*
			 * Equation 5: (D^v)*Dtag = g^Kd
			 */
			left = (D.power(v)).mult(Dtag);
			right = g.power(Kd.getElement());
			// if (left.compareTo(right) != 0) {
			// return false;
			// }

			/* All equalities exist. */
			return true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
