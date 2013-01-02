package arithmetic.objects;



import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ModGroupElement implements IGroupElement {

	
	private BigInteger element;
	private ModGroup group;
	
	public ModGroupElement (BigInteger element, ModGroup group) {
		this.element = element;
		this.group = group;
	}
	
	@SuppressWarnings("unchecked")
	public BigInteger getElement() {
		return element;
	}
	
	public ModGroup getGroup() {
		return group;
	}
	
	@Override
	public ModGroupElement mult(IGroupElement b) {
		  ModGroupElement ret = new ModGroupElement((this.getElement().multiply((BigInteger) ((ModGroupElement) b).getElement())).mod(this.getGroup().getFieldOrder()), getGroup());
		  return ret;
	}

	@Override
	public ModGroupElement inverse() {
		ModGroupElement ret = new ModGroupElement (getElement().modInverse(getGroup().getFieldOrder()), getGroup());
		return ret;
	}


	@Override
	public ModGroupElement power(BigInteger b) {
		BigInteger result = getElement();
		for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
			result = result.multiply(getElement());
		ModGroupElement ret = new ModGroupElement (result.mod(getGroup().getFieldOrder()), getGroup());
		return ret;
	}


	@Override
	public boolean equal(IGroupElement b) {
		if (getElement().mod(getGroup().getFieldOrder())==((BigInteger) ((ModGroupElement) b).getElement()).mod(((ModGroupElement) b).getGroup().getFieldOrder())) return true;
		else return false;
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = group.getFieldOrder().toByteArray().length;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(numOfOrderBytes).array();
		byte[] b = ByteBuffer.allocate(numOfOrderBytes).order(ByteOrder.BIG_ENDIAN).putInt(element.intValue()).array();
		byte[] c= new byte[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		byte[] ret = new byte[c.length+1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}


}
