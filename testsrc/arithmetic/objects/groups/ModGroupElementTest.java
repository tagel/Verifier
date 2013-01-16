package arithmetic.objects.groups;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;

public class ModGroupElementTest {

	private ModGroup group1 = new ModGroup(new LargeInteger("263"), new LargeInteger("131"), new LargeInteger("2"));


	ModGroupElement a = new ModGroupElement(new LargeInteger("1"), group1);
	ModGroupElement b = new ModGroupElement(new LargeInteger("2"), group1);
	ModGroupElement c = new ModGroupElement(new LargeInteger("276"), group1);
	ModGroupElement d = new ModGroupElement(new LargeInteger("300"), group1);
	ModGroupElement e = new ModGroupElement(new LargeInteger("264"), group1);
	ModGroupElement f = new ModGroupElement(new LargeInteger("132"), group1);
	

	@Test
	public void toByteArrayTest() {
		byte[] b = {1, 0, 0, 0, 2, 0, 1};
		Assert.assertEquals(Arrays.toString(b), Arrays.toString(a.toByteArray()));

		byte[] b2 = {1, 0, 0, 0, 2, 0, 13};
		Assert.assertEquals(Arrays.toString(b2), Arrays.toString(c.toByteArray()));

	
	}

	@Test
	public void getElementTest() {
		LargeInteger x = new LargeInteger("300");
		Assert.assertEquals(x.mod(new LargeInteger("263")), d.getElement());
	}

	@Test
	public void equalTest() {
		Assert.assertTrue(a.equal(new ModGroupElement(
				new LargeInteger("1"), group1)));
		Assert.assertTrue(a.equal(new ModGroupElement(
				new LargeInteger("264"), group1)));
	}

	@Test
	public void getGroupTest() {
		Assert.assertTrue(group1.equals(b.getGroup()));

		ModGroupElement ife = new ModGroupElement(new LargeInteger("1"), group1);
		Assert.assertTrue(group1.equals(ife.getGroup()));
	}


	@Test
	public void mult1_Test() {
		Assert.assertTrue(a.equal(e.mult(group1.one())));
	}


	@Test
	public void powerTest() {
		Assert.assertTrue(e.power(new LargeInteger("3")).equal(new ModGroupElement(e.getElement().modPow(new LargeInteger("3"), e.getGroup().getFieldOrder()), group1)));
	}

	@Test
	public void inverse_pof4Test() {
		Assert.assertTrue(b.equal(f.inverse()));
	}
}

	
	


