package com.louie.authcode.engine.model;

/**
 * The boundary line
 * Created by liuhongyu.louie on 2016/8/19.
 */
public class BoundaryLine<X, Y> {

    private int X;
    private int Y;

    public BoundaryLine(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
}
