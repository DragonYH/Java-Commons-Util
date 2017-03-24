package cc.commons.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import cc.commons.util.interfaces.IFilter;

public class ClassUtil{

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
     * 后者指定的访问修饰符值域是否存在在前者中或者相同
     * <p>
     * pWhich小于0,表示总是存在
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
        if(pWhich<0||pCol==pWhich)
            return true;
        return (pCol&pWhich)!=0;
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldNames
     *            可能的值域名
     * @return 第一个匹配名字的值域
     * @throws NoSuchFieldException
     *             没有匹配到任何值域
     */
    private static Field getFieldPrivate(Class<?> pClazz,String...pFieldNames) throws NoSuchFieldException{
        if(pFieldNames.length==1){
            return pClazz.getDeclaredField(pFieldNames[0]);
        }
        Field[] tFields=pClazz.getDeclaredFields();
        for(Field sField : tFields){
            for(String sFieldName : pFieldNames){
                if(sField.getName().equals(sFieldName)){
                    return sField;
                }
            }
        }
        throw new NoSuchFieldException(getArrayString(pFieldNames));
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldNames
     *            可能的值域名
     * @return 第一个匹配名字的值域
     */
    public static Field getField(Class<?> pClazz,String...pFieldNames){
        try{
            return ClassUtil.getFieldPrivate(pClazz,pFieldNames);
        }catch(NoSuchFieldException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 名字为 "+ClassUtil.getArrayString(pFieldNames)+" 的值域时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldClassShortName
     *            值域类型短名字
     * @param pModifier
     *            值域包含的修饰符,如果不限制则设置小于0
     * @see java.lang.reflect.Modifier
     * @return 符合的值域,非空
     */
    public static ArrayList<Field> getField(Class<?> pClazz,String pFieldClassShortName,int pModifier){
        ArrayList<Field> tFoundFields=new ArrayList<>();
        try{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                if(sField.getType().getSimpleName().equals(pFieldClassShortName)&&ClassUtil.includedModifier(sField.getModifiers(),pModifier)){
                    tFoundFields.add(sField);
                }
            }
            if(!tFoundFields.isEmpty()){
                return tFoundFields;
            }
            throw new NoSuchFieldException("类 "+pClazz.getName()+" 不存在值域类型短名字为 "+pFieldClassShortName+",且类型修饰符包含 "+pModifier+" 的值域");
        }catch(SecurityException|IllegalArgumentException|NoSuchFieldException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 类型短名字为 "+pFieldClassShortName+" 的值域时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldClazz
     *            值域类型
     * @param pModifier
     *            值域包含的值域修饰符,如果不限制则设置小于0
     * @see java.lang.reflect.Modifier
     * @return 符合的值域,非空
     */
    public static ArrayList<Field> getField(Class<?> pClazz,Class<?> pFieldClazz,int pModifier){
        try{
            ArrayList<Field> tFoundFields=new ArrayList<>();
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields)
                if(sField.getType().equals(pFieldClazz)&&ClassUtil.includedModifier(sField.getModifiers(),pModifier)){
                    tFoundFields.add(sField);
                }
            if(!tFoundFields.isEmpty()){
                return tFoundFields;
            }
            throw new NoSuchFieldException("类 "+pClazz.getName()+" 不存在类型为 "+pFieldClazz.getName()+",且类型修饰符包含 "+pModifier+" 的值域");
        }catch(SecurityException|IllegalArgumentException|NoSuchFieldException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 值域类型为 "+pFieldClazz+" 的值域时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            值域过滤器
     * @return 符合的值域,非空
     */
    public static ArrayList<Field> getField(Class<?> pClazz,IFilter<Field> pFilter){
        try{
            ArrayList<Field> tFoundFields=new ArrayList<>();
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields)
                if(pFilter.accept(sField)){
                    tFoundFields.add(sField);
                }
            if(!tFoundFields.isEmpty()){
                return tFoundFields;
            }
            throw new NoSuchFieldException("类 "+pClazz.getName()+" 不存在指定过滤器的值域");
        }catch(SecurityException|IllegalArgumentException|NoSuchFieldException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 的指定值域时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域的值
     * 
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @param pField
     *            值域
     * @return 值域的值
     */
    public static Object getFieldValue(Object pObj,Field pField){
        try{
            pField.setAccessible(true);
            return pField.get(pObj);
        }catch(IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="获取类 "+pField.getDeclaringClass().getName()+" 的值域 "+pField+" 的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @param pFieldNames
     *            可能的值域名
     * @return 值域的值
     */
    public static Object getFieldValue(Class<?> pClazz,Object pObj,String...pFieldNames){
        try{
            Field tField=ClassUtil.getFieldPrivate(pClazz,pFieldNames);
            tField.setAccessible(true);
            return tField.get(pObj);
        }catch(NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 名字为 "+getArrayString(pFieldNames)+" 的值域的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @param pFieldType
     *            值域类型
     * @param pModifier
     *            值域包含的值域修饰符,如果不限制则设置小于0
     * @see java.lang.reflect.Modifier
     * @return 符合的值域的值,非空
     * 
     */
    public static <T> ArrayList<T> getFieldValue(Class<?> pClazz,Object pObj,Class<T> pFieldType,int pModifier){
        ArrayList<Field> tFields=ClassUtil.getField(pClazz,pFieldType,pModifier);
        ArrayList<T> tFieldValues=new ArrayList<>();
        try{
            for(Field sField : tFields){
                sField.setAccessible(true);
                tFieldValues.add((T)sField.get(pObj));
            }
            return tFieldValues;
        }catch(IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 类型为 "+pFieldType+" 的值域的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 设置值域的值
     * 
     * @param pField
     *            值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * @param pValue
     *            要设置成的值
     */
    public static void setFieldValue(Field pField,Object pObj,Object pValue){
        try{
            pField.setAccessible(true);
            pField.set(pObj,pValue);
        }catch(IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="设置类 "+pField.getDeclaringClass().getName()+" 的值域 "+pField+" 的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 设置final值域的值
     * 
     * @param pField
     *            值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * 
     * @param pValue
     *            要设置成的值
     */
    public static void setFinalFieldValue(Field pField,Object pObj,Object pValue){
        try{
            pField.setAccessible(true);
            boolean tIsFinal=Modifier.isFinal(pField.getModifiers());
            int tOriginModifer=pField.getModifiers();
            if(tIsFinal){
                ClassUtil.setFieldValue(Field.class,pField,"modifiers",(tOriginModifer&(~Modifier.FINAL)));
            }
            pField.set(pObj,pValue);
            if(tIsFinal){
                ClassUtil.setFieldValue(Field.class,pField,"modifiers",tOriginModifer);
            }
        }catch(IllegalArgumentException|IllegalAccessException|IllegalStateException exp){
            String tErrorMsg="设置类 "+pField.getDeclaringClass().getName()+" 的final值域 "+pField+" 的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 设置值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * @param pFieldName
     *            值域名
     * @param pValue
     *            要设置成的值
     */
    public static void setFieldValue(Class<?> pClazz,Object pObj,String pFieldName,Object pValue){
        try{
            Field tField=ClassUtil.getFieldPrivate(pClazz,pFieldName);
            tField.setAccessible(true);
            tField.set(pObj,pValue);
        }catch(NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="设置类 "+pClazz.getName()+" 名字为 "+pFieldName+" 的值域的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 设置值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * @param pFieldNames
     *            可能的值域名
     * @param pValue
     *            要设置成的值
     */
    public static void setFieldValue(Class<?> pClazz,Object pObj,String[] pFieldNames,Object pValue){
        try{
            Field tField=ClassUtil.getFieldPrivate(pClazz,pFieldNames);
            tField.setAccessible(true);
            tField.set(pObj,pValue);
        }catch(NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="设置类 "+pClazz.getName()+" 名字为 "+getArrayString(pFieldNames)+" 的值域的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 设置值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * @param pFieldType
     *            值域类型
     * @param pValue
     *            要设置成的值
     * @param pModifier
     *            值域包含的值域修饰符,如果不限制则设置小于0
     * @see java.lang.reflect.Modifier
     */
    public static <T> void setFiledValue(Class<?> pClazz,Object pObj,Class<T> pFieldType,Object pValue,int pModifier){
        Field tField=ClassUtil.getField(pClazz,pFieldType,pModifier).get(0);
        try{
            tField.setAccessible(true);
            tField.set(pObj,pValue);
        }catch(IllegalArgumentException|IllegalAccessException exp){
            String tErrorMsg="设置类 "+pClazz.getName()+" 类型为 "+pFieldType+" 的值域的值时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取方法,无视参数
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getMethodIgnoreArg(Class<?> pClazz,String...pMethodNames){
        ArrayList<Method> tMethods=new ArrayList<>();
        try{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                for(String sName : pMethodNames){
                    if(sMethod.getName().equals(sName)){
                        tMethods.add(sMethod);
                        break;
                    }
                }
            }
            if(tMethods.isEmpty())
                throw new NoSuchMethodException(getArrayString(pMethodNames));
            return tMethods;
        }catch(IllegalArgumentException|NoSuchMethodException|SecurityException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 名字为 "+getArrayString(pMethodNames)+",忽略参数类型,的方法时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取无参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @return 第一个匹配到名字的方法
     */
    public static Method getMethod(Class<?> pClazz,String...pMethodNames){
        return getMethod(pClazz,pMethodNames,new Class<?>[]{});
    }

    /**
     * 获取单参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pArgType
     *            参数类型
     * @return 方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,Class<?> pArgType){
        return getMethod(pClazz,new String[]{pMethodName},new Class<?>[]{pArgType});
    }

    /**
     * 获取单参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pArgType
     *            参数类型
     * @return 方法
     */
    public static Method getMethod(Class<?> pClazz,String[] pMethodNames,Class<?> pArgType){
        return getMethod(pClazz,pMethodNames,new Class<?>[]{pArgType});
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pArgsTypes
     *            参数类型
     * @return 方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,Class<?>[] pArgsTypes){
        return ClassUtil.getMethod(pClazz,new String[]{pMethodName},pArgsTypes);
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @param pArgsTypes
     *            参数类型
     * @return 方法
     */
    public static Method getMethod(Class<?> pClazz,String[] pMethodNames,Class<?>[] pArgsTypes){
        pArgsTypes=ClassUtil.fixArray(Class.class,pArgsTypes);
        try{
            if(pMethodNames.length==1){
                return pClazz.getDeclaredMethod(pMethodNames[0],pArgsTypes);
            }
            for(Method sMethod : pClazz.getDeclaredMethods()){
                for(String sMethodName : pMethodNames){
                    if(sMethod.getName().equals(sMethodName)&&ClassUtil.isSameClazzs(sMethod.getParameterTypes(),pArgsTypes))
                        return sMethod;
                }
            }
            throw new NoSuchMethodException(getArrayString(pMethodNames));
        }catch(IllegalArgumentException|NoSuchMethodException|SecurityException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 名字为 "+getArrayString(pMethodNames)+",参数类型为 "+ClassUtil.getArrayString(pArgsTypes)+"的方法时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            方法过滤器
     * @return 符合条件的方法,非空
     */
    public static ArrayList<Method> getMethod(Class<?> pClazz,IFilter<Method> pFilter){
        try{
            ArrayList<Method> tFoundMethods=new ArrayList<>();
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(pFilter.accept(sMethod)){
                    tFoundMethods.add(sMethod);
                }
            }
            if(!tFoundMethods.isEmpty()){
                return tFoundMethods;
            }
            throw new NoSuchMethodException("类 "+pClazz.getName()+" 不存在指定过滤器的方法");
        }catch(NoSuchMethodException exp){
            String tErrorMsg="获取类 "+pClazz.getName()+" 的指定方法时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 获取无参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnTypeShortName
     *            返回类型短名字
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,String pReturnTypeShortName){
        return ClassUtil.getUnknowMethod(pClazz,pReturnTypeShortName,new Class<?>[]{});
    }

    /**
     * 获取单参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnTypeShortName
     *            返回类型短名字
     * @param pArgType
     *            参数类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,String pReturnTypeShortName,Class<?> pArgType){
        return ClassUtil.getUnknowMethod(pClazz,pReturnTypeShortName,new Class<?>[]{pArgType});
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pReturnTypeShortName
     *            返回类型短名字
     * @param pArgsClazzs
     *            参数类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,String pReturnTypeShortName,Class<?>[] pArgsClazzs){
        if(pReturnTypeShortName==null||pReturnTypeShortName.equals("null"))
            pReturnTypeShortName=void.class.getSimpleName();
        Method[] tMethods=pClazz.getDeclaredMethods();
        ArrayList<Method> tFoundMethods=new ArrayList<>();
        for(Method sMethod : tMethods){
            Class<?> tReturn=sMethod.getReturnType();
            if(tReturn.getSimpleName().equals(pReturnTypeShortName)&&isSameClazzs(sMethod.getParameterTypes(),pArgsClazzs))
                tFoundMethods.add(sMethod);
        }
        if(!tFoundMethods.isEmpty())
            return tFoundMethods;
        try{
            throw new NoSuchMethodException("未发现类 "+pClazz.getName()+" 返回类型名为 "+pReturnTypeShortName+" ,参数类型为 "+ClassUtil.getArrayString(pArgsClazzs)+" 的方法");
        }catch(NoSuchMethodException exp){
            throw new IllegalStateException(exp.getMessage(),exp);
        }
    }

    /**
     * 获取无参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType){
        return getUnknowMethod(pClazz,pReturnType,new Class<?>[]{});
    }

    /**
     * 获取单参数方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pArgClazz
     *            参数类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,Class<?> pArgClazz){
        return getUnknowMethod(pClazz,pReturnType,new Class<?>[]{pArgClazz});
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pArgsTypes
     *            参数类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,Class<?>[] pArgsTypes){
        Method[] tMethods=pClazz.getDeclaredMethods();
        ArrayList<Method> tFoundMethods=new ArrayList<>();
        for(Method sMethod : tMethods){
            Class<?> tReturn=sMethod.getReturnType();
            if(isSameClazz(tReturn,pReturnType)&&isSameClazzs(sMethod.getParameterTypes(),pArgsTypes))
                tFoundMethods.add(sMethod);
        }
        if(!tFoundMethods.isEmpty())
            return tFoundMethods;
        try{
            throw new NoSuchMethodException("未发现类 "+pClazz.getName()+" 返回类型为 "+pReturnType+",参数类型为 "+ClassUtil.getArrayString(pArgsTypes)+" 的方法");
        }catch(NoSuchMethodException exp){
            throw new IllegalStateException(exp.getMessage(),exp);
        }
    }

    /**
     * 获取单参方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pArgTypeShortName
     *            参数类型
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,String pArgTypeShortName){
        return getUnknowMethod(pClazz,pReturnType,new String[]{pArgTypeShortName});
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pReturnType
     *            返回类型
     * @param pArgsTypeShortNames
     *            参数短类型名
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getUnknowMethod(Class<?> pClazz,Class<?> pReturnType,String[] pArgsTypeShortNames){
        Method[] tMethods=pClazz.getDeclaredMethods();
        ArrayList<Method> tFoundMethods=new ArrayList<>();
        for(Method sMethod : tMethods){
            Class<?> tReturn=sMethod.getReturnType();
            if(isSameClazz(tReturn,pReturnType)&&isSameClazzs(sMethod.getParameterTypes(),pArgsTypeShortNames))
                tFoundMethods.add(sMethod);
        }
        if(!tFoundMethods.isEmpty())
            return tFoundMethods;
        try{
            throw new NoSuchMethodException("未发现类 "+pClazz.getName()+" 返回类型名为 "+pReturnType+" ,参数类型短名为 "+ClassUtil.getArrayString(pArgsTypeShortNames)+" 的方法");
        }catch(NoSuchMethodException exp){
            throw new IllegalStateException(exp.getMessage(),exp);
        }
    }

    /**
     * 执行无参方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethodNames
     *            可能的方法名
     * @return 方法返回值
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String...pMethodNames){
        return invokeMethod(pClazz,pObj,pMethodNames,new Class<?>[]{},new Object[]{});
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
     * @param pArgType
     *            参数类型
     * @param pArg
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String pMethodName,Class<?> pArgType,Object pArg){
        return invokeMethod(pClazz,pObj,pMethodName,new Class<?>[]{pArgType},new Object[]{pArg});
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
     * @param pArgCType
     *            参数类型
     * @param pArg
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String[] pMethodNames,Class<?> pArgCType,Object pArg){
        return ClassUtil.invokeMethod(pClazz,pObj,pMethodNames,new Class<?>[]{pArgCType},new Object[]{pArg});
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
     * @param pArgsTypes
     *            参数类型
     * @param pArgs
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String pMethodName,Class<?>[] pArgsTypes,Object[] pArgs){
        return ClassUtil.invokeMethod(pClazz,pObj,new String[]{pMethodName},pArgsTypes,pArgs);
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
     * @param pArgsTypes
     *            参数类型
     * @param pArgs
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Class<?> pClazz,Object pObj,String[] pMethodNames,Class<?>[] pArgsTypes,Object[] pArgs){
        Method tMethod=ClassUtil.getMethod(pClazz,pMethodNames,pArgsTypes);
        try{
            tMethod.setAccessible(true);
            return tMethod.invoke(pObj,pArgs);
        }catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException exp){
            String tErrorMsg="执行类 "+pClazz.getName()+" 名字为 "+getArrayString(pMethodNames)+" ,参数类型为 "+ClassUtil.getArrayString(pArgsTypes)+" ,参数为 "+ClassUtil.getArrayString(pArgs)+" 的方法时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 执行无参数方法,并返回结果
     * 
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethod
     *            方法
     * @return 方法返回值
     */
    public static Object invokeMethod(Method pMethod,Object pObj){
        return invokeMethod(pMethod,pObj,new Object[0]);
    }

    /**
     * 执行单参方法,并返回结果
     * 
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethod
     *            方法
     * @param pArg
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Method pMethod,Object pObj,Object pArg){
        return invokeMethod(pMethod,pObj,new Object[]{pArg});
    }

    /**
     * 执行方法,并返回结果
     * 
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethod
     *            方法
     * @param pArgs
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Method pMethod,Object pObj,Object[] pArgs){
        try{
            pMethod.setAccessible(true);
            return pMethod.invoke(pObj,pArgs);
        }catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException exp){
            String tErrorMsg="执行类 "+pMethod.getDeclaringClass().getName()+" 的方法 "+pMethod+" ,参数: "+ClassUtil.getArrayString(pArgs)+",时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 执行无参方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pReturnType
     *            返回类型
     * @return 方法返回值
     */
    public static <T> T invokeMethod(Class<?> pClazz,Object pObj,Class<T> pReturnType){
        return invokeMethod(pClazz,pObj,pReturnType,new Class<?>[0],new Object[0]);
    }

    /**
     * 执行单参方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pReturnType
     *            返回类型
     * @param pArgType
     *            参数类型
     * @param pArg
     *            参数
     * @return 方法返回值
     */
    public static <T> T invokeMethod(Class<?> pClazz,Object pObj,Class<T> pReturnType,Class<?> pArgType,Object pArg){
        return invokeMethod(pClazz,pObj,pReturnType,new Class<?>[]{pArgType},new Object[]{pArg});
    }

    /**
     * 执行方法,并返回结果
     * 
     * @param pClazz
     *            类,用于获取方法
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pReturnType
     *            返回类型
     * @param pArgsClazzs
     *            参数类型
     * @param pArgs
     *            参数
     * @return 方法返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Class<?> pClazz,Object pObj,Class<T> pReturnType,Class<?>[] pArgsClazzs,Object[] pArgs){
        try{
            Method tMethod=ClassUtil.getUnknowMethod(pClazz,pReturnType,pArgsClazzs).get(0);
            tMethod.setAccessible(true);
            return (T)tMethod.invoke(pObj,pArgs);
        }catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException exp){
            String tErrorMsg="执行类 "+pClazz.getName()+" 返回类型为 "+pReturnType.getName()+" ,参数类型为 "+ClassUtil.getArrayString(pArgsClazzs)+" ,参数为 "+ClassUtil.getArrayString(pArgs)+" 的方法时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 使用无参构造函数实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @return 实例
     */
    public static Object getInstance(String pClazz){
        return getInstance(pClazz,new Class<?>[]{},new Object[]{});
    }

    /**
     * 使用单参构造函数实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @param pArgClazz
     *            参数类型
     * @param pArg
     *            参数
     * @return 实例
     */
    public static Object getInstance(String pClazz,Class<?> pArgClazz,Object pArg){
        return getInstance(pClazz,new Class<?>[]{pArgClazz},new Object[]{pArg});
    }

    /**
     * 实例化类
     * 
     * @param pClazz
     *            类全限定名
     * @param pArgsTypes
     *            参数类型
     * @param pArgs
     *            参数
     * @return 实例
     */
    public static Object getInstance(String pClazz,Class<?>[] pArgsTypes,Object[] pArgs){
        try{
            Class<?> tClazz=Class.forName(pClazz);
            return ClassUtil.getInstance(tClazz,pArgsTypes,pArgs);
        }catch(ClassNotFoundException|SecurityException|IllegalArgumentException exp){
            String tErrorMsg="使用参数 "+ClassUtil.getArrayString(pArgsTypes)+" 实例化类 "+pClazz+" 时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 使用无参构造函数实例化类
     * 
     * @param pClazz
     *            类
     * @return 实例
     */
    public static <T> T getInstance(Class<? extends T> pClazz){
        return getInstance(pClazz,new Class<?>[]{},new Object[]{});
    }

    /**
     * 使用单参构造函数实例化类
     * 
     * @param pClazz
     *            类
     * @param pArgsType
     *            参数类型
     * @param pArg
     *            参数
     * @return 实例
     */
    public static <T> T getInstance(Class<? extends T> pClazz,Class<?> pArgsType,Object pArg){
        return getInstance(pClazz,new Class<?>[]{pArgsType},new Object[]{pArg});
    }

    /**
     * 实例化类
     * 
     * @param pClazz
     *            类
     * @param pArgsTypes
     *            参数类型
     * @param pArgs
     *            参数
     * @return 实例
     */
    public static <T> T getInstance(Class<? extends T> pClazz,Class<?>[] pArgsTypes,Object[] pArgs){
        try{
            Constructor<? extends T> tcons;
            if(pArgsTypes==null||pArgsTypes.length==0)
                tcons=pClazz.getDeclaredConstructor();
            else tcons=pClazz.getDeclaredConstructor(pArgsTypes);
            tcons.setAccessible(true);
            return tcons.newInstance(pArgs);
        }catch(NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException exp){
            String tErrorMsg="使用参数类型为 "+ClassUtil.getArrayString(pArgsTypes)+" ,参数为 "+ClassUtil.getArrayString(pArgs)+" 的构造函数实例化类 "+pClazz+" 时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
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
            String tErrorMsg="获取类"+pClazz+"时发生了错误";
            throw new IllegalStateException(tErrorMsg,exp);
        }
    }

    /**
     * 比较两个类数组是否包含顺序相同的类
     */
    private static boolean isSameClazzs(Class<?>[] pClazzs1,Class<?>[] pClazzs2){
        if(pClazzs1==pClazzs2)
            return true;
        if((pClazzs1==null||pClazzs1.length==0)&&(pClazzs2==null||pClazzs2.length==0))
            return true;
        if(pClazzs1==null||pClazzs2==null)
            return false;
        if(pClazzs1.length!=pClazzs2.length)
            return false;
        for(int i=0;i<pClazzs1.length;i++){
            if(pClazzs1[i]!=pClazzs2[i])
                return false;
        }
        return true;
    }

    /**
     * 检查值域是否存在
     * 
     * @param pClazz
     *            类
     * @param pFieldNames
     *            可能的值域名字
     * @return 是否存在
     */
    public static boolean isFieldExist(Class<?> pClazz,String...pFieldNames){
        for(Field sField : pClazz.getDeclaredFields()){
            for(String sFieldName : pFieldNames){
                if(sField.getName().equals(sFieldName)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查方法是否存在
     * <p>
     * 此检查不管参数和返回类型
     * </p>
     * 
     * @param pClazz
     *            类
     * @param pMethodNames
     *            可能的方法名
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String...pMethodNames){
        for(Method sMethod : pClazz.getDeclaredMethods()){
            for(String sName : pMethodNames){
                if(sMethod.getName().equals(sName)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查是否存在同名,同参数,同返回类型的方法
     * <p>
     * 此方法不会报错,不会检查方法归属哪个类
     * </p>
     * 
     * @param pClazz
     *            类
     * @param pMethod
     *            方法
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,Method pMethod){
        if(pMethod==null)
            return false;

        return ClassUtil.isMethodExist(pClazz,pMethod.getName(),pMethod.getReturnType(),pMethod.getParameterTypes());
    }

    /**
     * 检查是否存在同名,同参数,同返回类型的方法
     * <p>
     * 此方法不会报错
     * </p>
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名,如果为null,不检查方法名字
     * @param pReturnType
     *            返回类型
     * @param pArgsType
     *            参数类型
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,String pMethodName,Class<?> pReturnType,Class<?>...pArgsType){
        for(Method sMethod : pClazz.getDeclaredMethods()){
            if((StringUtil.isEmpty(pMethodName)||sMethod.getName().equals(pMethodName))&&sMethod.getReturnType()==pReturnType&&ClassUtil.isSameClazzs(sMethod.getParameterTypes(),pArgsType)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查方法是否存在
     * <p>
     * 此方法不会报错
     * </p>
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            方法过滤器
     * @return 是否存在
     */
    public static boolean isMethodExist(Class<?> pClazz,IFilter<Method> pFilter){
        for(Method sMethod : pClazz.getDeclaredMethods()){
            if(pFilter.accept(sMethod)){
                return true;
            }
        }
        return false;
    }

    /**
     * 比较两个数组是不是包含顺序相同的类
     */
    private static boolean isSameClazzs(Class<?>[] pClazzs1,String[] pClazzsShortName){
        if(pClazzs1==null&&pClazzsShortName==null)
            return true;
        if((pClazzs1==null&&pClazzsShortName.length==0)||(pClazzsShortName==null&&pClazzs1.length==0))
            return true;
        if(pClazzs1==null||pClazzsShortName==null)
            return false;
        if(pClazzs1.length!=pClazzsShortName.length)
            return false;
        for(int i=0;i<pClazzs1.length;i++){
            if(!pClazzs1[i].getSimpleName().equals(pClazzsShortName[i]))
                return false;
        }
        return true;
    }

    /**
     * 比较两个类是不是相同的类
     */
    private static boolean isSameClazz(Class<?> pClazz1,Class<?> pClazz2){
        if(pClazz1==pClazz2)
            return true;
        if((pClazz1==void.class&&pClazz2==null)||(pClazz1==null&&pClazz2==void.class))
            return true;
        return false;
    }

    private static String getArrayString(Object[] pArrays){
        if(pArrays==null||pArrays.length==0)
            return "无";
        else return Arrays.asList(pArrays).toString();
    }

    /**
     * 打印一个类所有的方法
     * 
     * @param pClazz
     *            类
     */
    public static void printfMethod(Class<?> pClazz){
        Method[] tMethods=pClazz.getDeclaredMethods();
        for(Method sMethod : tMethods){
            System.out.println(sMethod);
        }
    }

    private static String getSuitString(Object[] pObjs){
        StringBuilder tSB=new StringBuilder();
        if(pObjs.length>1){
            tSB.append('[');
        }
        for(Object sObj : pObjs){
            tSB.append(String.valueOf(sObj)).append(',');
        }
        if(tSB.length()>0&&tSB.charAt(tSB.length()-1)==','){
            tSB.deleteCharAt(tSB.length()-1);
        }
        if(pObjs.length>1){
            tSB.append(']');
        }
        return tSB.toString();
    }

    /**
     * 如果数组为null,实例化同类型0长度的数组并返回
     * 
     * @param pClazz
     *            类
     * @param pObjs
     *            检查的对象
     * @return 原对象或实例化的对象
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] fixArray(Class<T> pClazz,T[] pObjs){
        if(pObjs==null){
            pObjs=(T[])Array.newInstance(pClazz,0);
        }
        return pObjs;
    }

}
