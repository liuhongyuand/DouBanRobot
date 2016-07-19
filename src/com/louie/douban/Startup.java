package com.louie.douban;

import com.louie.douban.util.XmlUtils;

/**
 * The Main
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Startup {

    static {
        XmlUtils.initUsers();
    }

    public static void main(String[] args){
        System.out.println("Hello World!");
    }

}
