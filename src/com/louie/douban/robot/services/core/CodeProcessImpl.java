package com.louie.douban.robot.services.core;

import com.louie.douban.model.Letter;
import com.louie.douban.robot.services.AuthCodeProcess;
import com.louie.douban.robot.services.core.color.BinaryValue;
import com.louie.douban.robot.services.core.color.ColorProcessService;
import com.louie.douban.robot.services.core.cut.CharCutService;
import com.louie.douban.robot.services.core.cut.LineScan;
import com.louie.douban.robot.services.core.noise.NoiseProcessService;
import com.louie.douban.robot.services.core.noise.PointScan;
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

    @Override
    public Object[] process(String image) {
        final List<List<Point>> letterPointList = new LinkedList<>();
        ColorProcessService colorProcessService = new BinaryValue();
        NoiseProcessService noiseProcessService = new PointScan();
        CharCutService charCutService = new LineScan();
        int[][] newRGB = noiseProcessService.getImageWithoutNoise(image, colorProcessService);
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