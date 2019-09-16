package com.xzj.stu.java.io;

import java.io.*;

public class Iodemo {
    public static void main(String[] args) {
        //字节流输入输出
//        try (FileInputStream fileInputStream = new FileInputStream(new File("D:\\test.txt")); FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\test2.txt"))){
//            byte[] bytes = new byte[1024];
//            int read = fileInputStream.read(bytes);
//            while (read != -1) {
//                fileOutputStream.write(bytes, 0, read);
//                read = fileInputStream.read(bytes);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //字符流输入输出
//        try (FileReader fileReader = new FileReader(new File("D:\\test.txt")); FileWriter fileWriter = new FileWriter(new File("D:\\test2.txt"))){
//            char[] chars = new char[1024];
//            int read = 0;
//            while ((read=fileReader.read(chars)) != -1) {
//                System.out.println(new String(chars));
//            }
//            fileWriter.write("xzj test");
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
