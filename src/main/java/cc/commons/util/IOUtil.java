package cc.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class IOUtil{

    /**
     * 关闭一个流
     * 
     * @param pIPSteam
     *            流
     * @return 是否无报错的关闭了
     */
    public static boolean closeStream(InputStream pIPSteam){
        if(pIPSteam==null)
            return true;

        try{
            pIPSteam.close();
        }catch(IOException exp){
            return false;
        }
        return true;
    }

    /**
     * 关闭一个流
     * 
     * @param pOPSteam
     *            流
     * @return 是否无报错的关闭了
     */
    public static boolean closeStream(OutputStream pOPSteam){
        if(pOPSteam==null)
            return true;

        try{
            pOPSteam.close();
        }catch(IOException exp){
            return false;
        }
        return true;
    }

    /**
     * 关闭一个流
     * 
     * @param pReader
     *            流
     * @return 是否无报错的关闭了
     */
    public static boolean closeStream(Reader pReader){
        if(pReader==null)
            return true;

        try{
            pReader.close();
        }catch(IOException exp){
            return false;
        }
        return true;
    }

    /**
     * 关闭一个流
     * 
     * @param pWriter
     *            流
     * @return 是否无报错的关闭了
     */
    public static boolean closeStream(Writer pWriter){
        if(pWriter==null)
            return true;

        try{
            pWriter.close();
        }catch(IOException exp){
            return false;
        }
        return true;
    }

    /**
     * 复制流中的数据
     * <p>
     * 数据复制完毕后,函数不会主动关闭输入输出流
     * </p>
     * 
     * @param pIPStream
     *            输入流
     * @param pOPStream
     *            输出流
     * @return 复制的字节数
     * @throws IOException
     *             读入或写入数据时发生IO异常
     */
    public static long copy(InputStream pIPStream,OutputStream pOPStream) throws IOException{
        int copyedCount=0,readCount=0;
        byte[] tBuff=new byte[4096];
        while((readCount=pIPStream.read(tBuff))!=-1){
            pOPStream.write(tBuff,0,readCount);
            copyedCount+=readCount;
        }
        return copyedCount;
    }

    /**
     * 将流中的内容全部读取出来,并使用指定编码转换为String
     * 
     * @param pIPStream
     *            输入流
     * @param pEncoding
     *            转换编码
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
     * @throws UnsupportedEncodingException
     */
    public static String readContent(InputStream pIPStream,String pEncoding) throws IOException{
        if(StringUtil.isEmpty(pEncoding)){
            return IOUtil.readContent(new InputStreamReader(pIPStream));
        }else{
            return IOUtil.readContent(new InputStreamReader(pIPStream,pEncoding));
        }
    }

    /**
     * 将流中的内容全部读取出来
     * 
     * @param pIPSReader
     *            输入流
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
     */
    public static String readContent(InputStreamReader pIPSReader) throws IOException{
        int readCount=0;
        char[] tBuff=new char[4096];
        StringBuilder tSB=new StringBuilder();
        while((readCount=pIPSReader.read(tBuff))!=-1){
            tSB.append(tBuff,0,readCount);
        }
        return tSB.toString();
    }

    /**
     * 将流中的内容全部读取出来
     * 
     * @param pIStream
     *            输入流
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
     */
    public static byte[] readData(InputStream pIStream) throws IOException{
        ByteArrayOutputStream tBAOStream=new ByteArrayOutputStream();
        IOUtil.copy(pIStream,tBAOStream);
        return tBAOStream.toByteArray();
    }

}
