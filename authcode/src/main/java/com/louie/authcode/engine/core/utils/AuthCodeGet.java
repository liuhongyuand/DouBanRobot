package com.louie.authcode.engine.core.utils;

import com.louie.authcode.engine.config.EngineParameters;
import com.louie.authcode.utils.ImportFileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.UUID;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class AuthCodeGet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCodeGet.class);
    private static final String TEST_CODE_WEB = "https://www.douban.com/group/topic/88736817/?start=0#last";

    private static void refreshWeb(String url) throws IOException {
        final Connection connection = Jsoup.connect(url);
//        GlobalCollections.HEAD.forEach(connection::header);
        connection.validateTLSCertificates(false);
        Document document = connection.timeout(10 * 1000).get();
        Element element = document.getElementById("captcha_image");
        String src = element.attr("src");
        downloadSrc(src);
    }

    private static void downloadSrc(String url) throws IOException {
        URL srcURI = new URL(url);
        InputStream inputStream = srcURI.openStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(EngineParameters.PROJECT_ROOT + "/training/" + UUID.randomUUID() + ".jpg"));
        byte[] bytes = new byte[1024];
        for (int temp; (temp = inputStream.read(bytes, 0, bytes.length)) != -1;) {
            out.write(bytes, 0, temp);
        }
        out.close();
        inputStream.close();
        LOGGER.info("Download down.");
    }

    public static void main(String[] args) throws IOException {
        ImportFileUtils.initHEAD();
        for (int i = 0; i < 100; i++) {
            refreshWeb(TEST_CODE_WEB);
        }
    }

}
