package com.louie.douban.util;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class PointMap extends HashMap<String, HashSet<java.util.List<Point>>> implements Serializable{

    public void put(String letter, List<Point> pointList){
        if (this.containsKey(letter)){
            this.get(letter).add(pointList);
        } else {
            HashSet<List<Point>> lists = new HashSet<>();
            lists.add(pointList);
            put(letter, lists);
        }
    }

    public String getLetter(List<Point> pointKeyList, int deviation, double similarity) {
        final String[] letter = {null};
        for (Map.Entry<String, HashSet<List<Point>>> entry : entrySet()){
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
            if (letter[0] != null){
                break;
            }
        }
        return letter[0];
    }

    private boolean isInner(Point pointValue, Point pointKey, int deviation){
        int differenceX = Math.abs(pointKey.x - pointValue.x);
        int differenceY = Math.abs(pointKey.y - pointValue.y);
        return differenceX <= deviation && differenceY <= deviation;
    }

}