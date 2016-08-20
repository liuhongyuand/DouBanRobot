package com.louie.douban.robot.pic;

import com.louie.douban.model.Letter;
import com.louie.douban.util.Parameters;
import com.louie.douban.util.Type;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Process {

    private static final double eliminateValue = 0.92;     //过滤没有验证码的模块
    private static final int target = 16711423;             //黑色
    private static final int difRate = 8000000/4;           //色值偏移
    private static final int nearby = 1;                    //像素位
    public static final int LETTER_WIDTH = 20;             //字母宽度
    private static final int LETTER_GAP = 1;               //字母间隔
    private static final double pixelFilter = 3.5;         //像素过滤
    private static final String FILE = Parameters.PATH + "/resources/captcha5.jpg";
    private static final Set<Letter> LETTERS = new LinkedHashSet<>();
    private static final Set<BufferedImage> buffers = new LinkedHashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(Process.class);

    public static void processPic(String img) {
        File pic = new File(img);
        try {
            BufferedImage image = ImageIO.read(pic);
            JFrame frame = new JFrame();
            frame.setLayout(null);
            frame.setSize(image.getWidth() + 10, image.getHeight() * 2 + 10);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] newRGB;
            newRGB = removeColor(image, width, height);
            for (int i = 0; i < 3; i++) {
                denoising(image, newRGB, width, height);
            }
            divideToLetter(newRGB, image.getHeight(), image.getWidth());
            LETTERS.forEach((letter -> {
                BufferedImage bufferImg = new BufferedImage(letter.getEndX() - letter.getStartX(), letter.getEndY() - letter.getStartY(), BufferedImage.TYPE_INT_BGR);
                denoising(bufferImg, letter.getLetterRGB(), letter.getEndX() - letter.getStartX(), letter.getEndY() - letter.getStartY());
                int plus = 0;
                double multi = 1;
                for (int i = 0 ; i < letter.getEndX() - letter.getStartX(); i++){
                    for(int j = 0; j < letter.getEndY() - letter.getStartY(); j++){
                        plus += letter.getLetterRGB()[i][j];
                    }
                }
                System.out.println(plus + " " + multi);
                buffers.add(setBufferedImage(bufferImg, letter.getLetterRGB()));
            }));
            int temp = 10;
            for (BufferedImage buffer : buffers) {
                JLabel label = new JLabel("");
                label.setIcon(new ImageIcon(buffer));
                label.setBounds(temp, 0, buffer.getWidth(), buffer.getHeight());
                frame.add(label);
                temp = temp + buffer.getWidth() + 20;
            }
            frame.setVisible(true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static double getDouble(double num, int pos){
        BigDecimal bd = new BigDecimal(num);
        return bd.setScale(pos ,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static void denoising(BufferedImage image, int[][] newRGB, int width, int height){
        newRGB = doDenoising(image, newRGB, width, height);
        setBufferedImage(image, newRGB);
    }

    private static BufferedImage setBufferedImage(BufferedImage img, int[][] RGB){
        for (int i = 0; i < RGB.length; i++) {
            for (int j = 0; j < RGB[0].length; j++) {
                img.setRGB(i, j, RGB[i][j]);
            }
        }
        return img;
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
        for (int num = 0; num < width; num = num + LETTER_WIDTH + LETTER_GAP) {
            final int[][] LETTER = new int[LETTER_WIDTH][height];
            for (int WIDTH = num; WIDTH < num + LETTER_WIDTH; WIDTH++) {
                if (WIDTH >= width){
                    break;
                }
                for(int HEIGHT = 0; HEIGHT < height; HEIGHT++){
                    LETTER[WIDTH - num][HEIGHT] = picRGB[WIDTH][HEIGHT];
                }
            }
            Letter letter = boundaryIdentification(new Letter(picRGB, LETTER, num, num + LETTER_WIDTH));
            if (letter.isUseful()) {
                LETTERS.add(letter);
            }
        }

    }

    private static Letter boundaryIdentification(Letter letter){
        if (usefulCheck(letter)){
            letter = boundaryLocateHorizontal(boundaryLocateHorizontal(letter, Type.BoundaryDirection.LEFT), Type.BoundaryDirection.RIGHT);
            return boundaryLocateVertical(boundaryLocateVertical(letter, Type.BoundaryDirection.UP), Type.BoundaryDirection.DOWN);
        }
        letter.setUseful(false);
        return letter;
    }

    private static Letter boundaryLocateHorizontal(Letter letter, Type.BoundaryDirection type){
        double whiteLineLeft;
        double whiteLineMid;
        double whiteLineRight;
        int width = letter.getOriginalPicRBG().length;
        int height = letter.getOriginalPicRBG()[0].length;
        int midLine = type == Type.BoundaryDirection.LEFT ? letter.getStartX() : letter.getEndX();
        if (type == Type.BoundaryDirection.LEFT ? midLine > 0 : midLine < width) {
            whiteLineLeft = getWhiteCount(letter.getOriginalPicRBG(), midLine - 1, height, false);
            whiteLineMid = getWhiteCount(letter.getOriginalPicRBG(), midLine, height, false);
            whiteLineRight = getWhiteCount(letter.getOriginalPicRBG(), midLine + 1, height, false);
            if (whiteLineLeft < whiteLineRight && whiteLineMid < whiteLineRight){
                if (type == Type.BoundaryDirection.LEFT){
                    letter.setStartX(midLine + 1);
                } else {
                    letter.setEndX(midLine + 1);
                }
                letter = boundaryLocateHorizontal(letter, type);
            } else if (whiteLineRight < whiteLineLeft && whiteLineMid < whiteLineLeft){
                if (type == Type.BoundaryDirection.LEFT){
                    letter.setStartX(midLine - 1);
                } else {
                    letter.setEndX(midLine - 1);
                }
                letter = boundaryLocateHorizontal(letter, type);
            }
        }
        letter.updateLetterRGB();
        letter.setUseful(true);
        return letter;
    }

    private static Letter boundaryLocateVertical(Letter letter, Type.BoundaryDirection type){
        double whiteLineUp = 0;
        double whiteLineMid = 0;
        double whiteLineDown = 0;
        int width = letter.getLetterRGB().length;
        int height = letter.getLetterRGB()[0].length;
        int midLine = type == Type.BoundaryDirection.UP ? letter.getStartY() : letter.getEndY() - letter.getStartY() - 1;
        if (midLine < height && midLine >= 0 && letter.getEndY() - letter.getStartY() > 20) {
            if (type == Type.BoundaryDirection.UP) {
                whiteLineMid = getWhiteCount(letter.getLetterRGB(), midLine, width, true);
                whiteLineUp = getWhiteCount(letter.getLetterRGB(), midLine + 1, width, true);
            } else if (type == Type.BoundaryDirection.DOWN) {
                whiteLineMid = getWhiteCount(letter.getLetterRGB(), midLine, width, true);
                whiteLineDown = getWhiteCount(letter.getLetterRGB(), midLine - 1, width, true);
            }
            if (whiteLineMid <= whiteLineDown){
                letter.setEndY(letter.getEndY() - 1);
                letter = boundaryLocateVertical(letter, type);
            } else if (whiteLineMid <= whiteLineUp){
                letter.setStartY(midLine + 1);
                letter = boundaryLocateVertical(letter, type);
            } else if (whiteLineMid > whiteLineDown && width - whiteLineDown < pixelFilter){
                letter.setEndY(letter.getEndY() - 1);
                letter = boundaryLocateVertical(letter, type);
            } else if (whiteLineMid > whiteLineUp && width - whiteLineUp < pixelFilter){
                letter.setStartY(midLine + 1);
                letter = boundaryLocateVertical(letter, type);
            }
        }
        letter.updateLetterRGB();
        letter.setUseful(true);
        return letter;
    }

    private static int getWhiteCount(int[][] RGB, int x, int length, boolean isVertical){
        int whiteCount = 0;
        for (int i = 0; i < length; i++) {
            if (isVertical){
                if (RGB[i][x] == -1) {
                    whiteCount++;
                }
            }else {
                if (RGB[x][i] == -1) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    private static boolean usefulCheck(Letter letter){
        int[][] RGB = letter.getLetterRGB();
        double useless = 0;
        double width = RGB.length;
        double height = RGB[0].length;
        for (int[] aRGB : RGB) {
            for (int j = 0; j < height; j++) {
                if (aRGB[j] == -1 || aRGB[j] == 0) {
                    useless++;
                }
            }
        }
        return useless/(width * height) < eliminateValue; //when useless/(width * height) < eliminateValue, the data is useful;
    }

    public static void main(String[] args) {
        processPic(FILE);
    }

}