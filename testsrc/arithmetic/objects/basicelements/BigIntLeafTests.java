package arithmetic.objects.basicelements;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.BigIntLeaf;
import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class BigIntLeaf.
 * 
 * @author Daniel
 */
public class BigIntLeafTests {

	@Test
	public void toByteArrayTest() {
		BigIntLeaf bil = new BigIntLeaf(new LargeInteger("263"));
		Assert.assertEquals("01000000020107",
				CryptoUtils.bytesToHexString(bil.toByteArray()));

		bil = new BigIntLeaf(new LargeInteger("-263"));
		Assert.assertEquals("0100000002fef9",
				CryptoUtils.bytesToHexString(bil.toByteArray()));
		
		bil = new BigIntLeaf(new LargeInteger("100"), 4);
		Assert.assertEquals("010000000400000064",
				CryptoUtils.bytesToHexString(bil.toByteArray()));
		
	}

	@Test
	public void getNumTest() {
		LargeInteger bi = new LargeInteger("263");
		BigIntLeaf bil = new BigIntLeaf(bi);
		Assert.assertEquals(bi, bil.getNum());

		bi = new LargeInteger("-263");
		bil = new BigIntLeaf(bi);
		Assert.assertEquals(bi, bil.getNum());
	}
}

