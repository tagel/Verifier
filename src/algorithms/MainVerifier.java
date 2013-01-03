package algorithms;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import algorithms.params.Parameters;
import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BigIntLeaf;
import arithmetic.objects.Element;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
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

	/**
	 * @return true if verification was successful and false otherwise.
	 * @throws UnsupportedEncodingException
	 */
	public boolean verify(String protInfo, String directory, String type,
			String auxid, BigInteger w, boolean posc, boolean ccpos, boolean dec)
			throws UnsupportedEncodingException {

		params = new Parameters(protInfo, directory, type, auxid, w, posc,
				ccpos, dec);

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

		params.setGq(ElementsExtractor.unmarshal(params.getsGq()));

		deriveSetsAndObjects();

		HashFunction H = new SHA2HashFunction(params.getSh());
		PseudoRandomGenerator PRG = new HashFuncPRG(new SHA2HashFunction(
				params.getsPRG()));

		Element version_proof = new StringLeaf(params.getVersion());

		// s = sid|"."|auxid
		String s = params.getSessionID() + "." + params.getAuxsid();

		Element btAuxid = new StringLeaf(s);
		Element sGq = new StringLeaf(params.getsGq());
		Element sPRG = new StringLeaf(params.getsPRG());
		Element sH = new StringLeaf(params.getSh());

		Element Ne = new BigIntLeaf(params.getNe());
		Element Nr = new BigIntLeaf(params.getNr());

		Element Nv = new BigIntLeaf(params.getNv());
		Element btW = new BigIntLeaf(params.getW());

		Element[] input = new Element[9];
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

		if (!ReadKeys())// Attempt to read was unsuccessful
			return false;

		ReadLists();

		return true;
	}

	private boolean ReadKeys() {
		Element btPk = btFromFile(params.getDirectory().concat(
				"FullPublicKey.bt"));
		ProductGroupElement pk = new ProductGroupElement(btPk, params.getGq());
		GroupElement y = pk.getArr()[1];
		GroupElement g = pk.getArr()[0];

		params.setFullPublicKey(pk);
		GroupElement yi;
		IntegerFieldElement xi;
		int i;

		// Here we get the Identity element and multiply all of the yi's
		GroupElement res = (GroupElement) params.getGq().one();

		// TODO: Fix the path - now if we have more than 10 it will write 010.
		for (i = 0; i < params.getThreshold(); i++) {
			// Here we assume that the file exists
			yi = GroupElement(btFromFile(params.getDirectory() + "/proofs/"
					+ "PublicKey0" + (i + 1)), params.getGq());
			if (yi == null)
				return false;
			params.getMixPublicKey().addElement(yi);
			res = res.mult(yi);
		}

		if (!res.equal(y))
			return false;

		// Here we check the secret keys - xi:
		// Here the file can be null
		for (i = 0; i < params.getThreshold(); i++) {
			xi = IntegerFieldElement(btFromFile(params.getDirectory()
					+ "/proofs/" + "SecretKey0" + (i + 1)), params.getGq());
			params.getMixSecretKey().addElement(xi);
		}

		return true;

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
		// Derive Cw, Mw, Rw, Zp
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
