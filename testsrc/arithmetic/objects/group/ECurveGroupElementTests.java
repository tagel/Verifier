package arithmetic.objects.Groups;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.PseudoRandomGenerator;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.Arrays.ArrayOfElements;

public class ECurveGroupElementTests {
	@Test
	public void GroupTests() throws UnsupportedEncodingException {
		ECurveGroup G = new ECurveGroup("P-256");
		ECurveGroupElement one = G.one();
		
		Assert.assertEquals(BigInteger.valueOf(-1), one.getElement().getX().getElement());
		Assert.assertEquals(BigInteger.valueOf(-1), one.getElement().getY().getElement());
		
		
	}
	
	public ArrayOfElements<IGroupElement> createRandomArray(int N, PseudoRandomGenerator prg,
			byte[] seed, int nr) {
		
		ArrayOfElements<IGroupElement> RandArray = new ArrayOfElements<IGroupElement>();
		int nq = this.q.bitLength();
		
		int length = 8 * ((int) Math.ceil((double) (nr+nq / 8)));
		prg.setSeed(seed);
		
		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			BigInteger t = new BigInteger(arr);
			BigInteger ttag = t.mod(BigInteger.valueOf(2).pow(nq+nr));
			BigInteger zi = ttag.mod(this.q);
			
			
		}
		
		
		return RandArray;
	}
	

}
