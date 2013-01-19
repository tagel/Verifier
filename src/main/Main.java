package main;

import algorithms.verifiers.MainVerifier;

/**
 * The main class - parsing the command line and calling the correct verifiers
 * accordingly.
 * 
 * @author Daniel
 * 
 */

public class Main {

	public static void main(String[] argv) {
		CommandLineParser parser = new CommandLineParser();
		parser.parseCommand(argv);

		if (parser.shouldVerify()) {
			callMainVerifier(parser);
		}
	}

	private static void callMainVerifier(CommandLineParser parser) {
		MainVerifier verifier = new MainVerifier();
		verifier.verify(parser.getXml(), parser.getDir(), parser.getType(),
				parser.getAuxsid(), parser.getWidth(), parser.getPosc(),
				parser.getCcpos(), parser.getDec());

	}
}
