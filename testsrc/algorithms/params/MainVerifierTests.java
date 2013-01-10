package algorithms.params;

import org.junit.Assert;
import org.junit.Test;

import algorithms.MainVerifier;

public class MainVerifierTests {

	@Test
	public void ReadListsTest() {
	}
	
	@Test
	public void deriveSetsAndObjectsTest() {
		Parameters params = new Parameters(getClass().getClassLoader().getResource("protInfo.xml").getFile(), getClass().getClassLoader().getResource("export/default").getFile(), "type", "auxsid", 1, false, false, false);
		params.fillFromXML();
		params.fillFromDirectory();
		MainVerifier mainVer = new MainVerifier(params);
		Assert.assertTrue(mainVer.deriveSetsAndObjects());
	}
	
	public void createPrefixToRoTest() {
		
	}

//		TODO: read keysTest
}
