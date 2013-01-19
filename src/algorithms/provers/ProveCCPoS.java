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
 * This class provides the functionality of proving a shuffle for a given
 * permutation commitment.
 * 
 * @author Tagel
 */
public class ProveCCPoS extends Prover{

	/**
	 * @return true if the commitment-consistent proof of shuffle was correct
	 *         and false otherwise.
	 */
	public static boolean prove(RandomOracle ROSeed, RandomOracle ROChallenge,
			byte[] ro, int N, int Ne, int Nr, int Nv,
			PseudoRandomGenerator prg, IGroup Gq, ProductGroupElement pk,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ProductGroupElement> wOutput, int width,
			ArrayOfElements<IGroupElement> permutationCommitment,
			Node PoSCommitment, Node PoSReply) {

			/**
			 * 1(a) - interpret permutationCommitment (miu) as an array of
			 * Pedersen commitments in Gq
			 */
			ArrayOfElements<IGroupElement> u = permutationCommitment;

			/**
			 * 1(b) - interpret Tpos as Node(A',B')
			 */
			// creating A',B'
			ProductGroupElement Btag = (ProductGroupElement) PoSCommitment
					.getAt(5);
			IGroupElement Atag = (IGroupElement) PoSCommitment.getAt(0);

			/**
			 * 1(c) - interpret Opos as Node(Ka,Kb,Ke)
			 */
			IntegerRingElement Ka = (IntegerRingElement) PoSReply.getAt(0);
			ProductRingElement Kb = (ProductRingElement) PoSReply.getAt(1);
			@SuppressWarnings("unchecked")
			ArrayOfElements<IntegerRingElement> Ke = (ArrayOfElements<IntegerRingElement>) (PoSReply
					.getAt(2));

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
			 * 5 - Compute B and verify equalities
			 */
			ProductGroupElement B = computeF(N, Ne, seed, prg, wInput);
			
			/*
			 * Equation 1: A^v * Atag = (g^ka) * PI(h[i]^ke[i])
			 */
			if (!verifyAvAtag(A, Atag, v, Ke, g, N, h, Ka)) {
				return false;
			}

			/*
			 * Equation 2: B^v*Btag = Enc(1,-Kb) * PI(wOutput[i]^Ke[i])
			 */
			ProductGroupElement leftB = B.power(v).mult(Btag);

			ProductGroupElement W = wOutput.getAt(0).power(
					Ke.getAt(0).getElement());
			for (int i = 1; i < N; i++) {
				W = W.mult(wOutput.getAt(i).power(Ke.getAt(i).getElement()));
			}

			// create ProductGroupElement of 1s
			ArrayOfElements<IGroupElement> arrOfOnes = new ArrayOfElements<IGroupElement>();
			for (int i = 0; i < width; i++) {
				arrOfOnes.add(Gq.one());
			}

			ProductGroupElement ones = new ProductGroupElement(arrOfOnes);
			ProductGroupElement rigthB = encrypt(ones, Kb, pk, Gq);
			if (!leftB.equals(rigthB)) {
				return false;
			}

			/* All equalities exist. */
			return true;

	}
}
