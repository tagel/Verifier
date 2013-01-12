package arithmetic.objects.field;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;

/**
 * Tests for class PrimeOrderField.
 * 
 * @author Daniel
 * 
 */
public class PrimeOrderFieldTests {
	PrimeOrderField pof = new PrimeOrderField(new LargeInteger("263"));

	@Test
	public void oneTest() {

		Assert.assertEquals(LargeInteger.ONE, pof.one().getElement());
	}

	@Test
	public void zeroTest() {
		Assert.assertEquals(LargeInteger.ZERO, pof.zero().getElement());
	}

	@Test
	public void getOrderTest() {
		Assert.assertEquals(new LargeInteger("263"), pof.getOrder());
	}
}
