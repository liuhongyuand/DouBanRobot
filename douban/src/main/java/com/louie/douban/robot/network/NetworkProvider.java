package com.louie.douban.robot.network;

import com.louie.douban.util.GlobalCollections;
import com.louie.douban.util.ImportFileUtils;
import com.louie.douban.util.Parameters;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.Security;

/**
 * Provide the connections
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class NetworkProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkProvider.class);

    public static void send() {
        try {
            final Connection connection = Jsoup.connect(Parameters.HOSTPAGE);
            GlobalCollections.HEAD.forEach(connection::header);
            connection.validateTLSCertificates(false);
            Document document = connection.timeout(10 * 1000).get();
            Elements elements = document.getElementsByClass("pl");
            elements.forEach((element -> {
                String url = element.getElementsByTag("a").attr("href");
                if (!GlobalCollections.ExpiredTopic.contains(url)){
                    LOGGER.info(element.getElementsByClass("title").text());
                    replay(url);
                    GlobalCollections.ExpiredTopic.add(url);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replay(String url) {
        try {
            final Connection connection = Jsoup.connect(url + "add_comment");
            GlobalCollections.COMMENT_HEAD.forEach(connection::header);
            connection.data("ck", "q-Zm")
                    .data("rv_comment", "哇。")
                    .data("start", "0")
                    .data("submit_btn", "点我啊");
            connection.validateTLSCertificates(false);
            Connection.Response response = connection.timeout(10 * 1000).method(Connection.Method.POST).execute();
            LOGGER.info(response.body() + " " + response.statusCode() + " " + response.statusMessage() + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend() {
        ImportFileUtils.initHEAD();
        while (true) {
            send();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        replay();
    }

}
