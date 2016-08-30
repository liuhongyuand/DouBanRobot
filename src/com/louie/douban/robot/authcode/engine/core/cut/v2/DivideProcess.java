package com.louie.douban.robot.authcode.engine.core.cut.v2;

import com.louie.douban.model.Letter;
import com.louie.douban.robot.authcode.engine.core.cut.CharCutService;

import java.util.Set;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class DivideProcess implements CharCutService {

    public static int[][] forTestDivide(int[][] RGB){
        int[][] afterLeftScan = LeftScan.leftScan(RGB, 0);
        int[][] afterRightScan = RightScan.rightScan(afterLeftScan, afterLeftScan.length - 1);
        return afterRightScan;
    }

    @Override
    public Set<Letter> divideToLetters(int[][] RGB) {
        int[][] afterLeftScan = LeftScan.leftScan(RGB, 0);
        int[][] afterRightScan = RightScan.rightScan(afterLeftScan, afterLeftScan.length - 1);
        return null;
    }
}
