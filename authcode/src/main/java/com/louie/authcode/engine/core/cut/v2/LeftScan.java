package com.louie.authcode.engine.core.cut.v2;

import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;
import com.louie.authcode.engine.model.Letter;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class LeftScan {

    public static int[][] leftScan(int[][] srcRGB, int startWidth){
        int[][] newRGB = srcRGB;
        int widthRight = startWidth + 1;
        if (widthRight < srcRGB.length) {
            int blackPointCountNow = ScanUtil.blackPointCollector(srcRGB, startWidth, true);
            int blackPointCountRight = ScanUtil.blackPointCollector(srcRGB, widthRight, true);
            newRGB = ScanUtil.setArrays(srcRGB, widthRight, 0, srcRGB.length, srcRGB[0].length);
            if (blackPointCountRight <= blackPointCountNow){
                return leftScan(newRGB, 0);
            }
        }
        return newRGB;
    }

    public static Letter letterScan(Letter letter, int endWidth){
        int[][] newRGB = letter.getOriginalPicRBG();
        int widthRight = endWidth + 1;
        if (widthRight < newRGB.length){
            int black = ScanUtil.blackPointCollector(newRGB, widthRight, true);
            if (!(widthRight > 7 && ScanUtil.isNeedDivide(newRGB, widthRight))){
//              if (black > 0){
                newRGB = ScanUtil.setArrays(newRGB, 0, 0, widthRight, newRGB[0].length);
                letter.setLetterRGB(newRGB);
                return letterScan(letter, widthRight);
            }
        } else {
            newRGB = ScanUtil.setArrays(newRGB, 0, 0, newRGB.length, newRGB[0].length);
            letter.setLetterRGB(newRGB);
        }
        return letter;
    }

}
