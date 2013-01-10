package algorithms.provers;

import java.math.BigInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.BasicElements.BigIntLeaf;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.Field.IField;
import arithmetic.objects.Field.IntegerFieldElement;
import arithmetic.objects.Field.PrimeOrderField;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
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
			ArrayOfElements<ArrayOfElements<IGroupElement>> permutationCommitment,
			Node PoSCommitment, Node PoSReply, ArrayOfElements<IGroupElement> h) {

		try {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			// TODO: what does u contains?
			ArrayOfElements<IGroupElement> u = new ArrayOfElements<IGroupElement>(
					permutationCommitment);

			/**
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D')
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

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke)
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

			ArrayOfElements<IntegerFieldElement> Kb = new ArrayOfElements<IntegerFieldElement>(
					PoSReply.getAt(1));

			ArrayOfElements<IntegerFieldElement> Ke = new ArrayOfElements<IntegerFieldElement>(
					PoSReply.getAt(4));

			/**
			 * 2 - computing the seed
			 */

			IGroupElement g = Gq.getGenerator();
			Node nodeForSeed = new Node();
			nodeForSeed.add(g);
			nodeForSeed.add(h);
			nodeForSeed.add(u);
			byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

			/**
			 * 3 - Computation of A
			 */

			//TODO: computeE ?!
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
			
			return true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
