package com.louie.douban.robot.pic;

import com.louie.douban.robot.services.AbstractPicProcess;
import com.louie.douban.robot.services.CodeImport;
import com.louie.douban.robot.services.CodeProcess;
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

    public void trainingPicIdentify(String FILE, boolean importData){
        AbstractPicProcess process = new CodeImport();
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

    public void getCode(String FILE){
        AbstractPicProcess process = new CodeProcess();
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
        final String FILE = Parameters.PATH + "/training/a62c8c70-970a-4a4d-875c-dd23e6b83428.jpg";
        final String resources = Parameters.PATH + "/resources/d70cf794-536c-4641-b009-9e342ebb1143.jpg";
        new CodeIdentify().trainingPicIdentify(FILE, false);
        new CodeIdentify().getCode(FILE);
    }

}
