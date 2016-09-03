package com.louie.authcode.engine.core.noise;

import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.utils.PicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.louie.authcode.engine.EngineParameters.difRate;
import static com.louie.authcode.engine.EngineParameters.nearby;


/**
 * Created by liuhongyu.louie on 2016/8/23.
 */
public class PointNoiseScan extends AbstractNoiseProcess implements NoiseProcessService {

    public static boolean useLineScan = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(PointNoiseScan.class);

    @Override
    public int[][] getImageWithoutNoise(int[][] srcRGB, ColorProcessService colorProcessService) {
        NoiseProcessService lineScanService = new LineNoiseScan();
        int[][] newRGB = colorProcessService.processColor(srcRGB);
//        for (int i = 0; i < 2; i++) {
//            newRGB = denoising(srcRGB, newRGB);
//            srcRGB = newRGB;
//        }
        if (useLineScan) {
            newRGB = lineScanService.getImageWithoutNoise(newRGB, colorProcessService);
        }
        return newRGB;
    }

    public int[][] getImageWithoutNoise(String image, ColorProcessService colorProcessService){
        int[][] srcRGB = PicUtil.getRGBFromImageFile(image);
        return this.getImageWithoutNoise(srcRGB, colorProcessService);
    }

    private int[][] denoising(int[][] srcRGB, int[][] newRGB){
        newRGB = doDenoising(srcRGB, newRGB, srcRGB.length, srcRGB[0].length);
        return newRGB;
    }

    private int[][] doDenoising(int[][] srcRGB, int[][] newRGB, int width, int height){
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                for (int position = 1; position <= nearby; position++) {
                    if (differRate(srcRGB, i, j, width, height, nearby)) {
                        newRGB[i][j] = -1;
                    }
                }
            }
        }
        return newRGB;
    }

    private boolean differRate(int[][] srcRGB, int x, int y, int width, int height, int position) {
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
            if (value < 0 ? value * -1 > difRate : value > difRate || value < 0 ? value * -1 > difRate : value > difRate){
                isDiff[0] = isDiff[0] + 1;
            }
        }));
        return (double)isDiff[0] >= ((double)RGBCircle.get(StartPointRGB).size()/(double) 4) * (double) 3;
    }

}
