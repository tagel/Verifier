package arithmetic.objects;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.BasicElements.BigIntLeaf;
import arithmetic.objects.BasicElements.StringLeaf;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ModGroup;
import arithmetic.objects.Groups.ModGroupElement;

import cryptographic.primitives.CryptoUtils;

public class ElementsExtractorTests {

	@Test
	public void leafToIntTest() {
		BigIntLeaf bil = new BigIntLeaf(new BigInteger("263"));
		BigInteger num = ElementsExtractor.leafToInt(bil.toByteArray());
		Assert.assertEquals(num.intValue(), 263);
	}

	@Test
	public void leafToStringTest() {
		StringLeaf sl = new StringLeaf("abcde45");
		try {
			String str = ElementsExtractor.leafToString(sl.toByteArray());
			Assert.assertEquals(str, "abcde45");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void btFromFileTest() {
		Assert.assertNotNull(
				"res is not in the classpath - ask Tagel",
				getClass().getClassLoader().getResource(
						"export\\default\\FullPublicKey.bt"));

		File f = new File(getClass().getClassLoader()
				.getResource("export/default/FullPublicKey.bt").getFile());
		String path = f.getParent();
		String fileName = f.getName();

		try {
			byte[] byteArr = ElementsExtractor.btFromFile(path, fileName);
			Assert.assertEquals(
					"000000000200000000020100000021006B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2960100000021004FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5000000000201000000210036AA93BD66B72B139BA90BC91CC80DD97DDC665ECC0270D4D615096C9E37D636010000002100E72999D717E9A8AE3CD2BD01271A10F0B7D74241A20B3B1DF32F8EB071DEC934".toLowerCase(),
					CryptoUtils.bytesToHexString(byteArr));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void btFromFileWithSubDirTest() {
		Assert.assertNotNull(
				"res is not in the classpath - ask Tagel",
				getClass().getClassLoader().getResource(
						"export\\default\\FullPublicKey.bt"));

		File f = new File(getClass().getClassLoader()
				.getResource("export/default/FullPublicKey.bt").getFile());
		
		String path = f.getParentFile().getParent();
		String subDir = "default";
		String fileName = "FullPublicKey.bt";

		try {
			byte[] byteArr = ElementsExtractor.btFromFile(path, subDir, fileName);
			Assert.assertEquals(
					"000000000200000000020100000021006B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2960100000021004FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5000000000201000000210036AA93BD66B72B139BA90BC91CC80DD97DDC665ECC0270D4D615096C9E37D636010000002100E72999D717E9A8AE3CD2BD01271A10F0B7D74241A20B3B1DF32F8EB071DEC934".toLowerCase(),
					CryptoUtils.bytesToHexString(byteArr));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createGroupElementTest() {
		BigInteger bi = BigInteger.valueOf(258);
		ModGroup Gq = new ModGroup(BigInteger.valueOf(263),BigInteger.valueOf(131),null);
		//IGroupElement ige = new ModGroupElement(bi, Gq);
		IGroupElement ige = ElementsExtractor.createGroupElement(bi.toByteArray(), Gq);
		try {
			byte[] ans = ige.toByteArray();
			Assert.assertEquals(CryptoUtils.bytesToHexString(ige.toByteArray()), "01000000020102");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	@Test
	public void concatArraysTest() {
		byte[] byteArr0 = new BigIntLeaf(new BigInteger("0")).toByteArray(); // expecting 100010
		byte[] byteArr1 = new BigIntLeaf(new BigInteger("1")).toByteArray(); // expecting 100011
		byte[] ans = ElementsExtractor.concatArrays(byteArr0, byteArr1); // 100010100011 which is 257
		BigInteger BIans = new BigInteger(ans);
		Assert.assertEquals(BIans.intValue(), 257);
	}


}
