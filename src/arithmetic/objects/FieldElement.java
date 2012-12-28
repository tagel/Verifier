package arithmetic.objects;


public abstract class FieldElement<E> implements ByteTree {

	protected E element;
	protected IField<E> field;
	
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
