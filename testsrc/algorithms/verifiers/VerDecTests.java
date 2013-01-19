package algorithms.verifiers;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.ElementsExtractor;
import arithmetic.objects.arrays.ArrayGenerators;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.IGroup;
import arithmetic.objects.groups.ProductGroupElement;

public class VerDecTests {
	
	@Test
	public void multiplyArraysTest() {
		
		IGroup Gq = ElementsExtractor
				.unmarshal("ECqPGroup(P-256)::0000000002010000001c766572696669636174756d2e61726974686d2e4543715047726f75700100000005502d323536");
		int width = 1;
		
		
		byte[] bDecrFact1 = ElementsExtractor.btFromFile(getClass().getClassLoader()
				.getResource("export/default").getFile(), "proofs",
				"DecryptionFactors01.bt");
		ArrayOfElements<ProductGroupElement> factor1 = ArrayGenerators
				.createArrayOfPlaintexts(bDecrFact1, Gq, width);
		
		byte[] bDecrFact2 = ElementsExtractor.btFromFile(getClass().getClassLoader()
				.getResource("export/default").getFile(), "proofs",
				"DecryptionFactors02.bt");
		ArrayOfElements<ProductGroupElement> factor2 = ArrayGenerators
				.createArrayOfPlaintexts(bDecrFact2, Gq, width);
		
		Assert.assertNotNull(factor1);
		Assert.assertNotNull(factor2);
		
		ArrayOfElements<ProductGroupElement> mult = VerDec.multTwoArrays(factor1, factor2);
		Assert.assertNotNull(mult);
		Assert.assertEquals(100, mult.getSize());
		ProductGroupElement temp = factor1.getAt(0).mult(factor2.getAt(0));
		Assert.assertTrue(temp.equals(mult.getAt(0)));
		
		ArrayOfElements<ArrayOfElements<ProductGroupElement>> arr = new ArrayOfElements<ArrayOfElements<ProductGroupElement>>();
		arr.add(factor1);
		arr.add(factor2);
		
		ArrayOfElements<ProductGroupElement> tempArr = VerDec.multiplyArrays(arr);
		for (int i = 0; i<tempArr.getSize(); i++) {
			Assert.assertTrue(mult.getAt(i).equals(tempArr.getAt(i)));
		}
			
		
		
	}

}
