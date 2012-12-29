package algorithms;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BooleanArrayElement;
import arithmetic.objects.FieldElement;
import arithmetic.objects.GroupElement;
import arithmetic.objects.Node;

/**
 * This class describes an object that contains the parameters for a specific l'th mix-server (0 <= l <= k)
 * @author Tagel & Sofi
 *
 */
public class MixParams {
	
	private GroupElement publicKey;
	public void setPublicKey(GroupElement publicKey) {
		this.publicKey = publicKey;
	}

	public void setSecretKey(FieldElement secretKey) {
		this.secretKey = secretKey;
	}
	private FieldElement secretKey;
	
	private ArrayOfElements<GroupElement> ciphertexts;
	private ArrayOfElements<GroupElement> permutationCommitment;
	private Node poSCommitment;
	private Node poSReply;
	private Node poSCCommitment;
	private Node poSCReply;
	private Node ccPosCommitment;
	private Node ccPosReply;
	
	private BooleanArrayElement keepList;
	private ArrayOfElements<GroupElement> decryptionFactors;
	private Node decrFactCommitment;
	private Node decrFactReply;

	// getters
	/**
	 * @return the public key
	 */
	public GroupElement getPublicKey(){
		return publicKey;
	}

	/**
	 * @return the secret key
	 */
	public FieldElement getSecretKey(){
		return secretKey;
	}
	
	/**
	 * @return the ciphertext
	 */
	public ArrayOfElements<GroupElement> getCiphertexts(){
		return ciphertexts;
	}
	
	/**
	 * @return the commitment to a permutation
	 */
	public ArrayOfElements<GroupElement> getPermutationCommitment(){
		return permutationCommitment;
	}
	
	/**
	 * @return the "proof commitment" of the proof of a shuffle of commitments
	 */
	public Node getPoSCommitment() {
		return poSCommitment;
	}
	
	/**
	 * @return the "proof reply" of the proof of a shuffle of commitments
	 */
	public Node getPoSReply() {
		return poSReply;
	}
	
	/**
	 * @return the keep-list
	 */
	public BooleanArrayElement getKeepList() {
		return keepList;
	}

	/**
	 * @return the "proof commitment" of the commitment-consistent proof of a shuffle
	 */
	public Node getCcPosComminment() {
		return ccPosCommitment;
	}
	
	/**
	 * @return the "proof reply" of the commitment-consistent proof of a shuffle
	 */
	public Node getCcPosReply() {
		return ccPosReply;
	}
	
	/**
	 * @return the decryption factors of the l'th mix-server used to jointly decrypt the shuffled ciphertexts
	 */
	public ArrayOfElements<GroupElement> getDecryptionFactors() {
		return decryptionFactors;
	}
	
	/**
	 * @return the "proof reply" of the proof of correctness of the decryption factors
	 */	
	public Node getDecrFactReply() {
		return decrFactReply;
	}
	/**
	 * @return the "proof commitment" of the proof of correctness of the decryption factors
	 */	
	public Node getDecrFactCommitment() {
		return decrFactCommitment;
	}

}
