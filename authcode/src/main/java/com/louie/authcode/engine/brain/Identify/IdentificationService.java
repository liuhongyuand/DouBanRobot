package com.louie.authcode.engine.brain.Identify;

import com.louie.authcode.engine.brain.PointMap;

import java.awt.*;

/**
 * Created by liuhongyu.louie on 2016/9/7.
 */
public interface IdentificationService {

    String identifyLetter(java.util.List<Point> pointKeyList, PointMap POINT_MAP);

}
