package cc.commons.util.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import cc.commons.util.interfaces.IFilter;

/**
 * 工具类中的所有参数请严格格式,未注明的地方请勿使用null代替 *
 */
public class MethodUtil{

    private static final String REFLACT_OP_ERROR="反射操作异常";
    private static final String NO_SUCH_METHOD="未找到该类型的方法";

    private static void checkMethodA(String[] pMethodNames){
        if(pMethodNames==null||pMethodNames.length==0)
            throw new IllegalArgumentException("至少需要一个方法名");
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] checkTypeA(Class<T> pClazz,T[] pParamTypes){
        if(pParamTypes==null){
            pParamTypes=(T[])Array.newInstance(pClazz,0);
        }
        return pParamTypes;
    }

    // --------====| 打印方法 |====--------

    /**
     * 打印类所有的方法
     * 
     * @param pClazz
     *            类
     * @param pDeclared
     *            是否只打印该类定义的方法而不检索父类的值域
     */
    public static void printMethod(Class<?> pClazz,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                System.out.println(sMethod);
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);
    }

    // --------====| 检查方法 |====--------

    /**
     * 检查无参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String pMethodName,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pMethodName,new Class<?>[0],pDeclared);
    }

    /**
     * 检查但参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pParamType
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String pMethodName,Class<?> pParamType,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pMethodName,new Class<?>[]{pParamType},pDeclared);
    }

    /**
     * 检查方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pParamTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String pMethodName,Class<?>[] pParamTypes,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getName().equals(pMethodName)&&Arrays.equals(sMethod.getParameterTypes(),pParamTypes)){
                    return true;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    /**
     * 检查无参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String[] pMethodNames,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pMethodNames,new Class<?>[0],pDeclared);
    }

    /**
     * 检查单参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pParamType
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String[] pMethodNames,Class<?> pParamType,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pMethodNames,new Class<?>[]{pParamType},pDeclared);
    }

    /**
     * 检查方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pParamTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String[] pMethodNames,Class<?>[] pParamTypes,boolean pDeclared){
        MethodUtil.checkMethodA(pMethodNames);
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                for(String sMethodName : pMethodNames){
                    if(sMethod.getName().equals(sMethodName)&&Arrays.equals(sMethod.getParameterTypes(),pParamTypes)){
                        return true;
                    }
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    /**
     * 检查无参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,Class<?> pReturnType,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pReturnType,new Class<?>[0],pDeclared);
    }

    /**
     * 检查单参数方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pParamType
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,Class<?> pReturnType,Class<?> pParamType,boolean pDeclared){
        return MethodUtil.isMethodExist(pClazz,pReturnType,new Class<?>[]{pParamType},pDeclared);
    }

    /**
     * 检查方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pParamTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,Class<?> pReturnType,Class<?>[] pParamTypes,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getReturnType()==pReturnType&&Arrays.equals(sMethod.getParameterTypes(),pParamTypes)){
                    return true;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    /**
     * 检查方法是否存在
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            方法过滤器
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,IFilter<Method> pFilter,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(pFilter.accept(sMethod)){
                    return true;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    // --------====| 获取方法 |====--------

    /**
     * 获取方法,无视参数
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pDeclared
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getMethodIgnoreParam(Class<?> pClazz,String[] pMethodNames,boolean pDeclared){
        ArrayList<Method> tMethods=new ArrayList<>();
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                for(String sName : pMethodNames){
                    if(sMethod.getName().equals(sName)){
                        tMethods.add(sMethod);
                        break;
                    }
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tMethods.isEmpty()){
            return tMethods;
        }
        throw new IllegalStateException(NO_SUCH_METHOD+"(可能的方法名: "+Arrays.toString(pMethodNames)+")");
    }

    /**
     * 获取无参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,boolean pDeclared){
        return MethodUtil.getMethod(pClazz,pMethodName,new Class<?>[0],pDeclared);
    }

    /**
     * 获取单参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pParamType
     *            参数类型
     * @param pDeclared
     * 
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,Class<?> pParamType,boolean pDeclared){
        return MethodUtil.getMethod(pClazz,pMethodName,new Class<?>[]{pParamType},pDeclared);
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pParamsTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,Class<?>[] pParamsTypes,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getName().equals(pMethodName)&&Arrays.equals(sMethod.getParameterTypes(),pParamsTypes)){
                    return sMethod;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        throw new IllegalStateException(NO_SUCH_METHOD+"(方法名字: "+pMethodName+",参数类型: "+Arrays.toString(pParamsTypes)+")");
    }

    /**
     * 获取无参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @param pParamType
     *            参数类型
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String[] pMethodNames,boolean pDeclared){
        return MethodUtil.getMethod(pClazz,pMethodNames,new Class<?>[0],pDeclared);
    }

    /**
     * 获取单参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pParamType
     *            参数类型
     * @param pDeclare
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String[] pMethodNames,Class<?> pParamType,boolean pDeclare){
        return MethodUtil.getMethod(pClazz,pMethodNames,new Class<?>[]{pParamType},pDeclare);
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pParamTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String[] pMethodNames,Class<?>[] pParamTypes,boolean pDeclared){
        MethodUtil.checkMethodA(pMethodNames);

        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                for(String sMethodName : pMethodNames){
                    if(sMethod.getName().equals(sMethodName)&&Arrays.equals(sMethod.getParameterTypes(),pParamTypes))
                        return sMethod;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        throw new IllegalStateException(NO_SUCH_METHOD+"(可能的方法名: "+Arrays.toString(pMethodNames)+",参数类型: "+Arrays.toString(pParamTypes)+")");

    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            方法过滤器
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 符合条件的方法,非空
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static ArrayList<Method> getMethod(Class<?> pClazz,IFilter<Method> pFilter,boolean pDeclared){
        ArrayList<Method> tFoundMethods=new ArrayList<>();
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(pFilter.accept(sMethod)){
                    tFoundMethods.add(sMethod);
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tFoundMethods.isEmpty()){
            return tFoundMethods;
        }
        throw new IllegalStateException(NO_SUCH_METHOD+"(方法过滤器类型: "+pFilter.getClass().getName()+")");
    }

    /**
     * 获取无参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 符合的方法,非空
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,boolean pDeclared){
        return getUnknowMethod(pClazz,pReturnType,new Class<?>[0],pDeclared);
    }

    /**
     * 获取单参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pParamType
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 符合的方法,非空
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,Class<?> pParamType,boolean pDeclared){
        return getUnknowMethod(pClazz,pReturnType,new Class<?>[]{pParamType},pDeclared);
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pParamTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 符合的方法,非空
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,Class<?>[] pParamTypes,boolean pDeclared){
        ArrayList<Method> tFoundMethods=new ArrayList<>();
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getReturnType().equals(pReturnType)&&Arrays.equals(sMethod.getParameterTypes(),pParamTypes)){
                    tFoundMethods.add(sMethod);
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tFoundMethods.isEmpty()){
            return tFoundMethods;
        }
        throw new IllegalStateException(NO_SUCH_METHOD+"(返回类型: "+pReturnType.getName()+",参数类型: "+Arrays.toString(pParamTypes)+")");
    }

    // --------====| 执行方法 |====--------

    /**
     * 执行静态方法,并返回结果
     * 
     * @param pMethod
     *            方法
     * @param pParams
     *            参数
     * @return 方法返回值
     */
    public static Object invokeStaticMethod(Method pMethod,Object...pParams){
        return MethodUtil.invokeMethod(pMethod,(Object)null,pParams);
    }

    /**
     * 执行方法,并返回结果
     * 
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethod
     *            方法
     * @param pParams
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Method pMethod,Object pObj,Object...pParams){
        try{
            pMethod.setAccessible(true);
            return pMethod.invoke(pObj,pParams);
        }catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

    /**
     * 执行无参数方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodName
     *            方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String pMethodName,boolean pDeclared){
        return MethodUtil.invokeMethod(pClazz,pObj,pMethodName,new Class<?>[0],new Object[0],pDeclared);
    }

    /**
     * 执行单参数方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodName
     *            方法名
     * @param pParamType
     *            参数类型
     * @param pParam
     *            参数
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String pMethodName,Class<?> pParamType,Object pParam,boolean pDeclared){
        return MethodUtil.invokeMethod(pClazz,pObj,pMethodName,new Class<?>[]{pParamType},new Object[]{pParam},pDeclared);
    }

    /**
     * 执行方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodName
     *            方法名
     * @param pParamTypes
     *            参数类型
     * @param pParams
     *            参数
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String pMethodName,Class<?>[] pParamTypes,Object[] pParams,boolean pDeclared){
        return MethodUtil.invokeMethod(MethodUtil.getMethod(pClazz,pMethodName,pParamTypes,pDeclared),pObj,pParams);
    }

    /**
     * 执行无参数方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodNames
     *            可能的方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String[] pMethodNames,boolean pDeclared){
        return MethodUtil.invokeMethod(MethodUtil.getMethod(pClazz,pMethodNames,pDeclared),pObj);
    }

    /**
     * 执行单参数方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodNames
     *            可能的方法名
     * @param pParamType
     *            参数类型
     * @param pParam
     *            参数
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String[] pMethodNames,Class<?> pParamType,Object pParam,boolean pDeclared){
        return MethodUtil.invokeMethod(MethodUtil.getMethod(pClazz,pMethodNames,pParamType,pDeclared),pObj,pParam);
    }

    /**
     * 执行方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodNames
     *            可能的方法名
     * @param pParamTypes
     *            参数类型
     * @param pParams
     *            参数
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的值域
     * @return 方法返回值
     * @throws IllegalStateException
     *             没有匹配的方法,或反射操作发生异常
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String[] pMethodNames,Class<?>[] pParamTypes,Object[] pParams,boolean pDeclared){
        return MethodUtil.invokeMethod(MethodUtil.getMethod(pClazz,pMethodNames,pParamTypes,pDeclared),pObj,pParams);
    }

}
