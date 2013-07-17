package arithmetic.objects.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;
import arithmetic.objects.LargeInteger;

/**
 * This class represents an integer field element. since some integers may be
 * very large, we will use the LargeInteger class to store them. for every such
 * element, we will store both the integer and the field that it belongs to.
 * 
 * @author Itay
 * 
 */
public class IntegerFieldElement implements ByteTree {

	protected LargeInteger element;
	protected IField<IntegerFieldElement> field;

	/**
	 * Constructor
	 * 
	 * @param element
	 *            - the large integer
	 * @param f
	 *            - the field which the large integer belongs to.
	 */
	public IntegerFieldElement(LargeInteger element,
			IField<IntegerFieldElement> f) {
		
		this.field = f;
		this.element = element.mod(field.getOrder());
	}

	public LargeInteger getElement() {
		
		return element;
	}

	public IField<IntegerFieldElement> getField() {
		
		return field;
	}

	/**
	 * 
	 * @return The additive inverse of our element
	 */
	public IntegerFieldElement neg() {
		
		return new IntegerFieldElement(this.getField().getOrder()
				.subtract(this.getElement().mod(this.getField().getOrder())),
				this.getField());
	}

	/**
	 * 
	 * @param b
	 *            another integer field element
	 * @return The result of the addition of our element and b.
	 */

	public IntegerFieldElement add(IntegerFieldElement b) {
		
		return new IntegerFieldElement(
				(this.getElement().add(b.getElement())).mod(this.getField()
						.getOrder()), this.getField());
	}

	/**
	 * 
	 * @param ife
	 *            another integer field element
	 * @return The result of the addition of our element and the negative of b.
	 */

	public IntegerFieldElement subtract(IntegerFieldElement ife) {
		
		return new IntegerFieldElement((this.getElement().subtract(
				(ife.getElement())).mod(this.getField().getOrder())),
				this.getField());
	}

	/**
	 * @param ife
	 *            another integer field element
	 * @return The result of the multiplication of our element and b.
	 */

	public IntegerFieldElement mult(IntegerFieldElement ife) {
		
		return new IntegerFieldElement(
				(this.getElement().multiply(ife.getElement())).mod(this
						.getField().getOrder()), this.getField());
	}

	/**
	 * 
	 * @param largeInt
	 *            a large integer which is the exponent.
	 * @return our element in the b'th power.
	 */
	public IntegerFieldElement power(LargeInteger largeInt) {
		
		IntegerFieldElement base = this;
		IntegerFieldElement result = this.getField().one();
		String str = largeInt.toString(TWO);

		for (int i = str.length() - 1; i > -1; i--) {
			if (str.charAt(i) == '1') {
				result = result.mult(base);
			}
			base = base.mult(base);
		}

		return result;
	}

	/**
	 * 
	 * @param ife
	 *            another integer field element
	 * @return the result of the multiplication of our element with the inverse
	 *         of b (division).
	 */
	public IntegerFieldElement divide(IntegerFieldElement ife) {
		
		return new IntegerFieldElement(
				(this.getElement().multiply(ife.getElement().modInverse(
						this.getField().getOrder()))).mod(this.getField()
						.getOrder()), this.getField());
	}

	/**
	 * 
	 * @return the multiplicative inverse of our element.
	 */
	public IntegerFieldElement inverse() {
		
		return new IntegerFieldElement(this.getElement()
				.modInverse(this.getField().getOrder()), this.getField());
	}

	/**
	 * 
	 * @param ife
	 *            another integer field element
	 * @return true if and only if our element and b are equal. That means,
	 *         represent the same large integer and belong to the same field.
	 */
	public boolean equals(Object o) {
		
		if (!(o instanceof IntegerFieldElement)) {
			return false;
		}
				
		IntegerFieldElement ife = (IntegerFieldElement) o;
		
		if (this.getElement().mod(this.getField().getOrder())
				.compareTo(ife.getElement().mod(this.getField().getOrder())) == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	/**
	 * returns the byte array representation (as a byte tree) of the integer field element.
	 */
	public byte[] toByteArray() {
		
		int numOfOrderBytes = field.getOrder().toByteArray().length;
		byte[] helperArr = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
				.putInt(numOfOrderBytes).array();
		byte[] elementByteArr = element.toByteArray();
		
		while (elementByteArr.length < numOfOrderBytes) {
			byte[] d = new byte[elementByteArr.length + 1];
			System.arraycopy(elementByteArr, 0, d, 1, elementByteArr.length);
			d[0] = 0;
			elementByteArr = d;
		}
		
		byte[] c = new byte[helperArr.length + elementByteArr.length];
		System.arraycopy(helperArr, 0, c, 0, helperArr.length);
		System.arraycopy(elementByteArr, 0, c, helperArr.length, elementByteArr.length);
		byte[] ret = new byte[c.length + 1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}
}
