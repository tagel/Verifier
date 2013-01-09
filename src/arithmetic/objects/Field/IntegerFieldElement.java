package arithmetic.objects.Field;



import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;


public class IntegerFieldElement implements ByteTree {
	
	protected BigInteger element;
	protected IField<IntegerFieldElement> field;
	
	public IntegerFieldElement (BigInteger element, IField<IntegerFieldElement> f) {
		this.element = element;
		this.field = f;
	}
	
	public BigInteger getElement() {
		return element;
	}
	
	public IField<IntegerFieldElement> getField() {
		return field;
	}
	
	public IntegerFieldElement neg() {
		IntegerFieldElement ret = new IntegerFieldElement(this.getField().getOrder().min(this.getElement().mod(this.getField().getOrder())), this.getField());
		return ret;
	}

	
	public IntegerFieldElement add(IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement ((this.getElement().add(b.getElement())).mod(this.getField().getOrder()), this.getField());
		return ret;
	}

	
	public IntegerFieldElement mult(IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement ((this.getElement().multiply(b.getElement())).mod(this.getField().getOrder()), this.getField());
		return ret;
	}
	
	public IntegerFieldElement power (BigInteger b) {
		IntegerFieldElement result = this;
		for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE))
	    	result = result.mult(this);
		return result;
	}
	
	public IntegerFieldElement inverse() {
		IntegerFieldElement ret = new IntegerFieldElement (this.getElement().modInverse(this.getField().getOrder()), this.getField());
		return ret;
	}

	
	public boolean equal(IntegerFieldElement b) {
		if (this.getElement().mod(this.getField().getOrder())==b.getElement().mod(this.getField().getOrder())) return true;
		else return false;
	}




	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = field.getOrder().toByteArray().length;
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
