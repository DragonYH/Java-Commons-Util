package cc.commons.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.commons.util.interfaces.IFilter;

public class FieldUtil{

    private static final String REFLACT_OP_ERROR="反射操作异常";
    private static final String NO_SUCH_FIELD="未找到该类型的值域";

    private static void fieldNotEmpty(String[] pFieldNames){
        if(pFieldNames==null||pFieldNames.length==0)
            throw new IllegalArgumentException("至少需要一个值域名");
    }

    public List<Field> getAllField(Class<?> pClazz){
        List<Field> tFields=new ArrayList<>();
        while(pClazz!=null){
            for(Field sField : tFields){
                tFields.add(sField);
            }
            pClazz=pClazz.getSuperclass();
        }
        return tFields;
    }

    // --------====| 打印方法 |====--------

    /**
     * 打印值域
     * 
     * @param pClazz
     *            类
     * @param pDeclared
     *            是否只打印该类定义的值域而不打印父类的值域
     */
    public static void printField(Class<?> pClazz,boolean pDeclared){
        do{
            for(Field sField : pClazz.getDeclaredFields()){
                System.out.println(sField);
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);
    }

    // --------====| 检查方法 |====--------

    /**
     * 检查值域是否存在
     * 
     * @param pClazz
     *            类
     * @param pFieldNames
     *            可能的值域名字
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isFieldExist(Class<?> pClazz,String[] pFieldNames,boolean pDeclared){
        do{
            for(Field sField : pClazz.getDeclaredFields()){
                for(String sFieldName : pFieldNames){
                    if(sField.getName().equals(sFieldName)){
                        return true;
                    }
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    /**
     * 检查值域是否存在
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            值域过滤器
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 是否存在
     */
    public static boolean isFieldExist(Class<?> pClazz,IFilter<Field> pFilter,boolean pDeclared){
        do{
            for(Field sField : pClazz.getDeclaredFields()){
                if(pFilter.accept(sField))
                    return true;
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        return false;
    }

    // --------====| 获取值域方法 |====--------

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldName
     *            值域名
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 匹配名字的值域
     * @throws IllegalStateException
     *             没有匹配到任何值域
     */
    public static Field getField(Class<?> pClazz,String pFieldName,boolean pDeclared){
        do{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                if(sField.getName().equals(pFieldName)){
                    return sField;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        throw new IllegalStateException(NO_SUCH_FIELD+"(值域名字: "+pFieldName+")");
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldNames
     *            可能的值域名
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 第一个匹配名字的值域
     * @throws IllegalStateException
     *             没有匹配到任何值域
     */
    public static Field getField(Class<?> pClazz,String[] pFieldNames,boolean pDeclared){
        FieldUtil.fieldNotEmpty(pFieldNames);
        do{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                for(String sFieldName : pFieldNames){
                    if(sField.getName().equals(sFieldName)){
                        return sField;
                    }
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        throw new IllegalStateException(NO_SUCH_FIELD+"(可能的值域名字: "+Arrays.toString(pFieldNames)+")");
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldTypeShortName
     *            值域类型短名字
     * @param pModifier
     *            值域包含的修饰符,如果不限制则设置<=0
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 符合的值域,非空
     * @throws IllegalStateException
     *             没有符合条件的值域
     * @see java.lang.reflect.Modifier
     */
    public static ArrayList<Field> getField(Class<?> pClazz,String pFieldTypeShortName,int pModifier,boolean pDeclared){
        ArrayList<Field> tFoundFields=new ArrayList<>();
        do{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                if(sField.getType().getSimpleName().equals(pFieldTypeShortName)&&ClassUtil.includedModifier(sField.getModifiers(),pModifier)){
                    tFoundFields.add(sField);
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tFoundFields.isEmpty()){
            return tFoundFields;
        }
        throw new IllegalStateException(NO_SUCH_FIELD+"(类型短名字: "+pFieldTypeShortName+" ,访问限制符包含的值: "+pModifier+")");
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFieldClazz
     *            值域类型
     * @param pModifier
     *            值域包含的值域修饰符,如果不限制则设置<=0
     * @param pDeclared
     * 
     * @return 符合的值域,非空
     * @throws IllegalStateException
     *             没有符合条件的值域
     * @see java.lang.reflect.Modifier
     */
    public static ArrayList<Field> getField(Class<?> pClazz,Class<?> pFieldClazz,int pModifier,boolean pDeclared){
        ArrayList<Field> tFoundFields=new ArrayList<>();
        do{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                if(sField.getType().equals(pFieldClazz)&&ClassUtil.includedModifier(sField.getModifiers(),pModifier)){
                    tFoundFields.add(sField);
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tFoundFields.isEmpty()){
            return tFoundFields;
        }
        throw new IllegalStateException(NO_SUCH_FIELD+"(值域类型: "+pFieldClazz+" ,访问限制符包含的值: "+pModifier+")");
    }

    /**
     * 获取值域
     * 
     * @param pClazz
     *            类
     * @param pFilter
     *            值域过滤器
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 符合的值域,非空
     * @throws IllegalStateException
     *             没有符合条件的值域
     */
    public static ArrayList<Field> getField(Class<?> pClazz,IFilter<Field> pFilter,boolean pDeclared){
        ArrayList<Field> tFoundFields=new ArrayList<>();
        do{
            Field[] tFields=pClazz.getDeclaredFields();
            for(Field sField : tFields){
                if(pFilter.accept(sField)){
                    tFoundFields.add(sField);
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tFoundFields.isEmpty()){
            return tFoundFields;
        }
        throw new IllegalStateException(NO_SUCH_FIELD+"(值域过滤器类类型:  "+pFilter.getClass().getName()+")");
    }

    // --------====| 获取值域值的方法 |====--------

    /**
     * 获取静态值域的值
     * 
     * @param pField
     *            值域
     * @return 值域的值
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static Object getStaticFieldValue(Field pField){
        return FieldUtil.getFieldValue(pField,(Object)null);
    }

    /**
     * 获取值域的值
     * 
     * @param pField
     *            值域
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @return 值域的值
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static Object getFieldValue(Field pField,Object pObj){
        try{
            pField.setAccessible(true);
            return pField.get(pObj);
        }catch(IllegalArgumentException|IllegalAccessException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

    /**
     * 获取值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @param pFieldName
     *            值域名
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 值域的值
     * @throws IllegalStateException
     *             没有符合条件的值域
     */
    public static Object getFieldValue(Class<?> pClazz,Object pObj,String pFieldName,boolean pDeclared){
        return FieldUtil.getFieldValue(FieldUtil.getField(pClazz,pFieldName,pDeclared),pObj);
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
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 值域的值
     * @throws IllegalStateException
     *             没有符合条件的值域
     */
    public static Object getFieldValue(Class<?> pClazz,Object pObj,String[] pFieldNames,boolean pDeclared){
        return FieldUtil.getFieldValue(FieldUtil.getField(pClazz,pFieldNames,pDeclared),pObj);
    }

    /**
     * 获取值域的值
     * 
     * @param pClazz
     *            类,用于获取值域
     * @param pFilter
     *            值域过滤器
     * @param pObj
     *            要取值的实例,如果方法为静态,可以为null
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @return 符合的值域的值,非空
     * @throws IllegalStateException
     *             反射操作发生异常,或没有符合条件的值域
     */
    public static <T> ArrayList<T> getFieldValue(Class<?> pClazz,IFilter<Field> pFilter,Object pObj,boolean pDeclared){
        ArrayList<T> tFieldValues=new ArrayList<>();
        for(Field sField : FieldUtil.getField(pClazz,pFilter,pDeclared)){
            FieldUtil.getFieldValue(sField,pObj);
        }
        return tFieldValues;
    }

    // --------====| 设置值域值的方法 |====--------

    /**
     * 设置静态值域的值
     * 
     * @param pField
     *            值域
     * @param pValue
     *            要设置成的值
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static void setStaticFieldValue(Field pField,Object pValue){
        FieldUtil.setFieldValue(pField,(Object)null,pValue);
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
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static void setFieldValue(Field pField,Object pObj,Object pValue){
        try{
            pField.setAccessible(true);
            pField.set(pObj,pValue);
        }catch(IllegalArgumentException|IllegalAccessException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
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
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @throws IllegalStateException
     *             反射操作发生异常,或没有符合条件的值域
     */
    public static void setFieldValue(Class<?> pClazz,Object pObj,String pFieldName,Object pValue,boolean pDeclared){
        FieldUtil.setFieldValue(FieldUtil.getField(pClazz,pFieldName,pDeclared),pObj,pValue);
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
     * @param pDeclared
     *            是否只检索该类定义的值域而不检索父类的值域
     * @throws IllegalStateException
     *             反射操作发生异常,或没有符合条件的值域
     */
    public static void setFieldValue(Class<?> pClazz,Object pObj,String[] pFieldNames,Object pValue,boolean pDeclared){
        FieldUtil.setFieldValue(FieldUtil.getField(pClazz,pFieldNames,pDeclared),pObj,pValue);
    }

    /**
     * 设置静态final值域的值
     * 
     * @param pField
     *            值域
     * @param pValue
     *            要设置成的值
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static void setFinalFieldValue(Field pField,Object pValue){
        FieldUtil.setFinalFieldValue(pField,(Object)null,pValue);
    }

    /**
     * 设置final值域的值
     * 
     * @param pField
     *            值域
     * @param pObj
     *            要设置值的实例,如果值域为静态,可以为null
     * @param pValue
     *            要设置成的值
     * @throws IllegalStateException
     *             反射操作发生异常
     */
    public static void setFinalFieldValue(Field pField,Object pObj,Object pValue){
        try{
            pField.setAccessible(true);
            boolean tIsFinal=Modifier.isFinal(pField.getModifiers());
            int tOriginModifer=pField.getModifiers();
            if(tIsFinal){
                setFieldValue(Field.class,pField,"modifiers",(tOriginModifer&(~Modifier.FINAL)),true);
            }
            pField.set(pObj,pValue);
            if(tIsFinal){
                setFieldValue(Field.class,pField,"modifiers",tOriginModifer,true);
            }
        }catch(IllegalArgumentException|IllegalAccessException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }
}
