package algorithms;
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

		if (!params.getProtVersion().equals(params.getVersion()))
			return false;

		if (!((params.getType().equals("mixing"))
				|| (params.getType().equals("shuffling")) || (params.getType()
				.equals("decryption")))) {
			return false;
		}

		if (!params.getAuxsid().equals(params.getAuxidExp()))
			return false;

		if (params.getWidth() != params.getW())
			return false;

		// TODO: Width expected = null?

		params.setGq(ElementsExtractor.unmarshal(params.getsGq()));

		// TODO: Part 3 in the algorithm.

		String s = params.getSessionID() + "." + params.getAuxsid();

		ByteTree s = btFromString(s);
		ByteTree sGq = btFromString(params.getsGq());
		ByteTree sPRG = btFromString(params.getsPRG());
		ByteTree sH = btFromString(params.getSh());
		
		ByteTree Ne = btFromInt(params.getNe());
		ByteTree Nr = btFromInt(params.getNr());
		ByteTree Nv = btFromInt(params.getNv());
		ByteTree w = btFromInt(params.getW());
		
		Node Seed = Node(s,w,Ne,Nr,Nv,sGq,sPRG,sH);
		
		

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
