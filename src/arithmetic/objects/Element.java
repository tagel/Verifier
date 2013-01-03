package arithmetic.objects;




import java.io.UnsupportedEncodingException;

/**
 * This is a "wrapper" interface used to denote each arithmetic object as a byte tree.
 *
 */
public interface Element {

	/**
	 * @return the byte array representation of the byte tree.
	 * @throws UnsupportedEncodingException 
	 */
	public ByteTree toByteTree() throws UnsupportedEncodingException;

}
