package algorithms;

import arithmetic.objects.IGroup;

/**
 * This class describes an object that contains the parameters used by the
 * verifying algorithms
 * 
 * @author Tagel & Sofi
 */
public class Parameters {

	private byte[] prefixToRO;
	private IGroup Gq; 

	// parameters from directory
	private String version;
	private String type;
	private String auxsid;
	private int width;
	private ProductGroupElement fullPublicKey;
	private int maxciph;

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
	private int w;

	// parameters from CMD
	private boolean posc;
	private boolean ccpos;
	private boolean dec;
	private String auxidExp;
	private int widthExp;

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
	 * @return the width w>0 of a ciphertext
	 */
	public int getWidth() {
		return width;
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

	// parameters from lists
	private ArrayOfElements<GroupElement> ciphertexts;
	private ArrayOfElements<GroupElement> ShuffledCiphertexts;
	private AuxidExp<GroupElement> plaintexts;

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

	/**
	 * Fills the parameters (from xml, directory, lists but NOT from the cmd)
	 */
	public boolean fillParams() {
		return (fillFromDirectory() && fillFromXML());

	}

	/**
	 * Fills the relevant parameters from the given lists
	 */
	public boolean readLists() {
		return true;
	}

	// fill the relevant parameters from the given xml
	private boolean fillFromXML() {
		return true;
	}

	// fill the relevant parameters from the given directory
	private boolean fillFromDirectory() {
		return true;
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

}
