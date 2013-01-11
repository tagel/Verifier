package arithmetic.objects;

import java.math.BigInteger;

public class LargeInteger extends BigInteger {


	public LargeInteger (byte[] b) {
		super(b);

	}

	public LargeInteger (int n) {
		super(super.valueOf(n).toByteArray());
	}

	public LargeInteger (BigInteger n) {
		super(n.toByteArray());
		
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

	public LargeInteger power(LargeInteger exponent)
			throws Exception
			{
		if (exponent.bitLength() > 32)
			throw new Exception("Cannot raise a large Integer to the power of a large Integer");
		return new LargeInteger(BigInteger.pow(exponent.intValue()));
			}

	public LargeInteger mod(LargeInteger n)
	{
		return new LargeInteger(super.mod(n));
	}

	public LargeInteger modPow(LargeInteger a, LargeInteger b)
	{
		return new LargeInteger(super.modPow(a, b));
	}

	public int compareTo(LargeInteger b)
	{
		return super.compareTo(b);
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
