package com.louie.douban.robot.authcode.engine.core.cut.v2;

import com.louie.douban.model.Letter;
import com.louie.douban.robot.authcode.engine.core.cut.v2.utils.ScanUtil;

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
            if (blackPointCountRight <= blackPointCountNow){
                newRGB = ScanUtil.setArrays(srcRGB, widthRight, 0, srcRGB.length, srcRGB[0].length);
                return leftScan(newRGB, 0);
            }
        }
        return newRGB;
    }

    public static Letter letterScan(Letter letter, int endWidth){
        int[][] newRGB = letter.getOriginalPicRBG();
        int widthRight = endWidth + 1;
        if (widthRight < newRGB.length){
            double blackPointCountRight = ScanUtil.blackPointCollector(newRGB, widthRight, true);
            //TODO: 字符拆分前做连接判断，防止单个字母被拆开
            if (!(widthRight > 15 && !ScanUtil.isContinuouslyBlackPoint(newRGB, widthRight) && (blackPointCountRight / (double) newRGB[0].length < 0.19))){
                newRGB = ScanUtil.setArrays(newRGB, 0, 0, widthRight, newRGB[0].length);
                letter.setLetterRGB(newRGB);
                return letterScan(letter, widthRight);
            }
        }
        return letter;
    }

}
