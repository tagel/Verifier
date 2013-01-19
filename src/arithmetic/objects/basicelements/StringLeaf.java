package arithmetic.objects.basicelements;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arithmetic.objects.ByteTree;

/**
 * This class represent a leaf of an ASCII string.
 * @author Itay
 *
 */
public class StringLeaf  implements ByteTree{


	private String str;
	/**
	 * 
	 * @param str the string this leaf represents.
	 */
	public StringLeaf (String str) {
		this.str = str;
	}

	public String getString() {
		return str;
	}
	
	/**
	 * returns the byte array representation (as a byte tree) of the string.
	 */
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] b = str.getBytes("ASCII");
		int numOfBytes = b.length;
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(numOfBytes).array();
		byte[] c= new byte[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		byte[] ret = new byte[c.length+1];
		System.arraycopy(c, 0, ret, 1, c.length);
		ret[0] = 1;
		return ret;
	}

}


