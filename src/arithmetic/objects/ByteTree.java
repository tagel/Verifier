package arithmetic.objects;

/**
 * This is a "wrapper" interface used to denote each arithmetic object as a byte
 * tree. Thus, every arithmetic object is going to implement this interface.
 * (field, ring, and group elements, product elements, arrays of elements,
 * nodes, leaves, etc...
 */

public interface ByteTree {

	/**
	 * 
	 * @return the byte array representation (as a byte tree) of the object.
	 */
	public byte[] toByteArray();

}
