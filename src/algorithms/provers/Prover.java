package algorithms.provers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import arithmetic.objects.ArrayOfElements;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.IGroupElement;
import arithmetic.objects.IntegerFieldElement;
import arithmetic.objects.Node;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

public abstract class Prover {

	byte[] ComputeSeed(RandomOracle ROSeed, Node nodeForSeed, byte[] ro)
			throws UnsupportedEncodingException {
		byte[] seed = ROSeed.getRandomOracleOutput(ElementsExtractor
				.concatArrays(ro, nodeForSeed.toByteArray()));
		return seed;
	}

	IGroupElement computeC(ArrayOfElements<IGroupElement> u,
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

	IGroupElement computeD(ArrayOfElements<IntegerFieldElement> e,
			ArrayOfElements<IGroupElement> B, ArrayOfElements<IGroupElement> h,
			int N) {
		BigInteger DExponent = e.getAt(0).getElement();
		for (int i = 1; i < N; i++) {
			DExponent = DExponent.multiply(e.getAt(i).getElement());
		}
		IGroupElement D = B.getAt(N - 1).divide(h.getAt(0).power(DExponent));
		return D;
	}

	boolean verifyAvAtag(IGroupElement A, IGroupElement Atag, BigInteger v,
			ArrayOfElements<IntegerFieldElement> Ke, IGroupElement g, int N,
			ArrayOfElements<IGroupElement> h, IntegerFieldElement Ka) {

		IGroupElement left = (A.power(v)).mult(Atag);
		IGroupElement hPi = h.getAt(0).power(Ke.getAt(0).getElement());
		for (int i = 1; i < N; i++) {
			hPi = hPi.mult(h.getAt(i).power(Ke.getAt(i).getElement()));
		}
		IGroupElement right = g.power(Ka.getElement()).mult(hPi);
		if (left != right) {
			return false;
		}
		return true;
	}

	// TODO: implement this function
	ArrayOfElements<IntegerFieldElement> computeE(byte[] seed, int Ne,
			PseudoRandomGenerator prg, int N, IntegerFieldElement pow,
			BigInteger x, BigInteger y) {
		ArrayOfElements<IntegerFieldElement> arr = new ArrayOfElements<IntegerFieldElement>();
		return arr;

	}

}
