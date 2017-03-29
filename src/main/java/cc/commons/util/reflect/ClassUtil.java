package cc.commons.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtil{

    private static final String REFLACT_OP_ERROR="反射操作异常";

    /**
     * 后者指定的访问修饰符值域是否存在在前者中或者相同
     * <p>
     * pWhich<=0,表示总是存在
     * </p>
     * 
     * @param pCol
     *            值域修饰符集合
     * @param pWhich
     *            需要存在的值域修饰符集合
     * @return 是否存在
     * @see java.lang.reflect.Modifie
     */
    public static boolean includedModifier(int pCol,int pWhich){
        if(pWhich<=0||pCol==pWhich)
            return true;
        return (pCol&pWhich)!=0;
    }

    /**
     * 获取类全名的前段字符串
     * 
     * @param pClassName
     *            类全名
     * @return 类路径,如果不为空末端将包含点
     */
    public static String getClassPacket(String pClassName){
        String packetPath="";
        int pos=pClassName.lastIndexOf(".");
        if(pos!=-1)
            packetPath=pClassName.substring(0,pos+1);
        return packetPath;
    }

    /**
     * 查看指定类是否加载,不会报错
     * 
     * @param pName
     *            类完整名字
     * @return
     */
    public static boolean isClassLoaded(String pName){
        try{
            Class.forName(pName);
            return true;
        }catch(Throwable exp){
            return false;
        }
    }

    /**
     * 获取类
     * 
     * @param pClazz
     *            类全限定名
     * @return 类
     */
    public static Class<?> getClass(String pClazz){
        try{
            return Class.forName(pClazz);
        }catch(ClassNotFoundException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

    /**
     * 获取同一包下的类
     * 
     * @param pSiblingClazz
     *            其中一个兄弟类
     * @param pShortName
     *            另一个兄弟的类短名
     * @return 另一个兄弟类实例
     */
    public static Class<?> getSiblingClass(Class<?> pSiblingClazz,String pShortName){
        return ClassUtil.getClass(ClassUtil.getClassPacket(pSiblingClazz.getName())+pShortName);
    }

    /**
     * 使用无参构造函数实例化类
     * 
     * @param pClazz
     *            类
     * @return 实例
     */
    public static <T> T newInstance(Class<? extends T> pClazz){
        return newInstance(pClazz,new Class<?>[]{},new Object[]{});
    }

    /**
     * 使用单参构造函数实例化类
     * 
     * @param pClazz
     *            类
     * @param pParamType
     *            参数类型
     * @param pParam
     *            参数
     * @return 实例
     */
    public static <T> T newInstance(Class<? extends T> pClazz,Class<?> pParamType,Object pParam){
        return newInstance(pClazz,new Class<?>[]{pParamType},new Object[]{pParam});
    }

    /**
     * 实例化类
     * 
     * @param pClazz
     *            类
     * @param pParamTypes
     *            参数类型
     * @param pParams
     *            参数
     * @return 实例
     */
    public static <T> T newInstance(Class<? extends T> pClazz,Class<?>[] pParamTypes,Object[] pParams){
        try{
            Constructor<? extends T> tcons;
            if(pParamTypes==null||pParamTypes.length==0)
                tcons=pClazz.getDeclaredConstructor();
            else tcons=pClazz.getDeclaredConstructor(pParamTypes);
            tcons.setAccessible(true);
            return tcons.newInstance(pParams);
        }catch(NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

    /**
     * 使用无参构造函数实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @return 实例
     */
    public static Object newInstance(String pClazz){
        return newInstance(pClazz,new Class<?>[]{},new Object[]{});
    }

    /**
     * 使用单参构造函数实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @param pParamClazz
     *            参数类型
     * @param pParam
     *            参数
     * @return 实例
     */
    public static Object newInstance(String pClazz,Class<?> pParamClazz,Object pParam){
        return newInstance(pClazz,new Class<?>[]{pParamClazz},new Object[]{pParam});
    }

    /**
     * 实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @param pParamTypes
     *            参数类型
     * @param pParams
     *            参数
     * @return 实例
     */
    public static Object newInstance(String pClazz,Class<?>[] pParamTypes,Object[] pParams){
        return ClassUtil.newInstance(ClassUtil.getClass(pClazz),pParamTypes,pParams);
    }

}
