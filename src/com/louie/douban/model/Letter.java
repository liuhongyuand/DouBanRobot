package com.louie.douban.model;

/**
 * Auth letter
 * Created by liuhongyu.louie on 2016/8/19.
 */
public class Letter {

    private boolean isUseful;
    private int[][] letterRGB;
    private int[][] originalPicRBG;
    private BoundaryLine<Integer, Integer> boundaryLine;

    public Letter(){
    }

    public Letter(int[][] originalPicRBG, int[][] letterRGB){
        this.originalPicRBG = originalPicRBG;
        this.letterRGB = letterRGB;
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
}
