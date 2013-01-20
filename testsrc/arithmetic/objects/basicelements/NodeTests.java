package arithmetic.objects.basicelements;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import arithmetic.objects.LargeInteger;

/**
 * Tests for class Node.
 * 
 * @author Sofi
 * 
 */
public class NodeTests {
	
	private Node node1;

	

	@Test
	public void Test() {
		node1 = new Node();
		Assert.assertEquals(0, node1.getChildrenSize());
		

		byte[] a = {1, 0, 0, 0, 5, 6, 2, 8, 5, 2};
		byte[] b = {0, 0, 0, 0, 2, 1, 0, 0, 0, 5, 6, 7, 7, 5, 2, 1, 0, 0, 0, 5, 1, 2, 8, 5, 2};
		RawElement aa = new RawElement(a);
		RawElement bb = new RawElement(b);
		node1.add(aa);
		node1.add(bb);
		Assert.assertEquals(2, node1.getChildrenSize());
		byte[] c = {0, 0, 0, 0, 2, 1, 0, 0, 0, 5, 6, 2, 8, 5, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 5, 6, 7, 7, 5, 2, 1, 0, 0, 0, 5, 1, 2, 8, 5, 2};
		Assert.assertEquals(Arrays.toString(c),Arrays.toString(node1.toByteArray()));
		BigIntLeaf d = new BigIntLeaf(new LargeInteger("56"));
		node1.setAt(1, d);
		byte[] e = {1, 0, 0, 0, 1, 56};
		Assert.assertEquals(Arrays.toString(node1.getAt(1).toByteArray()), Arrays.toString(e)); 
		
		
		
}
}