package algorithms;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.FieldElement;
import arithmetic.objects.IGroup;

public class VerKeys {

	private MixParams mixParams;
	
	public ArrayOfElements verifyKeys(int lambda, IGroup Gq) {
		MixParams [] mixServers = new MixParams [lambda];
		
		// Joint public key
		ProductGroupElement pk = params.getFullPublicKey();		
		if (pk == null) {
			// TODO : tagel remove me!
			return null;
		}
		
		// Public keys
		//TODO: attempt to read y1...ylambda
		int [] publicKeys = new int [lambda]; //y1,y2,...,yl

		int mult = 1;
		for (int l = 0; l < lambda; l++) {
			// TODO: need a helpFunc group to BigInt
			publicKeys[l] = mixParams.getPublicKey();
			mult *= publicKeys[l];
		}
		// TODO: understand who us y, where does it come from
		if (y != mult) {
			return null;
		}
		
		// Secret keys
		int [] secretKeys = new int [lambda]; //x1,x2,...,xl
		for (int l = 0; l < lambda; l++) {
			FieldElement secretKeyFile = mixServers[lambda].getSecretKey();
			if (secretKeyFile != null && ){
				return null;
				
			}
		}
	
	}
	
	
}
