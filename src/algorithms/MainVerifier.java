package algorithms;


import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Node;

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
		if (!isProtocolInfoValid())
			return false;

		// fill the relevant parameters:
		// versionprot, sid, k, thresh, ne, nr, nv, sH, sPRG, sGq , and wdefault
		// (width);
		if (!params.fillParams())
			return false;

		
		//params.getVersion() = version_proof in the document
		if (!params.getProtVersion().equals(params.getVersion()))
			return false;

		if (!((params.getType().equals("mixing"))
				|| (params.getType().equals("shuffling")) || (params.getType()
				.equals("decryption")))) {
			return false;
		}

		if (!params.getAuxsid().equals(params.getAuxidExp()))
			return false;

		//The document says that widthExp should be NULL, but here we will only assign 0
		if ((params.getWidthExp()==0) && (params.getWidth()!=params.getW()))
			return false;
		
		if ((params.getWidthExp()!=0) && (params.getWidth()!=params.getWidthExp()))
			return false;

		params.setGq(ElementsExtractor.unmarshal(params.getsGq()));

		// TODO: Part 3 in the algorithm.

		
		ByteTree version_proof = btFromString(params.getVersion());
		
		//s = sid|"."|auxid
		String s = params.getSessionID() + "." + params.getAuxsid();

		ByteTree s = btFromString(s);
		ByteTree sGq = btFromString(params.getsGq());
		ByteTree sPRG = btFromString(params.getsPRG());
		ByteTree sH = btFromString(params.getSh());
		
		ByteTree Ne = btFromInt(params.getNe(),4);
		ByteTree Nr = btFromInt(params.getNr(),4);
		ByteTree Nv = btFromInt(params.getNv(),4);
		ByteTree w = btFromInt(params.getW(),4);
		
		Node Seed = Node(version_proof,s,w,Ne,Nr,Nv,sGq,sPRG,sH);
		
		
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
