package algorithms;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IField;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
import arithmetic.objects.ProductElement;
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
			byte[] ro, int N, 
			BigInteger Ne, BigInteger Nr, BigInteger Nv,
			PseudoRandomGenerator prg,
			IGroup Gq, int Rw, int Cw,
			ProductElement pk,
			BigInteger wInput, BigInteger wOutput,
			ArrayOfElements<ArrayOfElements<GroupElement>> permutationCommitment,
			ArrayOfElements<Node> PoSCommitment, ArrayOfElements<Node> PoSReply) {
	
			BigInteger q = Gq.getFieldOrder();
            IField Zq = new Field(q);
            
            //GroupElement ATag = new ModGroupElement(PoSCommitment.
            
			
		return false;
	}

}
