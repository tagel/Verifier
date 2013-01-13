package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.HashFuncPRG;
import cryptographic.primitives.HashFuncPRGRandomOracle;
import cryptographic.primitives.HashFunction;
import cryptographic.primitives.PseudoRandomGenerator;
import cryptographic.primitives.RandomOracle;
import cryptographic.primitives.SHA2HashFunction;

import algorithms.params.Parameters;
import arithmetic.objects.*;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.basicelements.Node;
import arithmetic.objects.basicelements.StringLeaf;
import arithmetic.objects.field.IntegerFieldElement;

public class ECurveRandArrayTest {

	@Test
	public void TestArr() throws UnsupportedEncodingException {
		ECurveRandArray one = new ECurveRandArray(new LargeInteger("37"));

		LargeInteger tst1 = one.shanksTonelli(new LargeInteger("28"));
		Assert.assertEquals(new LargeInteger("9"), one.getQ());
		Assert.assertEquals(new LargeInteger("2"), one.getS());
		Assert.assertEquals(new LargeInteger("18"), tst1);

		ECurveRandArray two = new ECurveRandArray(new LargeInteger("13"));
		LargeInteger tst2 = two.shanksTonelli(new LargeInteger("10"));
		Assert.assertEquals(new LargeInteger("3"), two.getQ());
		Assert.assertEquals(new LargeInteger("2"), two.getS());
		Assert.assertEquals(new LargeInteger("6"), tst2);

		LargeInteger q = new LargeInteger("19");
		Assert.assertEquals(new LargeInteger("3"), q.mod(new LargeInteger("4")));
		Assert.assertEquals(0,
				q.mod(new LargeInteger("4")).compareTo(new LargeInteger("3")));

		ECurveRandArray three = new ECurveRandArray(new LargeInteger("19"));
		LargeInteger tst3 = three.simpleShanksTonelli(new LargeInteger("11"));
		Assert.assertEquals(new LargeInteger("9"), three.getQ());
		Assert.assertEquals(new LargeInteger("1"), three.getS());
		Assert.assertEquals(new LargeInteger("7"), tst3);

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
				"type", "auxsid", 1, false, false, false);
		Assert.assertNotNull("res is not in the classpath - ask Daniel",
				getClass().getClassLoader()
						.getResource("export/default/proofs"));

		Assert.assertTrue(params.fillFromXML());
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

		ECurveGroup G = (ECurveGroup)Gq;
		
		ArrayOfElements<IGroupElement> h = Gq.createRandomArray(1000, prg,
				independentSeed, nr);
		
		LargeInteger s1 =new LargeInteger("2");
		LargeInteger s2 =new LargeInteger("3");
		LargeInteger s3 =new LargeInteger("4");
		LargeInteger s4 =new LargeInteger("5");
		System.out.println(s1. multiply(s2).subtract(s3));
		
		ECurveGroupElement check = (ECurveGroupElement) h.getAt(0);
		Assert.assertEquals(true, isOnCurve(G.getXCoefficient(),G.getB(),check));

	}

	/**
	 * 
	 * @param a
	 *            - parameter of the curve
	 * @param b
	 *            - parameter of the curve
	 * @param element
	 *            - the point I want to check
	 * @return true if the point is on the curve and false otherwise
	 */
	private boolean isOnCurve(LargeInteger a, LargeInteger b,
			ECurveGroupElement element) {
		Point point = element.getElement();
		IntegerFieldElement x = point.getX();
		IntegerFieldElement y = point.getY();
		
		IntegerFieldElement A = new IntegerFieldElement(a, x.getField());
		IntegerFieldElement B = new IntegerFieldElement(b, x.getField());
		IntegerFieldElement res = (x.power(new LargeInteger("3")).add(x.mult(A))).add(B);
		if (y.power(new LargeInteger("2")).equals(res))
			return true;

		return false;
	}

}
