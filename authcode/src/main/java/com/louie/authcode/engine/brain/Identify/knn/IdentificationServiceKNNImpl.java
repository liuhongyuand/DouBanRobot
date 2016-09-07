package com.louie.authcode.engine.brain.Identify.knn;

import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.brain.Identify.IdentificationService;
import com.louie.authcode.engine.brain.PointMap;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * KNN
 * Created by liuhongyu.louie on 2016/9/7.
 */
public class IdentificationServiceKNNImpl implements IdentificationService {
    @Override
    public String identifyLetter(java.util.List<Point> pointKeyList, PointMap POINT_MAP) {
        final Map<String, Integer> letterResult = new HashMap<>();
        for (Map.Entry<String, HashSet<java.util.List<Point>>> entry : POINT_MAP.entrySet()){
            for (java.util.List<Point> PointList : entry.getValue()){
                int[] findNum = {0};
                pointKeyList.forEach((KeyPoint -> {
                    int[] findNumInList = {0};
                    PointList.forEach(ValuePoint -> {
                        if (findNumInList[0] == 0 && isInner(ValuePoint, KeyPoint)){
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
                            if (findNumListReverse[0] == 0 && isInner(ValuePoint, KeyPoint)){
                                findNumListReverse[0]++;
                            }
                        });
                        findNumReverse[0] += findNumListReverse[0];
                    }));
                    if ((double) findNumReverse[0] / (double)PointList.size() > EngineConfiguration.getService().getSimilarity()) {
                        if ((PointList.size() > pointKeyList.size() ? (double)pointKeyList.size() / (double)PointList.size() : (double)PointList.size() / (double)pointKeyList.size()) > EngineConfiguration.getService().getSimilarity()) {
                            if (letterResult.containsKey(entry.getKey())){
                                letterResult.put(entry.getKey(), letterResult.get(entry.getKey()) + 1);
                            } else {
                                letterResult.put(entry.getKey(), 1);
                            }
                        }
                    }
                }
            }
        }
        if (letterResult.size() == 1){
            return letterResult.keySet().iterator().next();
        } else {
            final int[] temp = new int[]{-1};
            final String[] letterFind = new String[]{""};
            letterResult.forEach((k, v) -> {
                if (temp[0] == -1) {
                    temp[0] = v;
                } else {
                    if (temp[0] < v){
                        temp[0] = v;
                    }
                }
            });
            letterResult.forEach((k, v) -> {
                if (temp[0] == v){
                    letterFind[0] = k;
                }
            });
            return letterFind[0];
        }
    }

    private boolean isInner(Point pointValue, Point pointKey){
        int differenceX = Math.abs(pointKey.x - pointValue.x);
        int differenceY = Math.abs(pointKey.y - pointValue.y);
        return differenceX < EngineConfiguration.getService().getDeviation() && differenceY < EngineConfiguration.getService().getDeviation();
    }
}
