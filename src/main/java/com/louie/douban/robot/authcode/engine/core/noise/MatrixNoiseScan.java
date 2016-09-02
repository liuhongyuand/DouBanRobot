package com.louie.douban.robot.authcode.engine.core.noise;

import com.louie.douban.robot.authcode.engine.core.color.ColorProcessService;
import com.louie.douban.robot.authcode.engine.core.utils.PicUtil;

/**
 * Created by liuhongyu.louie on 2016/8/27.
 */
public class MatrixNoiseScan extends AbstractNoiseProcess implements NoiseProcessService {

    private int MatrixWidth = 4;
    private int MatrixHeight = 4;

    @Override
    public int[][] getImageWithoutNoise(int[][] srcRGB, ColorProcessService colorProcessService) {
        while (MatrixWidth < 13) {
            srcRGB = matrixScan(srcRGB);
            MatrixWidth++;
            MatrixHeight++;
        }
        while (MatrixWidth > 4) {
            srcRGB = matrixScan(srcRGB);
            MatrixWidth--;
            MatrixHeight--;
        }
        return srcRGB;
    }

    @Override
    public int[][] getImageWithoutNoise(String image, ColorProcessService colorProcessService) {
        return this.getImageWithoutNoise(PicUtil.getRGBFromImageFile(image), colorProcessService);
    }

    private int[][] matrixScan(int[][] srcRGB){
        int[][] newRGB = srcRGB;
        for (int width = 0; width + MatrixWidth <= srcRGB.length; width++){
            for (int height = 0; height + MatrixHeight <= srcRGB[0].length; height++) {
                boolean isNoiseInCircle = circleCheck(srcRGB, width, width + MatrixWidth, height, height + MatrixHeight);
                if (isNoiseInCircle){
                    newRGB = setMatrixColor(srcRGB, width, width + MatrixWidth, height, height + MatrixHeight, -1);
                }
            }
        }
        return newRGB;
    }

    private int scan(int[][] srcRGB, int widthStart, int widthEnd, int heightStart, int heightEnd){
        int pointCount = 0;
        for (int width = widthStart; width < widthEnd; width++) {
            for (int height = heightStart; height < heightEnd; height++) {
                if (srcRGB[width][height] != -1){
                    pointCount++;
                }
            }
        }
        return pointCount;
    }

    private boolean circleCheck(int[][] srcRGB, int widthStart, int widthEnd, int heightStart, int heightEnd){
        boolean isInnerNoise = false;
        boolean isClearCircle = true;
        for (int width = widthStart; width < widthEnd; width++) {
            for (int height = heightStart; height < heightEnd; height++) {
                if (width != widthStart && width != widthEnd - 1) {
                    if (height != heightStart && height != heightEnd - 1){
                        if (srcRGB[width][height] != -1) {
                            isInnerNoise = true;
                            continue;
                        }
                    }
                }
                if (isNeedCircleCheck(srcRGB, widthStart, widthEnd, heightStart, heightEnd, width, height) && srcRGB[width][height] != -1) {
                    isClearCircle = false;
                    break;
                } else {
                    if (isCircleBoundaryHasNoise(srcRGB, widthStart, widthEnd, heightStart, heightEnd, width, height)){
                        isInnerNoise = true;
                    }
                }
            }
        }
        return isClearCircle && isInnerNoise;
    }

    private boolean isCircleBoundaryHasNoise(int[][] srcRGB, int widthStart, int widthEnd, int heightStart, int heightEnd, int nowWidth, int nowHeight) {
        if (widthStart == 0 && nowWidth == widthStart){
            if (srcRGB[nowWidth][nowHeight] != -1){
                return true;
            }
        } else if (heightStart == 0 && nowHeight == heightStart){
            if (srcRGB[nowWidth][nowHeight] != -1){
                return true;
            }
        } else if (widthEnd == srcRGB.length && nowWidth == widthEnd - 1){
            if (srcRGB[nowWidth][nowHeight] != -1){
                return true;
            }
        } else if (heightEnd == srcRGB[0].length && nowHeight == heightEnd - 1){
            if (srcRGB[nowWidth][nowHeight] != -1){
                return true;
            }
        }
        return false;
    }

    private boolean isNeedCircleCheck(int[][] srcRGB, int widthStart, int widthEnd, int heightStart, int heightEnd, int nowWidth, int nowHeight) {
        if (widthStart == 0 && nowWidth == widthStart){
            return false;
        } else if (heightStart == 0 && nowHeight == heightStart){
            return false;
        } else if (widthEnd == srcRGB.length && nowWidth == widthEnd - 1){
            return false;
        } else if (heightEnd == srcRGB[0].length && nowHeight == heightEnd - 1){
            return false;
        }
        return true;
    }

    private int[][] setMatrixColor(int[][] srcRGB, int widthStart, int widthEnd, int heightStart, int heightEnd, int RGB){
        for (int width = widthStart; width < widthEnd; width++) {
            for (int height = heightStart; height < heightEnd; height++) {
                srcRGB[width][height] = RGB;
            }
        }
        return srcRGB;
    }

}
