package cryptographic.primitives;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;

public class SHA2HashFunctionTests {

	@Test (expected = IllegalArgumentException.class) 
	public void noSuchAlgorithmTest() {
		new SHA2HashFunction("daniel");
	}
	
	@Test (expected = IllegalArgumentException.class) 
	public void algorithmNotInSHA2Test() throws NoSuchAlgorithmException {
		new SHA2HashFunction("MD5");
	}
	
	@Test
	public void outlen_SHA256Test() {
		Assert.assertEquals(256, new SHA2HashFunction("SHA-256").outlen());
	}
	
	@Test
	public void outlen_SHA384Test() {
		Assert.assertEquals(384, new SHA2HashFunction("SHA-384").outlen());
	}
	
	@Test
	public void outlen_SHA512Test() {
		Assert.assertEquals(512, new SHA2HashFunction("SHA-512").outlen());
	}
	
	@Test
	public void digest_SHA256Test() throws UnsupportedEncodingException {
		byte[] output = new SHA2HashFunction("SHA-256").digest("Daniel".getBytes());
		Assert.assertEquals("7297db81c2f7916e25b9593f8c8785e1aa1487fa9f3961c50b7cc5f1a541bc82", byteToHexString(output));
		
	}
	
	@Test
	public void digest_SHA384Test() throws UnsupportedEncodingException {
		byte[] output = new SHA2HashFunction("SHA-384").digest("Daniel".getBytes());
		Assert.assertEquals("5d2f4bf1c57128a4c0f355d5302d880030258920b4b2905ea9f8671ee7fd18fa618a056de0c4bfdabc305845c4963958", byteToHexString(output));
		
	}
	
	@Test
	public void digest_SHA512Test() throws UnsupportedEncodingException {
		byte[] output = new SHA2HashFunction("SHA-512").digest("Daniel".getBytes());
		Assert.assertEquals("f5877e17fce9efcc2d5b49635131ae459a75d978134a03b973a41507f403bec51d87f31492ae9236481d06eeda62542943b289784bbcc59b3e55b5c9e835ae27", byteToHexString(output));
		
	}
	
	public static String byteToHexString(byte[] bytes) {		
		StringBuffer result = new StringBuffer();
		for (byte b : bytes) {
		    result.append(String.format("%02X", b));
		}
		return result.toString().toLowerCase();
	}
}
