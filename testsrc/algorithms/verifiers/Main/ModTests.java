package algorithms.verifiers.Main;

import main.Logger;

import org.junit.Assert;
import org.junit.Test;

import algorithms.params.Parameters.Type;
import algorithms.verifiers.MainVerifier;

public class ModTests {

	private final Logger logger = new MockedLogger();
	
	//****************SHUFFLE*******************

	@Test
	public void VerifyShuffleModGroupTest() throws Exception {

		MainVerifier mainVer = new MainVerifier(logger);
		Assert.assertTrue(mainVer.verify(
				getClass().getClassLoader()
						.getResource("exportModGroup/protInfo.xml").getFile(),
				getClass().getClassLoader()
						.getResource("exportModGroup/default").getFile(),
				Type.MIXING, "default", 1, true, true, false));
	}

	@Test
	public void VerifyShuffleModGroupSmallTest() throws Exception {

		MainVerifier mainVer = new MainVerifier(logger);
		Assert.assertTrue(mainVer.verify(getClass().getClassLoader()
				.getResource("exportSmall/protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("exportSmall/default").getFile(),
				Type.MIXING, "default", 1, true, true, false));
	}

	@Test
	public void VerifyShuffleModGroup3Test() throws Exception {

		MainVerifier mainVer = new MainVerifier(logger);
		Assert.assertTrue(mainVer.verify(getClass().getClassLoader()
				.getResource("exportMod3/protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("exportMod3/default").getFile(),
				Type.MIXING, "default", 1, true, true, false));
	}
	
	@Test
	public void VerifyShuffleModWidth3lTest() throws Exception {

		MainVerifier mainVer = new MainVerifier(logger);
		Assert.assertTrue(mainVer.verify(
				getClass().getClassLoader()
						.getResource("exportModWidth3/protInfo.xml").getFile(),
				getClass().getClassLoader()
						.getResource("exportModWidth3/default").getFile(),
				Type.MIXING, "default", 3, true, true, false));
	}
	
	
	//******************DECRYPTION*******************

	@Test
	public void VerifyDecModGroup3Test() throws Exception {

		MainVerifier mainVer = new MainVerifier(logger);
		Assert.assertTrue(mainVer.verify(getClass().getClassLoader()
				.getResource("exportMod3/protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("exportMod3/default").getFile(),
				Type.MIXING, "default", 1, false, false, true));
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