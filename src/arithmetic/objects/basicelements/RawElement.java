package arithmetic.objects.basicelements;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ByteTree;

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
