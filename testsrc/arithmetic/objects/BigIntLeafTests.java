package arithmetic.objects;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.BasicElements.BigIntLeaf;

import cryptographic.primitives.CryptoUtils;

public class BigIntLeafTests {
	
	@Test 
	public void toByteArrayTest() {
		BigIntLeaf bil = new BigIntLeaf(new BigInteger("263"));
		Assert.assertEquals("01000000020107", CryptoUtils.bytesToHexString(bil.toByteArray()));
		
		bil = new BigIntLeaf(new BigInteger("-263"));
		Assert.assertEquals("0100000002fef9", CryptoUtils.bytesToHexString(bil.toByteArray()));
	}
	
	@Test 
	public void getNumTest() {
		BigInteger bi = new BigInteger("263");
		BigIntLeaf bil = new BigIntLeaf(bi);
		Assert.assertEquals(bi, bil.getNum());
		
		bi = new BigInteger("-263");
		bil = new BigIntLeaf(bi);
		Assert.assertEquals(bi, bil.getNum());
	}
}
