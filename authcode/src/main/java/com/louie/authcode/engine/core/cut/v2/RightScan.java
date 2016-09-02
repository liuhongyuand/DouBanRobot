package com.louie.authcode.engine.core.cut.v2;

import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class RightScan {

    public static int[][] rightScan(int[][] srcRGB, int startWidth){
        int[][] newRGB = srcRGB;
        int widthLeft = startWidth - 1;
        if (widthLeft > -1) {
            int blackPointCountNow = ScanUtil.blackPointCollector(srcRGB, startWidth, true);
            int blackPointCountLeft = ScanUtil.blackPointCollector(srcRGB, widthLeft, true);
            if (blackPointCountLeft <= blackPointCountNow){
                newRGB = ScanUtil.setArrays(srcRGB, 0, 0, startWidth, srcRGB[0].length);
                return rightScan(newRGB, widthLeft);
            }
        }
        return newRGB;
    }
}
