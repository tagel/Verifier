package arithmetic.objects.groups;



import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

import cryptographic.primitives.PseudoRandomGenerator;


/**
 * This class represents a standard elliptic curve over a prime order field.
 *
 */
public class ECurveGroup implements IGroup{
	
	/**
	 * name = the name of the standard elliptic curve.
	 */
	private String name;
	/**
	 * p = order of the underlying field.
	 */
	private BigInteger p;
	/**
	 * q = order of the group
	 */
	private BigInteger q; 
	/**
	 * a = first coefficient of the curve equation
	 */
	private BigInteger a;
	/**
	 * b = second coefficient of the curve equation
	 */
	private BigInteger b;
	/**
	 * g = standard generator
	 */
	private Point g;
	/**
	 * group type: either modular or elliptic curve. (in this case, elliptic curve).
	 */
	private String groupType = "Elliptic Curve";

	/**
	 * @param p
	 * @param q
	 * @param a
	 * @param b
	 * @param g
	 * Constructor.
	 */
	public ECurveGroup (String name, BigInteger p, BigInteger q, BigInteger a, BigInteger b, Point g) {
		this.name = name;
		this.p=p;
		this.q=q;
		this.a=a;
		this.b=b;
		this.g=g;
	}
	
	/**
	 * @param s = a name of a standard elliptic curve.
	 * Constructor.
	 */
	public ECurveGroup (String s) {
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
	
	
	public BigInteger getFieldOrder() {
		return p;
	}
	

	@Override
	public BigInteger getOrder() {
		return q;
	}
	
	@Override
	public ECurveGroupElement getGenerator() {
		return new ECurveGroupElement(g, this);
	}

	@Override
	public String getGroupType() {
		return groupType;
	}
	
	@Override
	public ECurveGroupElement one() {
		IField<IntegerFieldElement> f = new PrimeOrderField(p);
		IntegerFieldElement minusOne = new IntegerFieldElement (BigInteger.valueOf(-1), f);
		Point infinity = new Point(minusOne, minusOne);
		ECurveGroupElement ret = new ECurveGroupElement(infinity, this);
		return ret;
	}




	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return new StringLeaf(name).toByteArray();
	}

	@Override
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
		
		return null;
	}



}

