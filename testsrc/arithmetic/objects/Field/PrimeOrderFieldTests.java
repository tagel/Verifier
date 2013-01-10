package arithmetic.objects.Field;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.Field.PrimeOrderField;

public class PrimeOrderFieldTests {

	@Test
	public void oneTest() {
		PrimeOrderField pof = new PrimeOrderField(BigInteger.valueOf(263));
		Assert.assertEquals(BigInteger.ONE, pof.one().getElement());
	}
	
	@Test
	public void zeroTest() {
		PrimeOrderField pof = new PrimeOrderField(BigInteger.valueOf(263));
		Assert.assertEquals(BigInteger.ZERO, pof.zero().getElement());
	}
	
	@Test
	public void getOrderTest() {
		PrimeOrderField pof = new PrimeOrderField(BigInteger.valueOf(263));
		Assert.assertEquals(BigInteger.valueOf(263), pof.getOrder());
	}
}

