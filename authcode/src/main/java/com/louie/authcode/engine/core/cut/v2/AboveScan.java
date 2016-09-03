package com.louie.authcode.engine.core.cut.v2;

import com.louie.authcode.engine.core.cut.v2.utils.ScanUtil;
import com.louie.authcode.engine.model.Letter;

/**
 * Created by liuhongyu.louie on 2016/8/28.
 */
public class AboveScan {

    public static Letter aboveScan(Letter letter, int startHeight){
        int[][] newRGB = letter.getLetterRGB();
        int heightBelow = startHeight + 1;
        if (heightBelow < newRGB[0].length){
            int blackPointCountBelow = ScanUtil.blackPointCollector(newRGB, heightBelow, false);
            if (blackPointCountBelow <= 0){
                newRGB = ScanUtil.setArrays(newRGB, 0, heightBelow, newRGB.length, newRGB[0].length);
                letter.setLetterRGB(newRGB);
                return aboveScan(letter, 0);
            }
        }
        return letter;
    }

}
