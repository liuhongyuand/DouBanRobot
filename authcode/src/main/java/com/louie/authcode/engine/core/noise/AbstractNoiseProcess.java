package com.louie.authcode.engine.core.noise;

import java.awt.image.BufferedImage;

/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public abstract class AbstractNoiseProcess {

    public static int[][] imageToRGBArray(BufferedImage image){
        int[][] RGB = new int[image.getWidth()][image.getHeight()];
        for (int width = 0; width < RGB.length; width++) {
            for (int height = 0; height < RGB[0].length; height++) {
                RGB[width][height] = image.getRGB(width, height);
            }
        }
        return RGB;
    }

}
