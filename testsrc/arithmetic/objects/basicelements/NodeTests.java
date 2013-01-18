package arithmetic.objects.basicelements;

import junit.framework.Assert;

import org.junit.Test;

import cryptographic.primitives.CryptoUtils;

/**
 * Tests for class Node.
 * 
 * @author Sofi
 * 
 */
public class NodeTests {

	private Node node1;
	private Node node2;
	private Node node3;

	@Test
	public void constructorTest() {
		node1 = new Node();
		Assert.assertEquals(0, node1.getChildrenSize());
		Assert.assertEquals(0, node1.getChildrenSize());
		
		//its a leaf
		byte[] b = CryptoUtils
				.hexStringToBytes("010000002100BA8D93A8B97F763FBE2119144E1EE80B8D5E146C9F29ECE2E280D459CECA5C95");
		
		node2 = new Node(b);
		
		
	}
}
