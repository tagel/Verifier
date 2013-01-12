package arithmetic.objects;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.basicelements.BooleanArrayElement;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for booleanArray object.
 * 
 * @author tagel
 *
 */
public class BooleanArrayTests {
	@Test
	public void toByteArrayTest() {

		boolean[] arrOfBooleans1 = {true,false,true};
		BooleanArrayElement bae = new BooleanArrayElement(arrOfBooleans1);
		Assert.assertEquals("0100000003010001", CryptoUtils.bytesToHexString(bae.toByteArray()));
		
		boolean[] arrOfBooleans2 = {true,true,false};
		bae = new BooleanArrayElement(arrOfBooleans2);
		Assert.assertEquals("0100000003010100", CryptoUtils.bytesToHexString(bae.toByteArray()));
		
		byte[] byteArr = bae.toByteArray();
		BooleanArrayElement bob = new BooleanArrayElement(byteArr);
		boolean[] boolArr = bob.getBooleanArray();
		Assert.assertEquals(true, boolArr[0]);
		Assert.assertEquals(true, boolArr[1]);
		Assert.assertEquals(false, boolArr[2]);
	}
	
	@Test
	public void BooleanArrayTest() {

		boolean[] arrOfBooleans = {true,true,false};
		BooleanArrayElement bae = new BooleanArrayElement(arrOfBooleans);
		
		byte[] byteArr = bae.toByteArray();
		BooleanArrayElement bob = new BooleanArrayElement(byteArr);
		boolean[] boolArr = bob.getBooleanArray();
		Assert.assertEquals(true, boolArr[0]);
		Assert.assertEquals(true, boolArr[1]);
		Assert.assertEquals(false, boolArr[2]);
	}
}
