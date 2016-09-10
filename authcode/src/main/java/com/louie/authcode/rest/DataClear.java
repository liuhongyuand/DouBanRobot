package com.louie.authcode.rest;

import com.louie.authcode.engine.brain.PointMap;
import com.louie.authcode.engine.model.Response;
import com.louie.authcode.engine.model.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
@RestController
@RequestMapping(path = "/{userId}")
public class DataClear {

    @RequestMapping(method = RequestMethod.POST, path = "/clear")
    public Response clearData(){
        PointMap.clearData();
        return new ResponseBody("1", "The data has been clear.");
    }

}
