package com.louie.authcode.engine.brain;

import com.louie.authcode.engine.EngineConfiguration;
import com.louie.authcode.engine.brain.Identify.IdentificationService;
import com.louie.authcode.engine.brain.utils.FileUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
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

    public static String getAuthCode(List<?> letterSet){
        IdentificationService identificationService = EngineConfiguration.getService().getIdentificationService();
        final StringBuilder AUTHCODE= new StringBuilder("");
        letterSet.forEach((letter -> AUTHCODE.append(identificationService.identifyLetter((List<Point>) letter, POINT_MAP))));
        return AUTHCODE.toString();
    }

}