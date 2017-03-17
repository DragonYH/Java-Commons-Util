package cc.commons.util.charset.impl;

import java.nio.charset.Charset;

import cc.commons.util.charset.ICharsetCalc;

public class CharsetCalc_GBK implements ICharsetCalc{

    private static Charset mCharset=Charset.forName("GBK");
    
    @Override
    public double getChance(byte[] pData,int pOffset,int tMaxLen){
        int tActualLen=Math.min(pData.length,pOffset+tMaxLen);
        int tCatchByte=0,tHandleByte=tActualLen-pOffset;
        for(int i=pOffset;i<tActualLen;i++){
            int tb2iHead=pData[i]&0xFF;
            if(tb2iHead==0){
                tHandleByte--;
                continue;
            }

            if(tb2iHead<0x7F){ // ANSI
                tCatchByte++;
                continue;
            }

            if(0x8E<=tb2iHead&&tb2iHead<=0xFE){
                if(i+2>tActualLen){ //减去不完整的字符字节数量
                    tHandleByte--;
                    break;
                }
                int tb2iLast=pData[i+1]&0xFF;

                if(0x40<=tb2iLast&&tb2iLast<=0xFE&&tb2iLast!=0x7F){
                    i++;
                    tCatchByte+=2;
                }
            }

        }

        return tHandleByte==0?0:tCatchByte*1.0/tHandleByte;
    }

    @Override
    public Charset getCharset(){
        return mCharset;
    }

    @Override
    public int getWeight(){
        return 500;
    }

}

