package com.louie.authcode;

import com.louie.authcode.engine.AuthCodeProcess;
import com.louie.authcode.engine.core.AbstractPicProcess;
import com.louie.authcode.engine.core.CodeImportImpl;
import com.louie.authcode.engine.core.CodeProcessImpl;
import com.louie.authcode.engine.core.cut.v2.DivideProcess;
import com.louie.authcode.engine.core.utils.PicUtil;
import com.louie.authcode.engine.brain.PointMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import static com.louie.authcode.engine.EngineParameters.*;

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
        int[][] imageRGB = DivideProcess.forTestDivide((int[][]) results[0]);
        BufferedImage image = new BufferedImage(imageRGB.length, imageRGB[0].length, BufferedImage.TYPE_INT_RGB);
        image = AbstractPicProcess.setBufferedImage(image, imageRGB);
        JLabel label = new JLabel(new ImageIcon(image));
        JLabel labe2 = new JLabel(new ImageIcon(FILE));
        label.setSize(image.getWidth(), image.getHeight());
        labe2.setBounds(image.getWidth(), 0, 250, 70);
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
            System.out.print(PointMap.getLetter(letterList, deviation, similarity));
        }
        System.out.println();
    }

    public static void main(String[] args){
        strings = new String[]{"", "", "t", "e", "", "", "", "", "", "", "", "", "", ""};
        final String FILE = PROJECT_ROOT + "/training/0fa82c65-1040-417c-a377-f23a95b21e62.jpg";
        final String resources = PROJECT_ROOT + "/resources/captcha.jpg";
//        new CodeIdentify().outputRGB(resources);
//        new CodeIdentify().codeView(resources);
        new CodeIdentify().trainingPicIdentify(resources, false);
//        new CodeIdentify().getCode(resources);
    }

}
