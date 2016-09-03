package com.louie.authcode.engine.brain;

import com.louie.authcode.engine.brain.util.FileUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class PointMap extends HashMap<String, HashSet<java.util.List<Point>>> implements Serializable{

    private static final long serialVersionUID = 1L;
    private static PointMap POINT_MAP = new PointMap();

    static {
        Object object = FileUtils.FileToObject();
        if (object != null){
            POINT_MAP = (PointMap) object;
        }
    }

    private PointMap(){

    }

    public static int mapSize(){
        return POINT_MAP.size();
    }

    public static void put(String letter, List<Point> pointList){
        try {
            if (POINT_MAP.containsKey(letter)) {
                POINT_MAP.get(letter).add(pointList);
            } else {
                HashSet<List<Point>> lists = new HashSet<>();
                lists.add(pointList);
                POINT_MAP.put(letter, lists);
            }
        }finally {
            FileUtils.ObjectToFile(POINT_MAP);
        }
    }

    public static String getLetter(List<Point> pointKeyList, double deviation, double similarity) {
        final String[] letter = {""};
        for (Map.Entry<String, HashSet<List<Point>>> entry : POINT_MAP.entrySet()){
            for (List<Point> PointList : entry.getValue()){
                int[] findNum = {0};
                pointKeyList.forEach((KeyPoint -> {
                    int[] findNumInList = {0};
                    PointList.forEach(ValuePoint -> {
                        if (findNumInList[0] == 0 && isInner(ValuePoint, KeyPoint, deviation)){
                            findNumInList[0]++;
                        }
                    });
                    findNum[0] += findNumInList[0];
                }));
                if ((double) findNum[0] / (double)pointKeyList.size() > similarity){
                    int[] findNumReverse = {0};
                    PointList.forEach((ValuePoint -> {
                        int[] findNumListReverse = {0};
                        pointKeyList.forEach(KeyPoint -> {
                            if (findNumListReverse[0] == 0 && isInner(ValuePoint, KeyPoint, deviation)){
                                findNumListReverse[0]++;
                            }
                        });
                        findNumReverse[0] += findNumListReverse[0];
                    }));
                    if ((double) findNumReverse[0] / (double)PointList.size() > similarity) {
                        if ((PointList.size() > pointKeyList.size() ? (double)pointKeyList.size() / (double)PointList.size() : (double)PointList.size() / (double)pointKeyList.size()) > similarity) {
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

    private static boolean isInner(Point pointValue, Point pointKey, double deviation){
        int differenceX = Math.abs(pointKey.x - pointValue.x);
        int differenceY = Math.abs(pointKey.y - pointValue.y);
        return differenceX < deviation && differenceY < deviation;
    }

}