package arithmetic.objects;

import java.math.BigInteger;

public interface IGroupElement extends ByteTree {
	
	public <E> E getElement();
	
	public IGroupElement mult(IGroupElement b);
	
	public IGroupElement inverse();
	
	public IGroupElement power(BigInteger b);
	
	public boolean equal(IGroupElement b);
}