package arithmetic.objects;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class IntegerRingElement extends RingElement<BigInteger> {

	
	public IntegerRingElement(BigInteger element, IRing<BigInteger> ring) {
		super(element, ring);
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = ring.getOrder().toByteArray().length;
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
