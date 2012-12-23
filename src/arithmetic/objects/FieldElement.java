package arithmetic.objects;

public abstract class FieldElement<E> implements ByteTree {

	private E element;
	private IField<E> field;
	
	public FieldElement (E element, IField<E> field) {
		this.element = element;
		this.field = field;
	}
	
	public E getElement() {
		return element;
	}
	
	public IField<E> getField() {
		return field;
	}
	

	
	

}
