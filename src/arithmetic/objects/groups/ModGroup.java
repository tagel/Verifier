package arithmetic.objects.groups;



import java.io.UnsupportedEncodingException;
import arithmetic.objects.LargeInteger;
import java.nio.ByteBuffer;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;

import cryptographic.primitives.PseudoRandomGenerator;


/**
 * This class represents a multiplicative modulo prime Group.
 *
 */
public class ModGroup implements IGroup {

	/**
	 * p = the order of the underlying field Z*p
	 */
	private LargeInteger p;
	/**
	 * q = the group order
	 */
	private LargeInteger q;
	/**
	 *  g = generator
	 */
	private LargeInteger g;
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
	public ModGroup (LargeInteger p, LargeInteger q, LargeInteger g) {
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
		this.p = new LargeInteger(a);
		a = new byte[4];
		a[0] = arr[9+x+2];
		a[1] = arr[9+x+3];
		a[2] = arr[9+x+4];
		a[3] = arr[9+x+5];
		int y = ByteBuffer.wrap(a).getInt();
		a = new byte[y];
		for (int i=0; i<y; i++)
			a[i] = arr[9+x+6+i];
		this.q = new LargeInteger(a);
		a = new byte[4];
		a[0] = arr[9+x+5+y+2];
		a[1] = arr[9+x+5+y+3];
		a[2] = arr[9+x+5+y+4];
		a[3] = arr[9+x+5+y+5];
		int z = ByteBuffer.wrap(a).getInt();
		a = new byte[z];
		for (int i=0; i<z; i++)
			a[i] = arr[9+x+6+y+6+i];
		this.g = new LargeInteger(a);
	}



	public LargeInteger getFieldOrder() {
		return p;
	}

	public LargeInteger getOrder() {
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
		ModGroupElement ret = new ModGroupElement(LargeInteger.ONE, this);
		return ret;
	}


	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		BigIntLeaf P = new BigIntLeaf(p);
		BigIntLeaf Q = new BigIntLeaf(q);
		BigIntLeaf G = new BigIntLeaf(g);
		Node groupNode = new Node();
		groupNode.add(P);
		groupNode.add(Q);
		groupNode.add(G);
		return groupNode.toByteArray();
	}

	@Override
	public ArrayOfElements<IGroupElement> createRandomArray(int N, PseudoRandomGenerator prg,
			byte[] seed, int Nr) {
		ArrayOfElements<IGroupElement> h = new ArrayOfElements<IGroupElement>() ;
		int Np = this.p.bitLength();
		int length = 8 * ((int) Math.ceil((double) ((Np+Nr) / 8)));
		prg.setSeed(seed);
		
		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			LargeInteger t = new LargeInteger(arr);
			LargeInteger ttag = t.mod(new LargeInteger("2").power(Np+Nr));
			LargeInteger hi = ttag.power((p.subtract(LargeInteger.ONE)).divide(q)).mod(p);
			IGroupElement ge = new ModGroupElement(hi, this);
			h.add(ge);
		}
		return h;
	}


}
