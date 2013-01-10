package arithmetic.objects.Arrays;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.BasicElements.Node;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ProductGroupElement;
import arithmetic.objects.Ring.IRing;
import arithmetic.objects.Ring.IntegerRingElement;

public class ArrayGenerators {
	

	
	public static ArrayOfElements<IGroupElement> createGroupElementArray (byte[] b, IGroup group) throws UnsupportedEncodingException {
		ArrayOfElements<IGroupElement> ret = new ArrayOfElements<IGroupElement>();
		Node node = new Node(b);
		for (int i=0; i<node.getChildrenSize(); i++)
			ret.add(ElementsExtractor.createGroupElement(node.getAt(i).toByteArray(), group));
		return ret;
	}
	
	public static ArrayOfElements<IntegerRingElement> createRingElementArray (byte[] b, IRing<IntegerRingElement> ring) throws UnsupportedEncodingException {
		ArrayOfElements<IntegerRingElement> ret = new ArrayOfElements<IntegerRingElement>();
		Node node = new Node(b);
		for (int i=0; i<node.getChildrenSize(); i++) 
			ret.add(new IntegerRingElement(ElementsExtractor.leafToInt(node.getAt(i).toByteArray()), ring));
		return ret;
	}
	
	public static ArrayOfElements<ProductGroupElement> createArrayOfCiphertexts (byte[] data, IGroup group) {
		return null;
	}
	
	
	public static ArrayOfElements<ProductGroupElement> createArrayOfPlaintexts (byte[] data, IGroup group) {
		return null;
	}

	
	public static byte[] concatArrays(byte[] A, byte[] B) {
		byte[] C= new byte[A.length+B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}
}