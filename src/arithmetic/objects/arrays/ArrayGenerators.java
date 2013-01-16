package arithmetic.objects.arrays;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IRing;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;

public class ArrayGenerators {

	public static ArrayOfElements<IGroupElement> createGroupElementArray(
			byte[] b, IGroup group) throws UnsupportedEncodingException {
		ArrayOfElements<IGroupElement> ret = new ArrayOfElements<IGroupElement>();
		Node node = new Node(b);
		for (int i = 0; i < node.getChildrenSize(); i++)
			ret.add(ElementsExtractor.createGroupElement(node.getAt(i)
					.toByteArray(), group));
		return ret;
	}

	public static ArrayOfElements<IntegerRingElement> createRingElementArray(
			byte[] b, IRing<IntegerRingElement> ring)
			throws UnsupportedEncodingException {
		ArrayOfElements<IntegerRingElement> ret = new ArrayOfElements<IntegerRingElement>();
		Node node = new Node(b);
		for (int i = 0; i < node.getChildrenSize(); i++)
			ret.add(new IntegerRingElement(ElementsExtractor.leafToInt(node
					.getAt(i).toByteArray()), ring));
		return ret;
	}

	public static ArrayOfElements<ProductGroupElement> createArrayOfCiphertexts(
			byte[] data, IGroup group, int w)
			throws UnsupportedEncodingException {
		ArrayOfElements<ProductGroupElement> ret = new ArrayOfElements<ProductGroupElement>();
		Node node = new Node(data);
		int arraySize = (new Node(node.getAt(0).toByteArray()))
				.getChildrenSize();
		if (w == 1
				&& new Node(new Node(node.getAt(0).toByteArray()).getAt(0)
						.toByteArray()).getChildrenSize() == 2) {
			ArrayOfElements<IGroupElement> leftArr = createGroupElementArray(
					node.getAt(0).toByteArray(), group);
			ArrayOfElements<IGroupElement> rightArr = createGroupElementArray(
					node.getAt(1).toByteArray(), group);
			for (int i = 0; i < arraySize; i++) {
				ArrayOfElements<IGroupElement> arr1 = new ArrayOfElements<IGroupElement>();
				arr1.add(leftArr.getAt(i));
				ArrayOfElements<IGroupElement> arr2 = new ArrayOfElements<IGroupElement>();
				arr2.add(rightArr.getAt(i));
				ProductGroupElement ciphertext = ElementsExtractor
						.createCiphertext(arr1, arr2);
				ret.add(ciphertext);
			}
		} else {
			for (int i = 0; i < arraySize; i++) {
				ProductGroupElement simplePGEleft = ElementsExtractor
						.createSimplePGE(new Node(node.getAt(0).toByteArray())
								.getAt(i).toByteArray(), group);
				ProductGroupElement simplePGEright = ElementsExtractor
						.createSimplePGE(new Node(node.getAt(1).toByteArray())
								.getAt(i).toByteArray(), group);
				ProductGroupElement ciphertext = new ProductGroupElement(
						simplePGEleft, simplePGEright);
				ret.add(ciphertext);
			}
		}
		return ret;
	}

	public static ArrayOfElements<ProductRingElement> createArrayOfPlaintexts(
			byte[] data, IRing<IntegerRingElement> ring)
			throws UnsupportedEncodingException {
		ArrayOfElements<ProductRingElement> ret = new ArrayOfElements<ProductRingElement>();
		Node node = new Node(data);
		int w = node.getChildrenSize();
		int size = (new Node(node.getAt(0).toByteArray())).getChildrenSize();
		for (int i = 0; i < size; i++) {
			ArrayOfElements<IntegerRingElement> arr = new ArrayOfElements<IntegerRingElement>();
			for (int j = 0; j < w; j++) {
				LargeInteger a = ElementsExtractor.leafToInt(new Node(node
						.getAt(j).toByteArray()).getAt(i).toByteArray());
				IntegerRingElement element = new IntegerRingElement(a, ring);
				arr.add(element);
			}
			ret.add(new ProductRingElement(arr));
		}
		return ret;
	}

	public static byte[] concatArrays(byte[] A, byte[] B) {
		byte[] C = new byte[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}
}
