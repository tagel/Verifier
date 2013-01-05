package arithmetic.objects;

public class Leaf implements ByteTree {
	
	private byte[] arr;
	
	public Leaf(byte[] arr) {
		this.arr = arr;
	}

	@Override
	public byte[] toByteArray() {
		return arr;
	}

}
