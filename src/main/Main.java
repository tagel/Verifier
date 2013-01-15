package main;

/**
 * The main class - parsing the command line and calling the correct verifiers
 * accordingly.
 * 
 * @author Daniel
 * 
 */

public class Main {

	private static String xml;
	private static String dir;

	private String auxsid = "default";
	private static int width = 0;
	private static boolean posc;
	private static boolean ccpos;
	private static boolean dec;

	public static void main(String[] argv) {
		parseCommand(argv);
	}

	private static void parseCommand(String[] argv) {
		// no match for the command - print the command line usage
		if (argv.length < 2) {
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

		// case there are more than 2 words in the command line but less than 4
		if (argv.length < 4) {
			printCommandLineUsage();
			return;
		}

		// case there are 4 or more words in the command line
		if (argv[1] == "-mix") {
			verifyProofOfMix(argv);
			return;
		}

		if (argv[1] == "-shuffle") {
			verifyProofOfShuffling(argv);
			return;
		}

		if (argv[1] == "-decrypt") {
			verifyProofOfDecryption(argv);
			return;
		}

		// no match for the command - print the command line usage
		printCommandLineUsage();
		return;
	}

	// MIX
	private static void verifyProofOfMix(String[] argv) {
		// TODO Daniel - mix case
		posc = true;
		ccpos = true;
		dec = true;

		fillXmlAndDir(argv);
	}

	// SHUFFLE
	private static void verifyProofOfShuffling(String[] argv) {
		// TODO Daniel - shuffle case
		posc = true;
		ccpos = true;
		dec = false;

		fillXmlAndDir(argv);
	}

	// DECRYPT
	private static void verifyProofOfDecryption(String[] argv) {
		// TODO Daniel - decryption case
		posc = false;
		ccpos = false;
		dec = true;

		fillXmlAndDir(argv);
	}

	// outputs a space separated list of all versions of Verificatum for
	// which the verifier is compatible.
	private static void printCompat() {
		// TODO Daniel - Auto-generated method stub
	}

	private static void printCommandLineUsage() {
		// TODO Daniel - print how to use the command line
	}

	private static void fillXmlAndDir(String[] argv) {
		xml = argv[2];
		dir = argv[3];
	}

	private static void callMainVerifier() {
		// TODO Daniel
		// MainVerifier verifier = new MainVerifier(params, H);

	}
}
