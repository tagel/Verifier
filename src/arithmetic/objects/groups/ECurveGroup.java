package arithmetic.objects.groups;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

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
 */
public class ECurveGroup implements IGroup{
	
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
	public ECurveGroup (String name, LargeInteger p, LargeInteger q, LargeInteger a, LargeInteger b, Point g) {
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
	
	
	public LargeInteger getFieldOrder() {
		return p;
	}
	

	@Override
	public LargeInteger getOrder() {
		return q;
	}
	
	public LargeInteger getXCoefficient() {
		return a;
	}
	
	public LargeInteger getB() {
		return b;
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
		IntegerFieldElement minusOne = new IntegerFieldElement (new LargeInteger(-1), f);
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
	
	
	public int[] shanksTonelli(int a, int p) {
		int[] retVal = new int[2];
		
		return retVal;
	}
	
	//Find positive integers Q and S such that p-1=2Q
	public int[] findPowers(int p) {
		int[] retVal = new int[2];
		int s;
		int Q=1;
		
		while (Q%2!=0) {
			//bla
			
		} 
		
		return retVal;
	}



}

