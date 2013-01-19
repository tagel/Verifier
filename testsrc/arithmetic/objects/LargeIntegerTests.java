package arithmetic.objects;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for LargeInteger class.
 * 
 * @author Daniel
 * 
 */
public class LargeIntegerTests {

	private static final int i = 100;

	@Test
	public void largeIntegerFromByteArrayTest() {
		LargeInteger large = new LargeInteger(CryptoUtils.intToByteArray(i));
		Assert.assertEquals(i, large.longValue());
	}

	@Test
	public void largeIntegerFromStringTest() {
		LargeInteger large = new LargeInteger(Integer.toString(i));
		Assert.assertEquals(i, large.longValue());
	}

	@Test
	public void largeIntegerFromBigIntegerTest() {
		LargeInteger large = new LargeInteger(BigInteger.valueOf(i));
		Assert.assertEquals(i, large.longValue());
	}

	@Test(expected = NumberFormatException.class)
	public void LargeIntegerFromStringNotInRadixTest() {
		LargeInteger large = new LargeInteger("4", 3);
		Assert.assertEquals(1, large.longValue());
	}

	public void LargeIntegerFromStringInRadixTest() {
		LargeInteger large = new LargeInteger("1", 3);
		Assert.assertEquals(1, large.longValue());
	}

	@Test
	public void addTest() {
		LargeInteger large = new LargeInteger("4");
		Assert.assertEquals(new LargeInteger("5"),
				large.add(new LargeInteger("1")));
	}

	@Test
	public void multiplyTest() {
		LargeInteger large = new LargeInteger("4");
		Assert.assertEquals(large, large.multiply(new LargeInteger("1")));
	}

	@Test
	public void subtractTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("4"),
				large.subtract(new LargeInteger("1")));
	}

	@Test
	public void divideTest() {
		LargeInteger large = new LargeInteger("4");
		Assert.assertEquals(large, large.divide(new LargeInteger("1")));
	}

	@Test
	public void remainderTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("1"),
				large.remainder(new LargeInteger("4")));
	}

	@Test
	public void powerWithLargeIntegerTest() throws Exception {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("25"),
				LargeInteger.power(large, new LargeInteger("2")));
	}

	@Test
	public void powerWithIntTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("25"), large.power(2));
	}

	@Test
	public void modTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("1"),
				large.mod(new LargeInteger("2")));
	}

	@Test
	public void modPowTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("5"),
				large.modPow(new LargeInteger("2"), new LargeInteger("20")));
	}

	@Test
	public void modInverseTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(new LargeInteger("0"),
				large.modInverse(new LargeInteger("1")));
	}

	@Test
	public void compareToTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(-1, large.compareTo(new LargeInteger("6")));
		Assert.assertEquals(0, large.compareTo(new LargeInteger("5")));
		Assert.assertEquals(1, large.compareTo(new LargeInteger("4")));
	}

	@Test
	public void minTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(large, large.min(new LargeInteger("6")));
		Assert.assertEquals(new LargeInteger("3"),
				large.min(new LargeInteger("3")));
	}

	@Test
	public void bitLengthTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(3, large.bitLength());
	}

	@Test
	public void toByteArrayTest() {
		LargeInteger large = new LargeInteger("5");
		Assert.assertEquals(1, large.toByteArray().length);
		Assert.assertEquals("05",
				CryptoUtils.bytesToHexString(large.toByteArray()));
	}
}
