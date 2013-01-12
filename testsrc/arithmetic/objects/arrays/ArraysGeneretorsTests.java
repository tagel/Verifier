package arithmetic.objects.arrays;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ModGroup;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.Ring;

public class ArraysGeneretorsTests {

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
		Ring ring_4 = new Ring(new LargeInteger("4"));
		IntegerRingElement ire1_ring4 = new IntegerRingElement(
				new LargeInteger("1"), ring_4);
		IntegerRingElement ire2_ring4 = new IntegerRingElement(
				new LargeInteger("2"), ring_4);
		Node node = new Node();
		node.add(ire1_ring4);
		node.add(ire2_ring4);
		ArrayOfElements<IntegerRingElement> ring = ArrayGenerators
				.createRingElementArray(node.toByteArray(), ring_4);
		Assert.assertEquals("0000000002010000000101010000000102",
				CryptoUtils.bytesToHexString(ring.toByteArray()));
	}

	@Test
	public void concatArraysTest() {
		byte[] byteArr0 = new BigIntLeaf(new LargeInteger("0")).toByteArray(); // 100010
		byte[] byteArr1 = new BigIntLeaf(new LargeInteger("1")).toByteArray(); // 100011
		byte[] ans = ArrayGenerators.concatArrays(byteArr0, byteArr1); // 100010100011
		LargeInteger BIans = new LargeInteger(ans);
		Assert.assertEquals(BIans.intValue(), 257);
	}
}
