package arithmetic.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.field.IField;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;
import arithmetic.objects.groups.ECurveGroup;
import arithmetic.objects.groups.ECurveGroupElement;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ModGroup;
import arithmetic.objects.groups.ModGroupElement;
import arithmetic.objects.groups.Point;
import arithmetic.objects.groups.ProductGroupElement;
import cryptographic.primitives.CryptoUtils;

/**
 * This class has methods which help us extract elements from given byte arrays.
 * 
 */
public class ElementsExtractor {

	/**
	 * @param byte array arr that represents a leaf of an integer
	 * @return the integer that the byte array represents.
	 */
	public static LargeInteger leafToInt(byte[] arr) {
		byte[] a = Arrays.copyOfRange(arr, 5, arr.length);
		return new LargeInteger(a);
	}

	/**
	 * @param byte array arr that represents a leaf of a string.
	 * @return the string that arr represents.
	 * @throws UnsupportedEncodingException
	 */
	public static String leafToString(byte[] arr)
			throws UnsupportedEncodingException {
		byte[] a = Arrays.copyOfRange(arr, 5, arr.length);
		return new String(a, "ASCII");
	}

	/**
	 * 
	 * @param arr
	 *            = a byte array that represents a point.
	 * @return the point that arr represents.
	 */
	public static Point nodeToPoint(byte[] arr, IGroup group) {
		byte[] arrX = Arrays.copyOfRange(arr, 5, 10 + group.getFieldOrder()
				.toByteArray().length);
		byte[] arrY = Arrays.copyOfRange(arr, 10 + group.getFieldOrder()
				.toByteArray().length, arr.length);
		IField<IntegerFieldElement> field = new PrimeOrderField(
				group.getFieldOrder());
		IntegerFieldElement x = new IntegerFieldElement(leafToInt(arrX), field);
		IntegerFieldElement y = new IntegerFieldElement(leafToInt(arrY), field);
		return new Point(x, y);

	}

	// if file is not found, returns null.
	public static byte[] btFromFile(String path, String filename)
			throws IOException {
		try {
			File file = new File(path, filename);
			InputStream stream = new FileInputStream(file);
			byte[] b = new byte[(int) (file.length())];
			stream.read(b, 0, b.length);
			stream.close();
			return b;
		} catch (FileNotFoundException e) {
			return null;
		}

	}

	public static byte[] btFromFile(String path, String subpath, String filename)
			throws IOException {
		try {
			File f1 = new File(path, subpath);
			File file = new File(f1, filename);
			InputStream stream = new FileInputStream(file);
			byte[] b = new byte[(int) (file.length())];
			stream.read(b, 0, b.length);
			stream.close();
			return b;
		} catch (FileNotFoundException e) {
			return null;
		}

	}

	public static IGroupElement createGroupElement(byte[] b, IGroup Gq) {
		if (Gq instanceof ModGroup)
			return new ModGroupElement(leafToInt(b), (ModGroup) Gq);
		if (Gq instanceof ECurveGroup)
			return new ECurveGroupElement(nodeToPoint(b, Gq), (ECurveGroup) Gq);
		else {
			System.out.println("ERROR: instance is not a group element");
			return null;
		}
	}

	public static ProductGroupElement createSimplePGE(byte[] bt, IGroup group)
			throws UnsupportedEncodingException {
		ArrayOfElements<IGroupElement> arr = ArrayGenerators
				.createGroupElementArray(bt, group);
		return new ProductGroupElement(arr);
	}

	public static ProductGroupElement createCiphertext(byte[] bt, IGroup group, int w)
			throws UnsupportedEncodingException {
		Node node = new Node(bt);
		if (w == 1
				&& new Node(node.getAt(0).toByteArray()).getChildrenSize() == 2) {
			ArrayOfElements<IGroupElement> leftArr = new ArrayOfElements<IGroupElement>();
			leftArr.add(createGroupElement(node.getAt(0).toByteArray(), group));
			ArrayOfElements<IGroupElement> rightArr = new ArrayOfElements<IGroupElement>();
			rightArr.add(createGroupElement(node.getAt(1).toByteArray(), group));
			
				return createCiphertext(leftArr, rightArr);
				
			}
		
			else {
		
		ProductGroupElement left = createSimplePGE(node.getAt(0).toByteArray(),
				group);
		ProductGroupElement right = createSimplePGE(
				node.getAt(1).toByteArray(), group);
		return new ProductGroupElement(left, right);
			}
	}

	public static ProductGroupElement createCiphertext(
			ArrayOfElements<IGroupElement> left,
			ArrayOfElements<IGroupElement> right) {
		return new ProductGroupElement(new ProductGroupElement(left),
				new ProductGroupElement(right));
	}

	/**
	 * @param <E>
	 * @param a
	 *            string s, representing a certain group.
	 * @return the group recovered from s by removing the comment and colons,
	 *         converting the hexa string to a byte array, converting the byte
	 *         array into a byte tree, and converting the byte tree into the
	 *         group.
	 * @throws UnsupportedEncodingException
	 */
	public static IGroup unmarshal(String s)
			throws UnsupportedEncodingException {
		int i = s.indexOf(":");
		s = s.substring(i + 2, s.length());
		byte[] b = CryptoUtils.hexStringToBytes(s);
		Node node = new Node(b);
		String type = leafToString(node.getAt(0).toByteArray());
		if (type.equals("verificatum.arithm.ModPGroup"))
			return new ModGroup(node.getAt(1).toByteArray());
		if (type.equals("verificatum.arithm.ECqPGroup"))
			return new ECurveGroup(leafToString(node.getAt(1).toByteArray()));
		else {
			System.out
					.println("ERROR: name of java class is unrecognized by the system");
			return null;
		}
	}

}
