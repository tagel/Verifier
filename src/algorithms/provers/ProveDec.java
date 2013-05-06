package algorithms.provers;

import main.Logger;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.ring.IntegerRingElement;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;

/**
 * This class provides the functionality of proving the correctness of the
 * decryption algorithm.
 * 
 * @author Tagel
 */
public class ProveDec extends Prover {

	/**
	 * This is the main function of this class which executes the decryption
	 * algorithm.
	 * 
	 * @param ROSeed
	 *            RandomOracle for computing the seed
	 * @param ROChallenge
	 *            RandomOracle for computing the challenge
	 * @param j
	 *            index of proof to verify
	 * @param ro
	 *            prefix to random oracleF
	 * @param N
	 *            size of the arrays
	 * @param Ne
	 *            number of bits in each component
	 * @param Nr
	 *            Acceptable "statistical error" when deriving independent
	 *            generators
	 * @param Nv
	 *            Number of bits in the challenge
	 * @param prg
	 *            Pseudo-random generator used to derive random vectors for
	 *            batching
	 * @param Gq
	 *            group of prime order
	 * @param g
	 *            standard generator of the group
	 * @param y
	 *            Partial public keys
	 * @param wInput
	 *            Array of input ciphertexts
	 * @param decryptionFactors
	 *            Arrays fi = (fi0,fi1,...fiN-1) of decryption factors in Mw
	 * @param decrFactCommitments
	 *            commitments of Fiat-Shamir proofs
	 * @param decrFactReplies
	 *            Replies of Fiat-Shamir proofs
	 * @return true if we accept the proof and false otherwise
	 */
	public static boolean prove(
			RandomOracle ROSeed,
			RandomOracle ROChallenge,
			int j,
			byte[] ro,
			int N,
			int Ne,
			int Nr,
			int Nv,
			PseudoRandomGenerator prg,
			IGroup Gq,
			IGroupElement g,
			ArrayOfElements<IGroupElement> y,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> decryptionFactors,
			ArrayOfElements<Node> decrFactCommitments,
			ArrayOfElements<IntegerRingElement> decrFactReplies, Logger logger) {

		logger.sendLog("Starting to prove the dectiption",
				Logger.Severity.NORMAL);

		/*
		 * 1(a) - interpret Tdec as Node(yl',B')
		 */
		Node decCommitment = decrFactCommitments.getAt(j);
		IGroupElement yltag = (IGroupElement) decCommitment.getAt(0);
		ProductGroupElement Btag = (ProductGroupElement) decCommitment.getAt(1);

		/*
		 * 1(b) - interpret Odec as ByteTree(Klx)
		 */
		ArrayOfElements<IntegerRingElement> klx = decrFactReplies;

		/*
		 * 2 - computing the seed
		 */
		Node nodeForSeed = computeNodeForSeed(g, y, wInput, decryptionFactors);
		byte[] seed = ComputeSeed(ROSeed, nodeForSeed, ro);

		/*
		 * 3 - Setting the PRG and computing e
		 */
		LargeInteger[] e = computeArrE(N, Ne, prg, seed);

		/*
		 * 4 - Computation of the challenge
		 */
		/* creating leaf(s) */
		ByteTree leaf = new BigIntLeaf(new LargeInteger(seed));

		/* creating node(T1Dec,...,TlambdaDec) */
		Node decCommitmentsNode = new Node(decCommitment.toByteArray());

		byte[] challenge = computeChallenge(ROChallenge, ro,
				decCommitmentsNode, leaf);

		LargeInteger v = computeV(Nv, challenge);

		/*
		 * 5 - Compute A and B
		 */

		ArrayOfElements<ProductGroupElement> u = computeU(wInput);
		ProductGroupElement A = computeDecA(N, e, u);

		// TODO printouts
		System.out.println("w : "+wInput);
		System.out.println("b(w) : "+bytArrayToHex(wInput.toByteArray()));
		System.out.println("u : " + u);
		System.out.println("s : " + bytArrayToHex(seed));
		System.out.println("f1 : " + decryptionFactors.getAt(0));
		System.out.println("f2 : " + decryptionFactors.getAt(1));
		System.out.println("yp_"+j+" : "+ yltag);
		System.out.println("v : "+bytArrayToHex(v.toByteArray()));
		System.out.println("k_x_1 : "+decrFactReplies.getAt(0));
		System.out.println("k_x_2 : "+decrFactReplies.getAt(1));
		System.out.println("A : "+A);
		System.out.println("ro : "+bytArrayToHex(ro));
		
		

		/*
		 * if j==0 then compute B = PI((PI(fli)^ei) and accept if
		 * PI(yl)^v*PI(yltag) == g^SIGMA(klx) and B^v*PI(Bltag) == PDec(A)
		 */
		if (j == 0) {
			return verifyJEquals0(decryptionFactors, N, e, Gq, y,
					decrFactCommitments, decCommitment, klx, yltag, v, A, g);

		} else if (j > 0) {
			ProductGroupElement Bj = decryptionFactors.getAt(j).getAt(0);
			for (int i = 1; i < decryptionFactors.getAt(j).getSize(); i++) {
				Bj = Bj.mult((decryptionFactors.getAt(j).getAt(i)).power(e[i]));
			}
			
			//TODO printouts
			System.out.println("B_"+j+" : "+Bj);

			/*
			 * verify yj^v*ytagj == g^kjx
			 */
			IntegerRingElement kjx = decrFactReplies.getAt(j);
			if (!(y.getAt(j).power(v)).mult(yltag).equals(
					g.power(kjx.getElement()))) {
				return false;
			}

			/*
			 * verify Bj^v*Bj' = PDEC(kjx,A)
			 */
			if (!((Bj.power(v)).mult(Btag)).equals(PDecrypt(kjx, A))) {
				return false;
			}

		} else {
			return false;
		}

		/* All equalities exist. */
		logger.sendLog("Shuffle of commitments proof succeeded",
				Logger.Severity.NORMAL);

		return true;
	}

	private static LargeInteger[] computeArrE(int N, int Ne,
			PseudoRandomGenerator prg, byte[] seed) {
		LargeInteger[] e = new LargeInteger[N];
		int length = 8 * ((int) Math.ceil((double) (Ne / 8)));
		prg.setSeed(seed);
		byte[] ByteArrToBigInt;
		LargeInteger t;

		for (int i = 0; i < N; i++) {
			ByteArrToBigInt = prg.getNextPRGOutput(length);
			t = ElementsExtractor.leafToInt(ByteArrToBigInt);
			e[i] = t.mod(new LargeInteger("2").power(Ne));
		}
		return e;
	}

	private static Node computeNodeForSeed(
			IGroupElement g,
			ArrayOfElements<IGroupElement> y,
			ArrayOfElements<ProductGroupElement> wInput,
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> decryptionFactors) {
		

				
		// creating node(g,w)
		Node leftNode = new Node();
		leftNode.add(g);
		leftNode.add(wInput);

		// creating node(a,b)
		Node rightNode = new Node();
		Node a = new Node(y.toByteArray());
		Node b = new Node(decryptionFactors.toByteArray());
		rightNode.add(a);
		rightNode.add(b);
		
		// creating node(node(g,w),node(a,b))
		Node nodeForSeed = new Node();
		nodeForSeed.add(leftNode);
		nodeForSeed.add(rightNode);
		
		//TODO printouts
		System.out.println("a : "+bytArrayToHex(a.toByteArray()));
		System.out.println("b : "+bytArrayToHex(b.toByteArray()));
		System.out.println("nodeForSeed : "+bytArrayToHex(nodeForSeed.toByteArray()));
		return nodeForSeed;
	}

	/*
	 * compute A = (PI(ui ^ ei),1). marking "left" = PI(ui ^ ei), the right side
	 * isn't used in the computation thus null.
	 */
	private static ProductGroupElement computeDecA(int N, LargeInteger[] e,
			ArrayOfElements<ProductGroupElement> u) {
		ProductGroupElement left = u.getAt(0).power(e[0]);
		for (int i = 1; i < N; i++) {
			left = left.mult(u.getAt(i).power(e[i]));
		}
		ProductGroupElement A = new ProductGroupElement(left, u.getAt(0));
		return A;
	}

	private static ArrayOfElements<ProductGroupElement> computeU(
			ArrayOfElements<ProductGroupElement> wInput) {
		ArrayOfElements<ProductGroupElement> u = new ArrayOfElements<ProductGroupElement>();
		for (int i = 0; i < wInput.getSize(); i++) {
			u.add(wInput.getAt(i).getLeft());
		}
		return u;
	}

	private static boolean verifyJEquals0(
			ArrayOfElements<ArrayOfElements<ProductGroupElement>> decryptionFactors,
			int N, LargeInteger[] e, IGroup Gq,
			ArrayOfElements<IGroupElement> y,
			ArrayOfElements<Node> decrFactCommitments, Node decCommitment,
			ArrayOfElements<IntegerRingElement> klx, IGroupElement yltag,
			LargeInteger v, ProductGroupElement A, IGroupElement g) {

		ProductGroupElement ret;
		ProductGroupElement B = null;

		for (int i = 0; i < N; i++) {
			ret = decryptionFactors.getAt(0).getAt(i);

			for (int l = 1; l < decryptionFactors.getSize(); l++) {
				ret = ret.mult(decryptionFactors.getAt(l).getAt(i));
			}
			if (i == 0) {
				B = ret.power(e[0]);
			} else {
				B = B.mult(ret.power(e[i]));
			}
		}

		// compute PI(Yl)
		IGroupElement piYl = Gq.one();
		for (int i = 0; i < y.getSize(); i++) {
			piYl = piYl.mult(y.getAt(i));
		}

		// creating the arrays for verifying the equations
		ArrayOfElements<IGroupElement> yltagArr = new ArrayOfElements<IGroupElement>();
		ArrayOfElements<ProductGroupElement> BtagArr = new ArrayOfElements<ProductGroupElement>();

		for (int i = 0; i < decrFactCommitments.getSize(); i++) {
			decCommitment = decrFactCommitments.getAt(i);

			yltag = (IGroupElement) decCommitment.getAt(0);
			yltagArr.add(yltag);
			ProductGroupElement Bltag = (ProductGroupElement) decCommitment
					.getAt(1);
			BtagArr.add(Bltag);
		}
		
		//TODO printouts
		System.out.println("B : "+B);

		// compute piYltag
		IGroupElement piYltag = Gq.one();
		for (int i = 0; i < yltagArr.getSize(); i++) {
			piYltag = piYltag.mult(yltagArr.getAt(i));
		}

		// compute g^SUM(klx)
		IntegerRingElement sumklx = klx.getAt(0).getRing().zero();
		for (int i = 0; i < klx.getSize(); i++) {
			sumklx = sumklx.add(klx.getAt(i));
		}

		ProductGroupElement piBltag = BtagArr.getAt(0);
		for (int i = 1; i < BtagArr.getSize(); i++) {
			piBltag = piBltag.mult(BtagArr.getAt(i));
		}

		/*
		 * verify PI(yl)^v * PI(yl') = g^(SUM(klx))
		 */
		boolean firstEq = true;
		if (!(piYl.power(v)).mult(piYltag).equals(g.power(sumklx.getElement()))) {
			firstEq = false;
		}

		/*
		 * verify B^v * PI(Bltag) = PDEC(SUM(klx),A)
		 */
		boolean secondEq = true;
		if (!(B.power(v)).mult(piBltag).equals(PDecrypt(sumklx, A))) {
			secondEq = false;
		}

		return (firstEq && secondEq);
	}

	// TODO printout method - delete?
	static String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
}
