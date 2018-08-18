package cc.commons.util.extra;

import java.util.ArrayList;
import java.util.Collection;

public class CList<E>extends ArrayList<E>{

    public CList(){
        super();
    }

    public CList(int initialCapacity){
        super(initialCapacity);
    }

    public CList(Collection<? extends E> c){
        super(c);
    }

    public E first(){
        return this.get(0);
    }

    public E last(){
        return this.get(this.size()-1);
    }

    public boolean onlyOne(){
        return this.size()==1;
    }

}
