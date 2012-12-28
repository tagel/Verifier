package algorithms;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.GroupElement;
import arithmetic.objects.ProductElement;

/**
 * This class provides the functionality of verifying the decryption.
 * @author Tagel & Sofi
 */

public class VerDec {

	
	/**
	 * @return true if verification of decryption was successful and false otherwise.
	 */
	public boolean verify(byte[] prefixToRO, int sizeOfArray, int Ne, int Nr, int Nv, String sPRG, String sGq, 
			ArrayOfElements<GroupElement> PlainTextGroup, ArrayOfElements<GroupElement> CiphertextGroup, 
			ProductElement fullPublicKey, GroupElement[] publicKeysOfParties,
			ArrayOfElements<GroupElement> ciphertexts, ArrayOfElements<GroupElement> plaintexts) {
		// TODO Auto-generated method stub
		return false;
	}

}
