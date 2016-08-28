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
            int blackPointCountNow = ScanUtil.blackPointCollertor(srcRGB, startWidth, true);
            int blackPointCountRight = ScanUtil.blackPointCollertor(srcRGB, widthRight, true);
            if (blackPointCountRight <= blackPointCountNow){
                newRGB = ScanUtil.setArrays(srcRGB, widthRight, 0, srcRGB.length, srcRGB[0].length);
                return leftScan(newRGB, widthRight);
            }
        }
        return newRGB;
    }

}
