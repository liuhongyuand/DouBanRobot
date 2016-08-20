package com.louie.douban.model;

import com.louie.douban.robot.pic.Process;

/**
 * Auth letter
 * Created by liuhongyu.louie on 2016/8/19.
 */
public class Letter {

    private boolean isUseful;
    private int StartX;
    private int EndX;
    private int width;
    private int height;
    private int[][] letterRGB;
    private int[][] originalPicRBG;
    private BoundaryLine<Integer, Integer> boundaryLine;

    public Letter(int[][] originalPicRBG, int[][] letterRGB, int StartX, int EndX){
        this.originalPicRBG = originalPicRBG;
        this.letterRGB = letterRGB;
        this.StartX = StartX;
        this.EndX = EndX;
        setBoundaryLine(new BoundaryLine<>(StartX, StartX + Process.LETTER_WIDTH));
    }

    public boolean isUseful() {
        return isUseful;
    }

    public void setUseful(boolean useful) {
        isUseful = useful;
    }

    public int[][] getLetterRGB() {
        return letterRGB;
    }

    public void setLetterRGB(int[][] letterRGB) {
        this.letterRGB = letterRGB;
    }

    public int[][] getOriginalPicRBG() {
        return originalPicRBG;
    }

    public void setOriginalPicRBG(int[][] originalPicRBG) {
        this.originalPicRBG = originalPicRBG;
    }

    public BoundaryLine<Integer, Integer> getBoundaryLine() {
        return boundaryLine;
    }

    public void setBoundaryLine(BoundaryLine<Integer, Integer> boundaryLine) {
        this.boundaryLine = boundaryLine;
    }

    public int getStartX() {
        return StartX;
    }

    public int getEndX() {
        return EndX;
    }

    public void setEndX(int EndX) {
        this.EndX = EndX;
    }

    public void setStartX(int startX) {
        this.StartX = startX;
        getBoundaryLine().setX(startX);
        getBoundaryLine().setY(startX + Process.LETTER_WIDTH);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void updateLetterRGB(){
        int[][] newRGB = new int[getEndX() - getStartX()][getOriginalPicRBG()[0].length];
        for (int width = getStartX(); width < getEndX(); width++){
            for (int height = 0; height < getOriginalPicRBG()[0].length; height++){
                newRGB[width - getStartX()][height] = getOriginalPicRBG()[width][height];
            }
        }
        setLetterRGB(newRGB);
    }

}
