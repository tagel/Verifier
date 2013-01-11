package arithmetic.objects.groups;

import java.math.BigInteger;

import arithmetic.objects.ByteTree;

public interface IGroupElement extends ByteTree {
	
	
	


	public abstract IGroupElement mult(IGroupElement b);
	
	public abstract IGroupElement inverse();
	
	public abstract IGroupElement divide (IGroupElement b);
	
	public abstract IGroupElement power(BigInteger b);
	
	public abstract boolean equal(IGroupElement b);
}