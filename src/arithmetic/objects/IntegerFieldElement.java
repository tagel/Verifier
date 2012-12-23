package arithmetic.objects;
import java.math.BigInteger;


public class IntegerFieldElement extends FieldElement<BigInteger> {
	
	public IntegerFieldElement(BigInteger element, IField<BigInteger> field) {
		super(element, field);
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
