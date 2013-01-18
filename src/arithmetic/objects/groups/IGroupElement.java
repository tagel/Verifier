package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;

public interface IGroupElement extends ByteTree {
	
	
	
	public abstract IGroup getGroup();

	public abstract IGroupElement mult(IGroupElement b);
	
	public abstract IGroupElement inverse();
	
	public abstract IGroupElement divide (IGroupElement b);
	
	public abstract IGroupElement power(LargeInteger b);
	
	public abstract boolean equals(IGroupElement b);
}