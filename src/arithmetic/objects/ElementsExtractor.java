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
 * the functions in this class are static and are mainly used by the verifiers
 * and provers, helping them derive the right elements from the .bt files that
 * we get from the verificatum.
 * 
 * @author Itay
 */
public class ElementsExtractor {
	private static final int TWO = 2;
	private static final int FROM_INDEX = 5;
	private static final int TEN = 10;

	/**
	 * @param byte array arr that represents a leaf of an integer
	 * @return the integer that the byte array represents.
	 */
	public static LargeInteger leafToInt(byte[] arr) {

		byte[] helperArr = Arrays.copyOfRange(arr, FROM_INDEX, arr.length);
		return new LargeInteger(helperArr);
	}

	/**
	 * @param byte array arr that represents a leaf of a string.
	 * @return the string that arr represents.
	 * 
	 */
	public static String leafToString(byte[] arr) {

		byte[] helperArr = Arrays.copyOfRange(arr, FROM_INDEX, arr.length);
		try {
			return new String(helperArr, "ASCII");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param arr
	 *            = a byte array that represents a point.
	 * @return the point that arr represents.
	 */
	public static Point nodeToPoint(byte[] arr, IGroup group) {

		byte[] arrX = Arrays.copyOfRange(arr, FROM_INDEX, TEN
				+ group.getFieldOrder().toByteArray().length);
		byte[] arrY = Arrays.copyOfRange(arr, TEN
				+ group.getFieldOrder().toByteArray().length, arr.length);
		IField<IntegerFieldElement> field = new PrimeOrderField(
				group.getFieldOrder());
		IntegerFieldElement x = new IntegerFieldElement(leafToInt(arrX), field);
		IntegerFieldElement y = new IntegerFieldElement(leafToInt(arrY), field);
		return new Point(x, y);
	}

	/**
	 * 
	 * @param path
	 *            a path to the file
	 * @param filename
	 *            the name of the .bt file
	 * @return the byte array written in the file. If the file is not found,
	 *         returns null.
	 */
	public static byte[] btFromFile(String path, String filename) {

		try {
			File file = new File(path, filename);
			InputStream stream = new FileInputStream(file);
			byte[] b = new byte[(int) (file.length())];
			stream.read(b, 0, b.length);
			stream.close();
			return b;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 
	 * the same function as the above, only here the input is a path and a
	 * subpath.
	 */
	public static byte[] btFromFile(String path, String subpath, String filename) {

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
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param groupElem
	 *            a byte array representing a group element
	 * @param Gq
	 *            the group which the element belongs to.
	 * 
	 * @return the group element which b represents. this static function is
	 *         needed because there are two types of groups currently in our
	 *         verifier: modular, and elliptic curve. the provers and verifiers
	 *         do not know which kind of group element they need to create and
	 *         for that reason this function checks which kind of group element
	 *         needs to be created and creates it appropriately.
	 */
	public static IGroupElement createGroupElement(byte[] groupElem, IGroup Gq) {

		if (Gq instanceof ModGroup)
			return new ModGroupElement(leafToInt(groupElem), (ModGroup) Gq);
		if (Gq instanceof ECurveGroup)
			return new ECurveGroupElement(nodeToPoint(groupElem, Gq),
					(ECurveGroup) Gq);
		else {
			return null;
		}
	}

	/**
	 * A simple PGE (=Product Group Element) means the product element has only
	 * group elements in its coordinates (it's not recursive, its coordinates
	 * cannot be product elements themselves), and all group elements belong to
	 * the same group. this is a static "helper" function for the verifiers and
	 * provers.
	 * 
	 * @param bt
	 *            a byte array representing a "simple" product group element.
	 * @param group
	 *            the group which all the group elements belong to.
	 * @param w
	 *            the width = the number of coordinates in the product group
	 *            element. we need this parameter only to check whether it is 1,
	 *            because then its byte array representation is different than
	 *            the representation specified in the document. it does not
	 *            start with 0 0 0 0 1 to state that it has one coordinate, but
	 *            rather begins straight with the presentation of its
	 *            coordinate.
	 * @return the product group element which bt represents.
	 * 
	 */
	public static ProductGroupElement createSimplePGE(byte[] bt, IGroup group,
			int w) {
		if (w == 1) {
			IGroupElement e = createGroupElement(bt, group);
			ArrayOfElements<IGroupElement> arr = new ArrayOfElements<IGroupElement>();
			arr.add(e);
			return new ProductGroupElement(arr);
		} else {
			ArrayOfElements<IGroupElement> arr = ArrayGenerators
					.createGroupElementArray(bt, group);
			return new ProductGroupElement(arr);
		}
	}

	/**
	 * A ciphertext is a product group element which has two coordinates
	 * (referred to as "left" and "right" in our project), each of the two
	 * coordinates is a product group element itself containing an equal number
	 * (w) of group elements, all belonging to the same group.
	 * 
	 * @param bt
	 *            a byte array representing a ciphertext (as a byte tree)
	 * @param group
	 *            the group which all the group elements inside the ciphertext
	 *            belong to.
	 * @param w
	 *            the width, the number of coordinates the "left" and "right"
	 *            product elements have.
	 * @return the ciphertext which bt represents (which is a profuct group
	 *         element).
	 * 
	 */

	public static ProductGroupElement createCiphertext(byte[] bt, IGroup group,
			int w) {

		Node node = new Node(bt);
		if (w == 1) {
			ArrayOfElements<IGroupElement> leftArr = new ArrayOfElements<IGroupElement>();
			leftArr.add(createGroupElement(node.getAt(0).toByteArray(), group));
			ArrayOfElements<IGroupElement> rightArr = new ArrayOfElements<IGroupElement>();
			rightArr.add(createGroupElement(node.getAt(1).toByteArray(), group));
			return new ProductGroupElement(new ProductGroupElement(leftArr),
					new ProductGroupElement(rightArr));
		}
		// width > 1
		else {
			ProductGroupElement left = createSimplePGE(node.getAt(0)
					.toByteArray(), group, w);
			ProductGroupElement right = createSimplePGE(node.getAt(1)
					.toByteArray(), group, w);
			return new ProductGroupElement(left, right);
		}
	}

	/**
	 * @param <E>
	 * @param s
	 *            string s, representing a certain group.
	 * @return the group recovered from s by removing the comment and colons,
	 *         converting the hexa string to a byte array, converting the byte
	 *         array into a byte tree, and converting the byte tree into the
	 *         group.
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public static IGroup unmarshal(String s) {
		
		int i = s.indexOf(":");
		s = s.substring(i + TWO, s.length());
		byte[] b = CryptoUtils.hexStringToBytes(s);
		Node node = new Node(b);
		String type = leafToString(node.getAt(0).toByteArray());
		if (type.equals("verificatum.arithm.ModPGroup"))
			return new ModGroup(node.getAt(1).toByteArray());
		if (type.equals("verificatum.arithm.ECqPGroup"))
			return new ECurveGroup(leafToString(node.getAt(1).toByteArray()));
		else {
			return null;
		}
	}
}
