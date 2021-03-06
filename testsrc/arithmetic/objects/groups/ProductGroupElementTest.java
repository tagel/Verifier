package arithmetic.objects.groups;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.arrays.ArrayOfElements;
import arithmetic.objects.groups.ModGroupElement;
import arithmetic.objects.groups.ProductGroupElement;
import arithmetic.objects.groups.ModGroup;

/**
 * Tests for the ProductGroupElement class.
 * @author Itay
 *
 */
public class ProductGroupElementTest {

	private ModGroup Group263 = new ModGroup(new LargeInteger("263"),
			new LargeInteger("131"), new LargeInteger("2"));
	private ModGroupElement ire5_Group263 = new ModGroupElement(
			new LargeInteger("5"), Group263);
	private ModGroupElement ire6_Group263 = new ModGroupElement(
			new LargeInteger("6"), Group263);
	private ModGroupElement ire258_Group263 = new ModGroupElement(
			new LargeInteger("258"), Group263);

	private ModGroup Group3 = new ModGroup(new LargeInteger("3"),
			new LargeInteger("2"), new LargeInteger("1"));
	private ModGroupElement ire1_Group3 = new ModGroupElement(new LargeInteger(
			"1"), Group3);
	private ModGroupElement ire2_Group3 = new ModGroupElement(new LargeInteger(
			"2"), Group3);

	private ArrayOfElements<IGroupElement> aoe = new ArrayOfElements<IGroupElement>();

	@Test
	public void toByteArrayTest() throws UnsupportedEncodingException {
		aoe.add(ire258_Group263);
		aoe.add(ire5_Group263);
		ProductGroupElement pre = new ProductGroupElement(aoe);
		byte[] b = { 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 1, 2, 1, 0, 0, 0, 2, 0, 5 };
		Assert.assertEquals(Arrays.toString(b),
				Arrays.toString(pre.toByteArray()));

		aoe.add(ire6_Group263);
		byte[] c = { 0, 0, 0, 0, 3, 1, 0, 0, 0, 2, 1, 2, 1, 0, 0, 0, 2, 0, 5,
				1, 0, 0, 0, 2, 0, 6 };
		Assert.assertEquals(Arrays.toString(c),
				Arrays.toString(pre.toByteArray()));

		pre = new ProductGroupElement(aoe);
		Assert.assertEquals(Arrays.toString(c),
				Arrays.toString(pre.toByteArray()));

	}

	@Test
	public void getArrTest() {
		aoe.add(ire258_Group263);
		aoe.add(ire5_Group263);
		ProductGroupElement pre = new ProductGroupElement(aoe);
		Assert.assertTrue(aoe.equals(pre.getElements()));
	}

	@Test
	public void multTest() {
		ArrayOfElements<IGroupElement> aoe1 = new ArrayOfElements<IGroupElement>();
		aoe1.add(ire1_Group3);
		ArrayOfElements<IGroupElement> aoe2 = new ArrayOfElements<IGroupElement>();
		aoe2.add(ire2_Group3);
		ProductGroupElement pre1 = new ProductGroupElement(aoe1);
		ProductGroupElement pre2 = new ProductGroupElement(aoe2);
		Assert.assertEquals(new LargeInteger("2"), ((ModGroupElement) pre1
				.mult(pre2).getElements().getAt(0)).getElement());
	}

	@Test
	public void powerTest() {
		aoe.add(ire1_Group3);
		aoe.add(ire2_Group3);
		ProductGroupElement pre = new ProductGroupElement(aoe);
		Assert.assertEquals(new LargeInteger("1"),
				((ModGroupElement) pre.power(new LargeInteger("3"))
						.getElements().getAt(0)).getElement());
		Assert.assertEquals(new LargeInteger("2"), ((ModGroupElement) pre
				.getElements().getAt(1)).getElement());
		Assert.assertEquals(new LargeInteger("1"),
				((ModGroupElement) pre.power(new LargeInteger("3"))
						.getElements().getAt(0)).getElement());
	}

	@Test
	public void equal_whenEqualTest() {
		ArrayOfElements<IGroupElement> aoe1 = new ArrayOfElements<IGroupElement>();
		aoe1.add(ire1_Group3);
		ProductGroupElement pre1 = new ProductGroupElement(aoe1);
		ProductGroupElement pre2 = new ProductGroupElement(aoe1);
		Assert.assertTrue(pre1.equals(pre2));
	}

	@Test
	public void equal_whenNotEqualTest() {
		ArrayOfElements<IGroupElement> aoe1 = new ArrayOfElements<IGroupElement>();
		aoe1.add(ire1_Group3);
		ArrayOfElements<IGroupElement> aoe2 = new ArrayOfElements<IGroupElement>();
		aoe2.add(ire2_Group3);
		ProductGroupElement pre1 = new ProductGroupElement(aoe1);
		ProductGroupElement pre2 = new ProductGroupElement(aoe2);
		Assert.assertFalse(pre1.equals(pre2));
	}
}
