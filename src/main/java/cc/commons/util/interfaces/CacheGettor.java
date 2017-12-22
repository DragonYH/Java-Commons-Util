package cc.commons.util.interfaces;

public abstract class CacheGettor<T>{

    /** 缓存的值,无值时为null */
    protected T mCache=null;

    /**
     * 使缓存的值失效
     */
    public void makeInvalid(){
        this.mCache=null;
    }

    /**
     * 获取数据
     * 
     * @return 数据
     */
    public T get(){
        return this.mCache==null?(this.mCache=this.make()):this.mCache;
    }

    /**
     * 当缓存为null时,调用此函数
     * @return 生成的缓存
     */
    protected abstract T make();

}
