package com.louie.douban.robot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class CodeImport extends AbstractPicProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeImport.class);

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
                BufferedImage bufferImg = new BufferedImage(letter.getWidth(), letter.getHeight(), BufferedImage.TYPE_INT_BGR);
                denoising(bufferImg, letter.getLetterRGB(), letter.getWidth(), letter.getHeight());
                List<Point> points = new LinkedList<>();
                for (int i = 0 ; i < letter.getWidth(); i++){
                    for(int j = 0; j < letter.getHeight(); j++){
                        if (letter.getLetterRGB()[i][j] != -1 && letter.getLetterRGB()[i][j] != 0){
                            points.add(new Point(i, j));
                        }
                    }
                }
                letters.add(points);
                buffers.add(setBufferedImage(bufferImg, letter.getLetterRGB()));
            }));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new Object[]{letters, buffers};
    }
}
