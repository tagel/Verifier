package arithmetic.objects.groups;

import java.math.BigInteger;

import arithmetic.objects.arrays.ArrayOfElements;
import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This object returns a new Array of N random Elliptic Curve elements
 * @author Sofia
 *
 */
public class ECurveRandArray {
	
	ArrayOfElements<IGroupElement> Rand;
	
	public ECurveRandArray(BigInteger q, int N, PseudoRandomGenerator prg,
			byte[] seed, int nr) {
		
		ArrayOfElements<IGroupElement> RandArray = new ArrayOfElements<IGroupElement>();
		int nq = q.bitLength();
		
		int length = 8 * ((int) Math.ceil((double) (nr+nq / 8)));
		prg.setSeed(seed);
		
		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			BigInteger t = new BigInteger(arr);
			BigInteger ttag = t.mod(BigInteger.valueOf(2).pow(nq+nr));
			BigInteger zi = ttag.mod(q);
		}
		
		
		Rand = RandArray;
		
	}
	
	
	
	
		
		
	
	public ArrayOfElements<IGroupElement> getRand() {
		return Rand;
	}







	public int[] shanksTonelli(int a, int p) {
		int[] retVal = new int[2];
		
		return retVal;
	}
	
	/**
	 * Find positive integers Q and S such that p-1=Q*2^s, where p is a prime number
	 * it means that p-1 is even.
	 * @param p
	 * @return an array of 2 integers where {s,Q}
	 */
	public static int[] findPowers(int p) {
		int[] retVal = new int[2];
		int s=0;
		int Q=p-1;//p-1 is even
		
		//Q needs to be odd
		while (Q%2==0) {
			Q = Q/2;
			s++;
		} 
		
		retVal[0] = s;
		retVal[1] = Q;
		
		return retVal;
	}




}
