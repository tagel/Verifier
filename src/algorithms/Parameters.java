package algorithms;

import arithmetic.objects.*;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.GroupElement;
import arithmetic.objects.IGroup;


/**
 * This class describes an object that contains the parameters used by the
 * verifying algorithms
 * 
 * @author Tagel & Sofi
 */
public class Parameters {

	MixParams[] mix;
	
	// The following parameters are created during the run
	private byte[] prefixToRO;
	private IGroup Gq;

	// parameters from directory
	private String version;
	private String type;
	private String auxsid;
	private int w;
	private ProductElement fullPublicKey;
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
	private int wDeafult;

	// parameters from CMD
	private boolean posc;
	private boolean ccpos;
	private boolean dec;
	private String auxidExp;
	private int widthExp;
	private String directory;
	private String protInfo;
	private String type_expected;

	// From lists
	private ArrayOfElements<GroupElement> ciphertexts;
	private ArrayOfElements<GroupElement> ShuffledCiphertexts;
	private ArrayOfElements<GroupElement> plaintexts;

	/**
	 * Fills the params from the cmd
	 */
	public Parameters(String protInfo, String directory, String type,
			String auxid, int w, boolean posc, boolean ccpos, boolean dec) {
		this.posc = posc;
		this.ccpos = ccpos;
		this.dec = dec;
		this.auxidExp = auxid;
		this.widthExp = w;
		this.directory = directory;
		this.protInfo = protInfo;
		this.type_expected = type;

		prefixToRO = null;
		Gq = null;
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
		wDeafult = 0;
		maxciph = 0;
		
		mix = null;
		
		}

	// fill the relevant parameters from the given xml
	// fill the relevant parameters:
	// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
	// (width);
	public boolean fillFromXML() {
		mix = new MixParams[threshold];
		return true;
	}

	// fill the relevant parameters from the given directory
	// fill version_proof(Version) type, auxid, w from proof directory
	public boolean fillFromDirectory() {
		return true;
	}

	/**
	 * Fills the relevant parameters from the given lists
	 */
	public boolean readLists() {
		return true;
	}
	
		
	//******Getters and Setters***********
	public byte[] getPrefixToRO() {
		return prefixToRO;
	}

	public void setPrefixToRO(byte[] prefixToRO) {
		this.prefixToRO = prefixToRO;
	}

	public IGroup getGq() {
		return Gq;
	}

	public void setGq(IGroup gq) {
		Gq = gq;
	}

	public ProductElement getFullPublicKey() {
		return fullPublicKey;
	}

	public void setFullPublicKey(ProductElement fullPublicKey) {
		this.fullPublicKey = fullPublicKey;
	}

	public ArrayOfElements<GroupElement> getCiphertexts() {
		return ciphertexts;
	}

	public void setCiphertexts(ArrayOfElements<GroupElement> ciphertexts) {
		this.ciphertexts = ciphertexts;
	}

	public ArrayOfElements<GroupElement> getShuffledCiphertexts() {
		return ShuffledCiphertexts;
	}

	public void setShuffledCiphertexts(
			ArrayOfElements<GroupElement> shuffledCiphertexts) {
		ShuffledCiphertexts = shuffledCiphertexts;
	}

	public ArrayOfElements<GroupElement> getPlaintexts() {
		return plaintexts;
	}

	public void setPlaintexts(ArrayOfElements<GroupElement> plaintexts) {
		this.plaintexts = plaintexts;
	}

	public String getVersion() {
		return version;
	}

	public String getType() {
		return type;
	}

	public String getAuxsid() {
		return auxsid;
	}

	public int getW() {
		return w;
	}

	public String getProtVersion() {
		return protVersion;
	}

	public String getSessionID() {
		return sessionID;
	}

	public int getNumOfParties() {
		return numOfParties;
	}

	public int getThreshold() {
		return threshold;
	}

	public int getNe() {
		return Ne;
	}

	public int getNr() {
		return Nr;
	}

	public int getNv() {
		return Nv;
	}

	public String getSh() {
		return sh;
	}

	public String getsGq() {
		return sGq;
	}

	public String getsPRG() {
		return sPRG;
	}

	public int getwDeafult() {
		return wDeafult;
	}

	public boolean isPosc() {
		return posc;
	}

	public boolean isCcpos() {
		return ccpos;
	}

	public boolean isDec() {
		return dec;
	}

	public String getAuxidExp() {
		return auxidExp;
	}

	public int getWidthExp() {
		return widthExp;
	}

	public String getDirectory() {
		return directory;
	}

	public String getProtInfo() {
		return protInfo;
	}

	public String getType_expected() {
		return type_expected;
	}

	public void setMaxciph(int maxciph) {
		this.maxciph = maxciph;
	}
		


}
