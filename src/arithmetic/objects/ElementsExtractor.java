package arithmetic.objects;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;




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

	public static byte[] byteArrFromFile (String path) throws IOException {
		File file = new File(path);
		InputStream stream = new FileInputStream(file);
		byte[] b = new byte[(int) (file.length())];
		stream.read(b, 0, b.length);
		stream.close();
		return b;
	}
	
	public static IGroupElement createGroupElement (ByteTree b) {
		if (b instanceof ModGroupElement) 
			b = (ModGroupElement) b;
		return b;
		
	}
	
	public static byte[] concatArrays(byte[] A, byte[] B) {
		   byte[] C= new byte[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);
		   return C;
		}


	/**
	 * @param <E>
	 * @param a string s, representing a certain group.
	 * @return the group recovered from s by removing the comment and colons, converting the hexa string to a byte array, converting the byte array into a byte tree, and converting the byte tree into the group.
	 */
	public static  IGroup unmarshal (String s) {
		BigInteger p = BigInteger.ONE;
		IGroup ret = new ModGroup(p,p,p);
		return ret;
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
