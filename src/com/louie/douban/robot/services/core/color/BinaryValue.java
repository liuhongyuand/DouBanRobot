package com.louie.douban.robot.services.core.color;

import static com.louie.douban.util.Parameters.difRate;
import static com.louie.douban.util.Parameters.target;

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
                if (target - nowPoint < difRate || nowPoint - target > difRate ) {
                    newRGB[i][j] = srcRGB[i][j];
                }else {
                    newRGB[i][j] = -1;
                }
            }
        }
        return newRGB;
    }

}
