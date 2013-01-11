package arithmetic.objects.groups;



import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This object returns a new Array of N random Elliptic Curve elements
 * @author Sofia
 *
 */
public class ECurveRandArray {
	
	ArrayOfElements<IGroupElement> Rand;
	
	public ECurveRandArray(LargeInteger q, int N, PseudoRandomGenerator prg,
			byte[] seed, int nr) {
		
		ArrayOfElements<IGroupElement> RandArray = new ArrayOfElements<IGroupElement>();
		int nq = q.bitLength();
		
		int length = 8 * ((int) Math.ceil((double) (nr+nq / 8)));
		prg.setSeed(seed);
		
		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			LargeInteger t = new LargeInteger(arr);
			LargeInteger ttag = t.mod(LargeInteger.valueOf(2).pow(nq+nr));
			LargeInteger zi = ttag.mod(q);
			//TODO finish the array after the shanks tonelli works
		}
		
		
		Rand = RandArray;
		
	}
	
	
	public LargeInteger[] shanksTonelli(LargeInteger a, LargeInteger p) {
		LargeInteger[] retVal = new LargeInteger[2];

		// Verify that the Lagendre symbol of a and p is not -1:
		if (Legendre(a, p) == -1)
			return null;

		// Define Q and s to be p-1=Q2^s when Q is odd
		LargeInteger[] powers = findPowers(p);
		LargeInteger Q = powers[1];
		LargeInteger s = powers[0];

		// Define b as a the least quadratic non residual
		LargeInteger b = findLeastQNR(p);

		// Find a's opposite in the modular field
		LargeInteger atag = a.modInverse(p);

		// compute c
		LargeInteger c = b.modPow(Q, p);

		// compute R
		LargeInteger r = a.modPow(
				Q.add(LargeInteger.ONE).divide(LargeInteger.valueOf(2)), p);

		LargeInteger i = LargeInteger.ONE;
		LargeInteger d;

		for (i = LargeInteger.ONE; !i.equals(s); ) {
			// compute the power as s-i-1
			LargeInteger pow = LargeInteger.valueOf(2).pow(
					s.intValue() - i.intValue() - 1);
			d = (r.pow(2).multiply(atag).modPow(pow, p));

			if (d.equals(p.subtract(LargeInteger.ONE)))
				r = r.multiply(c).mod(p);
			c = c.modPow(LargeInteger.valueOf(2), p);
			
			i = i.add(LargeInteger.ONE);

		}

		retVal[0] = r;
		retVal[1] = LargeInteger.ZERO.subtract(r).add(p); // -r mod p

		return retVal;
	}

	/**
	 * 
	 * @param p
	 *            - a prime number
	 * @return the first quadratic non residue of p
	 */
	public LargeInteger findLeastQNR(LargeInteger p) {
		LargeInteger retVal = LargeInteger.ONE;
		boolean flag = false;
		while (!flag) {
			retVal = retVal.add(LargeInteger.ONE);
			if (Legendre(retVal, p) == -1)
				return retVal;
			else
				retVal = retVal.add(LargeInteger.ONE);
			if (retVal.compareTo(p) >= 0)
				return retVal;
		}

		// The function shouldn't get here at all. Half of the values should be
		// quadratic non residues of p
		return retVal;
	}

	/**
	 * @param a
	 *            - integer
	 * @param p
	 *            - prime
	 * @return the Legendre symbol of a / b
	 */
	public int Legendre(LargeInteger a, LargeInteger p) {
		if (a.remainder(p).equals(LargeInteger.ZERO)) {
			return 0;
		}
		LargeInteger exponent = p.subtract(LargeInteger.ONE);
		exponent = exponent.divide(LargeInteger.valueOf(2));
		LargeInteger result = a.modPow(exponent, p); // 1 <= result <= p - 1

		if (result.equals(LargeInteger.ONE)) {
			return 1;
		} else if (result.equals(p.subtract(LargeInteger.ONE))) {
			return -1;
		} else {
			throw new ArithmeticException(
					"Error computing the Legendre symbol.");
		}
	}

	/**
	 * Find positive integers Q and S such that p-1=Q*2^s, where p is a prime
	 * number it means that p-1 is even.
	 * 
	 * @param p
	 * @return an array of 2 integers where {s,Q}
	 */
	public LargeInteger[] findPowers(LargeInteger p) {
		LargeInteger[] retVal = new LargeInteger[2];
		LargeInteger s = LargeInteger.ZERO;
		LargeInteger Q = p.subtract(LargeInteger.ONE);// p-1 is even
		// Q needs to be odd
		while (Q.mod(LargeInteger.valueOf(2)).equals(LargeInteger.ZERO))  {
			Q = Q.divide(LargeInteger.valueOf(2));
			s = s.add(LargeInteger.ONE);
		}
		retVal[0] = s;
		retVal[1] = Q;
		return retVal;
	}
	

	public ArrayOfElements<IGroupElement> getRand() {
		return Rand;
	}


}
