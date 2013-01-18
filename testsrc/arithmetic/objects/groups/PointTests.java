package arithmetic.objects.groups;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;
import arithmetic.objects.field.IntegerFieldElement;
import arithmetic.objects.field.PrimeOrderField;

/**
 * Tests for Point class.
 * 
 * @author Daniel
 * 
 */
public class PointTests {

	private PrimeOrderField field = new PrimeOrderField(new LargeInteger("4"));
	private IntegerFieldElement element3 = new IntegerFieldElement(
			new LargeInteger("3"), field);
	private IntegerFieldElement element4 = new IntegerFieldElement(
			new LargeInteger("4"), field);
	private Point point = new Point(element3, element4);

	@Test
	public void getXTest() {
		Assert.assertTrue(element3.equals(point.getX()));
	}

	@Test
	public void getYTest() {
		Assert.assertTrue(element4.equals(point.getY()));
	}

	@Test
	public void changeCoordinatesTest() {
		point.changeCoordinates(element4, element4);
		Assert.assertTrue(element4.equals(point.getX()));
		Assert.assertTrue(element4.equals(point.getY()));
	}
}
