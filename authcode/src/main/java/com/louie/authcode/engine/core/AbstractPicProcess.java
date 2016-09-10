package com.louie.authcode.engine.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public abstract class AbstractPicProcess {

    protected static boolean isCorrectImageFormat(String image){
        File file = new File(image);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if(bufferedImage == null) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static BufferedImage setBufferedImage(BufferedImage img, int[][] RGB){
        for (int i = 0; i < RGB.length; i++) {
            for (int j = 0; j < RGB[0].length; j++) {
                img.setRGB(i, j, RGB[i][j]);
            }
        }
        return img;
    }

}
