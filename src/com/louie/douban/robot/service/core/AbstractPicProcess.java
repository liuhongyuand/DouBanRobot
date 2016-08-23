package com.louie.douban.robot.service.core;

import java.awt.image.BufferedImage;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public abstract class AbstractPicProcess {

    BufferedImage setBufferedImage(BufferedImage img, int[][] RGB){
        for (int i = 0; i < RGB.length; i++) {
            for (int j = 0; j < RGB[0].length; j++) {
                img.setRGB(i, j, RGB[i][j]);
            }
        }
        return img;
    }

}
