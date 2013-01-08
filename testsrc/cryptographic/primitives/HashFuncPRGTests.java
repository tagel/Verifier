package cryptographic.primitives;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

public class HashFuncPRGTests {

	@Test
	public void seedlenSHA256Test() {
		Assert.assertEquals(256, new HashFuncPRG(
				new SHA2HashFunction("SHA-256")).seedlen());
	}

	@Test
	public void seedlenSHA384Test() {
		Assert.assertEquals(384, new HashFuncPRG(
				new SHA2HashFunction("SHA-384")).seedlen());
	}

	@Test
	public void seedlenSHA512Test() {
		Assert.assertEquals(512, new HashFuncPRG(
				new SHA2HashFunction("SHA-512")).seedlen());
	}

	@Test
	public void getNextPRGOutput_SHA256OnePartTest() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-256"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"));
		Assert.assertEquals(
				"70f4003d52b6eb03da852e93256b5986b5d4883098bb7973bc5318cc66637a84"
						+ "04a6950a06d3e3308ad7d3606ef810eb124e3943404ca746a12c51c7bf776839"
						+ "0f8d842ac9cb62349779a7537a78327d545aaeb33b2d42c7d1dc3680a4b23628"
						+ "627e9db8ad47bfe76dbe653d03d2c0a35999ed28a5023924150d72508668d244",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(128 * 8)));
	}

	@Test
	public void getNextPRGOutput_SHA256InPartsTest()
			throws UnsupportedEncodingException {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-256"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"));
		Assert.assertEquals(
				"70f4003d52b6eb03da852e93256b5986b5d4883098bb7973bc5318cc66637a84",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(32 * 8)));

		Assert.assertEquals(
				"04a6950a06d3e3308ad7d3606ef810eb124e3943404ca746a12c51c7bf776839",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(32 * 8)));

		Assert.assertEquals(
				"0f8d842ac9cb62349779a7537a78327d545aaeb33b2d42c7d1dc3680a4b23628",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(32 * 8)));

		Assert.assertEquals(
				"627e9db8ad47bfe76dbe653d03d2c0a35999ed28a5023924150d72508668d244",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(32 * 8)));

	}

	@Test
	public void getNextPRGOutput_SHA384OnePartTest() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-384"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f"));
		Assert.assertEquals(
				"e45ac6c0cafff343b268d4cbd773328413672a764df99ab823b53074d94152bd"
						+ "27fc38bcffdb7c1dc1b6a3656b2d4819352c482da40aad3b37f333c7afa81a92"
						+ "b7b54551f3009efa4bdb8937492c5afca1b141c99159b4f0f819977a4e10eb51"
						+ "61edd4b1734717de4106f9c184a17a9b5ee61a4399dd755f322f5d707a581cc1",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(128 * 8)));
	}

	@Test
	public void getNextPRGOutput_SHA384InParts1Test() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-384"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f"));
		Assert.assertEquals(
				"e45ac6c0cafff343b268d4cbd773328413672a764df99ab823b53074d94152bd"
						+ "27fc38bcffdb7c1dc1b6a3656b2d4819352c482da40aad3b37f333c7afa81a92",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(64 * 8)));
		Assert.assertEquals(
				"b7b54551f3009efa4bdb8937492c5afca1b141c99159b4f0f819977a4e10eb51"
						+ "61edd4b1734717de4106f9c184a17a9b5ee61a4399dd755f322f5d707a581cc1",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(64 * 8)));
	}

	@Test
	public void getNextPRGOutput_SHA384InParts2Test() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-384"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f"));
		Assert.assertEquals(
				"e45ac6c0cafff343b268d4cbd773328413672a764df99ab823b53074d94152bd"
						+ "27fc38bcffdb7c1dc1b6a3656b2d4819",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(48 * 8)));
		Assert.assertEquals(
				"352c482da40aad3b37f333c7afa81a92b7b54551f3009efa4bdb8937492c5afc",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(32 * 8)));
	}

	@Test
	public void getNextPRGOutput_SHA512OnePartTest() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-512"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"
						+ "202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f"));
		Assert.assertEquals(
				"979043771043f4f8e0a2a19b1fbfbe5a8f076c2b5ac003e0b9619e0c45faf767"
						+ "47295734980602ec1d8d3cd249c165b7db62c976cb9075e35d94197c0f06e1f3"
						+ "97a45017c508401d375ad0fa856da3dfed20847716755c6b03163aec2d9f43eb"
						+ "c2904f6e2cf60d3b7637f656145a2d32a6029fbda96361e1b8090c9712a48938",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(128 * 8)));
	}

	@Test
	public void getNextPRGOutput_SHA512InPartsTest() {
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-512"));
		prg.setSeed(CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"
						+ "202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f"));

		Assert.assertEquals(
				"979043771043f4f8e0a2a19b1fbfbe5a8f076c2b5ac003e0b9619e0c45faf767"
						+ "47295734980602ec1d8d3cd249c165b7db62c976cb9075e35d94197c0f06e1f3",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(64 * 8)));
		Assert.assertEquals(
				"97a45017c508401d375ad0fa856da3dfed20847716755c6b03163aec2d9f43eb"
						+ "c2904f6e2cf60d3b7637f656145a2d32",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(48 * 8)));
		Assert.assertEquals("a6029fbda96361e1b8090c9712a48938",
				CryptoUtils.bytesToHexString(prg.getNextPRGOutput(16 * 8)));
	}

	// private static String bytesToHexString(byte[] array) {
	// return DatatypeConverter.printHexBinary(array).toLowerCase();
	// }
	//
	// private static byte[] hexStringToBytes(String s) {
	// return DatatypeConverter.parseHexBinary(s);
	// }
}
