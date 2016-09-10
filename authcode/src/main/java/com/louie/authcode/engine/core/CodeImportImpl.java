package com.louie.authcode.engine.core;

import com.louie.authcode.engine.AuthCodeProcess;
import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.cut.CharCutService;
import com.louie.authcode.engine.core.noise.NoiseProcessService;
import com.louie.authcode.engine.model.Letter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class CodeImportImpl extends AbstractPicProcess implements AuthCodeProcess {
    public static boolean isDivide = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeImportImpl.class);
    private static final int MAX_MATRIX = EngineConfiguration.getService().getMaxMatrix();

    @Override
    public Object[] process(String image) {
        if (!isCorrectImageFormat(image)){
            return null;
        }
        final Set<BufferedImage> bufferedImages = new LinkedHashSet<>();
        final List<List<Point>> letterPointList = new LinkedList<>();
        ColorProcessService colorProcessService = EngineConfiguration.getService().getColorProcessService();
        NoiseProcessService noiseProcessService = EngineConfiguration.getService().getNoiseProcessService();
        CharCutService charCutService = EngineConfiguration.getService().getCharCutService();
        int[][] newRGB = noiseProcessService.getImageWithoutNoise(image, MAX_MATRIX, colorProcessService);
        if (isDivide) {
            for (Letter letter : charCutService.divideToLetters(newRGB)){
                BufferedImage bufferImg = new BufferedImage(letter.getLetterRGB().length, letter.getLetterRGB()[0].length, BufferedImage.TYPE_INT_BGR);
                List<Point> points = new LinkedList<>();
                for (int i = 0; i < letter.getWidth(); i++) {
                    for (int j = 0; j < letter.getHeight(); j++) {
                        if (letter.getLetterRGB()[i][j] != -1 && letter.getLetterRGB()[i][j] != 0) {
                            points.add(new Point(i, j));
                        }
                    }
                }
                letterPointList.add(points);
                bufferedImages.add(setBufferedImage(bufferImg, letter.getLetterRGB()));
            }
            return new Object[]{letterPointList, bufferedImages};
        } else {
            return new Object[]{newRGB};
        }
    }
}
