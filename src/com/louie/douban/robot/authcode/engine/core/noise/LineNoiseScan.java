package com.louie.douban.robot.authcode.engine.core.noise;

import com.louie.douban.robot.authcode.engine.core.AbstractPicProcess;
import com.louie.douban.robot.authcode.engine.core.color.ColorProcessService;
import com.louie.douban.robot.authcode.engine.core.utils.PicUtil;
import com.louie.douban.util.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by liuhongyu.louie on 2016/8/26.
 */
public class LineNoiseScan extends AbstractNoiseProcess implements NoiseProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LineNoiseScan.class);

    @Override
    public int[][] getImageWithoutNoise(int[][] srcRGB, ColorProcessService colorProcessService) {
        int[][] newRGB = colorProcessService.processColor(srcRGB);
        for (int i = 0; i < 3; i++) {
            for (int width = 0; width < newRGB.length; width++) {
                double whitePointCount = PicUtil.getWhitePointCount(newRGB, width, newRGB[0].length, false);
                if (whitePointCount / (double) newRGB[0].length > Parameters.similarity) {
                    newRGB = PicUtil.setColor(newRGB, width, newRGB[0].length, -1, false);
                }
            }
        }
        return newRGB;
    }

    @Override
    public int[][] getImageWithoutNoise(String image, ColorProcessService colorProcessService) {
        int[][] srcRGB = PicUtil.getRGBFromImageFile(image);
        return this.getImageWithoutNoise(srcRGB, colorProcessService);
    }
}
