package algorithms.provers;

import java.io.UnsupportedEncodingException;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import arithmetic.objects.ring.ProductRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

public abstract class Prover {

	/**
	 * This function encrypts the message m using the public key pk, and returns
	 * the CipherText.
	 * 
	 * @param pk
	 *            - Public key (g, y) used to encrypt the message
	 * @param s
	 * @param Gq
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static ProductGroupElement encrypt(ProductGroupElement m,
			ProductRingElement s, ProductGroupElement pk, IGroup Gq) {

		IGroupElement g = pk.getArr().getAt(0);
		IGroupElement y = pk.getArr().getAt(1);
		ArrayOfElements<IntegerRingElement> powers = s.getArr();
		ArrayOfElements<IGroupElement> ms = m.getArr();
		ArrayOfElements<IGroupElement> left = new ArrayOfElements<IGroupElement>();
		ArrayOfElements<IGroupElement> right = new ArrayOfElements<IGroupElement>();
		for (int i = 0; i < powers.getSize(); i++) {
			left.add(g.power(powers.getAt(i).getElement()));
			right.add((y.power(powers.getAt(i).getElement()).mult(ms.getAt(i))));
		}

		ProductGroupElement encryptedMsg = ElementsExtractor.createCiphertext(
				left, right);
		return encryptedMsg;
	}

	/**
	 * This function computes a seed.
	 * 
	 * @param ROSeed
	 *            - the RandomOracle
	 * @param nodeForSeed
	 *            - a node includes the params needed for the seed
	 * @param ro
	 *            - prefix to random oracle
	 * @return a seed represented as a byte[]
	 * @throws UnsupportedEncodingException
	 */
	protected static byte[] ComputeSeed(RandomOracle ROSeed, Node nodeForSeed,
			byte[] ro) throws UnsupportedEncodingException {
		byte[] seed = ROSeed.getRandomOracleOutput(ArrayGenerators
				.concatArrays(ro, nodeForSeed.toByteArray()));
		return seed;
	}

	/**
	 * This function computes A, needed in the proof.
	 * 
	 * @param N
	 *            - size of the arrays
	 * @param Ne
	 *            - num of bits in each component
	 * @param seed
	 *            - the byte[] needed to initialize the prg
	 * @param prg
	 *            - Pseudo-random generator used to derive random vectors for
	 *            batching
	 * @param u
	 *            - the array of Pedersen Commitments
	 * @return A - a multiplication of Ui^Ei N times
	 */
	protected static IGroupElement computeA(int N, int Ne, byte[] seed,
			PseudoRandomGenerator prg, ArrayOfElements<IGroupElement> u) {
		int length = 8 * ((int) Math.ceil((double) (Ne / 8)));
		prg.setSeed(seed);
		byte[] ByteArrToBigInt = prg.getNextPRGOutput(length);
		LargeInteger t = ElementsExtractor.leafToInt(ByteArrToBigInt);
		LargeInteger e = t.mod(new LargeInteger("2").power(Ne));
		IGroupElement A = u.getAt(0).power(e);

		for (int i = 1; i < N; i++) {
			ByteArrToBigInt = prg.getNextPRGOutput(length);
			t = ElementsExtractor.leafToInt(ByteArrToBigInt);
			e = t.mod(new LargeInteger("2").power(Ne));
			A = A.mult(u.getAt(i).power(e));
		}
		return A;
	}

	/**
	 * This function computes E, needed in the proof.
	 * 
	 * @param N
	 *            - size of the arrays
	 * @param Ne
	 *            - num of bits in each component
	 * @param seed
	 *            - the byte[] needed to initialize the prg
	 * @param prg
	 *            - Pseudo-random generator used to derive random vectors for
	 *            batching
	 * @return E, the multiplication of Ei N times
	 */
	protected static LargeInteger computeE(int N, int Ne, byte[] seed,
			PseudoRandomGenerator prg) {
		int length = 8 * ((int) Math.ceil((double) (Ne / 8)));
		prg.setSeed(seed);
		byte[] ByteArrToBigInt = prg.getNextPRGOutput(length);
		LargeInteger t = ElementsExtractor.leafToInt(ByteArrToBigInt);
		LargeInteger E = t.mod(new LargeInteger("2").power(Ne));

		for (int i = 1; i < N; i++) {
			ByteArrToBigInt = prg.getNextPRGOutput(length);
			t = ElementsExtractor.leafToInt(ByteArrToBigInt);
			E = E.multiply(t.mod(new LargeInteger("2").power(Ne)));
		}
		return E;
	}

	/**
	 * This function computes F, needed in the proof.
	 * 
	 * @param N
	 *            - size of the arrays
	 * @param Ne
	 *            - num of bits in each component
	 * @param seed
	 *            - the byte[] needed to initialize the prg
	 * @param prg
	 *            - Pseudo-random generator used to derive random vectors for
	 *            batching
	 * @param wInput
	 *            - array of input ciphertexts
	 * @return F, the multiplication of Wi^Ei N times
	 */
	protected static ProductGroupElement computeF(int N, int Ne, byte[] seed,
			PseudoRandomGenerator prg,
			ArrayOfElements<ProductGroupElement> wInput) {

		int length = 8 * ((int) Math.ceil((double) (Ne / 8)));
		prg.setSeed(seed);
		byte[] ByteArrToBigInt = prg.getNextPRGOutput(length);
		LargeInteger t = ElementsExtractor.leafToInt(ByteArrToBigInt);
		LargeInteger e = t.mod(new LargeInteger("2").power(Ne));
		ProductGroupElement F = wInput.getAt(0).power(e);

		for (int i = 1; i < N; i++) {
			ByteArrToBigInt = prg.getNextPRGOutput(length);
			t = ElementsExtractor.leafToInt(ByteArrToBigInt);
			e = t.mod(new LargeInteger("2").power(Ne));
			F = F.mult(wInput.getAt(i).power(e));
		}
		return F;
	}

	/**
	 * This function computes C, needed in the proof.
	 * 
	 * @param u
	 *            - the array of Pedersen Commitments in Gq
	 * @param h
	 *            - Array of random elements used to compute the seed
	 * @param N
	 *            - size of the arrays
	 * @return C, the multiplication of Ui N times
	 */
	protected static IGroupElement computeC(ArrayOfElements<IGroupElement> u,
			ArrayOfElements<IGroupElement> h, int N) {
		IGroupElement CNumerator = u.getAt(0);
		IGroupElement CDenominator = h.getAt(0);
		for (int i = 1; i < N; i++) {
			CNumerator = CNumerator.mult(u.getAt(i));
			CDenominator = CDenominator.mult(h.getAt(i));
		}
		IGroupElement C = CNumerator.divide(CDenominator);
		return C;
	}

	/**
	 * This function computes D, needed in the proof.
	 * 
	 * @param E
	 *            - the multiplication of Ei N times
	 * @param B
	 *            - an array of N elements on Gq
	 * @param h
	 *            - Array of random elements used to compute the seed
	 * @param N
	 *            - size of the arrays
	 * @return D, the multiplication of Ui N times
	 */
	protected static IGroupElement computeD(LargeInteger E,
			ArrayOfElements<IGroupElement> B, ArrayOfElements<IGroupElement> h,
			int N) {
		IGroupElement D = B.getAt(N - 1).divide(h.getAt(0).power(E));
		return D;
	}

	/**
	 * This function verifies the first equation: A^v * Atag = (g^ka) *
	 * PI(h[i]^ke[i])
	 * 
	 * @param A
	 *            - the multiplication of Ui^Ei N times
	 * @param Atag
	 *            - an element in Gq
	 * @param v
	 *            - the challenge computed by the Random Oracle
	 * @param Ke
	 *            - an array on N elements in Zq
	 * @param g
	 *            - the generator of Gq
	 * @param N
	 *            - size of the arrays
	 * @param h
	 *            - Array of random elements used to compute the seed
	 * @param Ka
	 *            - an element in Zq
	 * @return true if the equation is correct and false otherwise.
	 */
	protected static boolean verifyAvAtag(IGroupElement A, IGroupElement Atag,
			LargeInteger v, ArrayOfElements<IntegerFieldElement> Ke,
			IGroupElement g, int N, ArrayOfElements<IGroupElement> h,
			IntegerFieldElement Ka) {

		IGroupElement left = (A.power(v)).mult(Atag);
		IGroupElement hPi = h.getAt(0).power(Ke.getAt(0).getElement());
		for (int i = 1; i < N; i++) {
			hPi = hPi.mult(h.getAt(i).power(Ke.getAt(i).getElement()));
		}
		IGroupElement right = g.power(Ka.getElement()).mult(hPi);
		if (!left.equal(right)) {
			return false;
		}
		return true;
	}

	protected static boolean verifyBvBtag(ArrayOfElements<IGroupElement> B,
			ArrayOfElements<IGroupElement> Btag,
			ArrayOfElements<IntegerFieldElement> Kb,
			ArrayOfElements<IntegerFieldElement> Ke, IGroupElement g,
			LargeInteger v, ArrayOfElements<IGroupElement> h, int N) {
		IGroupElement left = ((B.getAt(0)).power(v)).mult(Btag.getAt(0));
		IGroupElement right = g.power(Kb.getAt(0).getElement()).mult(
				h.getAt(0).power(Ke.getAt(0).getElement()));
		if (!left.equal(right)) {
			return false;
		}

		for (int i = 1; i < N; i++) {
			left = (B.getAt(i)).power(v).mult(Btag.getAt(i));
			right = g.power(Kb.getAt(i).getElement()).mult(
					B.getAt(i - 1).power(Ke.getAt(i).getElement()));
		}
		if (!left.equal(right)) {
			return false;
		}
		return true;
	}

	protected static boolean verifyCvCtag(IGroupElement C, IGroupElement Ctag,
			LargeInteger v, IntegerFieldElement Kc, IGroupElement g) {
		IGroupElement left = (C.power(v)).mult(Ctag);
		IGroupElement right = g.power(Kc.getElement());
		if (!left.equal(right)) {
			return false;
		}
		return true;
	}

	protected static boolean verifyDvDtag(IGroupElement D, IGroupElement Dtag,
			LargeInteger v, IntegerFieldElement Kd, IGroupElement g) {
		IGroupElement left = (D.power(v)).mult(Dtag);
		IGroupElement right = g.power(Kd.getElement());
		if (!left.equal(right)) {
			return false;
		}
		return true;
	}
}
