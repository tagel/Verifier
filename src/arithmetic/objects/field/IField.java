package arithmetic.objects.field;

import arithmetic.objects.ring.IRing;

/**
 * This generic interface is used to represent a field over some kind of field
 * element. in our project we will use integer field elements.
 * 
 * @author Itay
 * 
 * @param <E>
 *            - the type of field element.
 */
public interface IField<E> extends IRing<E> {

}