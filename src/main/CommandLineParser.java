package main;

import algorithms.params.Parameters.Type;

// TODO Daniel - Test
public class CommandLineParser {

	private Type type = null;
	private String xml;
	private String dir;
	private String auxsid = "default";
	private int width = 0;
	private boolean verify = false;
	private boolean posc = true;
	private boolean ccpos = true;
	private boolean dec = true;

	public void parseCommand(String[] argv) {
		// missing arguments - print command line usage
		if (argv.length == 1 || argv.length == 3) {
			printCommandLineUsage();
			return;
		}

		// case there are only 2 words in the command
		if (argv.length == 2) {
			if (argv[1] == "-compat") {
				printCompat();
				return;
			}
			// no match for the command - print the command line usage
			printCommandLineUsage();
			return;
		}

		// case there are 4 or more words in the command line;
		parseVerifier(argv);
		if (verify == false) { // command was not properly written
			printCommandLineUsage();
		}
	}

	/**
	 * 
	 * @return if the parser succeeded parsing the command and it is a correct
	 *         verification command
	 */
	public boolean shouldVerify() {
		return verify;
	}

	/**
	 * 
	 * @return the xml file name entered in the command line
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * 
	 * @return the dir path entered in the command line
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * 
	 * @return the auxsid
	 */
	public String getAuxsid() {
		return auxsid;
	}

	/**
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the posc
	 */
	public boolean getPosc() {
		return posc;
	}

	/**
	 * 
	 * @return the ccpos
	 */
	public boolean getCcpos() {
		return ccpos;
	}

	/**
	 * 
	 * @return the dec
	 */
	public boolean getDec() {
		return dec;
	}

	/**
	 * 
	 * @return the type of verification should be called, null if there is none
	 */
	public Type getType() {
		return type;
	}

	// parse which proof to verify 
	private void parseVerifier(String[] argv) {
		fillXmlAndDir(argv);
		
		if (argv[1] == "-mix") {
			verify = verifyProofOfMix(argv);
		} else if (argv[1] == "-shuffle") {
			verify = verifyProofOfShuffling(argv);
		} else if (argv[1] == "-decrypt") {
			verify = verifyProofOfDecryption(argv);
		}
	}

	// MIX Proof
	private boolean verifyProofOfMix(String[] argv) {
		type = Type.MIXING;

		// TODO Daniel - parse flags
		return true;
	}

	// SHUFFLE Proof
	private boolean verifyProofOfShuffling(String[] argv) {
		type = Type.SHUFFLING;
		setFalgNodec();

		// TODO Daniel - parse flags
		return true;
	}

	// DECRYPT Proof
	private boolean verifyProofOfDecryption(String[] argv) {
		type = Type.DECRYPTION;
		setFalgNopos();

		// TODO Daniel - parse flags
		return true;
	}

	// outputs a space separated list of all versions of Verificatum for
	// which the verifier is compatible.
	private void printCompat() {
		// TODO Daniel - Auto-generated method stub
	}

	// use when command line entered couldn't be parsed
	private void printCommandLineUsage() {
		// System.out.println("verifier usage: verifier [-command] [xml-file-path] [dir-path]");
		// System.out.println("verifier commands:");
		// System.out.println("	-");
		// TODO Daniel - print how to use the command line
	}

	private void fillXmlAndDir(String[] argv) {
		xml = argv[2];
		dir = argv[3];
	}

	private void setFalgNopos() {
		posc = false;
		ccpos = false;
	}

	private void setFalgNoposc() {
		posc = false;
	}

	private void setFalgNoccpos() {
		ccpos = false;
	}

	private void setFalgNodec() {
		dec = false;
	}
	
	private boolean auxsidFalge(String auxsid) {
		// TODO add check
		this.auxsid = auxsid;
		return true;
	}
	
	private boolean widthFalge(String width) {
		try { 
			this.width = Integer.parseInt(width); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		return true;
	}

}
