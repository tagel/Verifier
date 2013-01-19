package algorithms.verifiers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import algorithms.params.Parameters;
import algorithms.params.Parameters.Type;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import arithmetic.objects.ring.Ring;
import cryptographic.primitives.HashFuncPRG;
import cryptographic.primitives.HashFuncPRGRandomOracle;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.SHA2HashFunction;

/**
 * This class describes the required behavior expected from the main verifier.
 * 
 * @author Sofi
 */
public class MainVerifier {

	private static final String SECRET_KEY = "SecretKey";
	private static final String PROOFS = "proofs";
	private static final String BT_EXT = ".bt";
	private static final String FULL_PUBLIC_KEY_BT = "FullPublicKey" + BT_EXT;
	private static final String CIPHERTEXTS_FILE_NAME = "Ciphertexts";
	private static final String SHUFFCT_FILE_PATH = "ShuffledCiphertexts"
			+ BT_EXT;
	private static final String PLAIN_TEXTS_FILE_PATH = "Plaintexts" + BT_EXT;

	private Parameters params;
	private HashFunction H;

	/**
	 * Constructor for tests
	 * 
	 * @param params
	 * @param H
	 */
	MainVerifier(Parameters params, HashFunction H) {
		this.params = params;
		this.H = H;
	}

	/**
	 * Empty constructor for the cmd line
	 */
	public MainVerifier() {
	}

	/**
	 * @return true if verification was successful and false otherwise.
	 * @throws Exception
	 */
	public boolean verify(String protInfo, String directory, Type type,
			String auxid, int w, boolean posc, boolean ccpos, boolean dec)
			throws Exception {

		// *****Section 1 and 2 in the algorithm*****
		// create the Parameters object using the command line parameters and
		// fill params from aml and dir
		params = new Parameters(protInfo, directory, type, auxid, w, posc,
				ccpos, dec);
		if (!fillParamsFromXmlAndDir(params) || !checkFilledParams()) {
			return false;
		}

		// *******Section 3 in the Algorithm*********
		// Derives the objects: IGroup Gq, Ring Zq, Hashfunction H, and PRG.
		if (!deriveSetsAndObjects()) {
			return false;
		}

		// *******Section 4 in the Algorithm*********
		// Creates the random oracles used in the provers using prg and
		// hashfunction
		createPrefixToRo();

		// *******Section 5 in the Algorithm*********
		if (!ReadKeys()) {
			// Attempt to read was unsuccessful
			return false;
		}

		// *******Section 6 in the Algorithm*********
		if (!ReadLists()) {
			return false;
		}

		// *******Section 7 in the Algorithm*********
		// Call the subroutines of the verifier
		// 7a - Verify Shuffling
		if ((Type.MIXING.equals(params.getType()) || Type.SHUFFLING.equals(
				params.getType()))
				&& (params.isPosc() || params.isCcpos())) {

			if (!runVerShuffling()) {
				return false;
			}
		}

		// 7b - Verify Decryption
		// if isDec==true means we need to check the decryption.
		if (params.isDec()) {
			
			if (Type.MIXING.equals(params.getType())) {
				if (!runVerDec(Type.MIXING)) {
					return false;
				}
			}

			if (Type.DECRYPTION.equals(params.getType())) {
				if (!runVerDec(Type.DECRYPTION)) {
					return false;
				}
			}
		}
		return true;
	}

	// expect type to be MIXING or DECRYPTION 
	private boolean runVerDec(Type type) throws Exception {
		ArrayOfElements<ProductGroupElement> l;
		if (Type.MIXING.equals(type)) {
			l = params.getShuffledCiphertexts();
		} else {
			l = params.getCiphertexts();
		}

		return VerDec.verify(params.getROseed(), params.getROchallenge(),
				params.getDirectory(), params.getPrefixToRO(),
				params.getThreshold(), params.getN(), params.getNe(),
				params.getNr(), params.getNv(), params.getPrg(),
				params.getGq(), params.getFullPublicKey(), l,
				params.getPlaintexts(), params.getZq(),
				params.getMixPublicKey(), params.getMixSecretKey(),
				params.getW());
	}

	private boolean runVerShuffling() throws Exception {
		return VerShuffling.verify(params.getROseed(), params.getROchallenge(),
				params.getDirectory(), params.getPrefixToRO(),
				params.getThreshold(), params.getN(), params.getNe(),
				params.getNr(), params.getNe(), params.getPrg(),
				params.getGq(), params.getFullPublicKey(),
				params.getCiphertexts(), params.getShuffledCiphertexts(),
				params.isPosc(), params.isCcpos(), params.getZq(),
				params.getW());
	}

	private boolean checkFilledParams() {
		if (!params.getProtVersion().equals(params.getVersion())) {
			return false;
		}

		if (!(params.getType().equals(params.getTypeExpected()))) {
			return false;
		}

		if (!params.getAuxsid().equals(params.getAuxidExp())) {
			return false;
		}

		if ((params.getWidthExp() == 0)
				&& (params.getW() != params.getwDefault())) {
			return false;
		}

		if ((params.getWidthExp() != 0)
				&& (params.getW() != params.getWidthExp())) {
			return false;
		}

		return true;
	}

	private boolean fillParamsFromXmlAndDir(Parameters params) {
		// fill the parameters from the xml, also checks that the file is valid
		if (!params.fillFromXML()) {
			return false;
		}

		// *****Section 2 in the algorithm*****
		// fill parameters from proof directory
		if (!params.fillFromDirectory()) {
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
		params.setZq(new Ring(params.getGq().getFieldOrder()));

		// Set the Hashfunction and the pseudo random generator
		H = new SHA2HashFunction(params.getSh());
		params.setPrg(new HashFuncPRG(new SHA2HashFunction(params.getsPRG())));

		return true;
	}

	public void createPrefixToRo() throws UnsupportedEncodingException {
		ByteTree btAuxid = new StringLeaf(params.getSessionID() + "."
				+ params.getAuxsid());
		ByteTree version_proof = new StringLeaf(params.getVersion());
		ByteTree sGq = new StringLeaf(params.getsGq());
		ByteTree sPRG = new StringLeaf(params.getsPRG());
		ByteTree sH = new StringLeaf(params.getSh());
		ByteTree Ne = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNe())));
		ByteTree Nr = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNr())));
		ByteTree Nv = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNv())));
		ByteTree btW = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getW())));

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

		byte[] Seed = new Node(input).toByteArray();
		params.setPrefixToRO(H.digest((Seed)));

		// Set random oracles:
		params.setROseed(new HashFuncPRGRandomOracle(H, params.getPrg()
				.seedlen()));
		params.setROchallenge(new HashFuncPRGRandomOracle(H, params.getNv()));
	}

	public boolean ReadKeys() {
		// Read Public Key
		ProductGroupElement pk;
		try {
			pk = ElementsExtractor.createSimplePGE(ElementsExtractor
					.btFromFile(params.getDirectory(), FULL_PUBLIC_KEY_BT),
					params.getGq());
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		// Extract y and g from the public key
		IGroupElement y = pk.getElements().getAt(1);
		IGroupElement g = pk.getElements().getAt(0);

		params.setFullPublicKey(pk);

		// Initialize the elements
		IGroupElement yi;
		IntegerRingElement xi;
		int i;

		// Here we get the Identity element and multiply all of the yi's
		IGroupElement res = params.getGq().one();

		for (i = 0; i < params.getThreshold(); i++) {
			// Here we assume that the file exists
			try {
				yi = ElementsExtractor.createGroupElement(ElementsExtractor
						.btFromFile(params.getDirectory(), PROOFS, "PublicKey"
								+ (i < 10 ? "0" : "") + (i + 1) + ".bt"),
						params.getGq());
			} catch (IOException e) {
				return false;
			}
			// Check if yi exists
			if (yi == null) {
				return false;
			}

			params.getMixPublicKey().add(yi);
			res = res.mult(yi);
		}

		if (!res.equals(y)) {
			return false;
		}

		// Here we check the secret keys - xi:
		// The file can be null
		for (i = 0; i < params.getThreshold(); i++) {

			byte[] xFile;
			try {
				xFile = ElementsExtractor.btFromFile(params.getDirectory(),
						PROOFS, SECRET_KEY + (i < 10 ? "0" : "") + (i + 1)
								+ BT_EXT);
			} catch (IOException e) {
				return false;
			}

			// xi = null if the file doesn't exist
			xi = (xFile == null) ? null : new IntegerRingElement(
					ElementsExtractor.leafToInt(xFile), params.getZq());
			params.getMixSecretKey().add(xi);
		}

		// Check the 3.b part of keys verifier
		for (i = 0; i < params.getThreshold(); i++) {
			xi = params.getMixSecretKey().getAt(i);
			yi = params.getMixPublicKey().getAt(i);

			if ((xi != null) && !(yi.equals(g.power(xi.getElement())))) {
				return false;
			}
		}

		return true;
	}

	// Part 6 of the algorithm
	public boolean ReadLists() throws IOException {
		// section 6a of the Algorithm
		byte[] file = ElementsExtractor.btFromFile(params.getDirectory(),
				CIPHERTEXTS_FILE_NAME + BT_EXT);
		if (file == null) {
			return false;
		}

		ArrayOfElements<ProductGroupElement> ciphertexts = ArrayGenerators
				.createArrayOfCiphertexts(file, params.getGq(), params.getW());

		params.setCiphertexts(ciphertexts);
		params.setN(ciphertexts.getSize());

		// section 6b of the Algorithm -- read shuffled ciphertexts
		// if the type==mixing, read the file Ciphertexts_threshold from
		// Directory/proofs
		// if the type==shuffling, read the file ShuffledCiphertexts.bt from
		// Directory

		if (params.getType().equals(Type.MIXING)) {
			file = ElementsExtractor.btFromFile(
					params.getDirectory(),
					PROOFS,
					CIPHERTEXTS_FILE_NAME
							+ (params.getThreshold() < 10 ? "0" : "")
							+ params.getThreshold() + BT_EXT);
			if (file == null) {
				return false;
			}
		}

		if (params.getType().equals(Type.SHUFFLING)) {
			file = ElementsExtractor.btFromFile(params.getDirectory(),
					SHUFFCT_FILE_PATH);
			if (file == null) {
				return false;
			}
		}

		ArrayOfElements<ProductGroupElement> ShuffledCiphertexts = ArrayGenerators
				.createArrayOfCiphertexts(file, params.getGq(), params.getW());
		if (ShuffledCiphertexts.getSize() != params.getN()) {
			return false;
		}

		params.setShuffledCiphertexts(ShuffledCiphertexts);

		// Section 6c of the algorithm
		if (params.getType().equals(Type.MIXING)
				|| params.getType().equals(Type.DECRYPTION)) {
			file = ElementsExtractor.btFromFile(params.getDirectory(),
					PLAIN_TEXTS_FILE_PATH);
			if (file == null) {
				return false;
			}

			ArrayOfElements<ProductGroupElement> plaintexts = ArrayGenerators
					.createArrayOfPlaintexts(file, params.getGq(), params.getW());
			params.setPlaintexts(plaintexts);
		}
		return true;
	}

	/**
	 * Getter for the params field.
	 * 
	 * @return the parameters
	 */
	public Parameters getParams() {
		return params;
	}
}
