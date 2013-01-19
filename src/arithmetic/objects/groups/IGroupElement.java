package arithmetic.objects.groups;

import arithmetic.objects.LargeInteger;

import arithmetic.objects.ByteTree;

/**
 * This interface is used to represent a group element. in our project we
 * implement two kinds of groups: a modular group and an elliptic curve.
 * therefore we will have 2 classes which will implement the interface: one for
 * modular group element and another for an elliptic curve element.
 * 
 * @author Itay
 * 
 */
public interface IGroupElement extends ByteTree {

	public abstract IGroup getGroup();

	/**
	 * @param b
	 *            another group element
	 * @return The result of the multiplication of our element and b.
	 */
	public abstract IGroupElement mult(IGroupElement b);

	/**
	 * 
	 * @return the multiplication inverse of our element.
	 */
	public abstract IGroupElement inverse();

	/**
	 * 
	 * @param b
	 *            another group element
	 * @return the result of the multiplication of our element with the inverse
	 *         of b (division).
	 */
	public abstract IGroupElement divide(IGroupElement b);

	/**
	 * 
	 * @param b
	 *            a large integer which is the exponent.
	 * @return our element in the b'th power.
	 */
	public abstract IGroupElement power(LargeInteger b);

	/**
	 * 
	 * @param b
	 *            another group element
	 * @return true if and only if our element and b are equal. That means,
	 *         represent the same number or point and belong to the same group.
	 */
	public abstract boolean equals(IGroupElement b);
}