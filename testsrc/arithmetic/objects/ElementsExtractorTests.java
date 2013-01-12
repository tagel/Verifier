package arithmetic.objects;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;
import arithmetic.objects.groups.ECurveGroup;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ModGroup;
import arithmetic.objects.groups.Point;
import cryptographic.primitives.CryptoUtils;

public class ElementsExtractorTests {

	@Test
	public void leafToIntTest() {
		BigIntLeaf bil = new BigIntLeaf(new LargeInteger("263"));
		LargeInteger num = ElementsExtractor.leafToInt(bil.toByteArray());
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
					"000000000200000000020100000021006B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2960100000021004FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5000000000201000000210036AA93BD66B72B139BA90BC91CC80DD97DDC665ECC0270D4D615096C9E37D636010000002100E72999D717E9A8AE3CD2BD01271A10F0B7D74241A20B3B1DF32F8EB071DEC934"
							.toLowerCase(), CryptoUtils
							.bytesToHexString(byteArr));
		} catch (IOException e) {
			System.err.println(e.getMessage());
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
			byte[] byteArr = ElementsExtractor.btFromFile(path, subDir,
					fileName);
			Assert.assertEquals(
					"000000000200000000020100000021006B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2960100000021004FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5000000000201000000210036AA93BD66B72B139BA90BC91CC80DD97DDC665ECC0270D4D615096C9E37D636010000002100E72999D717E9A8AE3CD2BD01271A10F0B7D74241A20B3B1DF32F8EB071DEC934"
							.toLowerCase(), CryptoUtils
							.bytesToHexString(byteArr));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void createGroupElementTest() {
		BigIntLeaf bi = new BigIntLeaf(new LargeInteger("258"));
		ModGroup Gq = new ModGroup(new LargeInteger("263"),
				new LargeInteger("131"), null);
		IGroupElement ige = ElementsExtractor.createGroupElement(
				bi.toByteArray(), Gq);

		try {
			Assert.assertEquals(
					CryptoUtils.bytesToHexString(ige.toByteArray()),
					"01000000020102");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void unmarshalTest() throws UnsupportedEncodingException {
		IGroup Gq = ElementsExtractor
				.unmarshal("ECqPGroup(P-256)::0000000002010000001c766572696669636174756d2e61726974686d2e4543715047726f75700100000005502d323536");
		Assert.assertEquals(Gq.getGroupType(), "Elliptic Curve");
		
		Gq = ElementsExtractor.unmarshal("ModPGroup(safe-prime modulus=2*order+1. order bit-length = 511)::0000000002010000001c766572696669636174756d2e61726974686d2e4d6f645047726f757000000000040100000041009a91c3b704e382e0c772fa7cf0e5d6363edc53d156e841555702c5b6f906574204bf49a551b695bed292e0218337c0861ee649d2fe4039174514fe2c23c10f6701000000404d48e1db8271c17063b97d3e7872eb1b1f6e29e8ab7420aaab8162db7c832ba1025fa4d2a8db4adf69497010c19be0430f7324e97f201c8ba28a7f1611e087b3010000004100300763b0150525252e4989f51e33c4e6462091152ef2291e45699374a3aa8acea714ff30260338bddbb48fc7446b273aaada90e3ee8326f388b582ea8a073502010000000400000001");
		Assert.assertEquals(Gq.getGroupType(), "Modular");
	}
	
	@Test
	public void nodeToPointTest() throws UnsupportedEncodingException {
		IGroup G = new ECurveGroup("P-192");
		IField<IntegerFieldElement> f = new PrimeOrderField(G.getFieldOrder());
		IntegerFieldElement a = new IntegerFieldElement(new LargeInteger("0"), f);
		IntegerFieldElement b = new IntegerFieldElement(new LargeInteger("1"), f);
		Node node = new Node();
		node.add(a);
		node.add(b);
		Point point = ElementsExtractor.nodeToPoint(node.toByteArray(), G);
		Assert.assertEquals(point.getX().getElement(), a.getElement());
		Assert.assertEquals(point.getY().getElement(), b.getElement());
		
	}
}
