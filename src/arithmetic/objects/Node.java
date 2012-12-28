package arithmetic.objects;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Node implements ByteTree {

	private ByteTree[] children; 
	
	 
	public Node (ByteTree[] children) {
		this.children = children;
	}
	
	public ByteTree[] getChildrenArray() {
		return children;
	}
	
	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(children.length).array();
		byte[] b = new byte[a.length+1];
		System.arraycopy(a, 0, b, 1, a.length);
		b[0] = 1;
		for (int i=0; i<children.length; i++) {
			byte[] c = children[i].toByteArray();
			
					
		}
		return b;
	}

}
