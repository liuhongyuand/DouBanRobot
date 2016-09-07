package com.louie.authcode.engine.model;


import static com.louie.authcode.engine.config.EngineParameters.LETTER_WIDTH;

/**
 * Auth letter
 * Created by liuhongyu.louie on 2016/8/19.
 */
public class Letter {

    private boolean isUseful;
    private int StartX;
    private int EndX;
    private int StartY;
    private int EndY;
    private int width;
    private int height;
    private int[][] letterRGB;
    private int[][] originalPicRBG;
    private BoundaryLine<Integer, Integer> boundaryLine;

    public Letter(int[][] originalPicRBG){
        this.originalPicRBG = originalPicRBG;
    }

    public Letter(int[][] originalPicRBG, int[][] letterRGB, int StartX, int EndX){
        this.originalPicRBG = originalPicRBG;
        this.letterRGB = letterRGB;
        this.StartX = StartX;
        this.EndX = EndX - 1;
        this.StartY = 0;
        this.EndY = originalPicRBG[0].length;
        setBoundaryLine(new BoundaryLine<>(StartX, StartX + LETTER_WIDTH));
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
        setEndX(letterRGB.length);
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
        getBoundaryLine().setY(startX + LETTER_WIDTH);
    }

    public int getStartY() {
        return StartY;
    }

    public void setStartY(int startY) {
        StartY = startY;
    }

    public int getEndY() {
        return EndY;
    }

    public void setEndY(int endY) {
        EndY = endY;
    }

    public int getWidth() {
        return letterRGB.length;
    }

    public int getHeight() {
        return letterRGB[0].length;
    }

    public void updateLetterRGB(){
        int[][] newRGB = new int[getEndX() - getStartX()][getEndY() - getStartY()];
        for (int width = getStartX(); width < getEndX(); width++){
            for (int height = getStartY(); height < getEndY(); height++){
                newRGB[width - getStartX()][height - getStartY()] = getOriginalPicRBG()[width][height];
            }
        }
        setLetterRGB(newRGB);
    }

}
