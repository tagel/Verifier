package algorithms.params;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

import arithmetic.objects.Arrays.ArrayOfElements;
import arithmetic.objects.BasicElements.BooleanArrayElement;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;

/**
 * This class describes an object that contains the parameters used by the
 * verifying algorithms
 * 
 * @author Tagel & Sofi
 */
public class Parameters {

	/**
	 * 
	 * @params - All the parameters we get from the cmd line 
	 */
	public Parameters(String protInfo, String directory, String type,
			java.lang.String auxsid, int w, boolean posc, boolean ccpos,
			boolean dec) {

		this.auxsid = auxsid;
		this.protInfo = protInfo;
		this.directory = directory;
		this.type = type;
		this.w = w;
		this.posc = posc;
		this.ccpos = ccpos;
		this.dec = dec;

		prefixToRO = null;
		Gq = null;
		prg = null;
		version = null;
		type = null;
		auxsid = null;
		w = 0;
		fullPublicKey = null;
		protVersion = null;
		sessionID = null;
		numOfParties = 0;
		threshold = 0;
		Ne = 0;
		Nr = 0;
		Nv = 0;
		sh = null;
		sGq = null;
		sPRG = null;
		wDefault = 0;
		maxciph = 0;
		
		ROseed = null;
		ROchallenge = null;
		
		initializeMix();

	}

	//Random Oracles
	RandomOracle ROseed;
	RandomOracle ROchallenge;
	
	
	//Derived Objects
	private byte[] prefixToRO;
	private IGroup Gq;
	private IRing<IntegerRingElement> Zq;
	PseudoRandomGenerator prg;

	// parameters from directory
	private String protInfo;
	private String directory;
	private String version;
	private String type;
	private String auxsid;
	private int w;
	private ProductGroupElement fullPublicKey;
	private int maxciph;//we don't read it in the main
	//TODO check the maxciph

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
	private String typeExpected;
	private boolean posc;
	private boolean ccpos;
	private boolean dec;
	private String auxidExp;
	private int widthExp;

	// parameters from lists
	private ArrayOfElements<ProductGroupElement> ciphertexts;//V
	private ArrayOfElements<ProductGroupElement> ShuffledCiphertexts; //V
	private ArrayOfElements<ProductGroupElement> plaintexts;//V
	private int N; //size of the arrays

	// MIX -- The parameters of each party
	private ArrayOfElements<IGroupElement> mixPublicKey;//Used in Keys Verifier
	private ArrayOfElements<IntegerRingElement> mixSecretKey;//Used in Keys Verifier

	private ArrayOfElements<ArrayOfElements<ProductGroupElement>> mixCiphertexts;
	private ArrayOfElements<ArrayOfElements<IGroupElement>> mixPermutationCommitment;
	private ArrayOfElements<Node> mixPoSCommitment;
	private ArrayOfElements<Node> mixPoSReply;
	private ArrayOfElements<Node> mixPoSCCommitment;
	private ArrayOfElements<Node> mixPoSCReply;
	private ArrayOfElements<Node> mixCcPosCommitment;
	private ArrayOfElements<Node> mixCcPosReply;

	private ArrayOfElements<BooleanArrayElement> mixKeepList;
	private ArrayOfElements<ArrayOfElements<IGroupElement>> mixDecryptionFactors;
	private ArrayOfElements<Node> mixDecrFactCommitment;
	private ArrayOfElements<Node> mixDecrFactReply;

	//TODO: Should we really need all of these mix-params?
	private void initializeMix() {
		 mixPublicKey = new ArrayOfElements<IGroupElement>();
		 mixSecretKey = new ArrayOfElements<IntegerRingElement>();
		 mixCiphertexts = new ArrayOfElements<ArrayOfElements<ProductGroupElement>>();
		 mixPermutationCommitment = new
		 ArrayOfElements<ArrayOfElements<IGroupElement>>();
		 mixPoSCommitment = new ArrayOfElements<Node>();
		 mixPoSReply = new ArrayOfElements<Node>();
		 mixPoSCCommitment = new ArrayOfElements<Node>();
		 mixPoSCReply = new ArrayOfElements<Node>();
		 mixCcPosCommitment = new ArrayOfElements<Node>();
		 mixCcPosReply = new ArrayOfElements<Node>();
		 mixKeepList = new ArrayOfElements<BooleanArrayElement>();
		 mixDecryptionFactors = new
		 ArrayOfElements<ArrayOfElements<IGroupElement>>();
		 mixDecrFactCommitment = new ArrayOfElements<Node>();
		 mixDecrFactReply = new ArrayOfElements<Node>();
	}

	// fill the relevant parameters from the given xml
	// fill the relevant parameters:
	// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
	// (width);
	public boolean fillFromXML() {
		XMLProtocolInfo protXML;
		try {
			protXML = new XMLProtocolInfo(protInfo);
		} catch (FileNotFoundException e) {
			System.out.println("XML ProtInfo not found");
			return false;
		} catch (XMLStreamException e) {
			System.out.println("Error reading XML");
			return false;
		} catch (FactoryConfigurationError e) {
			System.out.println("Error reading XML");
			return false;
		} catch (IllegalXmlFormatException e) {
			System.out.println("Error reading XML");
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

	// fill the relevant parameters from the given directory
	// fill version_proof(Version) type, auxid, w from proof directory
	public boolean fillFromDirectory() {
		Scanner text = null;
		try {
			text = new Scanner(new File(directory, "auxsid"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Cannot find file " + "auxsid");
			return false;
		}

		auxsid = text.next().trim();

		try {
			text = new Scanner(new File(directory, "version"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Cannot find file " + "version");
			return false;
		}

		if (text.hasNext())
			version = text.next().trim();
		else
			version = "";

		try {
			text = new Scanner(new File(directory, "type"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Cannot find file " + "type");
			return false;
		}

		type = text.next().trim();
	
		try {
			text = new Scanner(new File(directory, "width"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Cannot find file " + "auxsid");
			return false;
		}

		w = text.nextInt();
				
		return true;
	}

	
	//********************************************************************************
	//******************************Getters And Setters*******************************
	//********************************************************************************
	
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
	public String getType() {
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

	/**
	 * @return the number N0 of ciphertexts for which pre-computation was
	 *         performed
	 */
	public int getMaxciph() {
		return maxciph;
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

	public void setTypeExpected(String typeExpected) {
		this.typeExpected = typeExpected;
	}

	public String getTypeExpected() {
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

	
	public void setCiphertexts(ArrayOfElements<ProductGroupElement> ciphertexts) {
		this.ciphertexts = ciphertexts;
	}

	public void setShuffledCiphertexts(
			ArrayOfElements<ProductGroupElement> shuffledCiphertexts) {
		ShuffledCiphertexts = shuffledCiphertexts;
	}

	public void setPlaintexts(ArrayOfElements<ProductGroupElement> plaintexts) {
		this.plaintexts = plaintexts;
	}
	/**
	 * @return the input ciphertexts
	 */
	public ArrayOfElements<ProductGroupElement> getCiphertexts() {
		return ciphertexts;
	}

	/**
	 * @return the re-randomized and permuted ciphertexts (for the shuffling
	 *         session)
	 */
	public ArrayOfElements<ProductGroupElement> getShuffledCiphertexts() {
		return ShuffledCiphertexts;
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

	public void setMixSecretKey(
			ArrayOfElements<IntegerRingElement> mixSecretKey) {
		this.mixSecretKey = mixSecretKey;
	}

	public ArrayOfElements<ArrayOfElements<ProductGroupElement>> getMixCiphertexts() {
		return mixCiphertexts;
	}

	public void setMixCiphertexts(
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> mixCiphertexts) {
		this.mixCiphertexts = mixCiphertexts;
	}

	public ArrayOfElements<ArrayOfElements<IGroupElement>> getMixPermutationCommitment() {
		return mixPermutationCommitment;
	}

	public void setMixPermutationCommitment(
			ArrayOfElements<ArrayOfElements<IGroupElement>> mixPermutationCommitment) {
		this.mixPermutationCommitment = mixPermutationCommitment;
	}

	public ArrayOfElements<Node> getMixPoSCommitment() {
		return mixPoSCommitment;
	}

	public void setMixPoSCommitment(ArrayOfElements<Node> mixPoSCommitment) {
		this.mixPoSCommitment = mixPoSCommitment;
	}

	public ArrayOfElements<Node> getMixPoSReply() {
		return mixPoSReply;
	}

	public void setMixPoSReply(ArrayOfElements<Node> mixPoSReply) {
		this.mixPoSReply = mixPoSReply;
	}

	public ArrayOfElements<Node> getMixPoSCCommitment() {
		return mixPoSCCommitment;
	}

	public void setMixPoSCCommitment(ArrayOfElements<Node> mixPoSCCommitment) {
		this.mixPoSCCommitment = mixPoSCCommitment;
	}

	public ArrayOfElements<Node> getMixPoSCReply() {
		return mixPoSCReply;
	}

	public void setMixPoSCReply(ArrayOfElements<Node> mixPoSCReply) {
		this.mixPoSCReply = mixPoSCReply;
	}

	public ArrayOfElements<Node> getMixCcPosCommitment() {
		return mixCcPosCommitment;
	}

	public void setMixCcPosCommitment(ArrayOfElements<Node> mixCcPosCommitment) {
		this.mixCcPosCommitment = mixCcPosCommitment;
	}

	public ArrayOfElements<Node> getMixCcPosReply() {
		return mixCcPosReply;
	}

	public void setMixCcPosReply(ArrayOfElements<Node> mixCcPosReply) {
		this.mixCcPosReply = mixCcPosReply;
	}

	public ArrayOfElements<BooleanArrayElement> getMixKeepList() {
		return mixKeepList;
	}

	public void setMixKeepList(ArrayOfElements<BooleanArrayElement> mixKeepList) {
		this.mixKeepList = mixKeepList;
	}

	public ArrayOfElements<ArrayOfElements<IGroupElement>> getMixDecryptionFactors() {
		return mixDecryptionFactors;
	}

	public void setMixDecryptionFactors(
			ArrayOfElements<ArrayOfElements<IGroupElement>> mixDecryptionFactors) {
		this.mixDecryptionFactors = mixDecryptionFactors;
	}

	public ArrayOfElements<Node> getMixDecrFactCommitment() {
		return mixDecrFactCommitment;
	}

	public void setMixDecrFactCommitment(
			ArrayOfElements<Node> mixDecrFactCommitment) {
		this.mixDecrFactCommitment = mixDecrFactCommitment;
	}

	public ArrayOfElements<Node> getMixDecrFactReply() {
		return mixDecrFactReply;
	}

	public void setMixDecrFactReply(ArrayOfElements<Node> mixDecrFactReply) {
		this.mixDecrFactReply = mixDecrFactReply;
	}

}
