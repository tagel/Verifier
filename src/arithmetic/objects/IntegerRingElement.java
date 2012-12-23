package arithmetic.objects;
import java.math.BigInteger;


public class IntegerRingElement extends RingElement<BigInteger> {

	
	public IntegerRingElement(BigInteger element, IRing<BigInteger> ring) {
		super(element, ring);
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
