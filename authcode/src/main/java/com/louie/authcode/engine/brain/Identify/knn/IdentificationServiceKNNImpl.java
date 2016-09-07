package com.louie.authcode.engine.brain.Identify.knn;

import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.brain.Identify.IdentificationService;
import com.louie.authcode.engine.brain.PointMap;

import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by liuhongyu.louie on 2016/9/7.
 */
public class IdentificationServiceKNNImpl implements IdentificationService {
    @Override
    public String identifyLetter(java.util.List<Point> pointKeyList, PointMap POINT_MAP) {
        final String[] letter = {""};
        for (Map.Entry<String, HashSet<java.util.List<Point>>> entry : POINT_MAP.entrySet()){
            for (java.util.List<Point> PointList : entry.getValue()){
                int[] findNum = {0};
                pointKeyList.forEach((KeyPoint -> {
                    int[] findNumInList = {0};
                    PointList.forEach(ValuePoint -> {
                        if (findNumInList[0] == 0 && isInner(ValuePoint, KeyPoint, EngineConfiguration.getService().getDeviation())){
                            findNumInList[0]++;
                        }
                    });
                    findNum[0] += findNumInList[0];
                }));
                if ((double) findNum[0] / (double)pointKeyList.size() > EngineConfiguration.getService().getSimilarity()){
                    int[] findNumReverse = {0};
                    PointList.forEach((ValuePoint -> {
                        int[] findNumListReverse = {0};
                        pointKeyList.forEach(KeyPoint -> {
                            if (findNumListReverse[0] == 0 && isInner(ValuePoint, KeyPoint, EngineConfiguration.getService().getDeviation())){
                                findNumListReverse[0]++;
                            }
                        });
                        findNumReverse[0] += findNumListReverse[0];
                    }));
                    if ((double) findNumReverse[0] / (double)PointList.size() > EngineConfiguration.getService().getSimilarity()) {
                        if ((PointList.size() > pointKeyList.size() ? (double)pointKeyList.size() / (double)PointList.size() : (double)PointList.size() / (double)pointKeyList.size()) > EngineConfiguration.getService().getSimilarity()) {
                            letter[0] = entry.getKey();
                            break;
                        }
                    }
                }
            }
            if (!letter[0].isEmpty()){
                break;
            }
        }
        return letter[0];
    }

    private boolean isInner(Point pointValue, Point pointKey, double deviation){
        int differenceX = Math.abs(pointKey.x - pointValue.x);
        int differenceY = Math.abs(pointKey.y - pointValue.y);
        return differenceX < deviation && differenceY < deviation;
    }
}
