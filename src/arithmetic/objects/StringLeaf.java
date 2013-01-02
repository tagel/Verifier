package arithmetic.objects;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class StringLeaf implements ByteTree {


	private String str;

	public StringLeaf (String str) {
		this.str = str;
	}

	public String getString() {
		return str;
	}
	@Override
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


