package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import algorithms.params.Parameters;
import algorithms.verifiers.MainVerifierTests;
import arithmetic.objects.ByteTree;
import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.BigIntLeaf;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import cryptographic.primitives.HashFuncPRG;
import cryptographic.primitives.HashFuncPRGRandomOracle;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;
import cryptographic.primitives.SHA2HashFunction;

/**
 * Tests for the ECurveRandArray class.
 * 
 * @author Sofi
 * 
 */
public class ECurveRandArrayTest {

	// ECurveGroup parameters
	LargeInteger Q = new LargeInteger(
			"115792089210356248762697446949407573530086143415290314195533631308867097853951");
	LargeInteger a = new LargeInteger(
			"115792089210356248762697446949407573530086143415290314195533631308867097853948");
	LargeInteger b = new LargeInteger(
			"41058363725152142129326129780047268409114441015993725554835256314039467401291");

	@Test
	public void TestArr() throws UnsupportedEncodingException {
		ECurveRandArray one = new ECurveRandArray(new LargeInteger("37"),
				LargeInteger.ONE, LargeInteger.ONE);

		LargeInteger tst1 = one.shanksTonelli(new LargeInteger("28"));
		Assert.assertEquals(new LargeInteger("9"), one.getQ());
		Assert.assertEquals(new LargeInteger("2"), one.getS());
		Assert.assertEquals(new LargeInteger("18"), tst1);

		ECurveRandArray two = new ECurveRandArray(new LargeInteger("13"),
				LargeInteger.ONE, LargeInteger.ONE);
		LargeInteger tst2 = two.shanksTonelli(new LargeInteger("10"));
		Assert.assertEquals(new LargeInteger("3"), two.getQ());
		Assert.assertEquals(new LargeInteger("2"), two.getS());
		Assert.assertEquals(new LargeInteger("6"), tst2);

		LargeInteger q = new LargeInteger("19");
		Assert.assertEquals(new LargeInteger("3"), q.mod(new LargeInteger("4")));
		Assert.assertEquals(0,
				q.mod(new LargeInteger("4")).compareTo(new LargeInteger("3")));

		ECurveRandArray three = new ECurveRandArray(new LargeInteger("19"),
				LargeInteger.ONE, LargeInteger.ONE);
		LargeInteger tst3 = three.simpleShanksTonelli(new LargeInteger("11"));
		Assert.assertEquals(new LargeInteger("9"), three.getQ());
		Assert.assertEquals(new LargeInteger("1"), three.getS());
		Assert.assertEquals(new LargeInteger("7"), tst3);

		// check one point
		ECurveRandArray curve = new ECurveRandArray(Q, a, b);

		// Check if the points are really on the curve

		LargeInteger yValue = new LargeInteger(
				"53939506714489701886456415263120518424983556687449170106546387547120013873082");
		LargeInteger zValue = new LargeInteger(
				"19353912749743277024464628119578047917971379149422101751139737045657514424033");
		LargeInteger xValue = new LargeInteger(
				"99231359047137800212806420171596481116912646397664473919112123289570225611325");

		// y vs. shanksTonelli(z)
		Assert.assertEquals(yValue, curve.shanksTonelli(zValue));

		// z vs. y^2 (mod q)
		Assert.assertEquals(zValue, yValue.modPow(new LargeInteger("2"), Q));

		// z vs f(xi)
		Assert.assertEquals(zValue, curve.f(xValue));

		// y^2 (mod q) vs f(xi)
		Assert.assertEquals(yValue.modPow(new LargeInteger("2"), Q),
				curve.f(xValue));

		// f_xi - the function value
		LargeInteger f_xi = (xValue.modPow(new LargeInteger("3"), Q).add(
				xValue.multiply(a)).add(b)).mod(Q);

		// f_xi vs y^2
		Assert.assertEquals(f_xi, yValue.modPow(new LargeInteger("2"), Q));

	}

	@Test
	public void TestElements() throws Exception {
		// Create the array:
		int nr = 100;
		IGroup Gq = ElementsExtractor
				.unmarshal("ECqPGroup(P-256)::0000000002010000001c766572696669636174756d2e61726974686d2e4543715047726f75700100000005502d323536");
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-256"));
		HashFunction H = new SHA2HashFunction("SHA-256");

		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("protInfo.xml").getFile(), getClass()
				.getClassLoader().getResource("export/default").getFile(),
				null, "auxsid", 1, false, false, false,
				new MainVerifierTests.MockedLogger());
		Assert.assertNotNull("res is not in the classpath - ask Daniel",
				getClass().getClassLoader()
						.getResource("export/default/proofs"));

		params.fillFromXML();
		params.fillFromDirectory();

		String s = params.getSessionID() + "." + params.getAuxsid();
		ByteTree btAuxid = new StringLeaf(s);
		ByteTree version_proof = new StringLeaf(params.getVersion());
		ByteTree sGq = new StringLeaf(params.getsGq());
		ByteTree sPRG = new StringLeaf(params.getsPRG());
		ByteTree sH = new StringLeaf(params.getSh());

		ByteTree[] input = new ByteTree[5];
		input[0] = version_proof;
		input[1] = btAuxid;
		input[2] = sGq;
		input[3] = sPRG;
		input[4] = sH;

		Node node = new Node(input);
		byte[] Seed = node.toByteArray();

		RandomOracle ROseed = new HashFuncPRGRandomOracle(H, prg.seedlen());

		StringLeaf stringLeaf = new StringLeaf("generators");
		byte[] independentSeed = ROseed.getRandomOracleOutput(ArrayGenerators
				.concatArrays(H.digest(Seed), stringLeaf.toByteArray()));

		ArrayOfElements<IGroupElement> h = Gq.createRandomArray(100, prg,
				independentSeed, nr);

		ECurveGroupElement check;
		ECurveGroupElement check1 = (ECurveGroupElement) h.getAt(0);
		ECurveGroupElement check2 = (ECurveGroupElement) h.getAt(1);
		ECurveGroupElement check3 = (ECurveGroupElement) h.getAt(2);

		// Check the field order of the point:
		Assert.assertEquals(
				new LargeInteger(
						"115792089210356248762697446949407573530086143415290314195533631308867097853951"),
				check1.getGroup().getFieldOrder());
		Assert.assertEquals(
				new LargeInteger(
						"115792089210356248762697446949407573530086143415290314195533631308867097853951"),
				check2.getGroup().getFieldOrder());
		Assert.assertEquals(
				new LargeInteger(
						"115792089210356248762697446949407573530086143415290314195533631308867097853951"),
				check3.getGroup().getFieldOrder());

		// Check some points
		for (int i = 0; i < 100; i++) {
			check = (ECurveGroupElement) h.getAt(i);
			Assert.assertTrue(f(check.getElement().getX().getElement(), check
					.getElement().getY().getElement()));

		}

		// Check points associativity
		Assert.assertTrue(check1.mult((check2).mult(check3)).equals(
				(check1.mult(check2)).mult(check3)));

		Assert.assertTrue(check1.mult(check1.mult(check1.mult(check1))).equals(
				(check1.mult(check1)).mult(check1.mult(check1))));

	}

	@Test
	public void TestSmallEcurve() {
		int nr = 100;
		IGroup Gq = ElementsExtractor
				.unmarshal("ECqPGroup(P-256)::0000000002010000001c766572696669636174756d2e61726974686d2e4543715047726f75700100000005502d323536");
		PseudoRandomGenerator prg = new HashFuncPRG(new SHA2HashFunction(
				"SHA-256"));
		HashFunction H = new SHA2HashFunction("SHA-256");

		Parameters params = new Parameters(getClass().getClassLoader()
				.getResource("exportEcurveSmall/protInfo.xml").getFile(),
				getClass().getClassLoader()
						.getResource("exportEcurveSmall/default").getFile(),
				null, "auxsid", 1, false, false, false,
				new MainVerifierTests.MockedLogger());
		Assert.assertNotNull("res is not in the classpath - ask Daniel",
				getClass().getClassLoader()
						.getResource("export/default/proofs"));

		params.fillFromXML();
		params.fillFromDirectory();

		String s = params.getSessionID() + "." + params.getAuxsid();
		ByteTree btAuxid = new StringLeaf(s);
		ByteTree version_proof = new StringLeaf(params.getVersion());
		ByteTree sGq = new StringLeaf(params.getsGq());
		ByteTree sPRG = new StringLeaf(params.getsPRG());
		ByteTree sH = new StringLeaf(params.getSh());
		ByteTree Ne = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNe())), 4);
		ByteTree Nr = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNr())), 4);
		ByteTree Nv = new BigIntLeaf(new LargeInteger(Integer.toString(params
				.getNv())), 4);

		ByteTree[] input = new ByteTree[8];

		input[0] = version_proof;
		input[1] = btAuxid;
		input[2] = Nr;
		input[3] = Nv;
		input[4] = Ne;
		input[5] = sPRG;
		input[6] = sGq;
		input[7] = sH;

		byte[] Seed = new Node(input).toByteArray();

		RandomOracle ROseed = new HashFuncPRGRandomOracle(H, prg.seedlen());

		StringLeaf stringLeaf = new StringLeaf("generators");
		byte[] independentSeed = ROseed.getRandomOracleOutput(ArrayGenerators
				.concatArrays(H.digest(Seed), stringLeaf.toByteArray()));

		ArrayOfElements<IGroupElement> h = Gq.createRandomArray(2, prg,
				independentSeed, nr);

		Assert.assertEquals(
				"[(1ced80e81adf679bb53a8937ff457f8d3d3f4fbd256c124551a0492731085fd3):(4af6a11bf749f19a8ba5d73d05ad0eeb091f29d05453f2b85b3667d2bea471b9),(f076ee161ee3ceb09308673f342c61997337ef3f54d895fbbcf9ffc65aabd398):(22fe7f47bd4e8bf29caad965837ff90c95193a436ff7b013b71e6d47c3a3b6ca),]",
				h.toString());
	}

	private boolean f(LargeInteger xValue, LargeInteger yValue) {
		LargeInteger f_xi = (xValue.modPow(new LargeInteger("3"), Q).add(
				xValue.multiply(a)).add(b)).mod(Q);

		return f_xi.equals(yValue.modPow(new LargeInteger("2"), Q));
	}

}
