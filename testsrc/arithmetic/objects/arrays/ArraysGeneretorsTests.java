package arithmetic.objects.arrays;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.basicelements.BigIntLeaf;

public class ArraysGeneretorsTests {

	@Test
	public void concatArraysTest() { 
		byte[] byteArr0 = new BigIntLeaf(new BigInteger("0")).toByteArray(); // expecting 100010
		byte[] byteArr1 = new BigIntLeaf(new BigInteger("1")).toByteArray(); // expecting 100011
		byte[] ans = ArrayGenerators.concatArrays(byteArr0, byteArr1); // 100010100011 which is 257
		BigInteger BIans = new BigInteger(ans);
		Assert.assertEquals(BIans.intValue(), 257);
	}
}