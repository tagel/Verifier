package arithmetic.objects;


import java.math.BigInteger;




public abstract class GroupElement<E> implements ByteTree {

	protected E element;
	protected IGroup<GroupElement<E>> group;
	
	public GroupElement (E element, IGroup<GroupElement<E>> group) {
		this.element = element;
		this.group = group;
	}
	
	public E getElement() {
		return element;
	}
	
	public void setElement(E element) {
		this.element = element;
	}

	public void setGroup(IGroup<GroupElement<E>> group) {
		this.group = group;
	}

	public IGroup<GroupElement<E>> getGroup() {
		return group;
	}

	public abstract GroupElement<E> mult(GroupElement<E> b);
	
	public abstract GroupElement<E> inverse();

	public abstract GroupElement<E> power(BigInteger b);

	public abstract boolean equal(GroupElement<E> b);
		
	
	


	

}
