package com.louie.douban.util;

import com.louie.douban.model.User;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Utils for XML files
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class ImportFileUtils {

    private static final Properties propertiesCodeIdentify = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(ImportFileUtils.class);

    static {
        initHEAD();
    }
    public static void initUsers(){
        File myXML = new File(Parameters.PATH + "/conf/Users.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(myXML);
            Element root = document.getRootElement();
            Iterator users = root.elementIterator("User");
            while (users.hasNext()){
                Element userElement = (Element) users.next();
                User user = new User();
                user.setUsername(userElement.attributeValue("username"));
                user.setPassword(userElement.attributeValue("password"));
                user.setRemember(userElement.attributeValue("remember"));
                GlobalCollections.USERS.put(user.getUsername(), user);
            }
        } catch (DocumentException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void initHEAD(){
        Properties properties = new Properties();
        Properties propertiesComment = new Properties();
        Properties propertiesCommentFORM = new Properties();
        try {
            properties.load(new FileReader(new File(Parameters.PATH + "/conf/HEAD.properties")));
            propertiesComment.load(new FileReader(new File(Parameters.PATH + "/conf/COMMENT.properties")));
            propertiesCommentFORM.load(new FileReader(new File(Parameters.PATH + "/conf/COMMENT_FORM.properties")));
            propertiesCodeIdentify.load(new FileReader(new File(Parameters.PATH + "/conf/IdentifyParameters.properties")));
            properties.entrySet().forEach((k) -> GlobalCollections.HEAD.put(k.getKey().toString(), k.getValue().toString()));
            propertiesComment.entrySet().forEach((k) -> GlobalCollections.COMMENT_HEAD.put(k.getKey().toString(), k.getValue().toString()));
            propertiesCommentFORM.entrySet().forEach((k) -> GlobalCollections.COMMENT_FORM.put(k.getKey().toString(), k.getValue().toString()));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Properties getPropertiesCodeIdentify(){
        return propertiesCodeIdentify;
    }

    @Test
    public void TestInit(){
        initUsers();
        GlobalCollections.USERS.forEach((k, v) -> System.out.println(k + " " + v.getPassword() + " " + v.getRemember()));
        initHEAD();
    }

}
