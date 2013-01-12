package arithmetic.objects.groups;

import java.math.BigInteger;

import org.junit.Test;

import arithmetic.objects.*;

public class ECurveRandArrayTest {
	
	@Test
	public void TestArr() {
		ECurveRandArray one = new ECurveRandArray(37, 1, null, null, 1);
		
		LargeInteger tst = new LargeInteger("0");
		tst = one.shanksTonelli(new LargeInteger("10"), new LargeInteger("13"));
		System.out.println("The root "+tst);

	}
	
	
}
