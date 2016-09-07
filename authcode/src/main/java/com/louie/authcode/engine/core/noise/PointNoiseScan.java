package com.louie.authcode.engine.core.noise;

import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.utils.PicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public class PointNoiseScan extends AbstractNoiseProcess implements NoiseProcessService {

    public static boolean useLineScan = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(PointNoiseScan.class);

    @Override
    public int[][] getImageWithoutNoise(int[][] srcRGB, int maxMatrix, ColorProcessService colorProcessService) {
        NoiseProcessService lineScanService = new LineNoiseScan();
        int[][] newRGB = srcRGB;
        if (colorProcessService != null) {
             newRGB = colorProcessService.processColor(srcRGB);
        }
        if (useLineScan) {
            newRGB = lineScanService.getImageWithoutNoise(newRGB, maxMatrix, colorProcessService);
        }
        return newRGB;
    }

    @Override
    public int[][] getImageWithoutNoise(String image, int maxMatrix, ColorProcessService colorProcessService){
        int[][] srcRGB = PicUtil.getRGBFromImageFile(image);
        return this.getImageWithoutNoise(srcRGB, maxMatrix, colorProcessService);
    }

    private int[][] denoising(int[][] srcRGB, int[][] newRGB){
        newRGB = doDenoising(srcRGB, newRGB, srcRGB.length, srcRGB[0].length);
        return newRGB;
    }

    public static int[][] doDenoising(int[][] srcRGB, int[][] newRGB, int width, int height){
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                for (int position = 1; position <= EngineConfiguration.getService().getNearby(); position++) {
                    if (differRate(srcRGB, i, j, width, height, EngineConfiguration.getService().getNearby())) {
                        newRGB[i][j] = -1;
                    }
                }
            }
        }
        return newRGB;
    }

    private static boolean differRate(int[][] srcRGB, int x, int y, int width, int height, int position) {
        int StartPointRGB = srcRGB[x][y];
        final int[] isDiff = {0};
        int up, down, left, right, leftUp, leftDown, rightUp, rightDown;
        final Map<Integer, Vector<Integer>> RGBCircle = new HashMap<>();
        RGBCircle.put(StartPointRGB, new Vector<>());
        if (x - position > 0) {
            left = StartPointRGB - srcRGB[x - position][y];
            RGBCircle.get(StartPointRGB).add(left);
            if (y - position > 0) {
                leftDown = StartPointRGB - srcRGB[x - position][y - position];
                RGBCircle.get(StartPointRGB).add(leftDown);
            }
        }
        if (x + position < width) {
            right = StartPointRGB - srcRGB[x + position][y];
            RGBCircle.get(StartPointRGB).add(right);
            if (y + position < height) {
                rightUp = StartPointRGB - srcRGB[x + position][y + position];
                RGBCircle.get(StartPointRGB).add(rightUp);
            }
        }
        if (y - position >= 0) {
            down = StartPointRGB - srcRGB[x][y - position];
            RGBCircle.get(StartPointRGB).add(down);
            if (x + position < width) {
                rightDown = StartPointRGB - srcRGB[x + position][y - position];
                RGBCircle.get(StartPointRGB).add(rightDown);
            }
        }
        if (y + position < height) {
            up = StartPointRGB - srcRGB[x][y + position];
            RGBCircle.get(StartPointRGB).add(up);
            if (x - position > 0) {
                leftUp = StartPointRGB - srcRGB[x - position][y + position];
                RGBCircle.get(StartPointRGB).add(leftUp);
            }
        }
        RGBCircle.forEach((k, v) -> v.forEach((value) -> {
            if (value < 0 ? value * -1 > EngineConfiguration.getService().getColorDifferentRate() : value > EngineConfiguration.getService().getColorDifferentRate() || value < 0 ? value * -1 > EngineConfiguration.getService().getColorDifferentRate() : value > EngineConfiguration.getService().getColorDifferentRate()){
                isDiff[0] = isDiff[0] + 1;
            }
        }));
        return (double)isDiff[0] >= ((double)RGBCircle.get(StartPointRGB).size()/(double) 4) * (double) 3;
    }

}
