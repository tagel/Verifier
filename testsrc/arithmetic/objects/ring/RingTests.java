package arithmetic.objects.ring;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

public class RingTests {

	@Test
	public void getOrderTest() {
		Assert.assertEquals(BigInteger.valueOf(263), new Ring(BigInteger.valueOf(263)).getOrder());
	}
	
	@Test
	public void zeroTest() {
		Assert.assertEquals(BigInteger.ZERO, new Ring(BigInteger.valueOf(263)).zero().getElement());
	}
	
	@Test
	public void oneTest() {
		Assert.assertEquals(BigInteger.ONE, new Ring(BigInteger.valueOf(263)).one().getElement());
	}
}
