package arithmetic.objects.ring;

import arithmetic.objects.LargeInteger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;

/**
 * This class represents an integer ring element. since some integers may be
 * very large, we will use the LargeInteger class to store them. for every such
 * element, we will store both the integer and the ring that it belongs to.
 * 
 * @author Itay
 * 
 */
public class IntegerRingElement implements ByteTree {

	protected LargeInteger element;
	protected IRing<IntegerRingElement> ring;

	/**
	 * Constructor
	 * 
	 * @param element
	 *            - the large integer
	 * @param ring
	 *            - the ring which the large integer belongs to.
	 */
	public IntegerRingElement(LargeInteger element,
			IRing<IntegerRingElement> ring) {
		this.element = element;
		this.ring = ring;
	}

	public LargeInteger getElement() {
		return element.mod(getRing().getOrder());
	}

	public IRing<IntegerRingElement> getRing() {
		return ring;
	}

	/**
	 * 
	 * @return The additive inverse of our element
	 */
	public IntegerRingElement neg() {
		return new IntegerRingElement(getRing().getOrder().subtract(
				this.getElement().mod(getRing().getOrder())), this.getRing());
		//TODO fix bug??
		//return new IntegerRingElement(this.getElement().negate().mod(getRing().getOrder()), this.getRing());
	}

	/**
	 * 
	 * @param b
	 *            another integer ring element
	 * @return The result of the addition of our element and b.
	 */
	public IntegerRingElement add(IntegerRingElement b) {
		return new IntegerRingElement(
				(this.getElement().add(b.getElement())).mod(getRing()
						.getOrder()), this.getRing());
	}

	/**
	 * @param b
	 *            another integer ring element
	 * @return The result of the multiplication of our element and b.
	 */
	public IntegerRingElement mult(IntegerRingElement b) {
		return new IntegerRingElement((this.getElement().multiply(b
				.getElement())).mod(getRing().getOrder()), this.getRing());
	}

	/**
	 * 
	 * @param b
	 *            a large integer which is the exponent.
	 * @return our element in the b'th power.
	 */
	public IntegerRingElement power(LargeInteger b) {
		IntegerRingElement base = this;
		IntegerRingElement result = this.getRing().one();

		String str = b.toString(2);

		for (int i = str.length() - 1; i > -1; i--) {
			if (str.charAt(i) == '1')
				result = result.mult(base);
			base = base.mult(base);
		}
		return result;
	}

	/**
	 * 
	 * @param b
	 *            another integer ring element
	 * @return true if and only if our element and b are equal. That means,
	 *         represent the same large integer and belong to the same ring.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof IntegerRingElement)) {
			return false;
		}
				
		IntegerRingElement b = (IntegerRingElement) o;
		if (this.getElement().mod(getRing().getOrder())
				.equals(b.getElement().mod(getRing().getOrder()))) {
			return true;
		}
		return false;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the integer
	 * ring element.
	 */
	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = getRing().getOrder().toByteArray().length;
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
	
	@Override
	public String toString() {
		return element.toString();
	}
}
