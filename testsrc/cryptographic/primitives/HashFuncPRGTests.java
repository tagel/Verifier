package cryptographic.primitives;

import junit.framework.Assert;

import org.junit.Test;

public class HashFuncPRGTests {

	@Test
	public void seedlenSHA256Test() {
		Assert.assertEquals(256, new HashFuncPRG(new SHA2HashFunction("SHA-256")).seedlen());
	}
	
	@Test
	public void seedlenSHA384Test() {
		Assert.assertEquals(384, new HashFuncPRG(new SHA2HashFunction("SHA-384")).seedlen());
	}
	
	@Test
	public void seedlenSHA512Test() {
		Assert.assertEquals(512, new HashFuncPRG(new SHA2HashFunction("SHA-512")).seedlen());
	}
}
