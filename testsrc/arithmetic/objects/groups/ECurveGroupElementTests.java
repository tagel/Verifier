package arithmetic.objects.groups;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.ECurveGroup;
import arithmetic.objects.groups.ECurveGroupElement;
import arithmetic.objects.groups.IGroupElement;
import cryptographic.primitives.PseudoRandomGenerator;

public class ECurveGroupElementTests {
	@Test
	public void GroupTests() throws UnsupportedEncodingException {
		ECurveGroup G = new ECurveGroup("P-256");
		ECurveGroupElement one = G.one();
		
		Assert.assertEquals(BigInteger.valueOf(-1), one.getElement().getX().getElement());
		Assert.assertEquals(BigInteger.valueOf(-1), one.getElement().getY().getElement());
		
		
	}
	
			
		

}
