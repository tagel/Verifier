package main;

import java.io.IOException;

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
			try {
				callMainVerifier(parser);
			} catch (IOException e) {
				// TODO Daniel - talk to sofi (shouldn't throw exception)
				e.printStackTrace();
			}
		}
	}

	private static void callMainVerifier(CommandLineParser parser)
			throws IOException {
		MainVerifier verifier = new MainVerifier();
		try {
			verifier.verify(parser.getXml(), parser.getDir(), parser.getType(),
					parser.getAuxsid(), parser.getWidth(), parser.getPosc(),
					parser.getCcpos(), parser.getDec());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
