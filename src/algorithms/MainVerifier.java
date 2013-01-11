package algorithms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import algorithms.params.Parameters;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayGenerators;
import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.BasicElements.BigIntLeaf;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.BasicElements.StringLeaf;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.Ring;
import cryptographic.primitives.HashFuncPRG;
import cryptographic.primitives.HashFuncPRGRandomOracle;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.RandomOracle;
import cryptographic.primitives.SHA2HashFunction;

/**
 * This class describes the required behavior expected from the main verifier.
 * 
 * @author Tagel & Sofi
 */
public class MainVerifier {

	private Parameters params;
	private HashFunction H;

	public MainVerifier(Parameters params, HashFunction H){
		this.params = params;
		this.H = H;
	}
	/**
	 * @return true if verification was successful and false otherwise.
	 * @throws IOException
	 */
	public boolean verify(String protInfo, String directory, String type,
			String auxid, int w, boolean posc, boolean ccpos, boolean dec)
			throws IOException {

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
		// Derives the relevant objects: IGroup Gq, Ring Zq, Hashfunction H, and
		// PRG.
		if (!deriveSetsAndObjects())
			return false;

		// *******Section 4 in the Algorithm*********
		// Also creates the random oracles used in the provers using prg and
		// hashfunction
		createPrefixToRo();

		// *******Section 5 in the Algorithm*********
		if (!ReadKeys())// Attempt to read was unsuccessful
			return false;

		// *******Section 6 in the Algorithm*********
		if (!ReadLists())
			return false;

		// *******Section 7 in the Algorithm*********
		// Here we call the subroutines of the verifier
		// 7a -- Verify Shuffling
		if ((params.getType().equals("mixing") || params.getType().equals(
				"decryption"))
				&& (params.isPosc() || params.isCcpos()))

			if (!VerShuffling.verify(params.getROseed(),
					params.getROchallenge(), params.getDirectory(),
					params.getPrefixToRO(), params.getThreshold(),
					params.getN(), params.getNe(), params.getNr(),
					params.getNe(), params.getPrg(), params.getGq(),
					params.getFullPublicKey(), params.getCiphertexts(),
					params.getShuffledCiphertexts(), params.isPosc(),
					params.isCcpos(), params.getZq(),params.getW()))
				return false;

		// 7b - Verify Decryption
		if (params.isDec()) {// isDec==true means we need to check the
								// decryption.
			if (params.getType().equals("mixing"))
				if (!VerDec.verify(params.getROseed(),
						params.getROchallenge(), params.getDirectory(),
						params.getPrefixToRO(), params.getN(), params.getNe(),
						params.getNr(), params.getNv(), params.getPrg(),
						params.getGq(), params.getFullPublicKey(),
						params.getShuffledCiphertexts(),
						params.getPlaintexts(), params.getZq()))
					return false;

			if (params.getType().equals("decryption"))
				if (!VerDec.verify(params.getROseed(),
						params.getROchallenge(), params.getDirectory(),
						params.getPrefixToRO(), params.getN(), params.getNe(),
						params.getNr(), params.getNv(), params.getPrg(),
						params.getGq(), params.getFullPublicKey(),
						params.getCiphertexts(), params.getPlaintexts(),
						params.getZq()))
					return false;

		}

		return true;
	}

	/**
	 * @return true if successfully derived Gq, Zq, Mw, Cw, Rw, the hash
	 *         function H and the PRG and false otherwise.
	 */
	public boolean deriveSetsAndObjects() {
		// Unmarshall Gq
		try {
			params.setGq(ElementsExtractor.unmarshal(params.getsGq()));
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error unmarshalling Gq");
			return false;
		}

		// create the Ring
		IRing<IntegerRingElement> temp = new Ring(params.getGq()
				.getFieldOrder());
		params.setZq(temp);

		// Set the Hashfunction and the pseudo random generator
		H = new SHA2HashFunction(params.getSh());
		params.setPrg(new HashFuncPRG(new SHA2HashFunction(params.getsPRG())));

		return true;
	}

	public void createPrefixToRo() throws UnsupportedEncodingException {
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

		// Set random oracles:
		RandomOracle ROseed = new HashFuncPRGRandomOracle(H, params.getPrg()
				.seedlen());
		RandomOracle ROchallenge = new HashFuncPRGRandomOracle(H,
				params.getNv());

		params.setROseed(ROseed);
		params.setROchallenge(ROchallenge);

	}

	public boolean ReadKeys() throws IOException {
		ProductGroupElement pk = ElementsExtractor.createSimplePGE(
				ElementsExtractor.btFromFile(params.getDirectory(),
						"FullPublicKey.bt"), params.getGq());
		IGroupElement y = pk.getArr().getAt(1);
		IGroupElement g = pk.getArr().getAt(0);

		params.setFullPublicKey(pk);
		IGroupElement yi;
		IntegerRingElement xi;
		int i;

		// Here we get the Identity element and multiply all of the yi's
		IGroupElement res = params.getGq().one();

		for (i = 0; i < params.getThreshold(); i++) {
			// Here we assume that the file exists
			yi = ElementsExtractor.createGroupElement(ElementsExtractor
					.btFromFile(params.getDirectory(), "proofs", "PublicKey"
							+ (i < 10 ? "0" : "") + (i + 1) + ".bt"), params
					.getGq());
			if (yi == null)
				return false;
			params.getMixPublicKey().add(yi);
			res = res.mult(yi);
		}

		if (!res.equal(y))
			return false;

		// TODO: The bt from file returns NULL, not the FieldElement!!!
		// Here we check the secret keys - xi:
		// Here the file can be null
		for (i = 0; i < params.getThreshold(); i++) {
			xi = new IntegerRingElement(
					ElementsExtractor.leafToInt(ElementsExtractor.btFromFile(
							params.getDirectory(), "proofs", "SecretKey"
									+ (i < 10 ? "0" : "") + (i + 1) + ".bt")),
					params.getZq());
			params.getMixSecretKey().add(xi);
			// xi = null if the file doesn't exist
		}

		// Check the 3.b part of keys verifier
		for (i = 0; i < params.getThreshold(); i++) {
			xi = params.getMixSecretKey().getAt(i);
			yi = params.getMixPublicKey().getAt(i);

			// TODO: check this step
			if ((xi != null) && !(yi.equal(g.power(xi.getElement()))))
				return false;
		}

		return true;

	}

	// Part 6 of the algorithm
	public boolean ReadLists() throws IOException {
		// section 6a of the Algorithm
		byte[] file = ElementsExtractor.btFromFile(params.getDirectory(),
				"Ciphertexts.bt");
		if (file == null)
			return false;

		ArrayOfElements<ProductGroupElement> ciphertexts = ArrayGenerators
				.createArrayOfCiphertexts(file, params.getGq());

		params.setN(ciphertexts.getSize());

		// section 6b of the Algorithm
		// if the type==mixing, read the file ShuffledCiphertexts.bt from
		// Directory
		// if the type==shuffling, read the file Ciphertexts_threshold from
		// Directory/proofs
		if (params.getType().equals("mixing")) {
			file = ElementsExtractor.btFromFile(params.getDirectory(),
					"ShuffledCiphertexts.bt");
			if (file == null)
				return false;
		} else if (params.getType().equals("shuffling")) {
			file = ElementsExtractor.btFromFile(params.getDirectory(),
					"proofs",
					"Ciphertexts" + (params.getThreshold() < 10 ? "0" : "")
							+ params.getThreshold() + ".bt");
			if (file == null)
				return false;
		}

		ArrayOfElements<ProductGroupElement> ShuffledCiphertexts = ArrayGenerators
				.createArrayOfCiphertexts(file, params.getGq());
		if (ShuffledCiphertexts.getSize()!=params.getN())
			return false;
		params.setShuffledCiphertexts(ShuffledCiphertexts);

		// Section 6c of the algorithm
		if (params.getType().equals("mixing")
				|| params.getType().equals("decryption")) {
			file = ElementsExtractor.btFromFile(params.getDirectory(),
					"Plaintexts.bt");
			if (file == null)
				return false;

			ArrayOfElements<ProductGroupElement> plaintexts = ArrayGenerators
					.createArrayOfPlaintexts(file, params.getGq());
			params.setPlaintexts(plaintexts);
		}

		return true;
	}

}
