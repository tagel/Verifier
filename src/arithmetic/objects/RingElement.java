package arithmetic.objects;



public abstract class RingElement<E> implements ByteTree {
	
	protected E element;
	protected IRing<E> ring;
	
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
