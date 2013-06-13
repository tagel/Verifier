package algorithms.params;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import main.Logger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class describes an object that contains the parameters used by the
 * verifying algorithms
 * 
 * @author Sofi
 */
public class Parameters {

	private static final String DECRYPTION = "decryption";
	private static final String SHUFFLING = "shuffling";
	private static final String MIXING = "mixing";
	private static final String EMPTY_STRING = "";
	private static final String WIDTH_FILE_NAME = "width";
	private static final String TYPE_FILE_NAME = "type";
	private static final String VERSION_FILE_NAME = "version";
	private static final String AUXSID_FILE_NAME = "auxsid";

	private final Logger logger;

	// Random Oracles
	private RandomOracle ROseed;
	private RandomOracle ROchallenge;

	// Derived Objects
	private byte[] prefixToRO;
	private IGroup Gq;
	private IRing<IntegerRingElement> Zq;
	private PseudoRandomGenerator prg;
	private ArrayOfElements<IGroupElement> randArray;

	// parameters from directory
	private String protInfo;
	private String directory;
	private String version;
	private Type type;
	private String auxsid;
	private int w;
	private ProductGroupElement fullPublicKey;

	// parameters from the XML
	private String protVersion;
	private String sessionID;
	private int numOfParties;
	private int threshold;
	private int Ne;
	private int Nr;
	private int Nv;
	private String sh;
	private String sGq;
	private String sPRG;
	private int wDefault;

	// parameters from CMD
	private Type typeExpected;
	private boolean posc;
	private boolean ccpos;
	private boolean dec;
	private String auxidExp;
	private int widthExp;

	// parameters from lists
	private ArrayOfElements<ProductGroupElement> plaintexts;// V
	private int N; // size of the arrays
	private ArrayOfElements<ArrayOfElements<ProductGroupElement>> arrOfCiphertexts;
		

	// MIX -- The parameters of each party
	private ArrayOfElements<IGroupElement> mixPublicKey;// Used in Keys Verifier
	private ArrayOfElements<IntegerRingElement> mixSecretKey;// Used in Keys
																// Verifier

	// type of verification
	public enum Type {
		MIXING, SHUFFLING, DECRYPTION;
	}

	/**
	 * 
	 * @params - All the parameters we get from the cmd line
	 */
	public Parameters(String protInfo, String directory, Type type,
			java.lang.String auxsid, int w, boolean posc, boolean ccpos,
			boolean dec, Logger logger) {

		this.auxidExp = auxsid;
		this.protInfo = protInfo;
		this.directory = directory;
		this.typeExpected = type;
		this.widthExp = w;
		this.posc = posc;
		this.ccpos = ccpos;
		this.dec = dec;
		this.logger = logger;

		initializeParams();
		initializeMix();
	}

	private void initializeParams() {
		this.w = 0;
		this.numOfParties = 0;
		this.threshold = 0;
		this.Ne = 0;
		this.Nr = 0;
		this.Nv = 0;
		this.wDefault = 0;
		this.prefixToRO = null;
		this.Gq = null;
		this.prg = null;
		this.version = null;
		this.type = null;
		this.auxsid = null;
		this.fullPublicKey = null;
		this.protVersion = null;
		this.sessionID = null;
		this.sh = null;
		this.sGq = null;
		this.sPRG = null;
		this.ROseed = null;
		this.ROchallenge = null;
	}

	private void initializeMix() {
		mixPublicKey = new ArrayOfElements<IGroupElement>();
		mixSecretKey = new ArrayOfElements<IntegerRingElement>();
	}

	/**
	 * fill the relevant parameters from the given xml: versionprot, sid, k,
	 * thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault (width).
	 * 
	 * @return false if there was a problem reading the file, or missing args.
	 */
	public boolean fillFromXML() {
		XMLProtocolInfo protXML;
		try {
			protXML = new XMLProtocolInfo(protInfo);
		} catch (FileNotFoundException e) {
			logger.sendLog("XML ProtInfo not found", Logger.Severity.ERROR);
			return false;
		} catch (XMLStreamException e) {
			logger.sendLog("Error reading XML file", Logger.Severity.ERROR);
			return false;
		} catch (FactoryConfigurationError e) {
			logger.sendLog("Error reading XML file", Logger.Severity.ERROR);
			return false;
		} catch (IllegalXmlFormatException e) {
			logger.sendLog("Error reading XML file - not in XML format",
					Logger.Severity.ERROR);
			return false;
		}

		protVersion = protXML.getVersion();
		sessionID = protXML.getSessionId();
		numOfParties = protXML.getNumOfParties();
		threshold = protXML.getThreshold();
		Ne = protXML.getNe();
		Nr = protXML.getNr();
		Nv = protXML.getNv();
		sh = protXML.getHashFunction();
		sGq = protXML.getGq();
		sPRG = protXML.getPrg();
		wDefault = protXML.getWidth();

		return true;
	}

	/**
	 * fill the relevant parameters from the given directory: fill
	 * version_proof(Version) type, auxid, w from proof directory.
	 * 
	 * @return true if succeeded, false otherwise.
	 */
	public boolean fillFromDirectory() {
		Scanner text = null;
		try {
			text = new Scanner(new File(directory, AUXSID_FILE_NAME));
		} catch (FileNotFoundException e) {
			logger.sendLog("Cannot find file " + AUXSID_FILE_NAME,
					Logger.Severity.ERROR);
			return false;
		}
		auxsid = text.next().trim();

		try {
			text = new Scanner(new File(directory, VERSION_FILE_NAME));
		} catch (FileNotFoundException e) {
			logger.sendLog("Cannot find file " + VERSION_FILE_NAME,
					Logger.Severity.ERROR);
			return false;
		}

		if (text.hasNext()) {
			version = text.next().trim();
		} else {
			version = EMPTY_STRING;
		}

		try {
			text = new Scanner(new File(directory, TYPE_FILE_NAME));
		} catch (FileNotFoundException e) {
			logger.sendLog("Cannot find file " + TYPE_FILE_NAME,
					Logger.Severity.ERROR);
			return false;
		}
		type = stringToType(text.next().trim());

		try {
			text = new Scanner(new File(directory, WIDTH_FILE_NAME));
		} catch (FileNotFoundException e) {
			logger.sendLog("Cannot find file " + WIDTH_FILE_NAME,
					Logger.Severity.ERROR);
			return false;
		}
		w = text.nextInt();

		return true;
	}

	// ************************************************************************
	// **************************Getters And Setters***************************
	// ************************************************************************
	public Type stringToType(String next) {
		if (next.equals(MIXING))
			return Type.MIXING;
		if (next.equals(SHUFFLING))
			return Type.SHUFFLING;
		if (next.equals(DECRYPTION))
			return Type.DECRYPTION;
		return null;
	}
	
	public ArrayOfElements<ArrayOfElements<ProductGroupElement>> getArrOfCiphertexts() {
		return arrOfCiphertexts;
	}

	public void setArrOfCiphertexts(
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> arrOfCiphertexts) {
		this.arrOfCiphertexts = arrOfCiphertexts;
	}

	public RandomOracle getROseed() {
		return ROseed;
	}

	public void setROseed(RandomOracle rOseed) {
		ROseed = rOseed;
	}

	public RandomOracle getROchallenge() {
		return ROchallenge;
	}

	public void setROchallenge(RandomOracle rOchallenge) {
		ROchallenge = rOchallenge;
	}

	public IRing<IntegerRingElement> getZq() {
		return Zq;
	}

	public void setZq(IRing<IntegerRingElement> ring) {
		Zq = ring;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	/**
	 * 
	 * @return the directory path
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @return the Verificatum version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return the proof type: "mixing" \ "shuffling" \ "decryption"
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the auxiliary session identifier of this session
	 */
	public String getAuxsid() {
		return auxsid;
	}

	/**
	 * @return the full public key
	 */
	public ProductGroupElement getFullPublicKey() {
		return fullPublicKey;
	}

	// ***FROM CMD***

	/**
	 * @return true, if it is a proof of shuffle commitment and false otherwise
	 */
	public boolean isPosc() {
		return posc;
	}

	/**
	 * Sets true - if this is proof of shuffle commitment and false otherwise
	 */
	public void setPosc(boolean posc) {
		this.posc = posc;
	}

	/**
	 * @return true if it is commitment-consistent proof of a shuffle and false
	 *         otherwise
	 */
	public boolean isCcpos() {
		return ccpos;
	}

	/**
	 * Sets true - if this is commitment-consistent proof of a shuffle and false
	 * otherwise
	 */
	public void setCcpos(boolean ccpos) {
		this.ccpos = ccpos;
	}

	/**
	 * @return true if it is proof of correct decryption and false otherwise
	 */
	public boolean isDec() {
		return dec;
	}

	/**
	 * Sets true - if this is proof of correct decryption and false otherwise
	 */
	public void setDec(boolean dec) {
		this.dec = dec;
	}

	public void setTypeExpected(Type typeExpected) {
		this.typeExpected = typeExpected;
	}

	public Type getTypeExpected() {
		return typeExpected;
	}

	/**
	 * @return the version id of the verificatum.
	 */
	public String getProtVersion() {
		return protVersion;
	}

	/**
	 * @return the globally unique session identifier tied to the generation of
	 *         a particular joint public key
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * @return the number of parties (k).
	 */
	public int getNumOfParties() {
		return numOfParties;
	}

	/**
	 * @return the threshold = number of mix-servers that take part in the
	 *         shuffling
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * @return the number of bits in each component of random vectors used for
	 *         batching in proofs of shuffles and proofs of correct decryption.
	 */
	public int getNe() {
		return Ne;
	}

	/**
	 * @return the acceptable statistical error when sampling random values.
	 */
	public int getNr() {
		return Nr;
	}

	/**
	 * @return the number of bits used in the challenge of the verifier in
	 *         zero-knowledge proofs.
	 */
	public int getNv() {
		return Nv;
	}

	/**
	 * @return the hash function used to create the random oracles.
	 */
	public String getSh() {
		return sh;
	}

	/**
	 * @return the string representation of the group Gp.
	 */
	public String getsGq() {
		return sGq;
	}

	/**
	 * @return the hash function used to create the PRG used to expand
	 *         challenges into arrays.
	 */
	public String getsPRG() {
		return sPRG;
	}

	/**
	 * @return the default width of cipher-texts and plain-texts.
	 */
	public int getW() {
		return w;
	}

	public int getwDefault() {
		return wDefault;
	}

	public void setwDefault(int wDefault) {
		this.wDefault = wDefault;
	}

	public void setFullPublicKey(ProductGroupElement fullPublicKey) {
		this.fullPublicKey = fullPublicKey;
	}

	public void setPlaintexts(ArrayOfElements<ProductGroupElement> plaintexts2) {
		this.plaintexts = plaintexts2;
	}

	/**
	 * @return the output plaintext elements that has not been decoded in any
	 *         way
	 */
	public ArrayOfElements<ProductGroupElement> getPlaintexts() {
		return plaintexts;
	}

	public String getAuxidExp() {
		return auxidExp;
	}

	public void setAuxidExp(String auxidExp) {
		this.auxidExp = auxidExp;
	}

	public int getWidthExp() {
		return widthExp;
	}

	public void setWidthExpected(int widthExpected) {
		this.widthExp = widthExpected;
	}

	public IGroup getGq() {
		return Gq;
	}

	public void setGq(IGroup gq) {
		Gq = gq;
	}

	public byte[] getPrefixToRO() {
		return prefixToRO;
	}

	public void setPrefixToRO(byte[] prefixToRO) {
		this.prefixToRO = prefixToRO;
	}

	public PseudoRandomGenerator getPrg() {
		return prg;
	}

	public void setPrg(PseudoRandomGenerator prg) {
		this.prg = prg;
	}

	public ArrayOfElements<IGroupElement> getMixPublicKey() {
		return mixPublicKey;
	}

	public void setMixPublicKey(ArrayOfElements<IGroupElement> mixPublicKey) {
		this.mixPublicKey = mixPublicKey;
	}

	public ArrayOfElements<IntegerRingElement> getMixSecretKey() {
		return mixSecretKey;
	}

	public void setMixSecretKey(ArrayOfElements<IntegerRingElement> mixSecretKey) {
		this.mixSecretKey = mixSecretKey;
	}

	public void setRandArray(ArrayOfElements<IGroupElement> h) {
		this.randArray = h;
	}

	public ArrayOfElements<IGroupElement> getRandArray() {
		return randArray;
	}
}
