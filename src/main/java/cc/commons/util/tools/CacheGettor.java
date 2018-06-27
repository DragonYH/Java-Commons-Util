package cc.commons.util.tools;

import cc.commons.util.interfaces.IGettor;

public class CacheGettor<T>{

    public static <E> CacheGettor<E> create(IGettor<E> pGettor){
        return new CacheGettor(pGettor);
    }

    /** Gettor,获取值 */
    protected final IGettor<T> mGettor;
    /** 缓存的值,无值时为null */
    protected T mCacheValue=null;
    /** 标记是否已经缓存值 */
    private boolean mCached=false;

    public CacheGettor(IGettor<T> pGettor){
        this.mGettor=pGettor;
    }

    /**
     * 使缓存的值失效
     */
    public void makerInvalid(){
        synchronized(this){
            this.mCached=false;
            this.mCacheValue=null;
        }
    }

    /**
     * 获取数据
     * 
     * @return 数据
     */
    public T get(){
        synchronized(this){
            if(!this.mCached){
                this.mCacheValue=this.mGettor.get();
                this.mCached=true;
            }

            return this.mCacheValue;
        }
    }

}
