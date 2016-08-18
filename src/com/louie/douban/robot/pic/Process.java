package com.louie.douban.robot.pic;

import com.louie.douban.model.Letter;
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
import java.util.*;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Process {

    private static final int target = 16711423;  //黑色
    private static final int difRate = 8000000/4;  //色值偏移
    private static final int nearby = 1;  //像素位
    private static final int PIC_NUM = 220; //分割图片数量
    private static final int LETTER_WIDTH = 20; //字母宽度
    private static final Set<Letter> LETTERS = new HashSet<>();
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
            int[][] newRGB;
            newRGB = removeColor(image, width, height);
            newRGB = doDenoising(image, newRGB, width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    image.setRGB(i, j, newRGB[i][j]);
                }
            }
            divideToLetter(newRGB, image.getHeight(), image.getWidth());
            JLabel label = new JLabel("");
            label.setIcon(new ImageIcon(image));
            frame.add(label, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static int[][] removeColor(BufferedImage image, int width, int height){
        int[][] newRGB = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int nowPoint = image.getRGB(i, j) < 0 ? image.getRGB(i, j) * -1 : image.getRGB(i, j);
                if (target - nowPoint < difRate || nowPoint - target > difRate ) {
                    newRGB[i][j] = image.getRGB(i, j);
                }else {
                    newRGB[i][j] = -1;
                }
            }
        }
        return newRGB;
    }

    private static int[][] doDenoising(BufferedImage image, int[][] newRGB, int width, int height){
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                for (int position = 1; position <= nearby; position++) {
                    if (differRate(image, i, j, width, height, nearby)) {
                        newRGB[i][j] = -1;
                    }
                }
            }
        }
        return newRGB;
    }

    private static boolean differRate(BufferedImage image, int x, int y, int width, int height, int position) {
        int StartPointRGB = image.getRGB(x, y);
        final int[] isDiff = {0};
        int up, down, left, right, leftUp, leftDown, rightUp, rightDown;
        final Map<Integer, Vector<Integer>> RGBCircle = new HashMap<>();
        RGBCircle.put(StartPointRGB, new Vector<>());
        if (x - position > 0) {
            left = StartPointRGB - image.getRGB(x - position, y);
            RGBCircle.get(StartPointRGB).add(left);
            if (y - position > 0) {
                leftDown = StartPointRGB - image.getRGB(x - position, y - position);
                RGBCircle.get(StartPointRGB).add(leftDown);
            }
        }
        if (x + position < width) {
            right = StartPointRGB - image.getRGB(x + position, y);
            RGBCircle.get(StartPointRGB).add(right);
            if (y + position < height) {
                rightUp = StartPointRGB - image.getRGB(x + position, y + position);
                RGBCircle.get(StartPointRGB).add(rightUp);
            }
        }
        if (y - position >= 0) {
            down = StartPointRGB - image.getRGB(x, y - position);
            RGBCircle.get(StartPointRGB).add(down);
            if (x + position < width) {
                rightDown = StartPointRGB - image.getRGB(x + position, y - position);
                RGBCircle.get(StartPointRGB).add(rightDown);
            }
        }
        if (y + position < height) {
            up = StartPointRGB - image.getRGB(x, y + position);
            RGBCircle.get(StartPointRGB).add(up);
            if (x - position > 0) {
                leftUp = StartPointRGB - image.getRGB(x - position, y + position);
                RGBCircle.get(StartPointRGB).add(leftUp);
            }
        }
        RGBCircle.forEach((k, v) -> v.forEach((value) -> {
            if (value < 0 ? value * -1 > difRate : value > difRate || value < 0 ? value * -1 > difRate : value > difRate){
                isDiff[0] = isDiff[0] + 1;
            }
        }));
        return isDiff[0] >= RGBCircle.get(StartPointRGB).size()/4 * 3;
    }

    private static void divideToLetter(int[][] picRGB, int height, int width){
        for (int num = 0; num < width; num = num + LETTER_WIDTH) {
            final int[][] LETTER = new int[LETTER_WIDTH][height];
            for (int WIDTH = 0; WIDTH < LETTER_WIDTH; WIDTH++) {
                if (WIDTH > width){
                    break;
                }
                for (int HEIGHT = 0; HEIGHT < height; HEIGHT++){
                    LETTER[WIDTH][HEIGHT] = picRGB[WIDTH][HEIGHT];
                }
            }
            Letter letter = boundaryIdentification(new Letter(picRGB, LETTER));
            LETTERS.add(letter);
        }

    }

    private static Letter boundaryIdentification(Letter letter){

        return letter;
    }

    public static void main(String[] args) {
        processPic(Parameters.PATH + "/resources/captcha4.jpg");
    }

}
