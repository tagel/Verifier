package algorithms;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.IField;
import arithmetic.objects.IGroup;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.Node;
import arithmetic.objects.PrimeOrderField;
import arithmetic.objects.ProductElement;
import arithmetic.objects.ProductGroupElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of
 * re-encryption and permutation of the input ciphertexts.
 * 
 * @author Tagel & Sofi
 */
public class ProveShuffling {

	private RandomOracle ROSeed;
	private RandomOracle ROChallenge;

	/**
	 * @return true if the proof of shuffle was correct and false otherwise.
	 */
	public ProveShuffling(RandomOracle ROSeed, RandomOracle ROChallenge) {
		this.ROSeed = ROSeed;
		this.ROChallenge = ROChallenge;
	}

	public boolean prove(
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
			 * 1(b) - interpret Tpos as Node(B,A',B',C',D',F')
			 */
			ByteTree [] PosCommitmentArr = PoSCommitment.getChildrenArray();
	
			// creating B,A',B',C',D',F'
			IGroupElement [] BRaw = new IGroupElement[N];
			IGroupElement [] BTagRaw = new IGroupElement[N];
			
			// TODO: check how to interpret B,B'
			for (int i = 0; i < N i++) {
				BRaw[i] = ElementsExtractor.createIGroupElement(PosCommitmentArr[0], Gq);
				BTagRaw[i] = ElementsExtractor.createGroupElement(PosCommitmentArr[2], Gq);
			}
			
			ArrayOfElements<IGroupElement> B = new ArrayOfElements<IGroupElement>(BRaw);
			ArrayOfElements<IGroupElement> Btag = new ArrayOfElements<IGroupElement>(BTagRaw);
			
			IGroupElement Atag = ElementsExtractor.createGroupElement(PosCommitmentArr[1], Gq);
			IGroupElement Ctag = ElementsExtractor.createGroupElement(PosCommitmentArr[3], Gq);
			IGroupElement Dtag = ElementsExtractor.createGroupElement(PosCommitmentArr[4], Gq);
			IGroupElement Ftag = ElementsExtractor.createGroupElement(PosCommitmentArr[5], Gq);
			 
			/** 
			 * 1(c) - interpret Opos as Node(Ka,Kb,Kc,Kd,Ke,Kf)
			 */
			ByteTree [] PosReplyArr = PoSReply.getChildrenArray();
			BigInteger q = Gq.getFieldOrder();
			IField<IntegerFieldElement> Zq = new PrimeOrderField(q);
			IntegerFieldElement Ka = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[0].toByteArray()),Zq);
			IntegerFieldElement Kc = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[2].toByteArray()),Zq);
			IntegerFieldElement Kd = new IntegerFieldElement (ElementsExtractor.leafToInt(PosReplyArr[3].toByteArray()),Zq);
			IGroupElement Kf = ElementsExtractor.createGroupElement(PosReplyArr[5], Gq);
			
			IntegerFieldElement [] KbRaw = new IntegerFieldElement[N];
			IntegerFieldElement [] KeRaw = new IntegerFieldElement[N];
			
			// TODO: check how to interpret Ke,Kb
			for (int i = 0; i < N i++) {
				KbRaw[i] = ElementsExtractor.createGroupElement(PosCommitmentArr[0], Gq);
				KeRaw[i] = ElementsExtractor.createGroupElement(PosCommitmentArr[2], Gq);
			}
			
			ArrayOfElements<IntegerFieldElement> Kb = new ArrayOfElements<IntegerFieldElement>(KbRaw);
			ArrayOfElements<IntegerFieldElement> Ke = new ArrayOfElements<IntegerFieldElement>(KeRaw);
			
			/** 
			 * 2 - computing the seed
			 */
			GroupElement g = Gq.getGenerator();
			// TODO: ask Tomer what are h,u
			Node node = new Node(g, h, u, pk, wInput, wOutput);      
            //Computation of the seed:
            //TODO: do I need to know the omplementation of the RO?
			byte[] seed = HashFuncPRGRandomOracle.getRandomOracleOutput(ElementsExtractor.concatArrays(ro, node.toByteArray()));
			
			
			return true;
		}
		catch(Exception e){
               System.err.println(e.getMessage());
               return false;
		}
	}
}
