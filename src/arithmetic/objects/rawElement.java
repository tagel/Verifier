package arithmetic.objects;

import java.io.UnsupportedEncodingException;

public class rawElement implements ByteTree {
	
	private byte[] byteArr;
	
	public rawElement (byte[] byteArr) {
		this.byteArr = byteArr;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return byteArr;
	}

}
