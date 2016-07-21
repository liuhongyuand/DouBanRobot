package com.louie.douban.robot.pic;

import com.louie.douban.util.Parameters;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Process {

    private static final int difRate = 10000000;
    private static final int nearby = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(Process.class);

    public static void processPic(String img) {
        File pic = new File(img);
        try {
            BufferedImage image = ImageIO.read(pic);
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setSize(image.getWidth() + 10, image.getHeight() * 2 + 10);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] newRGB = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    newRGB[i][j] = image.getRGB(i, j);
                    for (int position = 1; position <= nearby; position++) {
                        if (!differRate(image, i, j, width, height, position)) {
                            break;
                        }
                        newRGB[i][j] = -1;
                    }
                }
            }
            for (int i = nearby; i < width - nearby; i++) {
                for (int j = nearby; j < height - nearby; j++) {
                    image.setRGB(i, j, newRGB[i][j]);
                }
            }
            JLabel label = new JLabel("");
            label.setIcon(new ImageIcon(image));
            frame.add(label, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static boolean differRate(BufferedImage image, int x, int y, int width, int height, int position) {
        int StartPointRGB = image.getRGB(x, y);
        int up = 0, down = 0, left = 0, right = 0, leftUp = 0, leftDown = 0, rightUp = 0, rightDown = 0;
        final Map<Integer, Vector<Integer>> RGBCircle = new HashMap<>();
        final Map<Integer, Vector<Integer>> RGBSquare = new HashMap<>();
        RGBCircle.put(StartPointRGB, new Vector<>());
        RGBSquare.put(StartPointRGB, new Vector<>());
        if (x - position >= 0) {
            left = StartPointRGB - image.getRGB(x - position, y);
            RGBCircle.get(StartPointRGB).add(left);
            if (y - position >= 0) {
                leftDown = StartPointRGB - image.getRGB(x - position, y - position);
                RGBCircle.get(StartPointRGB).add(leftDown);
            }
        }
        if (x + position <= width) {
            right = StartPointRGB - image.getRGB(x + position, y);
            RGBCircle.get(StartPointRGB).add(right);
            RGBSquare.get(StartPointRGB).add(right);
            if (y + position <= height) {
                rightUp = StartPointRGB - image.getRGB(x + position, y + position);
                RGBCircle.get(StartPointRGB).add(rightUp);
            }
        }
        if (y - position >= 0) {
            down = StartPointRGB - image.getRGB(x, y - position);
            RGBCircle.get(StartPointRGB).add(down);
            RGBSquare.get(StartPointRGB).add(down);
            if (x + position <= width) {
                rightDown = StartPointRGB - image.getRGB(x + position, y - position);
                RGBCircle.get(StartPointRGB).add(rightDown);
                RGBSquare.get(StartPointRGB).add(rightDown);
            }
        }
        if (y + position <= height) {
            up = StartPointRGB - image.getRGB(x, y + position);
            RGBCircle.get(StartPointRGB).add(up);
            if (x - position >= 0) {
                leftUp = StartPointRGB - image.getRGB(x - position, y + position);
                RGBCircle.get(StartPointRGB).add(leftUp);
            }
        }
    }

//    @Test
//    public void testPic() {
//        processPic();
//    }

    public static void main(String[] args) {
        processPic(Parameters.PATH + "/resources/captcha.jpg");
    }

}
