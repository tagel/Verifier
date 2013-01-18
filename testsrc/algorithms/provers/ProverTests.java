package algorithms.provers;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.IGroupElement;
import arithmetic.objects.groups.ModGroup;
import arithmetic.objects.groups.ModGroupElement;

public class ProverTests {
	
	@Test
	public void PDecryptTest() {
		// TODO Daniel
	}

	@Test
	public void TDecryptTest() {
		// TODO Daniel
	}

	@Test
	public void encryptTest() {
		// TODO Daniel
	}

	@Test
	public void ComputeSeedTest() {
		// TODO Daniel
	}

	@Test
	public void computeATest() {
		// TODO Daniel
	}

	@Test
	public void computeFTest() {
		// TODO Daniel
	}

	@Test
	public void computeCTest() {
		ArrayOfElements<IGroupElement> array1 = new ArrayOfElements<IGroupElement>();
		ArrayOfElements<IGroupElement> array2 = new ArrayOfElements<IGroupElement>();
		
		ModGroup group1 = new ModGroup(new LargeInteger("263"), new LargeInteger("131"), new LargeInteger("2"));
		ModGroupElement a = new ModGroupElement(new LargeInteger("1"), group1);
		ModGroupElement b = new ModGroupElement(new LargeInteger("2"), group1);
		ModGroupElement c = new ModGroupElement(new LargeInteger("3"), group1);
		ModGroupElement d = new ModGroupElement(new LargeInteger("4"), group1);
		array1.add(a);
		array1.add(b);
		array2.add(c);
		array2.add(d);
	
		Assert.assertEquals(new LargeInteger("6"), ((ModGroupElement) Prover.computeC(array2, array1, 2)).getElement());
	}

	@Test
	public void computeDTest() {
		// TODO Daniel
	}

	@Test
	public void verifyAvAtagTest() {
		// TODO Daniel
	}

	@Test
	public void verifyBvBtagTest() {
		// TODO Daniel
	}

	@Test
	public void verifyCvCtagTest() {
		// TODO Daniel
	}

	@Test
	public void verifyDvDtagTest() {
		// TODO Daniel
	}
}
