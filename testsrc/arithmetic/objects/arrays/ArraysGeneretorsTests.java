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

		//TODO: implement test for elliptic curve!
	}

	@Test
	public void concatArraysTest() {
		byte[] byteArr0 = new BigIntLeaf(new LargeInteger("0")).toByteArray(); // expecting
																				// 100010
		byte[] byteArr1 = new BigIntLeaf(new LargeInteger("1")).toByteArray(); // expecting
																				// 100011
		byte[] ans = ArrayGenerators.concatArrays(byteArr0, byteArr1); // 100010100011
																		// which
																		// is
																		// 257
		LargeInteger BIans = new LargeInteger(ans);
		Assert.assertEquals(BIans.intValue(), 257);
	}
}
