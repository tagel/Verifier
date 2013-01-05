package algorithms;

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
import arithmetic.objects.ProductGroupElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel
 */
public class ProveShuffling {

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
			IGroup Rw,
			IGroup Cw,
			ProductGroupElement pk,
			ArrayOfElements<IGroupElement> wInput,
			ArrayOfElements<IGroupElement> wOutput,
			ArrayOfElements<ArrayOfElements<IGroupElement>> permutationCommitment,
			Node PoSCommitment, Node PoSReply) {

		try {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			// TODO: what does u contains?
			ArrayOfElements<IGroupElement> u = new ArrayOfElements<IGroupElement>(permutationCommitment);

			/**
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
			 */
			ByteTree[] PosCommitmentArr = PoSCommitment.getChildrenArray();

			// creating B,A',B',C',D',F'

			// TODO: check how to interpret B,B'
			ArrayOfElements<IGroupElement> B = new ArrayOfElements<IGroupElement>(PosCommitmentArr[0]);
			ArrayOfElements<IGroupElement> Btag = new ArrayOfElements<IGroupElement>(PosCommitmentArr[2]);

			IGroupElement Atag = ElementsExtractor.createGroupElement(
					PosCommitmentArr[1], Gq);
			IGroupElement Ctag = ElementsExtractor.createGroupElement(
					PosCommitmentArr[3], Gq);
			IGroupElement Dtag = ElementsExtractor.createGroupElement(
					PosCommitmentArr[4], Gq);
			IGroupElement Ftag = ElementsExtractor.createGroupElement(
					PosCommitmentArr[5], Gq);

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
			 */
			ByteTree[] PosReplyArr = PoSReply.getChildrenArray();
			BigInteger q = Gq.getFieldOrder();
			IField<IntegerFieldElement> Zq = new PrimeOrderField(q);
			IntegerFieldElement Ka = new IntegerFieldElement(
					ElementsExtractor.leafToInt(PosReplyArr[0].toByteArray()), Zq);
			IntegerFieldElement Kc = new IntegerFieldElement(
					ElementsExtractor.leafToInt(PosReplyArr[2].toByteArray()), Zq);
			IntegerFieldElement Kd = new IntegerFieldElement(
					ElementsExtractor.leafToInt(PosReplyArr[3].toByteArray()), Zq);
			IGroupElement Kf = ElementsExtractor.createGroupElement(
					PosReplyArr[5], Gq);

			ArrayOfElements<IntegerFieldElement> Kb = new ArrayOfElements<IntegerFieldElement>(PosReplyArr[1]);

			ArrayOfElements<IntegerFieldElement> Ke = new ArrayOfElements<IntegerFieldElement>(PosReplyArr[4]);

			/**
			 * 2 - computing the seed
			 */
			IGroupElement g = Gq.getGenerator();
			// TODO: ask Tomer what are h,u
			Node nodeForSeed = new Node(g, h, u, pk, wInput, wOutput);
			// Computation of the seed:
			byte[] seed = ROSeed.getRandomOracleOutput(ElementsExtractor
					.concatArrays(ro, nodeForSeed.toByteArray()));

			/**
			 * 3 - Computation of A and F
			 */
			
			// int length = (int) Math.ceil((double)(Ne/8));
			// IGroupElement pow = BigInteger.valueOf(8 * length);
			//
			// IGroupElement [] e = computeRandomArray(seed, Ne, prg, N, pow,
			// BigInteger.valueOf(-1), BigInteger.valueOf(Ne));
			//
			// //Computation of A:
			// IGroupElement A = u.getElementForIndex(0).power(e[0]);
			// for (int i = 1; i < N; i++)
			// A = A.multiply(u.getElementForIndex(i).power(e[i]));

			/**
			 * 4 - Computation of the challenge
			 */
			// TODO: where does s comes from?
			ByteTree leaf = new BigIntLeaf(ElementsExtractor.leafToInt(seed));

			ByteTree nodeForChallenge = new Node(null);
			nodeForChallenge.add(leaf);
			nodeForChallenge.add(PoSCommitment);

			byte[] challenge = ROChallenge
					.getRandomOracleOutput(ElementsExtractor.concatArrays(ro,
							leaf.toByteArray()));

			/* Computation of v: */
			BigInteger v = new BigInteger(challenge);
			BigInteger twoNv = BigInteger.valueOf(2).pow(Nv);
			v = v.mod(twoNv);

			/**
			 * 5 - Compute C,D and verify equalities
			 */
			// Computation of C and D
			IGroupElement CNumerator = u.getAt(0);
			IGroupElement CDenominator = h.getElementAt(0);
			BigInteger DExponent = e[0];
			for (int i = 1; i < N; i++) {
				CNumerator = CNumerator.mult(u.getAt(i));
				CDenominator = CDenominator.mult(h.getElementAt(i));
				DExponent = DExponent.multiply(e[i]);
			}
			IGroupElement C = CNumerator.divide(CDenominator);
			IGroupElement D = B.getAt(N - 1).divide(
					h.getElementAt(0).power(DExponent));

			// Equality 1:
			// A^v*A' = (g^ka)*Pi(h[i]^ke[i]):
			IGroupElement left = (A.power(v)).mult(Atag);
			IGroupElement hPi = h.getElementAt(0).power(Ke.getAt(0));
			for (int i = 1; i < N; i++) {
				hPi = hPi.mult(h.getElementAt(i).power(Ke.getAt(i)));
			}
			IGroupElement right = g.power(Ka.getElement()).mult(hPi);
			if (left.compareTo(right) != 0) {
				return false;
			}

			// Equality 2:
			// (B[i]^v)*Btag[i] = (g^Kb[i])*(B[i-1]^Ke[i]), where B[-1] = h[0]:
			left = ((B.getAt(0)).power(v)).mult(Btag.getAt(0));
			right = g.power(Kb.getAt(0)).mult(
					h.getElementAt(0).power(Ke.getAt(0)));
			if (left.compareTo(right) != 0) {
				return false;
			}
			for (int i = 1; i < N; i++) {
				left = (B.getAt(i)).power(v).mult(Btag.getAt(i));
				right = g.power(Kb.getAt(i)).mult(
						B.getAt(i - 1).power(Ke.getAt(i)));
			}
			if (left.compareTo(right) != 0) {
				return false;
			}

			// Equality 3:
			// (C^v)*Ctag = g^Kc:
			left = (C.power(v)).mult(Ctag);
			right = g.power(Kc);
			if (left.compareTo(right) != 0) {
				return false;
			}

			// Equality 4: 
			// (D^v)*Dtag = g^Kd:
			left = (D.power(v)).mult(Dtag);
			right = g.power(Kd);
			if (left.compareTo(right) != 0)
				return false;

			/* All equalities exist. */
			return true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
