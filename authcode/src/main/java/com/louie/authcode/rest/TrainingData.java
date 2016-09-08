package com.louie.authcode.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhongyu.louie on 2016/9/8.
 */
@RestController
public class TrainingData {

    @RequestMapping(method = RequestMethod.POST, path = "/training")
    public String trainingData(){
        return "hello";
    }

}
