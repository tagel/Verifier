package arithmetic.objects;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

import cryptographic.primitives.CryptoUtils;

import arithmetic.objects.Groups.ECurveGroup;
import arithmetic.objects.Groups.ECurveGroupElement;
import arithmetic.objects.Groups.IGroup;
import arithmetic.objects.Groups.IGroupElement;
import arithmetic.objects.Groups.ModGroup;
import arithmetic.objects.Groups.ModGroupElement;
import arithmetic.objects.Groups.Point;
import arithmetic.objects.Groups.ProductGroupElement;




/**
 * This class has methods which help us extract elements from given byte arrays.
 *
 */
public class ElementsExtractor {

	/**
	 * @param byte array arr that represents a leaf of an integer
	 * @return the integer that the byte array represents.
	 */
	public static BigInteger leafToInt (byte[] arr) { 
		byte[] a = Arrays.copyOfRange(arr, 5, arr.length-1);
		return new BigInteger(a);
	}


	/**
	 * @param byte array arr that represents a leaf of a string.
	 * @return the string that arr represents.
	 * @throws UnsupportedEncodingException 
	 */
	public static String leafToString (byte[] arr) throws UnsupportedEncodingException {
		byte[] a = Arrays.copyOfRange(arr, 5, arr.length-1);
		return new String(a, "ASCII");
	}

	/**
	 * 
	 * @param arr = a byte array that represents a point.
	 * @return the point that arr represents.
	 */
	public static Point nodeToPoint (byte[] arr) {
		return null;
	}

	// if file is not found, returns null.
	public static byte[] btFromFile (String path, String filename) throws IOException {
		try {
			File file = new File(path, filename);
			InputStream stream = new FileInputStream(file);
			byte[] b = new byte[(int) (file.length())];
			stream.read(b, 0, b.length);
			stream.close();
			return b;
		}
		catch (FileNotFoundException e) { return null;}

	}
	
	public static byte[] btFromFile (String path, String subpath, String filename) throws IOException {
		try {
			File f1 = new File(path, subpath);
			File file = new File(f1, filename);
			InputStream stream = new FileInputStream(file);
			byte[] b = new byte[(int) (file.length())];
			stream.read(b, 0, b.length);
			stream.close();
			return b;
		}
		catch (FileNotFoundException e) { return null;}

	}
	
	

	public static IGroupElement createGroupElement (byte[] b, IGroup Gq ) {
		if (Gq instanceof ModGroup)
			return new ModGroupElement(leafToInt(b),(ModGroup) Gq);
		if (Gq instanceof ECurveGroup)
			return new ECurveGroupElement(nodeToPoint(b), (ECurveGroup) Gq);
		else {
			System.out.println("ERROR: instance is not a group element");
			return null;
		}
	}

	public static byte[] concatArrays(byte[] A, byte[] B) {
		byte[] C= new byte[A.length+B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}

//	public static String hex (byte[] a) {
//		StringBuilder sb = new StringBuilder();
//		for(byte b: a)
//			sb.append(String.format("%02x", b&0xff));
//		return sb.toString();
//	}
//
//	public static byte[] unhex (String s) {
//		int len = s.length();
//		byte[] data = new byte[len / 2];
//		for (int i = 0; i < len; i += 2) 
//			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
//		return data;
//	}
	/**
	 * @param <E>
	 * @param a string s, representing a certain group.
	 * @return the group recovered from s by removing the comment and colons, converting the hexa string to a byte array, converting the byte array into a byte tree, and converting the byte tree into the group.
	 * @throws UnsupportedEncodingException 
	 */
	public static IGroup unmarshal (String s) throws UnsupportedEncodingException  {
		int i = s.indexOf(":");
		String type = s.substring(0, i-1);
		s = s.substring(i+2, s.length()-1);
		byte[] b = CryptoUtils.hexStringToBytes(s);
		if (type.equals("verificatum.arithm.ModPGroup"))
			return new ModGroup(b);
		if (type.equals("verificatum.arithm.ECqPGroup"))
			return new ECurveGroup(leafToString(b));
		else { 
			System.out.println("ERROR: name of java class is unrecognized by the system");
			return null;
		}
	}
	
	public static ArrayOfElements<ProductGroupElement> createArrayOfCiphertexts (byte[] data) {
		return null;
	}
	
	
	public static ArrayOfElements<ProductGroupElement> createArrayOfPlaintexts (byte[] data) {
		return null;
	}
	
	public static ProductGroupElement createSimplePGE (byte[] bt, IGroup group) {
		return null;
	}
	
	public static ProductGroupElement createCipherText (ArrayOfElements<IGroupElement> left, ArrayOfElements<IGroupElement> right) {
		return null;
		//TODO: IMPLEMENT
	}

	/**
	 * @param p = the order of the underlying field Zp.
	 * @param seed used by the PRG to produce a random string.
	 * @return an array of modular group elements derived from a random string.
	 */
	public static int[] deriveModGroupElements (int p, int seed) {
		return null;
	}

	/**
	 * @param q = the order of the underlying field Zq.
	 * @param seed used by the PRG to produce a random string.
	 * @return an array of elliptic curve points derived from a random string
	 */
	public static Point[] deriveECurveGroupElements (int q, int seed) {
		return null;
	}

}
