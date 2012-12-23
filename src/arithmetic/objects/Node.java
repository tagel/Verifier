package arithmetic.objects;


public class Node implements ByteTree {

	private ByteTree[] children; 
	
	 
	public Node (ByteTree[] children) {
		this.children = children;
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
