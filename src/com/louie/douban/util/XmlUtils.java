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
import java.util.Iterator;

/**
 * Utils for XML files
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class XmlUtils {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUtils.class);

    public static void initUsers(){
        File myXML = new File(System.getProperties().getProperty("user.dir") + "/conf/Users.xml");
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

    @Test
    public void TestInit(){
        initUsers();
        GlobalCollections.USERS.forEach((k, v) -> System.out.println(k + " " + v.getPassword() + " " + v.getRemember()));
    }

}
