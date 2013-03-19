package arithmetic.objects.groups;

import algorithms.provers.Prover;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;

import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;

import cryptographic.primitives.PseudoRandomGenerator;

/**
 * This class represents a multiplicative modulo prime group.
 * 
 * @author Itay
 */
public class ModGroup implements IGroup {

	/**
	 * p = the order of the underlying field Z*p. has to be a prime number.
	 */
	private LargeInteger p;
	/**
	 * q = the group order
	 */
	private LargeInteger q;
	/**
	 * g = generator
	 */
	private LargeInteger g;

	/**
	 * Constructor.
	 */
	public ModGroup(LargeInteger p, LargeInteger q, LargeInteger g) {
		this.p = p;
		this.q = q;
		this.g = g;
	}

	public ModGroup(byte[] arr) {
		Node node = new Node(arr);
		if (node.getChildrenSize() != 3 && node.getChildrenSize() != 4)
			System.out
					.println("Error: byte array is not of a correct modular group structure");
		else {
			p = ElementsExtractor.leafToInt(node.getAt(0).toByteArray());
			q = ElementsExtractor.leafToInt(node.getAt(1).toByteArray());
			g = ElementsExtractor.leafToInt(node.getAt(2).toByteArray());
		}
	}

	/**
	 * 
	 * @return the order of the underlying field Z*p
	 */
	public LargeInteger getFieldOrder() {
		return p;
	}

	/**
	 * @return the order of this group
	 */
	public LargeInteger getOrder() {
		return q;
	}

	/**
	 * 
	 * @return the generator of the group.
	 */
	@Override
	public ModGroupElement getGenerator() {
		return new ModGroupElement(g, this);
	}

	/**
	 * @return the 1 of the Group. (the element which is indifferent to the
	 *         multiplication operation).
	 */
	@Override
	public ModGroupElement one() {
		ModGroupElement ret = new ModGroupElement(LargeInteger.ONE, this);
		return ret;
	}

	/**
	 * @return the byte array representation (as a byte tree) of this modular
	 *         group.
	 */
	@Override
	public byte[] toByteArray() {
		BigIntLeaf P = new BigIntLeaf(p);
		BigIntLeaf Q = new BigIntLeaf(q);
		BigIntLeaf G = new BigIntLeaf(g);
		Node groupNode = new Node();
		groupNode.add(P);
		groupNode.add(Q);
		groupNode.add(G);
		return groupNode.toByteArray();
	}

	/**
	 * 
	 * @param N
	 *            the size of the returned array
	 * @param prg
	 *            a pseudo-random generator
	 * @param seed
	 *            the seed used by the pseudo-random generator
	 * @param nr
	 *            auxiliary security parameter for the seed.
	 * @return an array of size N containing random group elements.
	 */
	@Override
	public ArrayOfElements<IGroupElement> createRandomArray(int N,
			PseudoRandomGenerator prg, byte[] seed, int Nr) {
		ArrayOfElements<IGroupElement> h = new ArrayOfElements<IGroupElement>();
		int Np = this.p.bitLength();
		//TODO check if this works for every mod group we have
		//int length = 8 * ((int) Math.ceil((double) ((Np + Nr) / 8.0)));
		int length = 7 + Nr + Np;
		prg.setSeed(seed);

		for (int i = 0; i < N; i++) {
			byte[] arr = prg.getNextPRGOutput(length);
			LargeInteger t = Prover.byteArrayToPosLargeInteger(arr);
			LargeInteger ttag = t.mod(new LargeInteger("2").power(Np + Nr));
			LargeInteger hi = LargeInteger.power(ttag,
					((p.subtract(LargeInteger.ONE)).divide(q))).mod(p);
			IGroupElement ge = new ModGroupElement(hi, this);
			h.add(ge);
		}
		return h;
	}
}
