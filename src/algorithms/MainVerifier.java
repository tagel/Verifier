
package algorithms;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BigIntLeaf;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
import arithmetic.objects.ProductElement;
import arithmetic.objects.StringLeaf;
import cryptographic.primitives.HashFuncPRG;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.SHA2HashFunction;

/**
 * This class describes the required behavior expected from the main verifier.
 * 
 * @author Tagel & Sofi
 */
public class MainVerifier {

	private Parameters params;

	/**
	 * @return true if verification was successful and false otherwise.
	 */
	public boolean verify(String protInfo, String directory, String type,
			String auxid, BigInteger w, boolean posc, boolean ccpos, boolean dec) {

		params = new Parameters(protInfo, directory, type, auxid, w, posc, ccpos, dec);

		if (!isProtocolInfoValid())
			return false;

		// fill the relevant parameters:
		// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
		// (width);
		if (!params.fillFromXML()) {
			return false;
		}
		// fill version_proof(Version) type, auxid, w from proof directory
		if (!params.fillFromDirectory()) {
			return false;
		}

		// params.getVersion() = version_proof in the document
		if (!params.getProtVersion().equals(params.getVersion()))
			return false;

		if (!(params.getType().equals(params.getTypeExpected())))
			return false;

		if (!params.getAuxsid().equals(params.getAuxidExp()))
			return false;

		// The document says that widthExp should be NULL, but here we will only
		// assign 0
		// Document: Code:
		// w_expected w_expected
		// w w
		// w_deafult wDeafult
		if ((params.getWidthExp().intValue() == 0)
				&& (params.getW() != params.getwDefault()))
			return false;

		if ((params.getWidthExp().intValue() != 0)
				&& (params.getW() != params.getWidthExp()))
			return false;

		ElementsExtractor elem = new ElementsExtractor();
		params.setGq(elem.unmarshal(params.getsGq()));

		deriveSetsAndObjects();

		HashFunction H = new SHA2HashFunction(params.getSh());
		PseudoRandomGenerator PRG = new HashFuncPRG(new SHA2HashFunction(
				params.getsPRG()));

		ByteTree version_proof = new StringLeaf(params.getVersion());

		// s = sid|"."|auxid
		String s = params.getSessionID() + "." + params.getAuxsid();

		ByteTree btAuxid = new StringLeaf(s);
		ByteTree sGq = new StringLeaf(params.getsGq());
		ByteTree sPRG = new StringLeaf(params.getsPRG());
		ByteTree sH = new StringLeaf(params.getSh());

		ByteTree Ne = new BigIntLeaf(params.getNe());
		ByteTree Nr = new BigIntLeaf(params.getNr());

		ByteTree Nv = new BigIntLeaf(params.getNv());
		ByteTree btW = new BigIntLeaf(params.getW());

		ByteTree[] input = new ByteTree[9];
		input[0] = version_proof;
		input[1] = btAuxid;
		input[2] = btW;
		input[3] = Ne;
		input[4] = Nr;
		input[5] = Nv;
		input[6] = sGq;
		input[7] = sPRG;
		input[8] = sH;

		Node node = new Node(input);
		byte[] Seed = node.toByteArray();

		params.setPrefixToRO(H.digest((Seed)));
		
		Node retVal = VerKeys.verifyKeys(params.getThreshold(), params.getGq());
		
		//Should this part be generic or what?
		//An array of 3 children: pk, (y1,...,yl), (x1,...,xl)
		ByteTree[] keys = retVal.getChildrenArray();
		ProductElement pk = (ProductElement)keys[0];
		ArrayOfElements<GroupElement> yi = (ArrayOfElements<GroupElement>)keys[1];
		ArrayOfElements<ProductElement> xi = (ArrayOfElements<ProductElement>)keys[2];
		
		AssignToParams(pk,yi,xi);
		
		

		return true;
	}

	private void AssignToParams(ProductElement pk,
			ArrayOfElements<GroupElement> yi, ArrayOfElements<ProductElement> xi) {
		int lam = params.getThreshold();
		int i;
		
		for (i=0; i<lam; i++) {
		
		}
		
		
	}

	/**
	 * @return true if the XML of the protocol info is valid and false
	 *         otherwise.
	 */
	private boolean isProtocolInfoValid() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return true if the proof params are valid and false otherwise.
	 */
	private boolean isProofParamsValid() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return true if successfully derived Gq, Mw, Cw, Rw, the hash function H
	 *         and the PRG and false otherwise.
	 */
	private boolean deriveSetsAndObjects() {
		// TODO: Part 3 in the algorithm.
		//Derive Cw, Mw, Rw
		return false;
	}

	/**
	 * @return the digest "ro" of selected protocol params for the calls of the
	 *         random oracles.
	 */
	private String prefixToRandomOracle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean ReadLists() {
		return true;
	}
}
