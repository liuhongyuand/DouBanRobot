package com.louie.douban.util;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class PointMap extends HashMap<String, HashSet<java.util.List<Point>>> implements Serializable{

    private static final PointMap POINT_MAP = new PointMap();

    private PointMap(){

    }

    public static void put(String letter, List<Point> pointList){
        if (POINT_MAP.containsKey(letter)){
            POINT_MAP.get(letter).add(pointList);
        } else {
            HashSet<List<Point>> lists = new HashSet<>();
            lists.add(pointList);
            POINT_MAP.put(letter, lists);
        }
    }

    public static String getLetter(List<Point> pointKeyList, double deviation, double similarity) {
        final String[] letter = {""};
        for (Map.Entry<String, HashSet<List<Point>>> entry : POINT_MAP.entrySet()){
            for (List<Point> PointList : entry.getValue()){
                int[] findNum = {0};
                pointKeyList.forEach((KeyPoint -> PointList.forEach(ValuePoint-> {
                    if (isInner(ValuePoint, KeyPoint, deviation)){
                        findNum[0]++;
                    }
                })));
                if (findNum[0]/(double)pointKeyList.size() > similarity){
                    letter[0] = entry.getKey();
                    break;
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
        return differenceX <= deviation && differenceY <= deviation;
    }

}