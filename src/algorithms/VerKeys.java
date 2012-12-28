package algorithms;

import java.util.List;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.ByteTree;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
import arithmetic.objects.ProductElement;

public class VerKeys {
//hi remove me
	private MixParams mixParams;
	private Parameters params;
	
	public Node verifyKeys(int lambda, IGroup Gq) {
		
		
		// read the joint public key
		ProductElement pk = params.getFullPublicKey();
		if (pk == null) {
			// TODO : tagel remove me!
			return null;
		}
		ArrayOfElements<GroupElement> pkList = new pk.toList();
		GroupElement g = pkList.get(0);
		GroupElement y = pkList.get(1);
		
		// read the public keys of the mix servers
		GroupElement yi
	
		
//		MixParams [] mixServers = new MixParams [lambda];		
//		// Joint public key
//		ProductGroupElement pk = params.getFullPublicKey();		
//		if (pk == null) {
//			return null;
//		}
//		
//		// Public keys
//		//TODO: attempt to read y1...ylambda
//		int [] publicKeys = new int [lambda]; //y1,y2,...,yl
//
//		int mult = 1;
//		for (int l = 0; l < lambda; l++) {
//			// TODO: need a helpFunc group to BigInt
//			publicKeys[l] = mixParams.getPublicKey();
//			mult *= publicKeys[l];
//		}
//		// TODO: understand who us y, where does it come from
//		if (y != mult) {
//			return null;
//		}
//		
//		// Secret keys
//		int [] secretKeys = new int [lambda]; //x1,x2,...,xl
//		for (int l = 0; l < lambda; l++) {
//			FieldElement secretKeyFile = mixServers[lambda].getSecretKey();
//			if (secretKeyFile != null && ){
//				return null;
//				
//			}
//		}
//	
		ByteTree [] a = new ByteTree [3];  
		Node retVal = new Node(a);
		return retVal;
	}
	
	
}
