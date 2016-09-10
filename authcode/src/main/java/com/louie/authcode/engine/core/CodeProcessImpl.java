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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class CodeProcessImpl extends AbstractPicProcess implements AuthCodeProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeProcessImpl.class);
    private static final int MAX_MATRIX = EngineConfiguration.getService().getMaxMatrix();

    @Override
    public Object[] process(String image) {
        if (!isCorrectImageFormat(image)){
            return null;
        }
        final List<List<Point>> letterPointList = new LinkedList<>();
        ColorProcessService colorProcessService = EngineConfiguration.getService().getColorProcessService();
        NoiseProcessService noiseProcessService = EngineConfiguration.getService().getNoiseProcessService();
        CharCutService charCutService = EngineConfiguration.getService().getCharCutService();
        int[][] newRGB = noiseProcessService.getImageWithoutNoise(image, MAX_MATRIX, colorProcessService);
        final Set<Letter> letterSet = charCutService.divideToLetters(newRGB);
        letterSet.forEach((letter -> {
            List<Point> points = new LinkedList<>();
            for (int i = 0 ; i < letter.getWidth(); i++){
                for(int j = 0; j < letter.getHeight(); j++){
                    if (letter.getLetterRGB()[i][j] != -1 && letter.getLetterRGB()[i][j] != 0){
                        points.add(new Point(i, j));
                    }
                }
            }
            letterPointList.add(points);
        }));
        return new Object[]{letterPointList};
    }

}