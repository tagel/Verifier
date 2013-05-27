package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class is used to represent an element in a multiplicative modulo prime
 * group. since some integers may be very large, we will use the LargeInteger
 * class to store them. for every such element, we will store both the integer
 * and the group that it belongs to.
 * 
 * @author Itay
 * 
 */

public class ModGroupElement implements IGroupElement {

	private LargeInteger element;
	private ModGroup group;

	/**
	 * Constructor
	 * 
	 * @param element
	 *            - the large integer
	 * @param group
	 *            - the group which the large integer belongs to.
	 */
	public ModGroupElement(LargeInteger element, ModGroup group) {
		this.element = element.mod(group.getFieldOrder());
		this.group = group;
	}

	/**
	 * 
	 * @return the large integer.
	 */
	public LargeInteger getElement() {
		return element;
	}

	/**
	 * @return the group which this element belongs to.
	 */
	@Override
	public ModGroup getGroup() {
		return group;
	}

	/**
	 * @param b
	 *            another group element
	 * @return The result of the multiplication of our element and b.
	 */
	@Override
	public ModGroupElement mult(IGroupElement b) {
		return new ModGroupElement(
				(this.getElement().multiply((LargeInteger) ((ModGroupElement) b)
						.getElement())).mod(this.getGroup().getFieldOrder()),
				getGroup());
	}

	/**
	 * 
	 * @return the multiplicative inverse of our element.
	 */
	@Override
	public ModGroupElement inverse() {
		return new ModGroupElement(getElement().modInverse(
				getGroup().getFieldOrder()), getGroup());
	}

	/**
	 * 
	 * @param b
	 *            another group element
	 * @return the result of the multiplication of our element with the inverse
	 *         of b (division).
	 */
	@Override
	public ModGroupElement divide(IGroupElement b) {
		return mult(b.inverse());
	}

	/**
	 * 
	 * @param b
	 *            a large integer which is the exponent.
	 * @return our element in the b'th power.
	 */
	@Override
	public ModGroupElement power(LargeInteger b) {
		ModGroupElement base = this;
		ModGroupElement result = this.getGroup().one();

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
	 *            another group element
	 * @return true if and only if our element and b are equal. That means,
	 *         represent the same number and belong to the same group.
	 */
	@Override
	public boolean equals(Object c) {
		
		if (!(c instanceof ModGroupElement)) {
			return false;
		}

		ModGroupElement b = (ModGroupElement) c;
		
		if (getElement().mod(getGroup().getFieldOrder()).equals(
				((LargeInteger) ((ModGroupElement) b).getElement())
						.mod(((ModGroupElement) b).getGroup().getFieldOrder()))) {
			return true;
		}
		return false;
	}

	/**
	 * returns the byte array representation (as a byte tree) of the modular
	 * group element.
	 */
	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = group.getFieldOrder().toByteArray().length;
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
	public String toString(){
		return element.toString();
		}
}
