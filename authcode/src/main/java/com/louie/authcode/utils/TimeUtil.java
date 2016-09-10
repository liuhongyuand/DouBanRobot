package com.louie.authcode.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class TimeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
