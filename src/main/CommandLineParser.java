package main;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import algorithms.params.Parameters.Type;

/**
 * Parser that can be used to parse command lines and determine if its legal
 * command and whether it is needed to run the MainVerifier.
 * 
 * @author Daniel
 * 
 */

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
	private boolean verbose = false;

	private CharsetEncoder asciiEncoder = Charset.forName("US-ASCII")
			.newEncoder();

	/**
	 * Parse the given command line, make sure it is in the right format and the
	 * flags are legal and determine if the main verifier should run or not.
	 * 
	 * @param argv
	 *            the command line in String[], split to words.
	 */
	public void parseCommand(String[] argv) {
		// missing arguments - print command line usage
		if (argv.length == 0 || argv.length == 2) {
			printCommandLineUsage();
			return;
		}

		// case there are only 2 words in the command
		if (argv.length == 1) {
			if (argv[0].equals("-compat")) {
				printCompat();
				return;
			}
			// no match for the command - print the command line usage
			printCommandLineUsage();
			return;
		}

		// case there are 4 or more words in the command line;
		if (!parseVerifier(argv)) {
			return;
		}
		
		if (!parseFlags(argv)) {
			// if there were unfamiliar flags - print command line usage
			printCommandLineUsage();
			return;
		}
		verify = true;
	}

	private boolean parseFlags(String[] argv) {
		fillXmlAndDir(argv);
		for (int i = 3; i < argv.length; i++) {
			if ("-auxsid".equals(argv[i])) {
				if ((i + 1) == argv.length || !auxsidFalge(argv[i + 1])) {
					return false;
				}
				i++;
			} else if ("-width".equals(argv[i])) {
				if ((i + 1) == argv.length || !widthFalge(argv[i + 1])) {
					return false;
				}
				i++;
			} else if ("-nopos".equals(argv[i])) {
				if (!setFalgNopos()) {
					return false;
				}
			} else if ("-noposc".equals(argv[i])) {
				if (!setFalgNoposc()) {
					return false;
				}
			} else if ("-noccpos".equals(argv[i])) {
				if (!setFalgNoccpos()) {
					return false;
				}
			} else if ("-v".equals(argv[i])) {
				this.verbose = true;
			} else {
				return false;
			}
		}
		return posc || ccpos || dec; // case all false is not legal
	}

	// parse type of prove to verify
	private boolean parseVerifier(String[] argv) {
		if (argv[0].equals("-mix")) {
			type = Type.MIXING;
			return true;
		} else if (argv[0].equals("-shuffle")) {
			setFalgNodec();
			type = Type.SHUFFLING;
			return true;
		} else if (argv[0].equals("-decrypt")) {
			setFalgNopos();
			type = Type.DECRYPTION;
			return true;
		} else {
			printCommandLineUsage();
			return false;
		}
	}

	private void fillXmlAndDir(String[] argv) {
		xml = argv[1];
		dir = argv[2];
	}

	private boolean setFalgNopos() {
		if (Type.DECRYPTION.equals(type)) {
			return false;
		}
		posc = false;
		ccpos = false;
		return true;
	}

	private boolean setFalgNoposc() {
		if (Type.DECRYPTION.equals(type)) {
			return false;
		}
		posc = false;
		return true;
	}

	private boolean setFalgNoccpos() {
		if (Type.DECRYPTION.equals(type)) {
			return false;
		}
		ccpos = false;
		return true;
	}

	private boolean setFalgNodec() {
		if (Type.DECRYPTION.equals(type)) {
			return false;
		}
		dec = false;
		return true;
	}

	private boolean widthFalge(String width) {
		try {
			this.width = Integer.parseInt(width);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean auxsidFalge(String auxsid) {
		if (!asciiEncoder.canEncode(auxsid)) {
			return false;
		}

		this.auxsid = auxsid;
		return true;
	}

	// outputs a space separated list of all versions of Verificatum for
	// which the verifier is compatible.
	private void printCompat() {
		System.out.println("Compatible versions of Verificatum: 1.1.0");
	}

	// use when command line entered couldn't be parsed
	private void printCommandLineUsage() {
		System.out
				.println("------------------------------------------------------------------------------");
		System.out
				.println("Wrong usage! please follow the usage instructions:");
		System.out
				.println("__________________________________________________");
		System.out.println();
		System.out
				.println("Windows users - instead of 'verifier' use 'verifier.bat'");
		System.out
				.println("Linux users - instead of 'verifier' use 'verifier.sh'");
		System.out.println();
		System.out
				.println("1. verifier -compat                         : list of compatible versions");
		System.out
				.println("2. verifier -mix protInfo.xml directory     : verify proof of mixing ");
		System.out
				.println("3. verifier -shuffle protInfo.xml directory : verify proof of shuffling");
		System.out
				.println("4. verifier -decrypt protInfo.xml directory : verify proof of decryption");
		System.out.println();
		System.out
				.println("Commands 2-4 can be used with the following optional flags:");
		System.out
				.println("[ -auxsid, -width, -nopos, -noposc, -noccpos, -nodec ]");
		System.out.println();
		System.out
				.println("protInfo - Path to protocol info file manufactured by Verificatum.");
		System.out
				.println("directory- Path to default directory which contains needed information.");
		System.out
				.println("for all commands, you can use the '-v' option to turn on verbose output.");
		System.out
				.println("------------------------------------------------------------------------------");
	}

	/**
	 * @return if the parser succeeded parsing the command and it is a correct
	 *         verification command
	 */
	public boolean shouldVerify() {
		return verify;
	}

	/**
	 * @return the xml file name entered in the command line
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * @return the dir path entered in the command line
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @return the auxsid
	 */
	public String getAuxsid() {
		return auxsid;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the posc
	 */
	public boolean getPosc() {
		return posc;
	}

	/**
	 * @return the ccpos
	 */
	public boolean getCcpos() {
		return ccpos;
	}

	/**
	 * @return the dec
	 */
	public boolean getDec() {
		return dec;
	}

	/**
	 * @return the type of verification should be called, null if there is none
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return true if the -v flag was set
	 */
	public boolean getVerbose() {
		return verbose;
	}
}
