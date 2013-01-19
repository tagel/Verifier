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

	public static LargeInteger power(LargeInteger a, LargeInteger b) {
		LargeInteger ret = LargeInteger.ONE;
		
		for (BigInteger bi = b; bi.compareTo(BigInteger.ZERO) > 0; bi = bi.subtract(BigInteger.ONE)) {
			ret = ret.multiply(a);
		}
		return ret;
	
		
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
