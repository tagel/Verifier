package arithmetic.objects.arrays;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ModGroup;
import arithmetic.objects.groups.ModGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.Ring;
import cryptographic.primitives.CryptoUtils;

/**
 * Tests for ArrayGenerators.
 * 
 * @author tagel
 * 
 */
public class ArraysGeneretorsTests {

	private Ring ring_4 = new Ring(new LargeInteger("4"));
	private IntegerRingElement ire1_ring4 = new IntegerRingElement(
			new LargeInteger("1"), ring_4);
	private IntegerRingElement ire2_ring4 = new IntegerRingElement(
			new LargeInteger("2"), ring_4);
	private Node node = new Node();

	@Test
	public void createGroupElementArrayTest()
			throws UnsupportedEncodingException {
		ModGroup Gq = new ModGroup(new LargeInteger("263"), new LargeInteger(
				"131"), null);
		BigIntLeaf b0 = new BigIntLeaf(new LargeInteger("0"));
		BigIntLeaf b1 = new BigIntLeaf(new LargeInteger("1"));
		IGroupElement ige0 = ElementsExtractor.createGroupElement(
				b0.toByteArray(), Gq);
		IGroupElement ige1 = ElementsExtractor.createGroupElement(
				b1.toByteArray(), Gq);
		Node node = new Node();
		node.add(ige0);
		node.add(ige1);
		ArrayOfElements<IGroupElement> arr = ArrayGenerators
				.createGroupElementArray(node.toByteArray(), Gq);
		Assert.assertEquals(CryptoUtils.bytesToHexString(arr.toByteArray()),
				"00000000020100000002000001000000020001");

		// TODO: implement test for elliptic curve!
	}

	@Test
	public void createRingElementArray() throws UnsupportedEncodingException {
		node.add(ire1_ring4);
		node.add(ire2_ring4);
		ArrayOfElements<IntegerRingElement> ring = ArrayGenerators
				.createRingElementArray(node.toByteArray(), ring_4);
		Assert.assertEquals("0000000002010000000101010000000102",
				CryptoUtils.bytesToHexString(ring.toByteArray()));
	}

	@Test
	public void createArrayOfPlaintextsTest()
			throws UnsupportedEncodingException {
		ModGroup group = new ModGroup(new LargeInteger("263"),
				new LargeInteger("100"), new LargeInteger("2"));
		LargeInteger b0 = new LargeInteger("6");
		LargeInteger b1 = new LargeInteger("7");
		LargeInteger b2 = new LargeInteger("8");
		ModGroupElement ire0 = new ModGroupElement(b0, group);
		ModGroupElement ire1 = new ModGroupElement(b1, group);
		ModGroupElement ire2 = new ModGroupElement(b2, group);
		Node node = new Node();
		node.add(ire0);
		node.add(ire1);
		node.add(ire2);
		ArrayOfElements<IGroupElement> arr = ArrayGenerators
				.createGroupElementArray(node.toByteArray(), group);
		ProductGroupElement pre1 = new ProductGroupElement(arr);
		ProductGroupElement pre2 = new ProductGroupElement(arr);
		ArrayOfElements<ProductGroupElement> arr2 = new ArrayOfElements<ProductGroupElement>();
		arr2.add(pre1);
		arr2.add(pre2);
		ArrayOfElements<ProductGroupElement> plaintexts = ArrayGenerators
				.createArrayOfPlaintexts(arr2.toByteArray(), group, 3);

		byte[] b = { 0, 0, 0, 0, 3, 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 0, 6, 1, 0,
				0, 0, 2, 0, 6, 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 0, 7, 1, 0, 0, 0,
				2, 0, 7, 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 0, 8, 1, 0, 0, 0, 2, 0,
				8 };
		Assert.assertEquals(Arrays.toString(b),
				Arrays.toString(plaintexts.toByteArray()),
				Arrays.toString(arr2.toByteArray()));
	}

	@Test
	public void concatArraysTest() {
		byte[] byteArr0 = new BigIntLeaf(new LargeInteger("0")).toByteArray(); // 100010
		byte[] byteArr1 = new BigIntLeaf(new LargeInteger("1")).toByteArray(); // 100011
		byte[] ans = ArrayGenerators.concatArrays(byteArr0, byteArr1); // 100010100011
		LargeInteger BIans = new LargeInteger(ans);
		Assert.assertEquals(BIans.intValue(), 257);
	}

	@Test
	public void createArrayOfCiphertextsTest()
			throws UnsupportedEncodingException {
		// Parameters params = new
		// Parameters(getClass().getClassLoader().getResource("protInfo.xml").getFile(),
		// getClass().getClassLoader().getResource("export/default").getFile(),
		// "type", "auxsid", 1, false, false, false);
		// params.fillFromXML();
		// params.fillFromDirectory();
		// HashFunction H = new SHA2HashFunction(params.getSh());
		// MainVerifier mainVer = new MainVerifier(params,H);
		// mainVer.deriveSetsAndObjects();
		byte[] b = { 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 0, 5, 1, 0, 0, 0,
				1, 7, 1, 0, 0, 0, 1, 7, 1, 0, 0, 0, 1, 7, 1, 0, 0, 0, 1, 7, 1,
				0, 0, 0, 1, 7, 0, 0, 0, 0, 5, 1, 0, 0, 0, 1, 8, 1, 0, 0, 0, 1,
				8, 1, 0, 0, 0, 1, 8, 1, 0, 0, 0, 1, 8, 1, 0, 0, 0, 1, 8, 0, 0,
				0, 0, 5, 1, 0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9,
				1, 0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9, 0, 0, 0, 0, 3, 0, 0, 0, 0,
				5, 1, 0, 0, 0, 1, 7, 1, 0, 0, 0, 1, 7, 1, 0, 0, 0, 1, 7, 1, 0,
				0, 0, 1, 7, 1, 0, 0, 0, 1, 7, 0, 0, 0, 0, 5, 1, 0, 0, 0, 1, 8,
				1, 0, 0, 0, 1, 8, 1, 0, 0, 0, 1, 8, 1, 0, 0, 0, 1, 8, 1, 0, 0,
				0, 1, 8, 0, 0, 0, 0, 5, 1, 0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9, 1,
				0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9, 1, 0, 0, 0, 1, 9 };
		IGroup group = new ModGroup(new LargeInteger("100"), new LargeInteger(
				"100"), new LargeInteger("2"));
		ArrayOfElements<ProductGroupElement> arr = ArrayGenerators
				.createArrayOfCiphertexts(b, group, 3);
		System.out.println(arr.getSize());
		for (int i = 0; i < arr.getSize(); i++) {
			System.out.println("left:");
			for (int j = 0; j < arr.getAt(i).getLeft().getSize(); j++)
				System.out.println(Arrays.toString(((arr.getAt(i)
						.getLeft().getElements().getAt(j)).toByteArray())));
			System.out.println("right:");
			for (int j = 0; j < arr.getAt(i).getLeft().getSize(); j++)
				System.out.println(Arrays.toString(arr.getAt(i).getRight()
						.getElements().getAt(j).toByteArray()));

		}
		System.out.println(arr.getSize());
		Assert.assertEquals(Arrays.toString(b),
				Arrays.toString(arr.toByteArray()));
	}
	
}
