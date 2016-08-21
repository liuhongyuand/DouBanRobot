package com.louie.douban.robot.services;

import com.louie.douban.model.Letter;
import com.louie.douban.util.Type;

import java.awt.image.BufferedImage;
import java.util.*;

import static com.louie.douban.util.Parameters.*;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public abstract class AbstractPicProcess {

    final Set<Letter> LETTERS = new LinkedHashSet<>();
    final Set<BufferedImage> buffers = new LinkedHashSet<>();

    abstract public Object[] process(String imgPath);

    void denoising(BufferedImage image, int[][] newRGB, int width, int height){
        newRGB = doDenoising(image, newRGB, width, height);
        setBufferedImage(image, newRGB);
    }

    BufferedImage setBufferedImage(BufferedImage img, int[][] RGB){
        for (int i = 0; i < RGB.length; i++) {
            for (int j = 0; j < RGB[0].length; j++) {
                img.setRGB(i, j, RGB[i][j]);
            }
        }
        return img;
    }

    int[][] removeColor(BufferedImage image, int width, int height){
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

    int[][] doDenoising(BufferedImage image, int[][] newRGB, int width, int height){
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

    private boolean differRate(BufferedImage image, int x, int y, int width, int height, int position) {
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

    void divideToLetter(int[][] picRGB, int height, int width){
        for (int num = 0; num < width; num = num + LETTER_WIDTH + LETTER_GAP) {
            final int[][] LETTER = new int[LETTER_WIDTH][height];
            for (int WIDTH = num; WIDTH < num + LETTER_WIDTH; WIDTH++) {
                if (WIDTH >= width){
                    break;
                }
                System.arraycopy(picRGB[WIDTH], 0, LETTER[WIDTH - num], 0, height);
            }
            Letter letter = boundaryIdentification(new Letter(picRGB, LETTER, num, num + LETTER_WIDTH));
            if (letter.isUseful()) {
                LETTERS.add(letter);
            }
        }

    }

    private Letter boundaryIdentification(Letter letter){
        if (usefulCheck(letter)){
            letter = boundaryLocateHorizontal(boundaryLocateHorizontal(letter, Type.BoundaryDirection.LEFT), Type.BoundaryDirection.RIGHT);
            return boundaryLocateVertical(boundaryLocateVertical(letter, Type.BoundaryDirection.UP), Type.BoundaryDirection.DOWN);
        }
        letter.setUseful(false);
        return letter;
    }

    private Letter boundaryLocateHorizontal(Letter letter, Type.BoundaryDirection type){
        double whiteLineLeft;
        double whiteLineMid;
        double whiteLineRight;
        int width = letter.getOriginalPicRBG().length;
        int height = letter.getOriginalPicRBG()[0].length;
        int midLine = type == Type.BoundaryDirection.LEFT ? letter.getStartX() : letter.getEndX();
        if (type == Type.BoundaryDirection.LEFT ? midLine > 1 : midLine < width - 1) {
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

    private Letter boundaryLocateVertical(Letter letter, Type.BoundaryDirection type){
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

    private int getWhiteCount(int[][] RGB, int x, int length, boolean isVertical){
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

    private boolean usefulCheck(Letter letter){
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


}
