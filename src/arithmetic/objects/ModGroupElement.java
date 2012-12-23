package arithmetic.objects;
import java.math.BigInteger;


public class ModGroupElement extends GroupElement<BigInteger> {

	public ModGroupElement(BigInteger element, IGroup<BigInteger> group) {
		super(element, group);
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}


}
