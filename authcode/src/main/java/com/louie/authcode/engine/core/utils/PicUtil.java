package com.louie.authcode.engine.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.louie.authcode.engine.core.noise.AbstractNoiseProcess.imageToRGBArray;

/**
 * Created by liuhongyu.louie on 2016/8/26.
 */
public class PicUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicUtil.class);

    /**
     *
     * @param RGB
     * @param pos
     * @param length
     * @param isVertical height = vertical
     * @return
     */
    public static int getWhitePointCount(int[][] RGB, int pos, int length, boolean isVertical){
        int whiteCount = 0;
        for (int i = 0; i < length; i++) {
            if (isVertical){
                if (RGB[i][pos] == -1) {
                    whiteCount++;
                }
            }else {
                if (RGB[pos][i] == -1) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    public static int[][] setColor(int[][] RGB, int pos, int length, int RGBColor, boolean isVertical){
        for (int i = 0; i < length; i++) {
            if (isVertical){
                RGB[i][pos] = RGBColor;
            }else {
                RGB[pos][i] = RGBColor;
            }
        }
        return RGB;
    }

    public static int[][] getRGBFromImageFile(String image){
        int[][] srcRGB = new int[0][];
        try {
            File pic = new File(image);
            BufferedImage bufferedImage = ImageIO.read(pic);
            srcRGB = imageToRGBArray(bufferedImage);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return srcRGB;
    }

}
