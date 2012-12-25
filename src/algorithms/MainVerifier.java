


import cryptographic.primitives.HashFunction;
import arithmetic.objects.*;

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
			String auxid, int w, boolean posc, boolean ccpos, boolean dec) {

		params = new Parameters(protInfo, directory, type, auxid, w, posc, ccpos, dec);
		
		if (!isProtocolInfoValid())
			return false;
		
		// fill the relevant parameters:
		// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
		// (width);
		if (!params.fillFromXML());
			return false;
		
			
		//fill version_proof(Version) type, auxid, w from proof directory
		if (!params.fillFromDirectory())
			return false;

		
		//params.getVersion() = version_proof in the document
		if (!params.getProtVersion().equals(params.getVersion()))
			return false;

		if (!(params.getType().equals(params.getType_expected())))
				return false;

		if (!params.getAuxsid().equals(params.getAuxidExp()))
			return false;

		//The document says that widthExp should be NULL, but here we will only assign 0
		//Document:		Code:
		//w_expected	w_expected
		//w				w
		//w_deafult		wDeafult
		if ((params.getWidthExp()==0) && (params.getW()!=params.getwDeafult()))
			return false;
		
		if ((params.getWidthExp()!=0) && (params.getW()!=params.getWidthExp()))
			return false;

		ElementsExtractor elem = new ElementsExtractor();
		params.setGq(elem.unmarshal(params.getsGq()));

		// TODO: Part 3 in the algorithm. The things with Cw
		
		HashFunction H = new Hashfunction(params.getSh());
		PseudoRandomGenerator PRG = new PseudoRandomGenerator(new Hashfunction(params.getsPRG()));
		
		ByteTree version_proof = btFromString(params.getVersion());
		
		//s = sid|"."|auxid
		String s = params.getSessionID() + "." + params.getAuxsid();

		ByteTree btAuxid = btFromString(s);
		ByteTree sGq = btFromString(params.getsGq());
		ByteTree sPRG = btFromString(params.getsPRG());
		ByteTree sH = btFromString(params.getSh());
		
		ByteTree Ne = btFromInt(params.getNe(),4);
		ByteTree Nr = btFromInt(params.getNr(),4);
		ByteTree Nv = btFromInt(params.getNv(),4);
		ByteTree btW = btFromInt(params.getW(),4);
		
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
		// TODO Auto-generated method stub
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
}
