package cc.commons.util.charset;

import java.nio.charset.Charset;

public interface ICharsetCalc{

    /**
     * 计算数据为指定编码的概率
     * 
     * @param pData
     *            数据
     * @param pOffset
     *            从哪个数据开始计算
     * @param tMaxLen
     *            最多的计算数据量,不满一个编码字符的数据可以不加入概率计算
     * @return 返回0-1
     */
    public double getChance(byte[] pData,int pOffset,int tMaxLen);

    /**
     * 获取此编码计算器绑定的编码
     * 
     * @return 编码
     */
    public Charset getCharset();

    /**
     * 获取此编码计算器的权重,如果两个计算器的{@link #getChance(byte[], int, int)}返回值相同,将取权重较大的
     * <p>
     * 以下部分默认权重:<br>
     * Unicode 1100 使用头判定,不判断内容<br>
     * UTF8 1000,可能包括纯英文<br>
     * Big5 600,可能包括UTF8,需要手动添加Provider<br>
     * GBk 500,可能包括UTF8,完全包括GB2312,BIG5<br>
     * </p>
     * 
     * @return 权重
     */
    public int getWeight();

}
