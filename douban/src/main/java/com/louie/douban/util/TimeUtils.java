package com.louie.douban.util;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class TimeUtils {

    public static void sleep(int second){
        try {
            Thread.sleep(second * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
