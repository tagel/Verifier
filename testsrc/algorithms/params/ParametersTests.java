package algorithms.params;

import org.junit.Assert;
import org.junit.Test;

public class ParametersTests {
	@Test
	public void testDirectory() {
		Parameters params = new Parameters(getClass().getClassLoader().getResource("protInfo.xml").getFile(), 
				getClass().getClassLoader().getResource("export/default").getFile(), "type",
				"auxsid", 1, false, false, false);
		Assert.assertNotNull("res is not in the classpath - ask Daniel", getClass().getClassLoader().getResource("export/default/proofs"));
		
		Assert.assertTrue(params.fillFromXML());
		Assert.assertEquals("",params.getProtVersion());
		
		params.fillFromDirectory();
		
		
	}
}
