package arithmetic.objects.groups;



import java.io.UnsupportedEncodingException;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;


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

	public ModGroup (byte[] arr) throws UnsupportedEncodingException  {
		Node node = new Node(arr);
		if (node.getChildrenSize()!=3)
			System.out.println("Error: byte array is not of a correct modular group structure");
		else {
			p = ElementsExtractor.leafToInt(node.getAt(0).toByteArray());
			q = ElementsExtractor.leafToInt(node.getAt(1).toByteArray());
			g = ElementsExtractor.leafToInt(node.getAt(2).toByteArray());
		}

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
