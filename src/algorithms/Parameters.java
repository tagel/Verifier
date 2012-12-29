package algorithms;

import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.BooleanArrayElement;
import arithmetic.objects.FieldElement;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroup;
import arithmetic.objects.Node;
import arithmetic.objects.ProductElement;

/**
 * This class describes an object that contains the parameters used by the
 * verifying algorithms
 * 
 * @author Tagel & Sofi
 */
public class Parameters {

	public Parameters(String protInfo, String directory, String type,
			java.lang.String auxsid, BigInteger w, boolean posc, boolean ccpos,
			boolean dec) {
		super();
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
		version = null;
		type = null;
		auxsid = null;
		w = null;
		fullPublicKey = null;
		protVersion = null;
		sessionID = null;
		numOfParties = 0;
		threshold = 0;
		Ne = null;
		Nr = null;
		Nv = null;
		sh = null;
		sGq = null;
		sPRG = null;
		wDefault = null;
		maxciph = 0;
		}

	public String getDirectory() {
		return directory;
	}

	private byte[] prefixToRO;
	private IGroup Gq;

	// parameters from directory
	private String protInfo;
	private String directory;
	private String version;
	private String type;
	private String auxsid;
	private BigInteger w;
	private ProductElement fullPublicKey;
	private int maxciph;

	// parameters from the XML
	private String protVersion;
	private String sessionID;
	private int numOfParties;
	private int threshold;
	private BigInteger Ne;
	private BigInteger Nr;
	private BigInteger Nv;
	private String sh;
	private String sGq;
	private String sPRG;
	private BigInteger wDefault;

	// parameters from CMD
	private String typeExpected;
	private boolean posc;
	private boolean ccpos;
	private boolean dec;
	private String auxidExp;
	private BigInteger widthExp;
	
	//MIX -- The parameters of each party
	private ArrayOfElements<GroupElement> mixPublicKey;
	private ArrayOfElements<FieldElement> mixSecretKey;
	
	private ArrayOfElements<ArrayOfElements<GroupElement>> mixCiphertexts;
	private ArrayOfElements<ArrayOfElements<GroupElement>> mixPermutationCommitment;
	private ArrayOfElements<Node> mixPoSCommitment;
	private ArrayOfElements<Node> mixPoSReply;
	private ArrayOfElements<Node> mixPoSCCommitment;
	private ArrayOfElements<Node> mixPoSCReply;
	private ArrayOfElements<Node> mixCcPosCommitment;
	private ArrayOfElements<Node> mixCcPosReply;
	
	private ArrayOfElements<BooleanArrayElement> mixKeepList;
	private ArrayOfElements<ArrayOfElements<GroupElement>> mixDecryptionFactors;
	private ArrayOfElements<Node> mixDecrFactCommitment;
	private ArrayOfElements<Node> mixDecrFactReply;
	
	// parameters from lists
	private ArrayOfElements<GroupElement> ciphertexts;
	private ArrayOfElements<GroupElement> ShuffledCiphertexts;
	private ArrayOfElements<GroupElement> plaintexts;
	
	
	// fill the relevant parameters from the given xml
	// fill the relevant parameters:
	// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
	// (width);
	public boolean fillFromXML() {
		//All of these arrays should be of size threshold
		//TODO: need to initialize the arrays of size threshold
		mixPublicKey = null;
		mixSecretKey = null;
		mixCiphertexts = null;
		mixPermutationCommitment = null;
		mixPoSCommitment = null;
		mixPoSReply = null;
		mixPoSCCommitment = null;
		mixPoSCReply = null;
		mixCcPosCommitment = null;
		mixCcPosReply = null;
		mixKeepList = null;
		mixDecryptionFactors = null;
		mixDecrFactCommitment = null;
		mixDecrFactReply = null;

		return true;
	}

	// fill the relevant parameters from the given directory
	// fill version_proof(Version) type, auxid, w from proof directory
	public boolean fillFromDirectory() {
		return true;
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
	public ProductElement getFullPublicKey() {
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
	public boolean getPosc() {
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
	public boolean getCcpos() {
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
	public boolean getDec() {
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

	// *****FROM XML*******

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
	public BigInteger getNe() {
		return Ne;
	}

	/**
	 * @return the acceptable statistical error when sampling random values.
	 */
	public BigInteger getNr() {
		return Nr;
	}

	/**
	 * @return the number of bits used in the challenge of the verifier in
	 *         zero-knowledge proofs.
	 */
	public BigInteger getNv() {
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
	public BigInteger getW() {
		return w;
	}

	public BigInteger getwDefault() {
		return wDefault;
	}

	public void setwDefault(BigInteger wDefault) {
		this.wDefault = wDefault;
	}

	public void setFullPublicKey(ProductElement fullPublicKey) {
		this.fullPublicKey = fullPublicKey;
	}

	
	/**
	 * @return the input ciphertexts
	 */
	public ArrayOfElements<GroupElement> getCiphertexts() {
		return ciphertexts;
	}

	/**
	 * @return the re-randomized and permuted ciphertexts (for the shuffling
	 *         session)
	 */
	public ArrayOfElements<GroupElement> getShuffledCiphertexts() {
		return ShuffledCiphertexts;
	}

	/**
	 * @return the output plaintext elements that has not been decoded in any
	 *         way
	 */
	public ArrayOfElements<GroupElement> getPlaintexts() {
		return plaintexts;
	}

	public String getAuxidExp() {
		return auxidExp;
	}

	public void setAuxidExp(String auxidExp) {
		this.auxidExp = auxidExp;
	}

	public BigInteger getWidthExp() {
		return widthExp;
	}

	public void setWidthExpected(BigInteger widthExpected) {
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
	
	public ArrayOfElements<GroupElement> getMixPublicKey() {
		return mixPublicKey;
	}

	public void setMixPublicKey(ArrayOfElements<GroupElement> mixPublicKey) {
		this.mixPublicKey = mixPublicKey;
	}

	public ArrayOfElements<FieldElement> getMixSecretKey() {
		return mixSecretKey;
	}

	public void setMixSecretKey(ArrayOfElements<FieldElement> mixSecretKey) {
		this.mixSecretKey = mixSecretKey;
	}

	public ArrayOfElements<ArrayOfElements<GroupElement>> getMixCiphertexts() {
		return mixCiphertexts;
	}

	public void setMixCiphertexts(
			ArrayOfElements<ArrayOfElements<GroupElement>> mixCiphertexts) {
		this.mixCiphertexts = mixCiphertexts;
	}

	public ArrayOfElements<ArrayOfElements<GroupElement>> getMixPermutationCommitment() {
		return mixPermutationCommitment;
	}

	public void setMixPermutationCommitment(
			ArrayOfElements<ArrayOfElements<GroupElement>> mixPermutationCommitment) {
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

	public ArrayOfElements<ArrayOfElements<GroupElement>> getMixDecryptionFactors() {
		return mixDecryptionFactors;
	}

	public void setMixDecryptionFactors(
			ArrayOfElements<ArrayOfElements<GroupElement>> mixDecryptionFactors) {
		this.mixDecryptionFactors = mixDecryptionFactors;
	}

	public ArrayOfElements<Node> getMixDecrFactCommitment() {
		return mixDecrFactCommitment;
	}

	public void setMixDecrFactCommitment(ArrayOfElements<Node> mixDecrFactCommitment) {
		this.mixDecrFactCommitment = mixDecrFactCommitment;
	}

	public ArrayOfElements<Node> getMixDecrFactReply() {
		return mixDecrFactReply;
	}

	public void setMixDecrFactReply(ArrayOfElements<Node> mixDecrFactReply) {
		this.mixDecrFactReply = mixDecrFactReply;
	}

}
