package com.louie.authcode.engine.core.cut.v2.slash;

import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;
import com.louie.authcode.engine.model.Letter;

/**
 * Created by liuhongyu.louie on 2016/9/1.
 */
public class SlashScan {
    private static int slashLevel = 10;

    public static Letter leftScan(Letter letter, int startWidth){
        int[][] newRGB = letter.getOriginalPicRBG();
        int widthRight = startWidth + 1;
        if (widthRight < newRGB.length) {
            int blackPointCountNow = ScanUtil.blackPointSlashCollector(newRGB, startWidth, slashLevel);
            int blackPointCountRight = ScanUtil.blackPointSlashCollector(newRGB, widthRight, slashLevel);
            if (blackPointCountRight > 0){
                newRGB = ScanUtil.setSlashArrays(newRGB, widthRight, 0, newRGB.length, newRGB[0].length, slashLevel);
                letter.setLetterRGB(newRGB);
                return leftScan(letter, widthRight);
            }
        }
        return letter;
    }

}
