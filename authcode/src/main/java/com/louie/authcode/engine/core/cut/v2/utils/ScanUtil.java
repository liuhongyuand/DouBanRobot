package com.louie.authcode.engine.core.cut.v2.utils;

import com.louie.authcode.engine.core.cut.v2.AboveScan;
import com.louie.authcode.engine.core.cut.v2.BelowScan;
import com.louie.authcode.engine.core.cut.v2.LeftScan;
import com.louie.authcode.engine.core.cut.v2.RightScan;
import com.louie.authcode.engine.core.noise.MatrixNoiseScan;
import com.louie.authcode.engine.core.noise.NoiseProcessService;
import com.louie.authcode.engine.core.noise.PointNoiseScan;
import com.louie.authcode.engine.model.Letter;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

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

    @Deprecated
    /**
     * Replaced by {@link ScanUtil.isNeedDivide(int[][] srcRGB, int width)};
     */
    public static boolean isContinuouslyBlackPoint(int[][] srcRGB, int width){
        boolean isLeftAllWhite = true;
        boolean isRightAllWhite = true;
        boolean isInLetter = false;
        boolean isOtherBlackPointInLetter = false;
        boolean blackPointIsStart = false;
        boolean blackPointIsEnd = false;
        Set<Integer> continuouslyBlackHeight = new HashSet<>();
        Set<Integer> otherBlackHeight = new HashSet<>();
        for (int height = 0; height < srcRGB[0].length; height++) {
            if (!blackPointIsStart){
                if (srcRGB[width][height] != -1){
                    continuouslyBlackHeight.add(height);
                    blackPointIsStart = true;
                }
            }
            if (blackPointIsStart && !blackPointIsEnd){
                if (srcRGB[width][height] == -1){
                    blackPointIsEnd = true;
                } else {
                    continuouslyBlackHeight.add(height);
                }
            }
            if (blackPointIsStart && blackPointIsEnd) {
                if (srcRGB[width][height] != -1) {
                    otherBlackHeight.add(height);
                    continuouslyBlackHeight.add(height);
                }
            }
        }
        if (width > 0 && width < srcRGB.length - 1) {
            for (int height = 0; height < srcRGB[0].length; height++) {
                if ((srcRGB[width + 1][height] != -1 && srcRGB[width - 1][height] != -1)){
                    if (continuouslyBlackHeight.contains(height)) {
                        isInLetter = true;
                    }
                }
                if (height == 0){
                    if ((srcRGB[width + 1][height] != -1 || srcRGB[width + 1][height + 1] != -1) && (srcRGB[width - 1][height] != -1 || srcRGB[width - 1][height + 1] != -1)){
                        if (continuouslyBlackHeight.contains(height) || continuouslyBlackHeight.contains(height + 1)){
                            isInLetter = true;
                        }
                    }
                }
                if (height > 1 && height < srcRGB[0].length - 1){
                    if ((srcRGB[width + 1][height + 1] != -1 || srcRGB[width + 1][height - 1] != -1) && (srcRGB[width - 1][height + 1] != -1 || srcRGB[width - 1][height - 1] != -1)){
                        if (continuouslyBlackHeight.contains(height - 1) || continuouslyBlackHeight.contains(height) || continuouslyBlackHeight.contains(height + 1)){
                            isInLetter = true;
                        }
                    }
                }
                if (height == srcRGB[0].length){
                    if ((srcRGB[width + 1][height - 1] != -1 || srcRGB[width + 1][height] != -1) && (srcRGB[width - 1][height - 1] != -1 || srcRGB[width - 1][height] != -1)){
                        if (continuouslyBlackHeight.contains(height) || continuouslyBlackHeight.contains(height - 1)){
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
//                if (isOtherBlackPointInLetter)
            }
            if (isLeftAllWhite || isRightAllWhite){
                isInLetter = true;
            }
            return blackPointIsEnd && isInLetter;
        }
        return blackPointIsEnd;
    }

    /**
     *  isNextLineHasNearBlackPoint[] :
     *  0:is right line has near black point.
     *  1:is right line has other near black point.
     *  2:is left line has near black point.
     *  3:is left line has other near black point.
     *  4:is right line has continuously black point.
     *  5:is left line has continuously black point.
     *  6:is this line has continuously black point.
     *  7:is right line has other black point near this line continuously black point.
     *  8:is left line has other black point near this line continuously black point.
     *  9:is right line has continuously black point near this line other black point.
     * 10:is left line has continuously black point near this line other black point.
     * @param srcRGB
     * @param width
     * @return
     */
    public static boolean isNeedDivide(int[][] srcRGB, int width){
        if (width == srcRGB.length){
            return true;
        }
        if (width > srcRGB.length - 2){
            return true;
        }
        int blackPointStart = -1;
        int blackPointStartLeft = -1;
        int blackPointStartRight = -1;
        boolean[] isNextLineHasNearBlackPoint = {false, false, false, false, false, false, false, false, false, false, false};
        Set<Integer> continuouslyBlackPointHeight = new HashSet<>();
        Set<Integer> otherBlackPointHeight = new HashSet<>();
        Set<Integer> continuouslyBlackPointHeightLeft = new HashSet<>();
        Set<Integer> otherBlackPointHeightLeft = new HashSet<>();
        Set<Integer> continuouslyBlackPointHeightRight = new HashSet<>();
        Set<Integer> otherBlackPointHeightRight = new HashSet<>();
        for (int height = 0; height < srcRGB[0].length; height++) {
            blackPointStartLeft = blackPointCollector(srcRGB, width - 1, height, blackPointStartLeft, continuouslyBlackPointHeightLeft, otherBlackPointHeightLeft);
            blackPointStart = blackPointCollector(srcRGB, width, height, blackPointStart, continuouslyBlackPointHeight, otherBlackPointHeight);
            blackPointStartRight = blackPointCollector(srcRGB, width + 1, height, blackPointStartRight, continuouslyBlackPointHeightRight, otherBlackPointHeightRight);
        }
        if (continuouslyBlackPointHeight.size() == 0){
            return true;
        }
        if (otherBlackPointHeight.size() == 0){
            isNextLineHasNearBlackPoint[6] = true;
        }
        if (otherBlackPointHeightLeft.size() == 0){
            isNextLineHasNearBlackPoint[5] = true;
        }
        if (otherBlackPointHeightRight.size() == 0){
            isNextLineHasNearBlackPoint[4] = true;
        }
        continuouslyBlackPointHeight.forEach((point) -> {
            if (hasBlackPointNearby(point, continuouslyBlackPointHeightRight)){
                isNextLineHasNearBlackPoint[0] = true;
            }
            if (hasBlackPointNearby(point, continuouslyBlackPointHeightLeft)){
                isNextLineHasNearBlackPoint[2] = true;
            }
            if (hasBlackPointNearby(point, otherBlackPointHeightRight)){
                isNextLineHasNearBlackPoint[7] = true;
            }
            if (hasBlackPointNearby(point, otherBlackPointHeightLeft)){
                isNextLineHasNearBlackPoint[8] = true;
            }
        });
        otherBlackPointHeight.forEach((point) -> {
            if (hasBlackPointNearby(point, otherBlackPointHeightRight)){
                isNextLineHasNearBlackPoint[1] = true;
            }
            if (hasBlackPointNearby(point, otherBlackPointHeightLeft)){
                isNextLineHasNearBlackPoint[3] = true;
            }
            if (hasBlackPointNearby(point, continuouslyBlackPointHeightRight)){
                isNextLineHasNearBlackPoint[9] = true;
            }
            if (hasBlackPointNearby(point, continuouslyBlackPointHeightLeft)){
                isNextLineHasNearBlackPoint[10] = true;
            }
        });
        if (continuouslyBlackPointHeightRight.size() < 2 && isNextLineHasNearBlackPoint[5] && !isNextLineHasNearBlackPoint[6] && !isNextLineHasNearBlackPoint[10]){
            return true;
        }
        if (isNextLineHasNearBlackPoint[4] && isNextLineHasNearBlackPoint[5] && !isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[1]){
            return true;
        }
        if (isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[3] && isNextLineHasNearBlackPoint[4]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[1] && isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[3]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[5] && isNextLineHasNearBlackPoint[9]){
            return false;
        }
        if((double)continuouslyBlackPointHeight.size()/(double) srcRGB[0].length > 0.35){
            return false;
        }
        if (isNextLineHasNearBlackPoint[5] && isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[0]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[0]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[2] && continuouslyBlackPointHeightRight.size() == 0){
            return false;
        }
        if (isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[4] && !isNextLineHasNearBlackPoint[0]){
            return true;
        }
        if (isNextLineHasNearBlackPoint[0] && isNextLineHasNearBlackPoint[1] && isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[3]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[0] && isNextLineHasNearBlackPoint[2]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[4] && isNextLineHasNearBlackPoint[0]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[2]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[6] && isNextLineHasNearBlackPoint[0]){
            return false;
        }
        if (isNextLineHasNearBlackPoint[2] && isNextLineHasNearBlackPoint[1] && isNextLineHasNearBlackPoint[3]){
            return false;
        }
        return true;
    }

    private static boolean hasBlackPointNearby(int height, Set<Integer> BlackPointHeightRight){
        boolean[] hasNearPoint = {false};
        BlackPointHeightRight.forEach((point) -> {
            if (height == point || height == point + 1 || height == point - 1){
                hasNearPoint[0] = true;
            }
        });
        return hasNearPoint[0];
    }

    private static int blackPointCollector(int[][] srcRGB, int width, int height, int lastBlackPointPos, Set<Integer> continuouslyBlackPointHeight, Set<Integer> otherBlackPointHeight){
        if (srcRGB[width][height] != -1){
            if (lastBlackPointPos == -1){
                lastBlackPointPos = height;
                continuouslyBlackPointHeight.add(height);
            } else {
                if (lastBlackPointPos + 1 == height) {
                    lastBlackPointPos = height;
                    continuouslyBlackPointHeight.add(height);
                } else {
                    otherBlackPointHeight.add(height);
                }
            }
        }
        return lastBlackPointPos;
    }

    public static boolean isNeedRemoved(int[][] srcRGB){
        double whitePointCount = 0.0;
        if (srcRGB.length < 5 || srcRGB[0].length < 15){
            return true;
        }
        for (int[] aSrcRGB : srcRGB) {
            for (int height = 0; height < srcRGB[0].length; height++) {
                if (aSrcRGB[height] == -1) {
                    whitePointCount++;
                }
            }
        }
        if (whitePointCount / (double) (srcRGB.length * srcRGB[0].length) < 0.329999){
            return true;
        }
        return false;
    }

    public static Letter removeAdditionWhite(Letter letter){
        MatrixNoiseScan noiseProcessService = new MatrixNoiseScan();
        letter.setLetterRGB(PointNoiseScan.doDenoising(letter.getLetterRGB(), letter.getLetterRGB(), letter.getLetterRGB().length, letter.getLetterRGB()[0].length));
        //// TODO: 2016/9/7 matrix scan after letter divided
        return BelowScan.belowScan(AboveScan.aboveScan(letter, 0), letter.getLetterRGB()[0].length);
    }

}
