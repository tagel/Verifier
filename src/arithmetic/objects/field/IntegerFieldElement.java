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
	 * @param element - the large integer
	 * @param f - the field which the large integer belongs to.
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

	public IntegerFieldElement add(IntegerFieldElement b) {
		return new IntegerFieldElement(
				(this.getElement().add(b.getElement())).mod(this.getField()
						.getOrder()), this.getField());
	}

	public IntegerFieldElement subtract(IntegerFieldElement b) {
		return new IntegerFieldElement((this.getElement().subtract(
				(b.getElement())).mod(this.getField().getOrder())),
				this.getField());
	}

	public IntegerFieldElement mult(IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement(
				(this.getElement().multiply(b.getElement())).mod(this
						.getField().getOrder()), this.getField());
		return ret;
	}

	public IntegerFieldElement power(LargeInteger b) {
		IntegerFieldElement base = this;
		IntegerFieldElement result = this.getField().one();

		String str = b.toString(2);

		for (int i = str.length() - 1; i > -1; i--) {
			if (str.charAt(i) == '1')
				result = result.mult(base);
			base = base.mult(base);
		}

		return result;
	}

	public IntegerFieldElement divide(IntegerFieldElement b) {
		IntegerFieldElement ret = new IntegerFieldElement(
				(this.getElement().multiply(b.getElement().modInverse(
						this.getField().getOrder()))).mod(this.getField()
						.getOrder()), this.getField());
		return ret;
	}

	public IntegerFieldElement inverse() {
		IntegerFieldElement ret = new IntegerFieldElement(this.getElement()
				.modInverse(this.getField().getOrder()), this.getField());
		return ret;
	}

	public boolean equals(IntegerFieldElement b) {
		if (this.getElement().mod(this.getField().getOrder())
				.equals(b.getElement().mod(this.getField().getOrder())))
			return true;
		else
			return false;
	}

	@Override
	public byte[] toByteArray() {
		int numOfOrderBytes = field.getOrder().toByteArray().length;
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

}
