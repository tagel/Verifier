package arithmetic.objects.groups;



import arithmetic.objects.LargeInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class ModGroupElement implements IGroupElement {


	private LargeInteger element;
	private ModGroup group;
	

	public ModGroupElement (LargeInteger element, ModGroup group) {
		this.element = element.mod(group.getFieldOrder());
		this.group = group;
	}



	public LargeInteger getElement() {
		return element;
	}
	
	@Override
	public ModGroup getGroup() {
		return group;
	}

	@Override
	public ModGroupElement mult(IGroupElement b) {
		ModGroupElement ret = new ModGroupElement((this.getElement().multiply((LargeInteger) ((ModGroupElement) b).getElement())).mod(this.getGroup().getFieldOrder()), getGroup());
		return ret;
	}

	@Override
	public ModGroupElement inverse() {
		ModGroupElement ret = new ModGroupElement (getElement().modInverse(getGroup().getFieldOrder()), getGroup());
		return ret;
	}

	@Override
	public ModGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}

	@Override
	public ModGroupElement power(LargeInteger b) {
		ModGroupElement base = this;
	    ModGroupElement result = this.getGroup().one();
	    
	    String str = b.toString(2);
	    
	    for (int i = str.length()-1; i>-1; i--)
	    {
	        if (str.charAt(i)=='1')
	            result = result.mult(base);
	        base =  base.mult(base);
	    }

	    return result;
	}


	@Override
	public boolean equals(IGroupElement b) {
		if (getElement().mod(getGroup().getFieldOrder()).equals(((LargeInteger) ((ModGroupElement) b).getElement()).mod(((ModGroupElement) b).getGroup().getFieldOrder()))) return true;
		else return false;
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = group.getFieldOrder().toByteArray().length;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(numOfOrderBytes).array();
		byte[] b = element.toByteArray();
		while (b.length<numOfOrderBytes) {
			byte[] d = new byte[b.length+1];
			System.arraycopy(b, 0, d, 1, b.length);
			d[0] = 0;
			b = d;
		}
		byte[] c= new byte[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		byte[] ret = new byte[c.length+1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}




}
