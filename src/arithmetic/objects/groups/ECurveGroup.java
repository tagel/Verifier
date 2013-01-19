package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This class represents a standard elliptic curve over a prime order field.
 * 
 * @author Itay
 */
public class ECurveGroup implements IGroup {

	/**
	 * name = the name of the standard elliptic curve.
	 */
	private String name;
	/**
	 * p = order of the underlying field.
	 */
	private LargeInteger p;
	/**
	 * q = order of the group
	 */
	private LargeInteger q;
	/**
	 * a = first coefficient of the curve equation
	 */
	private LargeInteger a;
	/**
	 * b = second coefficient of the curve equation
	 */
	private LargeInteger b;
	/**
	 * g = standard generator
	 */
	private Point g;

	/**
	 * Constructor.
	 */
	public ECurveGroup(String name, LargeInteger p, LargeInteger q,
			LargeInteger a, LargeInteger b, Point g) {
		this.name = name;
		this.p = p;
		this.q = q;
		this.a = a;
		this.b = b;
		this.g = g;
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            = a name of a standard elliptic curve. our project supports 26
	 *            different elliptic curves each having a unique name and
	 *            parameters.
	 */
	public ECurveGroup(String s) {
		ECurveParams params = new ECurveParams(s);
		name = s;
		p = params.getP();
		q = params.getQ();
		a = params.getA();
		b = params.getB();
		IField<IntegerFieldElement> f = new PrimeOrderField(p);
		IntegerFieldElement gx = new IntegerFieldElement(params.getGx(), f);
		IntegerFieldElement gy = new IntegerFieldElement(params.getGy(), f);
		g = new Point(gx, gy);
	}

	/**
	 * 
	 * @return the order of the underlying field Z*p
	 */
	public LargeInteger getFieldOrder() {
		return p;
	}

	/**
	 * @return the order of this group
	 */
	@Override
	public LargeInteger getOrder() {
		return q;
	}

	/**
	 * 
	 * @return the coefficient of x. (a).
	 */
	public LargeInteger getXCoefficient() {
		return a;
	}

	/**
	 * 
	 * @return b
	 */
	public LargeInteger getB() {
		return b;
	}

	/**
	 * 
	 * @return the generator of the group.
	 */
	@Override
	public ECurveGroupElement getGenerator() {
		return new ECurveGroupElement(g, this);
	}

	/**
	 * @return the 1 of the elliptic curve. (the element which is indifferent to
	 *         the multiplication operation).
	 */
	@Override
	public ECurveGroupElement one() {
		IField<IntegerFieldElement> f = new PrimeOrderField(p);
		IntegerFieldElement minusOne = new IntegerFieldElement(
				new LargeInteger("-1"), f);
		Point infinity = new Point(minusOne, minusOne);
		ECurveGroupElement ret = new ECurveGroupElement(infinity, this);
		return ret;
	}

	/**
	 * @return the byte array representation (as a byte tree) of this elliptic
	 *         curve.
	 */
	@Override
	public byte[] toByteArray() {
		return new StringLeaf(name).toByteArray();
	}

	/**
	 * 
	 * @param N
	 *            the size of the returned array
	 * @param prg
	 *            a pseudo-random generator
	 * @param seed
	 *            the seed used by the pseudo-random generator
	 * @param nr
	 *            auxiliary security parameter for the seed.
	 * @return an array of size N containing random group elements.
	 */
	@Override
	public ArrayOfElements<IGroupElement> createRandomArray(int N,
			PseudoRandomGenerator prg, byte[] seed, int nr) {
		ECurveRandArray arr = new ECurveRandArray(this.p, N, prg, seed, nr, a,
				b, this);
		return arr.getRand();
	}

}
