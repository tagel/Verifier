package arithmetic.objects;


public abstract class GroupElement<E> implements ByteTree {

	private E element;
	private IGroup<E> group;
	
	public GroupElement (E element, IGroup<E> group) {
		this.element = element;
		this.group = group;
	}
	
	public E getElement() {
		return element;
	}
	
	public IGroup<E> getGroup() {
		return group;
	}
	


	

}