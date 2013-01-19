package arithmetic.objects.basicelements;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ByteTree;

/**
 * This class is used to represent a byte tree element which we yet don't know
 * what it is. therefore is is called "raw", as we only have its byte array
 * representation and do not know yet what it represents. Note that this class
 * is only being used by the Node class, therefore can be considered as a helper
 * class for Node.
 * 
 * @author Itay
 * 
 */
public class RawElement implements ByteTree {

	private byte[] byteArr;

	public RawElement(byte[] byteArr) {
		this.byteArr = byteArr;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return byteArr;
	}

}
