package arithmetic.objects;

import java.io.UnsupportedEncodingException;

public class RawElement implements ByteTree {
	
	private byte[] byteArr;
	
	public RawElement (byte[] byteArr) {
		this.byteArr = byteArr;
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		return byteArr;
	}

}
