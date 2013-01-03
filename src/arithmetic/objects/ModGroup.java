package arithmetic.objects;



import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;


/**
 * This class represents a multiplicative modulo prime Group.
 *
 */
public class ModGroup implements IGroup {

	/**
	 * p = the order of the underlying field Z*p
	 */
	private BigInteger p;
	/**
	 * q = the group order
	 */
	private BigInteger q;
	/**
	 *  g = generator
	 */
	private BigInteger g;
	/**
	 * group type: either modular or elliptic curve. (in this case, modular).
	 */
	private String groupType = "Modular";

	/**
	 * @param p
	 * @param q
	 * @param g
	 * Constructor.
	 */
	public ModGroup (BigInteger p, BigInteger q, BigInteger g) {
		this.p = p;
		this.q = q;
		this.g = g;
	}

	public ModGroup (byte[] arr) {
		byte[] a = new byte[4];
		a[0] = arr[6];
		a[1] = arr[7];
		a[2] = arr[8];
		a[3] = arr[9];
		int x = ByteBuffer.wrap(a).getInt();
		a = new byte[x];
		for (int i=0; i<x; i++)
			a[i] = arr[10+i];
		this.p = new BigInteger(a);
		a = new byte[4];
		a[0] = arr[9+x+2];
		a[1] = arr[9+x+3];
		a[2] = arr[9+x+4];
		a[3] = arr[9+x+5];
		int y = ByteBuffer.wrap(a).getInt();
		a = new byte[y];
		for (int i=0; i<y; i++)
			a[i] = arr[9+x+6+i];
		this.q = new BigInteger(a);
		a = new byte[4];
		a[0] = arr[9+x+5+y+2];
		a[1] = arr[9+x+5+y+3];
		a[2] = arr[9+x+5+y+4];
		a[3] = arr[9+x+5+y+5];
		int z = ByteBuffer.wrap(a).getInt();
		a = new byte[z];
		for (int i=0; i<z; i++)
			a[i] = arr[9+x+6+y+6+i];
		this.g = new BigInteger(a);
	}



	public BigInteger getFieldOrder() {
		return p;
	}

	public BigInteger getOrder() {
		return q;
	}

	@Override
	public ModGroupElement getGenerator() {
		return new ModGroupElement(g, this);
	}

	public String getGroupType() {
		return groupType;
	}

	@Override
	public ModGroupElement one() {
		ModGroupElement ret = new ModGroupElement(BigInteger.ONE, this);
		return ret;
	}


	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		BigIntLeaf P = new BigIntLeaf(p);
		BigIntLeaf Q = new BigIntLeaf(q);
		BigIntLeaf G = new BigIntLeaf(g);
		Element[] arr = {P, Q, G};
		Node groupNode = new Node(arr);
		return groupNode.toByteArray();
	}


}
