package com.louie.douban.robot.authcode.engine.core.cut.v2.utils;

import com.louie.douban.model.Letter;
import com.louie.douban.robot.authcode.engine.core.cut.v2.AboveScan;
import com.louie.douban.robot.authcode.engine.core.cut.v2.BelowScan;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class ScanUtil {

    public static int blackPointCollector(int[][] srcRGB, int pos, boolean posIsWidth){
        int blackCounts = 0;
        for (int i = 0; i < (posIsWidth ? srcRGB[0].length : srcRGB.length); i++) {
            if (posIsWidth && srcRGB[pos][i] != -1){
                blackCounts++;
            } else if (!posIsWidth && srcRGB[i][pos] != -1){
                blackCounts++;
            }
        }
        return blackCounts;
    }

    public static int[][] setArrays(int[][] srcRGB, int startWidth, int startHeight, int endWidth, int endHeight){
        int[][] newRGB = new int[endWidth - startWidth][endHeight - startHeight];
        for (int width = startWidth; width < endWidth; width++) {
            for (int height = startHeight; height < endHeight; height++) {
                newRGB[width - startWidth][height - startHeight] = srcRGB[width][height];
            }
        }
        return newRGB;
    }

    public static Letter removeAdditionWhite(Letter letter){
        return BelowScan.belowScan(AboveScan.aboveScan(letter, 0), letter.getLetterRGB()[0].length);
    }

}
