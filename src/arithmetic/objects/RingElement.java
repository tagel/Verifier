package arithmetic.objects;

public abstract class RingElement<E> implements ByteTree {
	
	private E element;
	private IRing<E> ring;
	
	public RingElement (E element, IRing<E> ring) {
		this.element = element;
		this.ring = ring;
	}
	
	public E getElement() {
		return element;
	}
	
	public IRing<E> getRing() {
		return ring;
	}
	
	
	

}
