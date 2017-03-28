package cc.commons.util;

import java.nio.charset.Charset;
import java.util.Random;

public class ToolKit{

    public static final Charset UTF_8=Charset.forName("UTF-8");

    protected static final String systemLineSeparator=System.getProperty("line.separator","\r\n");

    /**
     * 打印当前位置的堆栈
     */
    public static void printStackTrace(){
        StackTraceElement[] tElements=Thread.currentThread().getStackTrace();
        for(int i=2;i<tElements.length;i++){
            System.out.println(tElements[i]);
        }
    }

    /**
     * 获取忽略大小写的Enum值
     * 
     * @param pValues
     *            该Enum的所有制
     * @param pEnumName
     *            Enum的名字
     * @return 符合添加的值或null
     */
    public static <T extends Enum<T>> T getElement(T[] pValues,String pEnumName){
        for(T sT : pValues){
            if(sT.name().equalsIgnoreCase(pEnumName))
                return sT;
        }
        return null;
    }

    /***
     * 生成长度的随机字节数组
     * 
     * @param pLength
     *            长度,>0
     * @return 生成的随机字节
     */
    public static byte[] randomByteArray(int pLength){
        if(pLength<=0)
            throw new IllegalArgumentException("The number must be positive ("+pLength+")");

        byte[] tData=new byte[pLength];
        Random tRandom=new Random(System.nanoTime());
        for(int i=0;i<pLength;i++){
            tData[i]=(byte)tRandom.nextInt(256);
        }
        return tData;
    }

    /**
     * 取不超过范围的值
     * 
     * @param pMin
     *            最小值
     * @param pMax
     *            最大值
     * @param pValue
     *            当前输入值
     * @return 调整后的值
     */
    public static int between(int pMin,int pMax,int pValue){
        return Math.max(pMin,Math.min(pMax,pValue));
    }

    /**
     * 转换字符串为数值
     * <p>
     * 首次转换失败将尝试调出字符串中的合法字符进行再次转换,再次转换失败,则返回默认值
     * </p>
     * 
     * @param pStr
     *            数值字符串
     * @param pDefValue
     *            如果转换失败返回的默认值
     * @return 数字
     */
    public static int paseIntOrDefault(String pStr,int pDefValue){
        return paseIntOrDefault(pStr,10,pDefValue);
    }

    /**
     * 转换字符串为数值
     * <p>
     * 首次转换失败将尝试调出字符串中的合法字符进行再次转换,再次转换失败,则返回默认值
     * </p>
     * 
     * @param pStr
     *            数值字符串
     * @param pRadix
     *            转换进制
     * @param pDefValue
     *            如果转换失败返回的默认值
     * @return 数字
     */
    public static int paseIntOrDefault(String pStr,int pRadix,int pDefValue){
        if(StringUtil.isEmpty(pStr))
            return pDefValue;

        try{
            return Integer.parseInt(pStr,pRadix);
        }catch(NumberFormatException exp){
            int tMaxCharValue=pRadix+'0';
            StringBuilder tSB=new StringBuilder();
            pStr=pStr.toLowerCase();
            for(int i=0;i<pStr.length();i++){
                char c=pStr.charAt(i);
                if(c>='0'&&c<tMaxCharValue){
                    tSB.append(c);
                }
            }
            try{
                return Integer.parseInt(tSB.toString(),pRadix);
            }catch(NumberFormatException expc){
                return pDefValue;
            }
        }
    }
}
