package algorithms.params;

import org.junit.Assert;
import org.junit.Test;

public class ParametersTests {

	private Parameters params = new Parameters(getClass().getClassLoader()
			.getResource("protInfo.xml").getFile(), getClass().getClassLoader()
			.getResource("export/default").getFile(), null, "auxsid", 1,
			false, false, false);

	@Test
	public void fillFromXMLTest() {
		Assert.assertNotNull("res is not in the classpath - ask Daniel",
				getClass().getClassLoader()
						.getResource("export/default/proofs"));

		Assert.assertTrue(params.fillFromXML());
		Assert.assertEquals("", params.getProtVersion());
		Assert.assertEquals("MyDemo", params.getSessionID());
		Assert.assertEquals(3, params.getNumOfParties());
		Assert.assertEquals(2, params.getThreshold());
	}

	@Test
	public void fillFromDirectoryTest() {
		Assert.assertTrue(params.fillFromDirectory());
		Assert.assertEquals("", params.getVersion());
		Assert.assertEquals("default", params.getAuxsid());
		Assert.assertEquals(1, params.getW());
	}
}

