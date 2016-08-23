package com.louie.douban.robot.service.core.noise;

import com.louie.douban.robot.service.core.color.ColorProcessService;

/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public interface NoiseProcessService {

    int[][] getImageWithoutNoise(String image, ColorProcessService colorProcessService);

}
