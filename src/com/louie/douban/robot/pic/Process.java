package com.louie.douban.robot.pic;

import com.louie.douban.util.Parameters;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Process the pic
 * Created by liuhongyu.louie on 2016/7/20.
 */
public class Process {

    public static void processPic() {
        File pic = new File(Parameters.PATH + "/resources/captcha.jpg");
        try {
            int difRate = 10000000/3;
            BufferedImage image = ImageIO.read(pic);
            JFrame frame = new JFrame("Test");
            frame.setLayout(new BorderLayout());
            frame.setSize(image.getWidth(), image.getHeight() * 2);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    if ((image.getRGB(i, j) - image.getRGB(i, j - 1) < difRate && image.getRGB(i, j) - image.getRGB(i, j - 1) > -difRate && image.getRGB(i, j) - image.getRGB(i, j + 1) < difRate && image.getRGB(i, j) - image.getRGB(i, j + 1) > -difRate) && (image.getRGB(i, j) - image.getRGB(i + 1, j) < difRate && image.getRGB(i, j) - image.getRGB(i + 1, j) > -difRate && image.getRGB(i, j) - image.getRGB(i - 1, j) < difRate && image.getRGB(i, j) - image.getRGB(i - 1, j) > -difRate)) {
//                        image.setRGB(i, j, 0);
                    }else {
                        image.setRGB(i, j, 255255255);
                    }
                }
            }
            JLabel label = new JLabel("");
            label.setIcon(new ImageIcon(image));
            frame.add(label, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testPic() {
//        processPic();
//    }

    public static void main(String[] args){
        processPic();
    }

}
