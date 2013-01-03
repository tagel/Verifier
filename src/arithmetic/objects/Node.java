package arithmetic.objects;




import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class Node implements Element {

	private Element[] children; 
	
	 
	public Node (Element[] children) {
		this.children = children;
	}
	
	public Element[] getChildrenArray() {
		return children;
	}
	
	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(children.length).array();
		byte[] b = new byte[a.length+1];
		System.arraycopy(a, 0, b, 1, a.length);
		b[0] = 0;
		for (int i=0; i<children.length; i++) {
			byte[] c = children[i].toByteArray();
			b = Arrays.copyOf(b, b.length+c.length);
			for (int j=b.length; j<b.length+c.length; j++)
				b[j]=c[j-b.length];
		}
		return b;
	}

}
