package cryptographic.primitives;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for class HashFuncPRGRandomOracle.
 * @author Daniel
 *
 */
public class HashFuncPRGRandomOracleTests {

	@Test
	public void getRandomOracleOutput_SHA256_65Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-256"), 65);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals("001a8d6b6f65899ba5",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}

	@Test
	public void getRandomOracleOutput_SHA256_261Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-256"), 261);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals(
				"1c04f57d5f5856824bca3af0ca466e283593bfc556ae2e9f4829c7ba8eb76db878",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}

	@Test
	public void getRandomOracleOutput_SHA384_93Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-384"), 93);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals("04713a5e22935833d436d1db",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}

	@Test
	public void getRandomOracleOutput_SHA384_411Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-384"), 411);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals(
				"00dc086c320e38b92722a9c0f87f2f5de81b976400e2441da542d1c3f3f391e41d6bcd8297c541c2431a7272491f496b622266aa",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}

	@Test
	public void getRandomOracleOutput_SHA512_111Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-512"), 111);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals("28d742c34b97367eb968a3f28b6c",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}

	@Test
	public void getRandomOracleOutput_SHA512_579Test() {
		RandomOracle ro = new HashFuncPRGRandomOracle(new SHA2HashFunction(
				"SHA-512"), 579);
		byte[] roInput = CryptoUtils
				.hexStringToBytes("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");
		Assert.assertEquals(
				"00a6f79b8450fef79af71005c0b1028c9f025f322f1485c2b245f658fe641d47"
						+ "dcbb4fe829e030b52e4a81ca35466ad1ca9be6feccb451e7289af318ddc9dae0"
						+ "98a5475d6119ff6fe0",
				CryptoUtils.bytesToHexString(ro.getRandomOracleOutput(roInput)));
	}
}
