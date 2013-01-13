package arithmetic.objects.basicelements;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for StringLeaf class.
 * 
 * @author Daniel
 *
 */
public class StringLeafTest {

	private String str = "ABCD";
	private StringLeaf strLeaf = new StringLeaf(str);

	@Test
	public void getStringTest() {
		Assert.assertEquals(str, strLeaf.getString());
	}

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		Assert.assertEquals(
				"0100000004"
						+ CryptoUtils.bytesToHexString(str.getBytes("ASCII")),
				CryptoUtils.bytesToHexString(strLeaf.toByteArray()));
	}
}
