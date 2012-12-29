package arithmetic.objects;

import java.io.UnsupportedEncodingException;



public class ECurveGroupElement extends GroupElement<Point> {

	public ECurveGroupElement(Point element, IGroup<Point> group) {
		super(element, group);
	}

	@Override
	public byte[] toByteArray() throws UnsupportedEncodingException {
		IntegerFieldElement[] arr ={element.getX(), element.getY()};
		Node pointNode = new Node(arr);
		return pointNode.toByteArray();
	}

}
