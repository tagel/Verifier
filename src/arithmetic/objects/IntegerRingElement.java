package arithmetic.objects;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class IntegerRingElement implements ByteTree{

	
	protected BigInteger element;
	protected IRing<IntegerRingElement> ring;
	
	public IntegerRingElement (BigInteger element, IRing<IntegerRingElement> ring) {
		this.element = element;
		this.ring = ring;
	}
	
	public BigInteger getElement() {
		return element;
	}
	
	public IRing<IntegerRingElement> getRing() {
		return ring;
	}


	
	public IntegerRingElement neg() {
		IntegerRingElement ret = new IntegerRingElement(this.getRing().getOrder().min(this.getElement().mod(this.getRing().getOrder())), this.getRing());
		return ret;
	}

	
	public IntegerRingElement add(IntegerRingElement b) {
		IntegerRingElement ret = new IntegerRingElement ((this.getElement().add(b.getElement())).mod(this.getRing().getOrder()), this.getRing());
		return ret;
	}

	
	public IntegerRingElement mult(IntegerRingElement b) {
		IntegerRingElement ret = new IntegerRingElement ((this.getElement().multiply(b.getElement())).mod(this.getRing().getOrder()), this.getRing());
		return ret;
	}
	
	public IntegerRingElement power (BigInteger b) {
		IntegerRingElement result = this;
		for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
	    	result = result.mult(this);
		return result;
	}

	
	public boolean equal(IntegerRingElement b) {
		if (this.getElement().mod(this.getRing().getOrder())==b.getElement().mod(this.getRing().getOrder())) return true;
		else return false;
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = this.ring.getOrder().toByteArray().length;
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
