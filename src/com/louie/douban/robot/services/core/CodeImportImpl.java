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
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class CodeImportImpl extends AbstractPicProcess implements AuthCodeProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeImportImpl.class);

    @Override
    public Object[] process(String image) {
        final Set<BufferedImage> bufferedImages = new LinkedHashSet<>();
        final List<List<Point>> letterPointList = new LinkedList<>();
        ColorProcessService colorProcessService = new BinaryValue();
        NoiseProcessService noiseProcessService = new PointScan();
        CharCutService charCutService = new LineScan();
        int[][] newRGB = noiseProcessService.getImageWithoutNoise(image, colorProcessService);
        final Set<Letter> letterSet = charCutService.divideToLetters(newRGB);
        letterSet.forEach((letter -> {
            BufferedImage bufferImg = new BufferedImage(letter.getWidth(), letter.getHeight(), BufferedImage.TYPE_INT_BGR);
            List<Point> points = new LinkedList<>();
            for (int i = 0 ; i < letter.getWidth(); i++){
                for(int j = 0; j < letter.getHeight(); j++){
                    if (letter.getLetterRGB()[i][j] != -1 && letter.getLetterRGB()[i][j] != 0){
                        points.add(new Point(i, j));
                    }
                }
            }
            letterPointList.add(points);
            bufferedImages.add(setBufferedImage(bufferImg, letter.getLetterRGB()));
        }));
        return new Object[]{letterPointList, bufferedImages};
    }
}
