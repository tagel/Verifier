package arithmetic.objects.ring;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;

/**
 * Tests for class Ring.
 * 
 * @author Daniel
 * 
 */
public class RingTests {

	@Test
	public void getOrderTest() {
		Assert.assertEquals(BigInteger.valueOf(263), new Ring(new LargeInteger(
				"263")).getOrder());
	}

	@Test
	public void zeroTest() {
		Assert.assertEquals(BigInteger.ZERO, new Ring(new LargeInteger("263"))
				.zero().getElement());
	}

	@Test
	public void oneTest() {
		Assert.assertEquals(BigInteger.ONE, new Ring(new LargeInteger("263"))
				.one().getElement());
	}
}
