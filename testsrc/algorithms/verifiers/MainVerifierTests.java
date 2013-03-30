package algorithms.verifiers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import main.Logger;

import org.junit.Assert;
import org.junit.Test;

import algorithms.params.Parameters;
import algorithms.params.Parameters.Type;
import cryptographic.primitives.CryptoUtils;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.SHA2HashFunction;

/**
 * Tests for the MainVerifier class.
 * 
 * @author Sofi & Tagel & Itay & Daniel
 * 
 */
public class MainVerifierTests {

	private final Logger logger = new MockedLogger();

	@Test
	public void deriveSetsAndObjectsTest() {
		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("export/default").getFile(),
				null, "auxsid", 1, false, false, false, logger);
		params.fillFromXML();
		params.fillFromDirectory();
		HashFunction H = new SHA2HashFunction(params.getSh());
		MainVerifier mainVer = new MainVerifier(params, H);
		Assert.assertTrue(mainVer.deriveSetsAndObjects());
	}

	@Test
	public void createPrefixToRoTest() throws UnsupportedEncodingException {
		//Elliptic Curve
		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("export/default").getFile(),
				null, "auxsid", 1, false, false, false, logger);
		
		//Mod Group
		/*
		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("exportModGroup/protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("exportModGroup/default").getFile(),
				null, "auxsid", 1, false, false, false, logger); */
		
		params.fillFromXML();
		params.fillFromDirectory();
		HashFunction H = new SHA2HashFunction(params.getSh());
		MainVerifier mainVer = new MainVerifier(params, H);
		mainVer.deriveSetsAndObjects();
		Assert.assertNull(params.getPrefixToRO());
		mainVer.createPrefixToRo();
		byte[] prefix = params.getPrefixToRO();
		Assert.assertEquals(CryptoUtils.bytesToHexString(prefix),
				"26c5c23d1cfb4d192b4d04fbb8171fc0e2a710cb4a28fbe3d0df06ff490507fb");
	}

	@Test
	public void readKeysTest() throws IOException {
		// Elliptic curve
		/*
		 * Parameters params = new Parameters(getClass().getClassLoader()
		 * .getResource("protInfo.xml").getFile(), getClass()
		 * .getClassLoader().getResource("export/default").getFile(), null,
		 * "auxsid", 1, false, false, false, logger);
		 */

		// Mod group
		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("exportModGroup/protInfo.xml").getFile(),
				getClass().getClassLoader()
						.getResource("exportModGroup/default").getFile(), null,
				"auxsid", 1, false, false, false, logger);

		params.fillFromXML();
		params.fillFromDirectory();
		HashFunction H = new SHA2HashFunction(params.getSh());
		MainVerifier mainVer = new MainVerifier(params, H);
		mainVer.deriveSetsAndObjects();
		Assert.assertTrue(mainVer.ReadKeys());
		Assert.assertNotNull(params.getMixPublicKey());
		Assert.assertEquals(2, params.getMixPublicKey().getSize());
		// TODO - test case secret key does exits
		Assert.assertNull(params.getMixSecretKey().getAt(0));
		Assert.assertTrue(params.getProtVersion().equals(params.getVersion()));
		Assert.assertEquals(params.getwDefault(), params.getW());
	}

	@Test
	public void readListsTest() throws IOException {
		//Elliptc curve
		/*Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("export/default").getFile(),
				Type.MIXING, "auxsid", 1, false, false, false, logger);*/
		
		//Mod group
		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("exportModGroup/protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("exportModGroup/default").getFile(),
				Type.MIXING, "auxsid", 1, false, false, false, logger);
		
		params.fillFromXML();
		params.fillFromDirectory();
		HashFunction H = new SHA2HashFunction(params.getSh());
		MainVerifier mainVer = new MainVerifier(params, H);
		mainVer.deriveSetsAndObjects();
		Assert.assertTrue(mainVer.ReadLists());
		Assert.assertEquals(mainVer.getParams().getN(), 100);

	}

	public static class MockedLogger extends Logger {
		public MockedLogger() {
			super(false);
		}

		@Override
		public void sendLog(String message, Severity severity) {
		}
	}
}
