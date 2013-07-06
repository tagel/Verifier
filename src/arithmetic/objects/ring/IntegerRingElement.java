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
	 * @param exp
	 *            a large integer which is the exponent.
	 * @return our element in the exp'th power.
	 */
	public IntegerRingElement power(LargeInteger exp) {
		
		IntegerRingElement base = this;
		IntegerRingElement result = this.getRing().one();

		String str = exp.toString(TWO);

		for (int i = str.length() - 1; i > -1; i--) {
			if (str.charAt(i) == '1')
				result = result.mult(base);
			base = base.mult(base);
		}
		return result;
	}

	/**
	 * 
	 * @param ire
	 *            another integer ring element
	 * @return true if and only if our element and ire are equal. That means,
	 *         represent the same large integer and belong to the same ring.
	 */
	public boolean equals(Object o) {
		
		if (!(o instanceof IntegerRingElement)) {
			return false;
		}

		IntegerRingElement ire = (IntegerRingElement) o;
		if (this.getElement().mod(getRing().getOrder())
				.equals(ire.getElement().mod(getRing().getOrder()))) {
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
		byte[] helperArr1 = ByteBuffer.allocate(CAPACITY).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfOrderBytes).array();
		byte[] elemArr = element.toByteArray();
		while (elemArr.length < numOfOrderBytes) {
			byte[] d = new byte[elemArr.length + 1];
			System.arraycopy(elemArr, 0, d, 1, elemArr.length);
			d[0] = 0;
			elemArr = d;
		}
		byte[] helperArr2 = new byte[helperArr1.length + elemArr.length];
		System.arraycopy(helperArr1, 0, helperArr2, 0, helperArr1.length);
		System.arraycopy(elemArr, 0, helperArr2, helperArr1.length, elemArr.length);
		byte[] ret = new byte[helperArr2.length + 1];
		System.arraycopy(helperArr2, 0, ret, 1, helperArr2.length);
		ret[0] = 1;
		
		return ret;
	}

	@Override
	public String toString() {
		
		return element.toString();
	}
}
