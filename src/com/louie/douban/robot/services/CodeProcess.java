package com.louie.douban.robot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class CodeProcess extends AbstractPicProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeProcess.class);

    @Override
    public Object[] process(String img) {
        final List<List<Point>> letters = new LinkedList<>();
        File pic = new File(img);
        try {
            BufferedImage image = ImageIO.read(pic);
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] newRGB;
            newRGB = removeColor(image, width, height);
            for (int i = 0; i < 2; i++) {
                denoising(image, newRGB, width, height);
            }
            divideToLetter(newRGB, image.getHeight(), image.getWidth());
            LETTERS.forEach((letter -> {
                List<Point> points = new LinkedList<>();
                for (int i = 0 ; i < letter.getWidth(); i++){
                    for(int j = 0; j < letter.getHeight(); j++){
                        if (letter.getLetterRGB()[i][j] != -1 && letter.getLetterRGB()[i][j] != 0){
                            points.add(new Point(i, j));
                        }
                    }
                }
                letters.add(points);
            }));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new Object[]{letters};
    }

}