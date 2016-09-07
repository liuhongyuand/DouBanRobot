package com.louie.authcode.engine.core.color;

import com.louie.authcode.engine.EngineConfiguration;

/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public class BinaryValue implements ColorProcessService {

    @Override
    public int[][] processColor(int[][] srcRGB){
        int[][] newRGB = new int[srcRGB.length][srcRGB[0].length];
        for (int i = 0; i < newRGB.length; i++) {
            for (int j = 0; j < newRGB[0].length; j++) {
                int nowPoint = srcRGB[i][j] < 0 ? srcRGB[i][j] * -1 : srcRGB[i][j];
                if (EngineConfiguration.getService().getTargetColor() - nowPoint < EngineConfiguration.getService().getColorDifferentRate() || nowPoint - EngineConfiguration.getService().getTargetColor() > EngineConfiguration.getService().getColorDifferentRate() ) {
                    newRGB[i][j] = srcRGB[i][j];
                }else {
                    newRGB[i][j] = -1;
                }
            }
        }
        return newRGB;
    }

}
