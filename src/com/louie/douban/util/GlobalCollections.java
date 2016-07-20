package com.louie.douban.util;

import com.louie.douban.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Global Collections
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class GlobalCollections {

    public static final Map<String, User> USERS = new HashMap<>();
    public static final Map<String, String> HEAD = new HashMap<>();
    public static final Map<String, String> COMMENT_HEAD = new HashMap<>();
    public static final Map<String, String> COMMENT_FORM = new HashMap<>();
    public static final Set<String> ExpiredTopic = new HashSet<>();

}
