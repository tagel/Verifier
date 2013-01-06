package arithmetic.objects;

import java.math.BigInteger;

public interface IGroupElement extends ByteTree {
	
	
	


	public abstract IGroupElement mult(IGroupElement b);
	
	public abstract IGroupElement inverse();
	
	public abstract IGroupElement divide (IGroupElement b);
	
	public abstract IGroupElement power(BigInteger b);
	
	public abstract boolean equal(IGroupElement b);
}