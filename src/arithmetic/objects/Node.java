package arithmetic.objects;




import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;


public class Node implements ByteTree {

	private ArrayList<byte[]> children;
	private int numOfChildren;


	public Node (byte[] bt) {
		//TODO: implement
	}

	public Node(ByteTree[] arr) {
		//TODO: IMPLEMENT
	}

	public Node() {

	}

	private int getEndObjectIndex(byte[] b, int i) {
		if (b[i] == 1) {
			int size = intFromByteArray(Arrays.copyOfRange(b, i+1, 4));
			return i + 4 + size + 1;
		}
		if (b[i] == 0) {
			int numOfChildren = calcSize(object, i+1, 4);
			i=i+5;
			for (int j = 0; j < numOfChildren; j++) {
				i = getEndObjectIndex(object, i);
			}
			return i;
		}
	}
	
	
		public int intFromByteArray (byte[] a) {
			ByteBuffer b = ByteBuffer.wrap(a);
		    return b.getInt();
		}
		
		public ByteTree getAt (int index) {
			return children.get(index);
		}

		public void add(ByteTree element) {
			children.add(element);
		}

		public byte[] toByteArray() throws UnsupportedEncodingException {
			byte[] a = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(children.size()).array();
			byte[] b = new byte[a.length+1];
			System.arraycopy(a, 0, b, 1, a.length);
			b[0] = 0;
			for (int i=0; i<children.size(); i++) {
				byte[] c = children.get(i).toByteArray();
				b = Arrays.copyOf(b, b.length+c.length);
				for (int j=b.length; j<b.length+c.length; j++)
					b[j]=c[j-b.length];
			}
			return b;
		}

			}
