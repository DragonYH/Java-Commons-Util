package cc.commons.util.charset.impl;

import java.nio.charset.Charset;

import cc.commons.util.charset.ICharsetCalc;

public class CharsetCalc_Unicode implements ICharsetCalc{

    private static Charset mCharset=Charset.forName("Unicode");

    @Override
    public double getChance(byte[] pData,int pOffset,int tMaxLen){
        if(pData.length>=pOffset+2){
            if(0xFF==(pData[pOffset]&0xFF)&&0xFE==(pData[pOffset+1]&0xFF)){ // 小端
                return 1D;
            }
            if(0xFE==(pData[pOffset]&0xFF)&&0xFF==(pData[pOffset+1]&0xFF)){ // 大端
                return 1D;
            }
        }
        return 0D;
    }

    @Override
    public Charset getCharset(){
        return mCharset;
    }

    @Override
    public int getWeight(){
        return 1100;
    }

}
