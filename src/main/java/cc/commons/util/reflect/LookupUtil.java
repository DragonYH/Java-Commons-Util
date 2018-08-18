package cc.commons.util.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
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

    /**
     * @param pCaller
     * @param pMethod
     * @return
     */
    public static MethodHandle unreflect(Class<?> pCaller,Method pMethod){
        return LookupUtil.unreflect(ClassUtil.newLookup(pCaller),pMethod);
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

    public static Object invoke(ICall pCall){
        try{
            return pCall.call();
        }catch(Throwable e){
            throw new IllegalStateException(e);
        }
    }

}
