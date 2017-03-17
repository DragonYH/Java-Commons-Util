package cc.commons.util.charset.impl;

import java.nio.charset.Charset;

import cc.commons.util.charset.ICharsetCalc;

public class CharsetCalc_UTF8 implements ICharsetCalc{

    private static Charset mCharset=Charset.forName("utf-8");

    @Override
    public double getChance(byte[] pData,int pOffset,int tMaxLen){
        if(pData.length>=pOffset+3){
            if(0xFF==(pData[pOffset]&0xFF)&&0xBB==(pData[pOffset+1]&0xFF)&&0xBF==(pData[pOffset+2]&0xFF)){
                return 1D;
            }
        }

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
            }else if((tb2iHead&0xC0)==0x80){// 10xxxxxx
                continue;
            }

            int tCharLen,k;
            for(tCharLen=2;tCharLen<=6;tCharLen++){
                if((0xFF>>(7-tCharLen))-1==tb2iHead>>(7-tCharLen)){
                    if(i+tCharLen>tActualLen){ //减去不完整的字符字节数量
                        tHandleByte-=tActualLen-i;
                        i=tActualLen;
                        break;
                    }

                    for(k=1;k<tCharLen;k++){
                        int b2i=pData[i+k]&0xFF;
                        if(!(0x80<=b2i&&b2i<=0xBF))
                            break;
                    }
                    if(k>=tCharLen){
                        i+=tCharLen-1;
                        tCatchByte+=tCharLen;
                    }
                    break;
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
        return 1000;
    }

}
