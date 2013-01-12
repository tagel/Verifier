package algorithms.provers;

import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
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

			IGroupElement Atag = (IGroupElement)PoSCommitment.getAt(1);
					
			IGroupElement Ctag = (IGroupElement)PoSCommitment.getAt(3);
			IGroupElement Dtag = (IGroupElement)PoSCommitment.getAt(4);
			ProductGroupElement Ftag = (ProductGroupElement) PoSCommitment.getAt(5);

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
			 */
			IntegerFieldElement Ka = (IntegerFieldElement) PoSReply.getAt(0);
			IntegerFieldElement Kc = (IntegerFieldElement) PoSReply.getAt(2);
			IntegerFieldElement Kd = (IntegerFieldElement)PoSReply.getAt(3);
			ProductRingElement Kf = (ProductRingElement)PoSReply.getAt(5);
			

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
