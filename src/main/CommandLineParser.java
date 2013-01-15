package main;

import java.io.IOException;

import algorithms.params.Parameters.Type;
import algorithms.verifiers.MainVerifier;

public class CommandLineParser {
	private String xml;
	private String dir;

	private Type type;
	private String auxsid = "default";
	private int width = 0;
	private boolean posc;
	private boolean ccpos;
	private boolean dec;

	public void parseCommand(String[] argv) {
		// no match missing arguments - print command line usage
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
		try {
			if (!parseVerifier(argv)) {
				// no match for the command - print the command line usage
				printCommandLineUsage();
				return;
			}
		} catch (IOException e) {
			// TODO Daniel - verifier throws IOException - print Error
		}
	}

	private boolean parseVerifier(String[] argv) throws IOException {
		boolean verify = false;

		if (argv[1] == "-mix") {
			verify = verifyProofOfMix(argv);
		} else if (argv[1] == "-shuffle") {
			verify = verifyProofOfShuffling(argv);
		} else if (argv[1] == "-decrypt") {
			verify = verifyProofOfDecryption(argv);
		} 

		if (verify) {
			callMainVerifier();
			return true;
		}
		return false;
	}

	// MIX
	private boolean verifyProofOfMix(String[] argv) {
		type = Type.MIXING;
		posc = true;
		ccpos = true;
		dec = true;

		fillXmlAndDir(argv);
		// TODO Daniel - parse flags
		return true;
	}

	// SHUFFLE
	private boolean verifyProofOfShuffling(String[] argv) {
		type = Type.SHUFFLING;
		posc = true;
		ccpos = true;
		dec = false;

		fillXmlAndDir(argv);
		// TODO Daniel - parse flags
		return true;
	}

	// DECRYPT
	private boolean verifyProofOfDecryption(String[] argv) {
		type = Type.DECRYPTION;
		posc = false;
		ccpos = false;
		dec = true;

		fillXmlAndDir(argv);
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
		// TODO Daniel - print how to use the command line
	}

	private void fillXmlAndDir(String[] argv) {
		xml = argv[2];
		dir = argv[3];
	}

	private void callMainVerifier() throws IOException {
		MainVerifier verifier = new MainVerifier();
		verifier.verify(xml, dir, type, auxsid, width, posc, ccpos, dec);
	}
}
