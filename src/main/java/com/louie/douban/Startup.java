package com.louie.douban;

import com.louie.douban.util.ImportFileUtils;

/**
 * The Main
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Startup {

    static {
        ImportFileUtils.initUsers();
        ImportFileUtils.initHEAD();
    }

    public static void main(String[] args){
        System.out.println("Hello DouBan.");
    }

}
