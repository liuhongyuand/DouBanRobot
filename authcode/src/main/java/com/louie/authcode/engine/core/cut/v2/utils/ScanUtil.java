package com.louie.authcode.engine.core.cut.v2.utils;

import com.louie.authcode.engine.core.cut.v2.AboveScan;
import com.louie.authcode.engine.core.cut.v2.BelowScan;
import com.louie.authcode.engine.core.cut.v2.LeftScan;
import com.louie.authcode.engine.core.cut.v2.RightScan;
import com.louie.authcode.engine.model.Letter;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class ScanUtil {
    
    public static int blackPointSlashCollector(int[][] srcRGB, int width, int slashLevel){
        int blackPointCount = 0;
        for (int i = 0; i < srcRGB[0].length; i++) {
            if (i != 0 && i%(srcRGB[0].length/slashLevel) == 0){
                if (srcRGB[--width][i] != -1){
                    blackPointCount++;
                }
            } else {
                if (srcRGB[width][i] != -1){
                    blackPointCount++;
                }
            }
        }
        return blackPointCount;
    }

    public static int[][] setSlashArrays(int[][] srcRGB, int startWidth, int startHeight, int endWidth, int endHeight, int slashLevel){
        int[][] newRGB = new int[endWidth - startWidth + slashLevel][endHeight - startHeight];
        for (int width = startWidth - slashLevel; width < endWidth; width++) {
            int slashStartWidth = startWidth;
            int slashEndWidth = endWidth;
            for (int height = startHeight; height < endHeight; height++) {
                if (height != 0 && height%(srcRGB[0].length/slashLevel) == 0){
                    slashStartWidth--;
                    slashEndWidth--;
                }
                if (width <= slashStartWidth || width > slashEndWidth){
                    newRGB[width - startWidth + slashLevel][height - startHeight] = -1;
                } else {
                    newRGB[width - startWidth + slashLevel][height - startHeight] = srcRGB[width][height];
                }
            }
        }
        newRGB = LeftScan.leftScan(newRGB, 0);
        return RightScan.rightScan(newRGB, newRGB.length - 1);
    }

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

    public static boolean isContinuouslyBlackPoint(int[][] srcRGB, int width){
        boolean blackPointIsStart = false;
        boolean blackPointIsEnd = false;
        for (int height = 0; height < srcRGB[0].length; height++) {
            if (!blackPointIsStart){
                if (srcRGB[width][height] != -1){
                    blackPointIsStart = true;
                    continue;
                }
            }
            if (blackPointIsStart && !blackPointIsEnd){
                if (srcRGB[width][height] == -1){
                    blackPointIsEnd = true;
                    continue;
                }
            }
            if (blackPointIsStart && blackPointIsEnd) {
                if (srcRGB[width][height] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Letter removeAdditionWhite(Letter letter){
        return BelowScan.belowScan(AboveScan.aboveScan(letter, 0), letter.getLetterRGB()[0].length);
    }

}
