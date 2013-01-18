package arithmetic.objects;

import java.math.BigInteger;


public class LargeInteger extends BigInteger {

	private static final long serialVersionUID = 1L;
	public static LargeInteger ONE = new LargeInteger("1");
	public static LargeInteger ZERO = new LargeInteger("0");

	public LargeInteger (byte[] b) {
		super(b);

	}

	public LargeInteger (String s) {
		super(s);
	}

	public LargeInteger (BigInteger n) {
		super(n.toByteArray());
		
	}
	
	public LargeInteger (String s, int i) {
		super(s, i);
	}
	
	public LargeInteger add(LargeInteger b)
	{
		return new LargeInteger(super.add(b));
	}

	public LargeInteger multiply(LargeInteger b)
	{
		return new LargeInteger(super.multiply(b));
	}

	public LargeInteger subtract(LargeInteger b)
	{
		return new LargeInteger(super.subtract(b));
	}

	public LargeInteger divide(LargeInteger b)
	{
		return new LargeInteger(super.divide(b));
	}

	public LargeInteger remainder(LargeInteger b)
	{
		return new LargeInteger(super.remainder(b));
	}

	public LargeInteger power(LargeInteger b) throws CloneNotSupportedException {
		
		LargeInteger base = (LargeInteger) super.clone();
	    LargeInteger result = LargeInteger.ONE;
	    
	    String str = b.toString(2);
	    
	    for (int i = str.length()-1; i>-1; i--)
	    {
	        if (str.charAt(i)=='1')
	            result = (LargeInteger) super.multiply(result);
	        base =  base.multiply(base);
	    }

	    return result;
	}
		
	

	public LargeInteger power (int i) {
		return new LargeInteger(super.pow(i));
	}


	public LargeInteger mod(LargeInteger n)
	{
		return new LargeInteger(super.mod(n));
	}

	public LargeInteger modPow(LargeInteger a, LargeInteger b)
	{
		return new LargeInteger(super.modPow(a, b));
	}
	
	public LargeInteger modInverse(LargeInteger a)
	{
		return new LargeInteger(super.modInverse(a));
	}

	public int compareTo(LargeInteger b)
	{
		return super.compareTo(b);
	}
	
	public LargeInteger min(LargeInteger b) {
		return new LargeInteger(super.min(b));
	}

	public int bitLength()
	{
		return super.bitLength();
	}

	public byte[] toByteArray()
	{
		return super.toByteArray();
	}

}
