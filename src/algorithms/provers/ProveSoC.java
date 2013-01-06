package algorithms.provers;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.IGroup;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.Node;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of the
 * shuffle of commitments.
 * 
 * @author Tagel
 */
public class ProveSoC {

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
			Node PoSCommitment, Node PoSReply) {
		
		

		return false;
	}

}
