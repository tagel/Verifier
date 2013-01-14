package main;

/**
 * The main class - parsing the command line and calling the correct verifiers
 * accordingly.
 * 
 * @author Daniel
 * 
 */
// TODO Daniel - ask sofi - maybe main verify should get another arg saying
// which
// verify to run
public class Main {

	private static String xml;
	private static String dir;

	private String auxsid = "default";
	// TODO Daniel - ask tagel/sofi what the default is?
	private static int width;
	private static boolean posc;
	private static boolean ccpos;
	private static boolean dec;

	public static void main(String[] argv) {

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

	private static void verifyProofOfDecryption(String[] argv) {
		// TODO Daniel - decryption case
		posc = false;
		ccpos = false;
		dec = true;

		fillXmlAndDir(argv);
	}

	private static void verifyProofOfShuffling(String[] argv) {
		// TODO Daniel - shuffle case
		posc = true;
		ccpos = true;
		dec = false;

		fillXmlAndDir(argv);
	}

	private static void verifyProofOfMix(String[] argv) {
		// TODO Daniel - mix case
		posc = true;
		ccpos = true;
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
		// TODO Daniel - check if need to test the xml and dir path correctness
		xml = argv[2];
		dir = argv[3];
	}
}
