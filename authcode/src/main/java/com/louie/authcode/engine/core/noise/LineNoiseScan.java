package com.louie.authcode.engine.core.noise;

import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.utils.PicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by liuhongyu.louie on 2016/8/26.
 */
public class LineNoiseScan extends AbstractNoiseProcess implements NoiseProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LineNoiseScan.class);

    @Override
    public int[][] getImageWithoutNoise(int[][] srcRGB, int maxMatrix, ColorProcessService colorProcessService) {
        NoiseProcessService noiseProcessService = new MatrixNoiseScan();
        int[][] newRGB = srcRGB;
        for (int i = 0; i < 3; i++) {
            for (int width = 0; width < newRGB.length; width++) {
                double whitePointCount = PicUtil.getWhitePointCount(newRGB, width, newRGB[0].length, false);
                if (whitePointCount / (double) newRGB[0].length > EngineConfiguration.getService().getSimilarity() + 0.39999 || whitePointCount / (double) newRGB[0].length < 0.02) {
                    newRGB = PicUtil.setColor(newRGB, width, newRGB[0].length, -1, false);
                }
            }
            for (int height = 0; height < newRGB[0].length; height++) {
                double whitePointCount = PicUtil.getWhitePointCount(newRGB, height, newRGB.length, true);
                if (whitePointCount / (double) newRGB.length > EngineConfiguration.getService().getSimilarity() + 0.39999 || whitePointCount / (double) newRGB.length < 0.02 ) {
                    newRGB = PicUtil.setColor(newRGB, height, newRGB.length, -1, true);
                }
            }
        }
        newRGB = PicUtil.setColor(newRGB, 0, newRGB[0].length, -1, false);
        newRGB = PicUtil.setColor(newRGB, 1, newRGB[0].length, -1, false);
        newRGB = PicUtil.setColor(newRGB, 2, newRGB[0].length, -1, false);
        newRGB = PicUtil.setColor(newRGB, newRGB.length - 3, newRGB[0].length, -1, false);
        newRGB = PicUtil.setColor(newRGB, newRGB.length - 2, newRGB[0].length, -1, false);
        newRGB = PicUtil.setColor(newRGB, newRGB.length - 1, newRGB[0].length, -1, false);
        newRGB = noiseProcessService.getImageWithoutNoise(newRGB, maxMatrix, colorProcessService);
        return newRGB;
    }

    @Override
    public int[][] getImageWithoutNoise(String image, int maxMatrix, ColorProcessService colorProcessService) {
        int[][] srcRGB = PicUtil.getRGBFromImageFile(image);
        return this.getImageWithoutNoise(srcRGB, maxMatrix, colorProcessService);
    }
}
