package cc.commons.util.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
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

    public static Object invoke(ICall pCall){
        try{
            return pCall.call();
        }catch(Throwable e){
            throw new IllegalStateException(e);
        }
    }

}
