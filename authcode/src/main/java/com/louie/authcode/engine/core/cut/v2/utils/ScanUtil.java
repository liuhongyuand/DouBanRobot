package com.louie.authcode.engine.core.cut.v2.utils;

import com.louie.authcode.engine.core.cut.v2.AboveScan;
import com.louie.authcode.engine.core.cut.v2.BelowScan;
import com.louie.authcode.engine.core.cut.v2.LeftScan;
import com.louie.authcode.engine.core.cut.v2.RightScan;
import com.louie.authcode.engine.core.noise.MatrixNoiseScan;
import com.louie.authcode.engine.core.noise.NoiseProcessService;
import com.louie.authcode.engine.core.noise.PointNoiseScan;
import com.louie.authcode.engine.model.Letter;

import java.util.HashSet;
import java.util.Set;

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
        boolean isLeftAllWhite = true;
        boolean isRightAllWhite = true;
        boolean isInLetter = false;
        boolean blackPointIsStart = false;
        boolean blackPointIsEnd = false;
        Set<Integer> blackHeight = new HashSet<>();
        for (int height = 0; height < srcRGB[0].length; height++) {
            if (!blackPointIsStart){
                if (srcRGB[width][height] != -1){
                    blackHeight.add(height);
                    blackPointIsStart = true;
                }
            }
            if (blackPointIsStart && !blackPointIsEnd){
                if (srcRGB[width][height] == -1){
                    blackPointIsEnd = true;
                } else {
                    blackHeight.add(height);
                }
            }
            if (blackPointIsStart && blackPointIsEnd) {
                if (srcRGB[width][height] != -1) {
                    return false;
                }
            }
        }
        if (width > 0 && width < srcRGB.length - 1) {
            for (int height = 0; height < srcRGB[0].length; height++) {
                if ((srcRGB[width + 1][height] != -1 && srcRGB[width - 1][height] != -1)){
                    if (blackHeight.contains(height)) {
                        isInLetter = true;
                    }
                }
                if (height == 0){
                    if ((srcRGB[width + 1][height] != -1 || srcRGB[width + 1][height + 1] != -1) && (srcRGB[width - 1][height] != -1 || srcRGB[width - 1][height + 1] != -1)){
                        if (blackHeight.contains(height) || blackHeight.contains(height + 1)){
                            isInLetter = true;
                        }
                    }
                }
                if (height > 1 && height < srcRGB[0].length - 1){
                    if ((srcRGB[width + 1][height - 1] != -1 || srcRGB[width + 1][height - 1] != -1) && (srcRGB[width - 1][height + 1] != -1 || srcRGB[width - 1][height - 1] != -1)){
                        if (blackHeight.contains(height - 1) || blackHeight.contains(height) || blackHeight.contains(height + 1)){
                            isInLetter = true;
                        }
                    }
                }
                if (height == srcRGB[0].length){
                    if ((srcRGB[width + 1][height - 1] != -1 || srcRGB[width + 1][height] != -1) && (srcRGB[width - 1][height - 1] != -1 || srcRGB[width - 1][height] != -1)){
                        if (blackHeight.contains(height) || blackHeight.contains(height - 1)){
                            isInLetter = true;
                        }
                    }
                }
                if (srcRGB[width - 1][height] != -1){
                    isLeftAllWhite = false;
                }
                if (srcRGB[width + 1][height] != -1){
                    isRightAllWhite = false;
                }
            }
            if (isLeftAllWhite || isRightAllWhite){
                isInLetter = true;
            }
            return blackPointIsEnd && isInLetter;
        }
        return blackPointIsEnd;
    }

    public static Letter removeAdditionWhite(Letter letter){
        letter.setLetterRGB(PointNoiseScan.doDenoising(letter.getLetterRGB(), letter.getLetterRGB(), letter.getLetterRGB().length, letter.getLetterRGB()[0].length));
        return BelowScan.belowScan(AboveScan.aboveScan(letter, 0), letter.getLetterRGB()[0].length);
    }

}
