package arithmetic.objects.arrays;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;

/**
 * This class has functions that serve as helpers to the verifiers and provers
 * by creating arrays of different kinds.
 * 
 * @author Itay
 * 
 */
public class ArrayGenerators {

	/**
	 * 
	 * @param b
	 *            = a byte array representation of an array of group elements
	 *            all belonging to the same group.
	 * @param group
	 *            = the group which the elements in the array belong to.
	 * @return an array of group elements which "b" represents.
	 */
	public static ArrayOfElements<IGroupElement> createGroupElementArray(
			byte[] b, IGroup group) {
		ArrayOfElements<IGroupElement> ret = new ArrayOfElements<IGroupElement>();
		Node node = new Node(b);
		for (int i = 0; i < node.getChildrenSize(); i++)
			ret.add(ElementsExtractor.createGroupElement(node.getAt(i)
					.toByteArray(), group));
		return ret;
	}

	/**
	 * 
	 * @param b
	 *            = a byte array representation of an array of ring elements all
	 *            belonging to the same ring.
	 * @param ring
	 *            = the ring which the elements in the array belong to.
	 * @return an array of ring elements which "b" represents.
	 */
	public static ArrayOfElements<IntegerRingElement> createRingElementArray(
			byte[] b, IRing<IntegerRingElement> ring) {
		ArrayOfElements<IntegerRingElement> ret = new ArrayOfElements<IntegerRingElement>();
		Node node = new Node(b);
		for (int i = 0; i < node.getChildrenSize(); i++)
			ret.add(new IntegerRingElement(ElementsExtractor.leafToInt(node
					.getAt(i).toByteArray()), ring));
		return ret;
	}

	/**
	 * 
	 * @param data
	 *            = a byte array representation of an array of ciphertexts.
	 * @param group
	 *            = the ciphertexts in the array all contain product group
	 *            elements which contain group elements belonging to the same
	 *            group. this group is our input group.
	 * @param w
	 *            = the width of each product group element in the ciphertexts
	 *            of the array.
	 * @return an array of product group elements which is an array of
	 *         ciphertexts.
	 */
	public static ArrayOfElements<ProductGroupElement> createArrayOfCiphertexts(
			byte[] data, IGroup group, int w) {
		ArrayOfElements<ProductGroupElement> ret = new ArrayOfElements<ProductGroupElement>();
		Node node = new Node(data);
		int arraySize = (new Node(node.getAt(0).toByteArray()))
				.getChildrenSize();
		if (w == 1) {
			ArrayOfElements<IGroupElement> leftArr = createGroupElementArray(
					node.getAt(0).toByteArray(), group);
			ArrayOfElements<IGroupElement> rightArr = createGroupElementArray(
					node.getAt(1).toByteArray(), group);
			for (int i = 0; i < arraySize; i++) {
				ArrayOfElements<IGroupElement> arr1 = new ArrayOfElements<IGroupElement>();
				arr1.add(leftArr.getAt(i));
				ArrayOfElements<IGroupElement> arr2 = new ArrayOfElements<IGroupElement>();
				arr2.add(rightArr.getAt(i));
				ProductGroupElement ciphertext = new ProductGroupElement(
						new ProductGroupElement(arr1), new ProductGroupElement(
								arr2));
				ret.add(ciphertext);
			}
		} else {
			for (int i = 0; i < arraySize; i++) {
				ProductGroupElement simplePGEleft = ElementsExtractor
						.createSimplePGE(new Node(node.getAt(0).toByteArray())
								.getAt(i).toByteArray(), group, w);
				ProductGroupElement simplePGEright = ElementsExtractor
						.createSimplePGE(new Node(node.getAt(1).toByteArray())
								.getAt(i).toByteArray(), group, w);
				ProductGroupElement ciphertext = new ProductGroupElement(
						simplePGEleft, simplePGEright);
				ret.add(ciphertext);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param data
	 *            = a byte array representation of an array of plaintexts
	 * @param group
	 *            = the plaintexts in the array are all product group elements
	 *            which contain group elements belonging to the same group. this
	 *            group is our input group.
	 * @return an array of product group elements which is an array of
	 *         plaintexts.
	 */
	public static ArrayOfElements<ProductGroupElement> createArrayOfPlaintexts(
			byte[] data, IGroup group, int w) {
		ArrayOfElements<ProductGroupElement> ret = new ArrayOfElements<ProductGroupElement>();
		Node node = new Node(data);
		if (w == 1) {
			int size = node.getChildrenSize();
			for (int i = 0; i < size; i++) {
				ArrayOfElements<IGroupElement> arr = new ArrayOfElements<IGroupElement>();
				arr.add(ElementsExtractor.createGroupElement(node.getAt(i)
						.toByteArray(), group));
				ret.add(new ProductGroupElement(arr));
			}
		} else {
			int size = new Node(node.getAt(0).toByteArray()).getChildrenSize();
			for (int i = 0; i < size; i++) {
				ArrayOfElements<IGroupElement> arr = new ArrayOfElements<IGroupElement>();
				for (int j = 0; j < w; j++) {
					IGroupElement a = ElementsExtractor.createGroupElement(
							new Node(node.getAt(j).toByteArray()).getAt(i)
									.toByteArray(), group);
					arr.add(a);
				}
				ret.add(new ProductGroupElement(arr));
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param A
	 *            byte array
	 * @param B
	 *            byte array
	 * @return one byte array which is a concatenation of A and B.
	 */
	public static byte[] concatArrays(byte[] A, byte[] B) {
		byte[] C = new byte[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}
}
