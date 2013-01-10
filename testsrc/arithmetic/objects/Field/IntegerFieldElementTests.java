package arithmetic.objects.Field;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

public class IntegerFieldElementTests {

	private PrimeOrderField pof_263 = new PrimeOrderField(
			BigInteger.valueOf(263));
	private PrimeOrderField pof_4 = new PrimeOrderField(BigInteger.valueOf(4));

	@Test
	public void toByteArraytest() {
		Assert.assertEquals("01000000020102", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(
						new BigInteger("258"), pof_263).toByteArray()));
		Assert.assertEquals("01000000020005", CryptoUtils
				.bytesToHexString(new IntegerFieldElement(new BigInteger("5"),
						pof_263).toByteArray()));
	}

	@Test
	public void equalTest() {
		IntegerFieldElement ife = new IntegerFieldElement(new BigInteger("1"),
				pof_4);
		Assert.assertTrue(ife.equal(new IntegerFieldElement(
				new BigInteger("1"), pof_4)));
		Assert.assertTrue(ife.equal(new IntegerFieldElement(
				new BigInteger("5"), pof_4)));
	}
}
