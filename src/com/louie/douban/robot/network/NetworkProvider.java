package com.louie.douban.robot.network;

import com.louie.douban.util.GlobalCollections;
import com.louie.douban.util.ImportFileUtils;
import com.louie.douban.util.Parameters;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.Security;

/**
 * Provide the connections
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class NetworkProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkProvider.class);

    public static void send(){
        try {
            Security.addProvider(
                    new com.sun.net.ssl.internal.ssl.Provider());
            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(Parameters.HOSTPAGE, 443);
            socket.getOutputStream().write(("Host: www.douban.com\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                    "Referer: https://www.douban.com/\n" +
                    "Accept-Encoding: gzip, deflate, sdch, br\n" +
                    "Accept-Language: zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2\n" +
                    "Cookie: ll=\"108296\"; bid=0u3ApPkCXjY; ct=y; _vwo_uuid_v2=0473AD2C69B807AA57AE9D4711DA312F|ec53f70935cd8f956172a1de802d3bf2; __utmt=1; ap=1; dbcl2=\"147821903:HFGwsEbWve4\"; ck=RQ3h; push_noty_num=0; push_doumail_num=0; _pk_id.100001.8cb4=6066777e20bdf902.1468242445.8.1468938398.1468933461.; _pk_ses.100001.8cb4=*; __utma=30149280.1665885727.1468242446.1468933459.1468935724.6; __utmb=30149280.18.7.1468938258045; __utmc=30149280; __utmz=30149280.1468242446.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=30149280.14782\n").getBytes());
            InputStream inputStream = socket.getInputStream();
            int i;
            byte[] bytes = new byte[1024];
            while ((i = inputStream.read(bytes, 0, bytes.length)) > 0){
                System.out.println(new String(bytes, 0, i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend(){
        ImportFileUtils.initHEAD();
        send();
    }

}
