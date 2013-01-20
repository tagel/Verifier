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
		// parse the command line
		CommandLineParser parser = new CommandLineParser();
		parser.parseCommand(argv);

		if (parser.shouldVerify()) {
			Logger logger = new Logger(parser.getVerbose());
			if (!callMainVerifier(parser, logger)) {				
				logger.sendLog("Exiting", Logger.Severity.NORMAL);
			} else {
				logger.sendLog("Finished verifing successfully", Logger.Severity.NORMAL);
			}
		}
	}

	private static boolean callMainVerifier(CommandLineParser parser, Logger logger) {
		MainVerifier verifier = new MainVerifier(logger);
		return verifier.verify(parser.getXml(), parser.getDir(), parser.getType(),
				parser.getAuxsid(), parser.getWidth(), parser.getPosc(),
				parser.getCcpos(), parser.getDec());
	}
}
