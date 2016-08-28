package com.louie.douban.robot.authcode;

import com.louie.douban.robot.authcode.engine.AuthCodeProcess;
import com.louie.douban.robot.authcode.engine.core.AbstractPicProcess;
import com.louie.douban.robot.authcode.engine.core.CodeImportImpl;
import com.louie.douban.robot.authcode.engine.core.CodeProcessImpl;
import com.louie.douban.robot.authcode.engine.core.utils.PicUtil;
import com.louie.douban.util.Parameters;
import com.louie.douban.util.PointMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class CodeIdentify {

    private static String[] strings;

    private void outputRGB(String FILE){
        PicUtil.getRGBFromImageFile(FILE);
    }

    private void codeView(final String FILE){
        CodeImportImpl.isDivide = false;
        AuthCodeProcess process = new CodeImportImpl();
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(600, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Object[] results = process.process(FILE);
        int[][] imageRGB = (int[][]) results[0];
        BufferedImage image = new BufferedImage(imageRGB.length, imageRGB[0].length, BufferedImage.TYPE_INT_RGB);
        image = AbstractPicProcess.setBufferedImage(image, imageRGB);
        JLabel label = new JLabel(new ImageIcon(image));
        JLabel labe2 = new JLabel(new ImageIcon(FILE));
        label.setSize(image.getWidth(), image.getHeight());
        labe2.setBounds(image.getWidth(), 0, image.getWidth(), image.getHeight());
        frame.add(label);
        frame.add(labe2);
        frame.setVisible(true);
    }

    public void trainingPicIdentify(final String FILE, boolean importData){
        AuthCodeProcess process = new CodeImportImpl();
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(600, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Object[] results = process.process(FILE);
        List<?> letters = (List<?>) results[0];
        Set<?> buffers = (Set<?>) results[1];
        int temp = 10;
        for (Object bufferObj : buffers) {
            BufferedImage buffer = (BufferedImage) bufferObj;
            JLabel label = new JLabel("");
            label.setIcon(new ImageIcon(buffer));
            label.setBounds(temp, 0, buffer.getWidth(), buffer.getHeight());
            frame.add(label);
            temp = temp + buffer.getWidth() + 20;
        }
        frame.setVisible(true);
        if (importData) {
            int letterNum = 0;
            for (Object letterObj : letters) {
                List<Point> letterList = (List<Point>) letterObj;
                    if (!strings[letterNum].equals("")) {
                        PointMap.put(strings[letterNum], letterList);
                    }
                letterNum++;
            }
        }
        System.out.println("Map size: " + PointMap.mapSize());
    }

    public void getCode(final String FILE){
        AuthCodeProcess process = new CodeProcessImpl();
        Object[] results = process.process(FILE);
        List<?> letters = (List<?>) results[0];
        for (Object letterObj : letters) {
            List<Point> letterList = (List<Point>) letterObj;
            System.out.print(PointMap.getLetter(letterList, Parameters.deviation, Parameters.similarity));
        }
        System.out.println();
    }

    public static void main(String[] args){
        strings = new String[]{"", "", "t", "e", "", "", "", "", "", "", "", "", "", ""};
        final String FILE = Parameters.PATH + "/training/121a90fb-3f60-4859-94de-aa5b5d71bb0a.jpg";
        final String resources = Parameters.PATH + "/resources/captcha2.jpg";
//        new CodeIdentify().outputRGB(resources);
        new CodeIdentify().codeView(resources);
//        new CodeIdentify().trainingPicIdentify(resources, false);
//        new CodeIdentify().getCode(resources);
    }

}
