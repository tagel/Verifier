package arithmetic.objects.ring;

import arithmetic.objects.LargeInteger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;
import arithmetic.objects.groups.ECurveGroupElement;

/**
 * 
 * @author Itay
 * 
 */
public class IntegerRingElement implements ByteTree {

	protected LargeInteger element;
	protected IRing<IntegerRingElement> ring;

	public IntegerRingElement(LargeInteger element,
			IRing<IntegerRingElement> ring) {
		this.element = element;
		this.ring = ring;
	}

	public LargeInteger getElement() {
		return element.mod(getRingOrder());
	}

	public IRing<IntegerRingElement> getRing() {
		return ring;
	}

	public IntegerRingElement neg() {
		return new IntegerRingElement(getRingOrder().subtract(
				this.getElement().mod(getRingOrder())), this.getRing());
	}

	public IntegerRingElement add(IntegerRingElement b) {
		return new IntegerRingElement(
				(this.getElement().add(b.getElement())).mod(getRingOrder()),
				this.getRing());
	}

	public IntegerRingElement mult(IntegerRingElement b) {
		return new IntegerRingElement((this.getElement().multiply(b
				.getElement())).mod(getRingOrder()), this.getRing());
	}

	
	public IntegerRingElement power(LargeInteger b) {
		IntegerRingElement base = this;
		IntegerRingElement result = this.getRing().one();
	    
	    String str = b.toString(2);
	    
	    for (int i = str.length()-1; i>-1; i--)
	    {
	        if (str.charAt(i)=='1')
	            result = result.mult(base);
	        base =  base.mult(base);
	    }

	    return result;
	}
	

	public boolean equals(IntegerRingElement b) {
		if (this.getElement().mod(getRingOrder())
				.equals(b.getElement().mod(getRingOrder()))) {
			return true;
		}
		return false;
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = getRingOrder().toByteArray().length;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfOrderBytes).array();
		byte[] b = element.toByteArray();
		while (b.length < numOfOrderBytes) {
			byte[] d = new byte[b.length + 1];
			System.arraycopy(b, 0, d, 1, b.length);
			d[0] = 0;
			b = d;
		}
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		byte[] ret = new byte[c.length + 1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}

	private LargeInteger getRingOrder() {
		return this.ring.getOrder();
	}
}
