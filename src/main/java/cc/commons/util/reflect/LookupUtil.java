package cc.commons.util.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LookupUtil{

    public static interface ICall{

        public Object call() throws Throwable;

    }

    /**
     * @param pMethod
     * @return
     */
    public static MethodHandle unreflect(Method pMethod){
        return LookupUtil.unreflect(ClassUtil.newLookup(pMethod.getDeclaringClass()),pMethod);
    }

    public static MethodHandle unreflectGetter(Field pField){
        return LookupUtil.unreflectGetter(ClassUtil.newLookup(pField.getDeclaringClass()),pField);
    }

    public static MethodHandle unreflectSetter(Field pField){
        return LookupUtil.unreflectSetter(ClassUtil.newLookup(pField.getDeclaringClass()),pField);
    }

    public static MethodHandle unreflectConstructor(Constructor<?> pConstructor){
        return LookupUtil.unreflectConstructor(ClassUtil.newLookup(pConstructor.getDeclaringClass()),pConstructor);
    }
    /**
     * @param pCaller
     * @param pMethod
     * @return
     */
    public static MethodHandle unreflect(Class<?> pCaller,Method pMethod){
        return LookupUtil.unreflect(ClassUtil.newLookup(pCaller),pMethod);
    }

    public static MethodHandle unreflectGetter(Class<?> pCaller,Field pField){
        return LookupUtil.unreflectGetter(ClassUtil.newLookup(pCaller),pField);
    }

    public static MethodHandle unreflectSetter(Class<?> pCaller,Field pField){
        return LookupUtil.unreflectSetter(ClassUtil.newLookup(pCaller),pField);
    }

    public static MethodHandle unreflectConstructor(Class<?> pCaller,Constructor<?> pConstructor){
        return LookupUtil.unreflectConstructor(ClassUtil.newLookup(pCaller),pConstructor);
    }
    /**
     * @param pLookup
     * @param pMethod
     * @return
     */
    public static MethodHandle unreflect(Lookup pLookup,Method pMethod){
        try{
            return pLookup.unreflect(pMethod);
        }catch(IllegalAccessException e){
            throw new IllegalStateException(e);
        }
    }

    public static MethodHandle unreflectGetter(Lookup pLookup,Field pField){
        try{
            return pLookup.unreflectGetter(pField);
        }catch(IllegalAccessException e){
            throw new IllegalStateException(e);
        }
    }

    public static MethodHandle unreflectSetter(Lookup pLookup,Field pField){
        try{
            return pLookup.unreflectSetter(pField);
        }catch(IllegalAccessException e){
            throw new IllegalStateException(e);
        }
    }

    public static MethodHandle unreflectConstructor(Lookup pLookup,Constructor<?> pConstructor){
        try{
            return pLookup.unreflectConstructor(pConstructor);
        }catch(IllegalAccessException e){
            throw new IllegalStateException(e);
        }
    }
    public static Object invoke(ICall pCall){
        try{
            return pCall.call();
        }catch(Throwable e){
            throw new IllegalStateException(e);
        }
    }

    /**
     * 执行一个Methhandle方法
     * <p>
     * 由于javac的机制,此函数只适用于无参的方法
     * </p>
     * 
     * @param pHandle
     *            Methhandle
     * @param pInstance
     *            实例,如果为静态方法,直接写null
     * @return 方法执行返回值
     */
    public static Object invoke(MethodHandle pHandle,Object pInstance){
        try{
            return pHandle.invoke(pInstance);
        }catch(Throwable e){
            throw new IllegalStateException(e);
        }
    }

}
