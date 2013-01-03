package algorithms;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.IField;
import arithmetic.objects.IGroup;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.Node;
import arithmetic.objects.PrimeOrderField;
import arithmetic.objects.ProductGroupElement;
import arithmetic.objects.StringLeaf;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel & Sofi
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
			 * 1(a) - interpret permutationCommitment (miu) as an array of Pedersen commitments in Gq
			 */
			// TODO: ask Sofi/Tomer how to do this? 
			
			/** 
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
			 */
			ByteTree[] PosCommitmentArr = PoSCommitment.getChildrenArray();

			// creating B,A',B',C',D',F'
//			IGroupElement[] BRaw = new IGroupElement[N];
//			IGroupElement[] BTagRaw = new IGroupElement[N];

			// TODO: check how to interpret B,B'
			ArrayOfElements<IGroupElement> B = ElementsExtractor.createArrayOfElements(PosCommitmentArr[0]);
			ArrayOfElements<IGroupElement> Btag = ElementsExtractor.createArrayOfElements(PosCommitmentArr[2]);
			
			IGroupElement Atag = ElementsExtractor.createGroupElement(PosCommitmentArr[1],Gq);
			IGroupElement Ctag = ElementsExtractor.createGroupElement(PosCommitmentArr[3],Gq);
			IGroupElement Dtag = ElementsExtractor.createGroupElement(PosCommitmentArr[4],Gq);
			IGroupElement Ftag = ElementsExtractor.createGroupElement(PosCommitmentArr[5],Gq);

			/** 
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
			 */
			ByteTree[] PosReplyArr = PoSReply.getChildrenArray();
			BigInteger q = Gq.getFieldOrder();
			IField<IntegerFieldElement> Zq = new PrimeOrderField(q);
			IntegerFieldElement Ka = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[0].toByteArray()),Zq);
			IntegerFieldElement Kc = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[2].toByteArray()),Zq);
			IntegerFieldElement Kd = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[3].toByteArray()),Zq);
			IGroupElement Kf = ElementsExtractor.createGroupElement(PosReplyArr[5]);
			
			ArrayOfElements<IntegerFieldElement> Kb = ElementsExtractor.createArrayOfElements(PosReplyArr[1]);
			ArrayOfElements<IntegerFieldElement> Ke = ElementsExtractor.createArrayOfElements(PosReplyArr[4]);

			/** 
			 * 2 - computing the seed
			 */
			IGroupElement g = Gq.getGenerator();
			// TODO: ask Tomer what are h,u
			Node node = new Node(g, h, u, pk, wInput, wOutput);      
			//Computation of the seed:
			byte[] seed = ROSeed.getRandomOracleOutput(ElementsExtractor.concatArrays(ro, node.toByteArray()));

			/** 
			 * 3 - Computation of A and F
			 */
//			int length = (int) Math.ceil((double)(Ne/8));
//			IGroupElement pow = BigInteger.valueOf(8 * length);
//
//			IGroupElement [] e = computeRandomArray(seed, Ne, prg, N, pow, BigInteger.valueOf(-1), BigInteger.valueOf(Ne));
//
//			//Computation of A:
//			IGroupElement A = u.getElementForIndex(0).power(e[0]);
//			for (int i = 1; i < N; i++)
//				A = A.multiply(u.getElementForIndex(i).power(e[i]));

			/** 
			 * 4 - Computation of the challenge
			 */
            ByteTree leaf = new StringLeaf("generators");
            byte [] e = ROSeed.getRandomOracleOutput(ElementsExtractor.concatArrays(ro, leaf.toByteArray()));
            //IGroupElement v = computeChallenge(ro, seed, tau, nv);
            
			return true;
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			return false;
		}
	}

//	protected IGroupElement computeChallenge(byte[] rho, byte[] seed, ByteTree tau, int nv) throws Exception {
//        /* Computes a challenge, equal to ROv(rho | node(leaf(seed), tau)). */
//        
//        /* Creation of the node concatenated with rho for the computation of v: */
//        ByteTree node = new Node();
//        
//        node.AddMultiChildsToNode(ByteTree.CreateLeaf(seed), tau);
//        
//        /* Computation of v: */
//        byte[] tempRes = ROv.runRandomOracle(concatenate(rho, node));
//        LargeNumber v=new LargeNumber(tempRes);
//        v=v.mod((LargeNumber.TWO.power(new LargeNumber(nv))));
//        return new LargeNumberObject(v);
//	
//	/**
//	 * Computes a random array of elements from the given seed. Input
//	 * description is as follows: seed - the seed set in the prg. bit_length -
//	 * bit length of the prime of the modular group used throughtout the
//	 * algorithm. prg - pseudo-random generator used to compute the random
//	 * array. exp1 - first exponent used in the computation of the random array.
//	 * exp2 - second exponent used in the computation of the random array (set
//	 * to -1 if not needed). n - modulus.
//	 */
//	public IGroupElement[] computeRandomArray(byte[] seed, int bit_length,
//			PseudoRandomGenerator prg, int N0, IGroupElement pow1,
//			IGroupElement pow2, IGroupElement n) throws Exception {
//		int length = (int) Math.ceil((double) bit_length / 8);
//		prg.prg(seed);
//
//		byte[] byteArr = prg.runPRG(length * N0);
//		BigInteger t;
//		int position = 0;
//		byte[] temp = new byte[length];
//		IGroupElement[] randomArray = new IGroupElement[N0];
//		for (int i = 0; i < N0; i++) {
//			System.arraycopy(byteArr, position, temp, 0, length);
//			temp[0] &= (0xFF >> (8 - (bit_length % 8)));
//			position += length;
//			t = BigInteger.valueOf(temp);
//			t = t.mod((LargeNumber.TWO.power(pow1.getValue())));
//			if (pow2.compareTo(BigInteger.valueOf(0)) > 0) { //if exp2 != -1, perform randomArray[i] = (t^exp2) mod n
//				BigInteger a = t.modPow(pow2.getValue(), n.getValue());
//				randomArray[i] = BigInteger.valueOf((a);
//			} else { // if exp2 == -1, perform randomArray[i] = t mod 2^n
//				BigInteger a = t.mod(LargeNumber.TWO.power((n.getValue())));
//				;
//				randomArray[i] = a;
//			}
//		}
//		return randomArray;
//	}
}
