package cc.commons.util.charset;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cc.commons.util.IOUtil;
import cc.commons.util.charset.impl.CharsetCalc_GBK;
import cc.commons.util.charset.impl.CharsetCalc_UTF8;
import cc.commons.util.charset.impl.CharsetCalc_Unicode;

public class CharsetUtil{

    public static final Charset UTF8=Charset.forName("UTF-8");
    public static final Charset GBK=Charset.forName("GBK");
    /** 其他名字: UCS-2, UTF-16 */
    public static final Charset Unicode=Charset.forName("Unicode");
    /**
     * 所有的编码计算器实例
     * <p>
     * 由于GBK与BIG5编码范围重叠关系,默认未添加BIG5的Provider,如有需要,请自行添加
     * </p>
     */
    public static final HashMap<Charset,ICharsetCalc> mProviders=new HashMap<>();

    /** 内容判断的最大长度 */
    private static int mMaxJudgeLen=1024*1024;

    static{
        addProvider(new CharsetCalc_UTF8());
        addProvider(new CharsetCalc_GBK());
        addProvider(new CharsetCalc_Unicode());
    }

    /**
     * 设置编码的最大判断字节长度
     * 
     * @param pLength
     *            长度,至少512k
     * @return 之前的最大长度
     */
    public static int setMaxJudgeLen(int pLength){
        if(pLength>=512*1024){
            CharsetUtil.mMaxJudgeLen=pLength;
        }
        return CharsetUtil.mMaxJudgeLen;
    }

    /**
     * 添加编码概率计算器
     * 
     * @param pSet
     *            绑定的编码
     * @param pProvider
     *            计算器
     * @param pWeight
     *            权重
     * @return 被替换的计算器
     */
    public static ICharsetCalc addProvider(ICharsetCalc pProvider){
        return CharsetUtil.mProviders.put(pProvider.getCharset(),pProvider);
    }

    /**
     * 移除编码概率计算器
     * 
     * @param pSet
     *            绑定的编码
     * @return 移除的计算器
     */
    public static ICharsetCalc removeProvider(Charset pSet){
        return CharsetUtil.mProviders.remove(pSet);
    }

    /**
     * 获取所有的编码概率计算器
     * 
     * @return 编码概率计算器集合,不可编辑
     */
    public static Map<Charset,ICharsetCalc> providers(){
        return Collections.unmodifiableMap(CharsetUtil.mProviders);
    }

    /**
     * 获取文件内容编码
     * <p>
     * 返回识别率达到95%以上的编码,如果识别率均未达到该值,将尝试比较 系统默认编码<br>
     * 如果系统编码未计算过概率,将返回系统编码,不然就返回 概率最高的编码
     * </p>
     * 
     * @param pFile
     *            计算编码的文件
     * @return 文件编码
     * @throws FileNotFoundException
     *             文件不存在
     * @throws IOException
     *             文件读取过程发生异常
     */
    public static Charset getCharset(File pFile) throws FileNotFoundException,IOException{
        FileInputStream tFIStream=null;
        try{
            tFIStream=new FileInputStream(pFile);
            return CharsetUtil.getCharset(tFIStream);
        }finally{
            IOUtil.closeStream(tFIStream);
        }
    }

    /**
     * 获取流内容编码
     * <p>
     * 返回识别率达到95%以上的编码,如果识别率均未达到该值,将尝试比较 系统默认编码<br>
     * 如果系统编码未计算过概率,将返回系统编码,不然就返回 概率最高的编码
     * </p>
     * 
     * @param pIStream
     *            计算编码的流
     * @return 流内容编码
     * @throws IOException
     *             流读取过程发生异常
     */
    public static Charset getCharset(InputStream pIStream) throws IOException{
        int tBuffSize=4096,tTotalReadCount=0,tLastReadCount=-1;
        byte[] tBuff=new byte[tBuffSize];
        ByteArrayOutputStream tBAOStream=new ByteArrayOutputStream();
        while((tLastReadCount=pIStream.read(tBuff))!=-1){
            tBAOStream.write(tBuff,0,tLastReadCount);
            tTotalReadCount+=tLastReadCount;
            if(tTotalReadCount>=CharsetUtil.mMaxJudgeLen)
                break;
        }
        return getCharset(tBAOStream.toByteArray());
    }

    public static Charset getCharset(byte[] pData){
        return CharsetUtil.getCharset(pData,0,pData.length);
    }

    public static Charset getCharset(byte[] pData,int pOffset,int pLength){
        if(CharsetUtil.mProviders.isEmpty())
            return Charset.defaultCharset();

        pLength=Math.min(CharsetUtil.mMaxJudgeLen,Math.max(pLength,0));
        double tDefChance=-1D,tMaxChance=0D;
        ICharsetCalc tMaxChanceSet=null;
        for(ICharsetCalc sCalc : CharsetUtil.mProviders.values()){
            double tChance=sCalc.getChance(pData,pOffset,pLength);
            if(tChance>tMaxChance||(tChance==tMaxChance&&(tMaxChanceSet!=null&&tMaxChanceSet.getWeight()<sCalc.getWeight()))){
                tMaxChance=tChance;
                tMaxChanceSet=sCalc;
            }

            if(sCalc.getCharset()==Charset.defaultCharset()){
                tDefChance=tChance;
            }
        }

        return (tMaxChance<0.95D&&tDefChance<0D)?Charset.defaultCharset():tMaxChanceSet.getCharset();
    }
}
