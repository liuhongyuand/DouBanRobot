package com.louie.authcode.engine.core.cut.v2;

import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;
import com.louie.authcode.engine.model.Letter;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class BelowScan {

    public static Letter belowScan(Letter letter, int startHeight){
        int[][] newRGB = letter.getLetterRGB();
        int heightAbove = startHeight - 1;
        if (heightAbove >= 0 ){
            int blackPointCountAbove = ScanUtil.blackPointCollector(newRGB, heightAbove, false);
            if (blackPointCountAbove <= 0){
                newRGB = ScanUtil.setArrays(newRGB, 0, 0, newRGB.length, heightAbove);
                letter.setLetterRGB(newRGB);
                return belowScan(letter, newRGB[0].length);
            }
        }
        return letter;
    }

}
