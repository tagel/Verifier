package arithmetic.objects;

import java.io.UnsupportedEncodingException;

/**
 * This is a "wrapper" interface used to denote each arithmetic object as a byte tree. Thus, every arithmetic object is going to implement this interface.
 */

public interface ByteTree {
	
	
	public byte[] toByteArray() throws UnsupportedEncodingException;
		

}
