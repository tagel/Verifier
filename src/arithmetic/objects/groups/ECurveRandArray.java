package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This object returns a new Array of N random Elliptic Curve elements
 * 
 * @author Sofia
 * 
 */
public class ECurveRandArray {

	ArrayOfElements<IGroupElement> Rand;
	// Powers:
	LargeInteger Q;
	LargeInteger s;
	// Least QNR
	LargeInteger b;

	public ECurveRandArray(LargeInteger q, int N, PseudoRandomGenerator prg,
			byte[] seed, int nr) {

		ArrayOfElements<IGroupElement> RandArray = new ArrayOfElements<IGroupElement>();
		int nq = q.bitLength();

		int length = 8 * ((int) Math.ceil((double) (nr + nq / 8)));
		prg.setSeed(seed);

		/*
		 * Prepare the ground for running ressol algorithm: We compute all of
		 * the things we can compute only once, like the powers Q and s, the the
		 * least quadratic non residue (mod q). We do this here because the
		 * complexity of each function can be finally O(q).
		 */
		// Define Q and s to be p-1=Q2^s when Q is odd
		findPowers(q);

		// Define b as a the least quadratic non residual
		findLeastQNR(q);

		/*
		 * Create the random array - first we generate numbers using prg, and
		 * then we check if these numbers fit to the elliptic curve. We do this
		 * by computing f(zi) and checking if f(zi) is a quadratic residue of p.
		 * If it is - we find the roots using Shanks-Tonelli algorithm.
		 */
		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			LargeInteger t = new LargeInteger(arr);
			LargeInteger ttag = t.mod(new LargeInteger("2").power(nq + nr));
			LargeInteger zi = ttag.mod(q);
			// TODO finish the array after the shanks tonelli works
		}

		Rand = RandArray;

	}

	/**
	 * 
	 * @param a
	 *            - an integer which is a quadratic residue (mod p).
	 * @param p
	 *            - prime order of field
	 * @return the smallest square root that satisfies y^2 = a (mod p)
	 */
	public LargeInteger shanksTonelli(LargeInteger a, LargeInteger p) {
		LargeInteger[] retVal = new LargeInteger[2];

		// Verify that the Lagendre symbol of a and p is not -1:
		if (Legendre(a, p) == -1)
			return null;

		// Find a's opposite in the modular field
		LargeInteger atag = a.modInverse(p);

		// compute c
		LargeInteger c = b.modPow(Q, p);

		// compute R
		LargeInteger r = a.modPow(
				Q.add(LargeInteger.ONE).divide(new LargeInteger("2")), p);

		LargeInteger i = LargeInteger.ONE;
		LargeInteger d;

		for (i = LargeInteger.ONE; !i.equals(s);) {
			// compute the power as s-i-1
			LargeInteger pow = new LargeInteger("2").power(s.intValue()
					- i.intValue() - 1);
			d = (r.power(2).multiply(atag).modPow(pow, p));

			if (d.equals(p.subtract(LargeInteger.ONE)))
				r = r.multiply(c).mod(p);
			c = c.modPow(new LargeInteger("2"), p);

			i = i.add(LargeInteger.ONE);

		}

		retVal[0] = r;
		retVal[1] = LargeInteger.ZERO.subtract(r).add(p); // -r mod p

		return retVal[0].min(retVal[1]);
	}

	/**
	 * 
	 * @param p
	 *            - a prime number
	 * @return the first quadratic non residue of p
	 */
	public void findLeastQNR(LargeInteger p) {
		LargeInteger retVal = LargeInteger.ONE;
		boolean flag = false;
		while (!flag) {
			retVal = retVal.add(LargeInteger.ONE);
			if (Legendre(retVal, p) == -1) {
				this.b = retVal;
				flag = true;
			} else
				retVal = retVal.add(LargeInteger.ONE);

			if (retVal.compareTo(p) >= 0)
				this.b = retVal;
		}

		// The function shouldn't get here at all. Half of the values should be
		// quadratic non residues of p
		this.b = retVal;
	}

	/**
	 * @param a
	 *            - integer
	 * @param p
	 *            - prime
	 * @return the Legendre symbol of a / b using Euler criterion.
	 */
	public int Legendre(LargeInteger a, LargeInteger p) {
		// This should happen in our case because we only check numbers smaller
		// than q, and q is prime.
		if (a.remainder(p).equals(LargeInteger.ZERO)) {
			return 0;
		}
		LargeInteger exponent = p.subtract(LargeInteger.ONE);
		exponent = exponent.divide(new LargeInteger("2"));
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
	 * number it means that p-1 is even. Time complexity of this function is
	 * log(n).
	 * 
	 * @param p
	 * @return an array of 2 integers where {s,Q}
	 */
	public void findPowers(LargeInteger p) {

		LargeInteger s = LargeInteger.ZERO;
		LargeInteger Q = p.subtract(LargeInteger.ONE);// p-1 is even
		// Q needs to be odd
		while (Q.mod(LargeInteger.valueOf(2)).equals(LargeInteger.ZERO)) {
			Q = Q.divide(new LargeInteger("2"));
			s = s.add(LargeInteger.ONE);
		}
		this.s = s;
		this.Q = Q;

	}

	public ArrayOfElements<IGroupElement> getRand() {
		return Rand;
	}

	public LargeInteger getQ() {
		return Q;
	}

	public void setQ(LargeInteger q) {
		Q = q;
	}

	public LargeInteger getS() {
		return s;
	}

	public void setS(LargeInteger s) {
		this.s = s;
	}

	public LargeInteger getB() {
		return b;
	}

	public void setB(LargeInteger b) {
		this.b = b;
	}

	public void setRand(ArrayOfElements<IGroupElement> rand) {
		Rand = rand;
	}

}
