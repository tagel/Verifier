package arithmetic.objects.groups;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.*;

public class ECurveRandArrayTest {

	@Test
	public void TestArr() {
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
		Assert.assertEquals(new LargeInteger("3"),q.mod(new LargeInteger("4")));
		Assert.assertEquals(0, q.mod(new LargeInteger("4")).compareTo(new LargeInteger("3")));
				
		ECurveRandArray three = new ECurveRandArray(new LargeInteger("19"));
		LargeInteger tst3 = three.simpleShanksTonelli(new LargeInteger("11"));
		Assert.assertEquals(new LargeInteger("9"), three.getQ());
		Assert.assertEquals(new LargeInteger("1"), three.getS());
		Assert.assertEquals(new LargeInteger("7"), tst3);
		
		

	}

}
