package com.louie.authcode.engine.core.cut.v2;

import com.louie.authcode.engine.core.cut.CharCutService;
import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;
import com.louie.authcode.engine.model.Letter;

import java.util.LinkedHashSet;
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
        Set<Letter> letters = new LinkedHashSet<>();
        int[][] afterLeftScan = LeftScan.leftScan(RGB, 0);
        int[][] afterRightScan = RightScan.rightScan(afterLeftScan, afterLeftScan.length - 1);
        Letter letter = new Letter(afterRightScan);
        letters.add(ScanUtil.removeAdditionWhite(LeftScan.letterScan(letter, 0)));
        int endWidth = letter.getEndX();
        int[][] srcRGB = afterRightScan;
        for (int times = 0; times < 15; times++) {
            int[][] letterAfterLeftScan = LeftScan.leftScan(srcRGB, endWidth);
            Letter letterExceptFirst = new Letter(letterAfterLeftScan);
            letterExceptFirst = LeftScan.letterScan(letterExceptFirst, 0);
            if (letterExceptFirst.getLetterRGB() != null) {
                if (letterExceptFirst.getEndX() > 5) {
                    letters.add(ScanUtil.removeAdditionWhite(letterExceptFirst));
                }
                endWidth = letterExceptFirst.getEndX();
                srcRGB = letterAfterLeftScan;
            } else {
                break;
            }
            if (letterAfterLeftScan.length - endWidth <= 1){
                break;
            }
        }
        return getUsefulLetter(letters);
    }

    private LinkedHashSet<Letter> getUsefulLetter(Set<Letter> letters){
        final Set<Letter> NEW_LETTERS = new LinkedHashSet<>();
        letters.forEach((letter -> {
            if (isValidLetter(letter)){
                NEW_LETTERS.add(letter);
            }
        }));
        return (LinkedHashSet<Letter>) NEW_LETTERS;
    }

    private boolean isValidLetter(Letter letter){
        if (ScanUtil.isNeedRemoved(letter.getLetterRGB())){
            return false;
        }
        return true;
    }
}
