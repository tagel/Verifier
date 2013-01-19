package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;

/**
 * Tests for the ModGroup class.
 * @author Itay
 *
 */
public class ModGroupTest {

	private byte[] b = { 0, 0, 0, 0, 3, 1, 0, 0, 0, 2, 1, 7, 1, 0, 0, 0, 2, 0,
			-125, 1, 0, 0, 0, 1, 2 };
	private ModGroup group1 = new ModGroup(new LargeInteger("263"),
			new LargeInteger("131"), new LargeInteger("2"));

	@Test
	public void oneTest() throws UnsupportedEncodingException {
		Assert.assertEquals(new LargeInteger("1"), group1.one().getElement());
		ModGroup group2 = new ModGroup(b);
		Assert.assertEquals(new LargeInteger("1"), group2.one().getElement());
	}

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		Assert.assertEquals(Arrays.toString(b),
				Arrays.toString(group1.toByteArray()));
		ModGroup group2 = new ModGroup(b);
		Assert.assertEquals(Arrays.toString(b),
				Arrays.toString(group2.toByteArray()));
	}

	@Test
	public void getFieldOrderTest() throws UnsupportedEncodingException {
		Assert.assertEquals(new LargeInteger("263"), group1.getFieldOrder());
		ModGroup group2 = new ModGroup(b);
		Assert.assertEquals(new LargeInteger("263"), group2.getFieldOrder());
	}

	@Test
	public void getOrderTest() throws UnsupportedEncodingException {
		Assert.assertEquals(new LargeInteger("131"), group1.getOrder());
		ModGroup group2 = new ModGroup(b);
		Assert.assertEquals(new LargeInteger("131"), group2.getOrder());
	}

	@Test
	public void getGeneratorTest() throws UnsupportedEncodingException {
		Assert.assertEquals(new LargeInteger("2"), group1.getGenerator()
				.getElement());
		ModGroup group2 = new ModGroup(b);
		Assert.assertEquals(new LargeInteger("2"), group2.getGenerator()
				.getElement());
	}
}
