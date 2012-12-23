package arithmetic.objects;
import java.math.BigInteger;


/**
 * This class has methods which help us extract elements from given byte arrays.
 *
 */
public class ElementsExtractor {
	
	/**
	 * @param byte array arr that represents a leaf of an integer
	 * @return the integer that the byte array represents.
	 */
	public BigInteger leafToInt (byte[] arr) { 
		return null;
	}

	/**
	 * @param byte array arr that represents a leaf of an array of booleans
	 * @return the array of booleans that arr represents.
	 */
	public boolean[] leafToBoolArr (byte[] arr) {
		return null;
	}
	
	/**
	 * @param byte array arr that represents a leaf of a string.
	 * @return the string that arr represents.
	 */
	public String leafToString (byte[] arr) {
		return null;
	}
	
	/**
	 * @param byte array arr that represents a point.
	 * @return the point that arr represents
	 */
	public Point nodeToPoint (byte[] arr) {
		return null;
	}
	
	/**
	 * @param byte array arr that represent a product element.
	 * @return the product element that arr represents.
	 */
	public  ProductElement nodeToProductElement (byte[] arr) {
		return null;
	}

	
	/**
	 * @param <E>
	 * @param a string s, representing a certain group.
	 * @return the group recovered from s by removing the comment and colons, converting the hexa string to a byte array, converting the byte array into a byte tree, and converting the byte tree into the group.
	 */
	public <E> IGroup<E> unmarshal (String s) {
		return null;
	}
	
	
	/**
	 * @param p = the order of the underlying field Zp.
	 * @param seed used by the PRG to produce a random string.
	 * @return an array of modular group elements derived from a random string.
	 */
	public int[] deriveModGroupElements (int p, int seed) {
		return null;
	}
	
	/**
	 * @param q = the order of the underlying field Zq.
	 * @param seed used by the PRG to produce a random string.
	 * @return an array of elliptic curve points derived from a random string
	 */
	public Point[] deriveECurveGroupElements (int q, int seed) {
		return null;
	}
	
}
