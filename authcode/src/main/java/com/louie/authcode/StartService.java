package com.louie.authcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by liuhongyu.louie on 2016/9/8.
 */
@SpringBootApplication
public class StartService {

    public static Boolean SystemIsOnline = true;

    public static void main(String[] args){
        Object[] objects = new Object[]{StartService.class};
        SpringApplication.run(objects, args);
    }

}
