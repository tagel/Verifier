package algorithms.provers;

import java.math.BigInteger;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayGenerators;
import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.BasicElements.BigIntLeaf;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.BasicElements.StringLeaf;
import arithmetic.objects.Field.IField;
import arithmetic.objects.Field.IntegerFieldElement;
import arithmetic.objects.Field.PrimeOrderField;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import arithmetic.objects.Ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel
 */
public class ProveShuffling extends Prover {

	public static boolean prove(
			RandomOracle ROSeed,
			RandomOracle ROChallenge,
			byte[] ro,
			int N,
			int Ne,
			int Nr,
			int Nv,
			PseudoRandomGenerator prg,
			IGroup Gq,
			ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> wOutput,
			int width,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply) {
		

		try {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			ArrayOfElements<IGroupElement> u = permutationCommitment;

			/**
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
			 */
			// creating B,A',B',C',D',F'
			ArrayOfElements<IGroupElement> B = (ArrayOfElements<IGroupElement>)(
					PoSCommitment.getAt(0));
			ArrayOfElements<IGroupElement> Btag = (ArrayOfElements<IGroupElement>)(
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
			
			ProductRingElement Kf = new ProductRingElement(PoSReply.getAt(5));

			ArrayOfElements<IntegerFieldElement> Kb = (ArrayOfElements<IntegerFieldElement>)(
					PoSReply.getAt(1));

			ArrayOfElements<IntegerFieldElement> Ke = (ArrayOfElements<IntegerFieldElement>)(
					PoSReply.getAt(4));

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
			nodeForSeed.add(pk);
			nodeForSeed.add(wInput);
			nodeForSeed.add(wOutput);
			byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

			/**
			 * 3 - Computation of A and F
			 */
			IGroupElement A = computeA(N, Ne, seed, prg, u);
			ProductGroupElement F = computeF(N, Ne, seed, prg, wInput);

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
			BigInteger v = new BigInteger(challenge);
			BigInteger twoNv = BigInteger.valueOf(2).pow(Nv);
			v = v.mod(twoNv);

			/**
			 * 5 - Compute C,D and verify equalities
			 */
			BigInteger E = computeE(N, Ne, seed, prg);
			IGroupElement C = computeC(u, h, N);
			IGroupElement D = computeD(E, B, h, N);

			/*
			 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
			 */
			verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka);

			/*
			 * Equation 2: (B[i]^v) * Btag[i] = (g^Kb[i]) * (B[i-1]^Ke[i]),
			 * where B[-1] = h[0]
			 */
			IGroupElement left = ((B.getAt(0)).power(v)).mult(Btag.getAt(0));
			IGroupElement right = g.power(Kb.getAt(0).getElement()).mult(
					h.getAt(0).power(Ke.getAt(0).getElement()));
			if (!left.equal(right)) {
				return false;
			}

			for (int i = 1; i < N; i++) {
				left = (B.getAt(i)).power(v).mult(Btag.getAt(i));
				right = g.power(Kb.getAt(i).getElement()).mult(
						B.getAt(i - 1).power(Ke.getAt(i).getElement()));
			}
			if (!left.equal(right)) {
				return false;
			}

			/*
			 * Equation 3: F^v*Ftag = Enc(1,-Kf) * PI(wOutput[i]^Ke[i])
			 */
			ProductGroupElement leftF = F.power(v).mult(Ftag);

			ProductGroupElement W = wOutput.getAt(0).power(Ke.getAt(0).getElement());
			for (int i = 1; i < N; i++) {
				W = W.mult(wOutput.getAt(i).power(Ke.getAt(i).getElement()));
			}

			// create ProductGroupElement of 1s
			ArrayOfElements<IGroupElement> arrOfOnes = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < width; i++) {
				arrOfOnes.add(Gq.one());
			}
			
			ProductGroupElement ones = new ProductGroupElement(arrOfOnes);
			ProductGroupElement rigthF = encrypt(ones,Kf,pk,Gq);
			if (!leftF.equal(rigthF)) {
				return false;
			}

			/*
			 * Equation 4: (C^v)*Ctag = g^Kc
			 */
			left = (C.power(v)).mult(Ctag);
			right = g.power(Kc.getElement());
			if (!left.equal(right)) {
				return false;
			}

			/*
			 * Equation 5: (D^v)*Dtag = g^Kd
			 */
			left = (D.power(v)).mult(Dtag);
			right = g.power(Kd.getElement());
			if (!left.equal(right)) {
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
