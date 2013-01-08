package algorithms;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import algorithms.params.Parameters;
import algorithms.params.XMLProtocolInfo;
import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BigIntLeaf;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.IField;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
import arithmetic.objects.PrimeOrderField;
import arithmetic.objects.ProductGroupElement;
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
	private HashFunction H;

	/**
	 * @return true if verification was successful and false otherwise.
	 * @throws UnsupportedEncodingException
	 */
	public boolean verify(String protInfo, String directory, String type,
			String auxid, int w, boolean posc, boolean ccpos, boolean dec)
			throws UnsupportedEncodingException {

		// *****Section 1 in the algorithm*****
		// First create the Parameters object using the
		// command line parameters
		params = new Parameters(protInfo, directory, type, auxid, w, posc,
				ccpos, dec);

		// create the Xml protInfo file and check it
		// fill the relevant parameters:
		// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
		// fillFromXML checks if the file is valid, rejects if not
		if (!params.fillFromXML()) {
			return false;
		}

		// *****Section 2 in the algorithm*****
		// fill version_proof(Version) type, auxid, w from proof directory
		// if this fails, return false.
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
		if ((params.getWidthExp() == 0)
				&& (params.getW() != params.getwDefault()))
			return false;

		if ((params.getWidthExp() != 0)
				&& (params.getW() != params.getWidthExp()))
			return false;

		// *******Section 3 in the Algorithm*********
		if (!deriveSetsAndObjects())
			return false;

		// *******Section 4 in the Algorithm*********
		createPrefixToRo();
		
		// *******Section 5 in the Algorithm*********
		if (!ReadKeys())// Attempt to read was unsuccessful
			return false;

		// *******Section 6 in the Algorithm*********
		if (!ReadLists())
			return false;

		return true;
	}

	/**
	 * @return true if successfully derived Gq, Zq, Mw, Cw, Rw, the hash function H
	 *         and the PRG and false otherwise.
	 */
	private boolean deriveSetsAndObjects() {
		//Unmarshall Gq
		try {
			params.setGq(ElementsExtractor.unmarshal(params.getsGq()));
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error unmarshalling Gq");
			return false;
		}
		
		//Derive the Zq prime order Field
		BigInteger q = params.getGq().getFieldOrder();
		IField<IntegerFieldElement> Zq = new PrimeOrderField(q);
		params.setZq(Zq);

		//Set the Hashfunction and the pseudo random generator
		H = new SHA2HashFunction(params.getSh());
		params.setPrg(new HashFuncPRG(new SHA2HashFunction(params.getsPRG())));
		
		return true;
	}
	
	
	private void createPrefixToRo() {
		String s = params.getSessionID() + "." + params.getAuxsid();
		ByteTree btAuxid = new StringLeaf(s);
		ByteTree version_proof = new StringLeaf(params.getVersion());
		ByteTree sGq = new StringLeaf(params.getsGq());
		ByteTree sPRG = new StringLeaf(params.getsPRG());
		ByteTree sH = new StringLeaf(params.getSh());

		ByteTree Ne = new BigIntLeaf(BigInteger.valueOf(params.getNe()));
		ByteTree Nr = new BigIntLeaf(BigInteger.valueOf(params.getNr()));
		ByteTree Nv = new BigIntLeaf(BigInteger.valueOf(params.getNv()));
		ByteTree btW = new BigIntLeaf(BigInteger.valueOf(params.getW()));

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
		}
	
	
	private boolean ReadKeys() {
		//TODO: Change the file names and the paths so it will be generic 
		//TODO: and will fit to the new constructor.
		ProductGroupElement pk = newProductGroupElement(
				ElementsExtractor.btFromFile(params.getDirectory(),
						"FullPublicKey.bt"), params.getGq());
		IGroupElement y = pk.getArr()[1];
		IGroupElement g = pk.getArr()[0];

		params.setFullPublicKey(pk);
		IGroupElement yi;
		IntegerFieldElement xi;
		int i;

		// Here we get the Identity element and multiply all of the yi's
		IGroupElement res = params.getGq().one();

		for (i = 0; i < params.getThreshold(); i++) {
			// Here we assume that the file exists
			yi = ElementsExtractor.createGroupElement(
					ElementsExtractor.btFromFile(params.getDirectory(),
							"proofs","PublicKey" + (i < 10 ? "0" : "")
							+ (i + 1))+".bt", params.getGq());
			if (yi == null)
				return false;
			params.getMixPublicKey().add(yi);
			res = res.mult(yi);
		}

		if (!res.equal(y))
			return false;

		//TODO: concat the file name correctly
		// Here we check the secret keys - xi:
		// Here the file can be null
		for (i = 0; i < params.getThreshold(); i++) {
			xi = new IntegerFieldElement(
					ElementsExtractor.leafToInt(ElementsExtractor
							.btFromFile(params.getDirectory(),"/proofs/"
									+ "SecretKey0" + (i < 10 ? "0" : "")
									+ (i + 1))), params.getZq());
			params.getMixSecretKey().add(xi);
			//xi = null if the file doesn't exist
		}
		
		
		//Check the 3.b part of keys verifier
		for (i = 0; i < params.getThreshold(); i++) {
			xi = params.getMixSecretKey().getAt(i);
			yi = params.getMixPublicKey().getAt(i);
			
			if ((xi != null) &&
					!(yi.equal(g.power(xi.getElement()))))
				return false;
		}

		return true;

	}

	private boolean ReadLists() {
		String filename = "name";
		
		
		return true;
	}
	
	
	
	/**
	 * @return true if the proof params are valid and false otherwise.
	 */
	private boolean isProofParamsValid() {
		// TODO Auto-generated method stub
		return false;
	}

	

	
}
