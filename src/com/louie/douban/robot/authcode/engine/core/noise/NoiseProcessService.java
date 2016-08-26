package com.louie.douban.robot.authcode.engine.core.noise;

import com.louie.douban.robot.authcode.engine.core.color.ColorProcessService;

/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public interface NoiseProcessService {

    int[][] getImageWithoutNoise(int[][] srcRGB, ColorProcessService colorProcessService);

    int[][] getImageWithoutNoise(String image, ColorProcessService colorProcessService);

}
