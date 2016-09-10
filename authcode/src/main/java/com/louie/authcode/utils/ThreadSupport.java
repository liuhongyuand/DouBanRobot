package com.louie.authcode.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class ThreadSupport {

    public static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

}
