package com.louie.authcode.engine.core.cut.v1;

import com.louie.authcode.engine.core.cut.CharCutService;
import com.louie.authcode.engine.core.utils.PicUtil;
import com.louie.authcode.engine.model.Letter;
import com.louie.authcode.engine.core.utils.Type;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.louie.authcode.engine.config.EngineParameters.*;


/**
 * Use v2 to replace this divide service
 * Created by liuhongyu.louie on 2016/8/23.
 */
@Deprecated
public class LineScan implements CharCutService {

    @Override
    public Set<Letter> divideToLetters(int[][] RGB){
        final Set<Letter> letterSet = new LinkedHashSet<>();
        for (int num = 0; num < RGB.length; num = num + LETTER_WIDTH + LETTER_GAP) {
            final int[][] LETTER = new int[LETTER_WIDTH][RGB[0].length];
            for (int WIDTH = num; WIDTH < num + LETTER_WIDTH; WIDTH++) {
                if (WIDTH >= RGB.length){
                    break;
                }
                System.arraycopy(RGB[WIDTH], 0, LETTER[WIDTH - num], 0, RGB[0].length);
            }
            Letter letter = boundaryIdentification(new Letter(RGB, LETTER, num, num + LETTER_WIDTH));
            if (letter.isUseful()) {
                letterSet.add(letter);
            }
        }
        return letterSet;
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
            whiteLineLeft = PicUtil.getWhitePointCount(letter.getOriginalPicRBG(), midLine - 1, height, false);
            whiteLineMid = PicUtil.getWhitePointCount(letter.getOriginalPicRBG(), midLine, height, false);
            whiteLineRight = PicUtil.getWhitePointCount(letter.getOriginalPicRBG(), midLine + 1, height, false);
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
                whiteLineMid = PicUtil.getWhitePointCount(letter.getLetterRGB(), midLine, width, true);
                whiteLineUp = PicUtil.getWhitePointCount(letter.getLetterRGB(), midLine + 1, width, true);
            } else if (type == Type.BoundaryDirection.DOWN) {
                whiteLineMid = PicUtil.getWhitePointCount(letter.getLetterRGB(), midLine, width, true);
                whiteLineDown = PicUtil.getWhitePointCount(letter.getLetterRGB(), midLine - 1, width, true);
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
