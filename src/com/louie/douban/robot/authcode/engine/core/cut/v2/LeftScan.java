package com.louie.douban.robot.authcode.engine.core.cut.v2;

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

    public static void letterScan(int[][] srcRGB, int startWidth){
        int[][] newRGB = srcRGB;
        int widthRight = startWidth + 1;
        if (widthRight < srcRGB.length){
            int blackPointCountNow = ScanUtil.blackPointCollector(srcRGB, startWidth, true);
            int blackPointCountRight = ScanUtil.blackPointCollector(srcRGB, widthRight, true);
            //// TODO: 2016/8/30 根据每个图横向像素个数来判断字母的间隔。
        }
    }

}
