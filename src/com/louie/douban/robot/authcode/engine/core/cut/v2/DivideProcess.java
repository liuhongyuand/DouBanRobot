package com.louie.douban.robot.authcode.engine.core.cut.v2;

import com.louie.douban.model.Letter;
import com.louie.douban.robot.authcode.engine.core.cut.CharCutService;

import java.util.HashSet;
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
        Set<Letter> letters = new HashSet<>();
        int[][] afterLeftScan = LeftScan.leftScan(RGB, 0);
        int[][] afterRightScan = RightScan.rightScan(afterLeftScan, afterLeftScan.length - 1);
        Letter letter = new Letter(afterRightScan);
        letters.add(LeftScan.letterScan(letter, 0));
        int endWidth = letter.getEndX();
        int[][] srcRGB = afterRightScan;
        for (int times = 0; times < 15; times++) {
            int[][] letterAfterLeftScan = LeftScan.leftScan(srcRGB, endWidth);
            Letter letterExceptFirst = new Letter(letterAfterLeftScan);
            letterExceptFirst = LeftScan.letterScan(letterExceptFirst, 0);
            if (letterExceptFirst.getLetterRGB() != null) {
                letters.add(letterExceptFirst);
                endWidth = letterExceptFirst.getEndX();
                srcRGB = letterAfterLeftScan;
            } else {
                break;
            }
            if (afterRightScan.length - endWidth <= 1){
                break;
            }
        }
        return letters;
    }
}
