package com.xzj.stu.java.serializable.staticmembervarriable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author zhijunxie
 * @date 2019/3/6
 */
public class StaticMemberVarriableSerializableTest implements Serializable {
    private static final long serialVersionUID = 1L;

    public static int staticVar = 896;

    public int abc;

    public int getAbc() {
        return abc;
    }

    public void setAbc(int abc) {
        this.abc = abc;
    }

    public StaticMemberVarriableSerializableTest(int param) {
        this.abc = param;
    }


    public static void main(String[] args) {
        try {
            File file = new File("D:" + File.separator + "ss.txt");
            OutputStream os = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(new StaticMemberVarriableSerializableTest(111));
            oos.close();

            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
            StaticMemberVarriableSerializableTest t = (StaticMemberVarriableSerializableTest) oin.readObject();
            oin.close();

            // 再读取，通过t.staticVar打印新的值
            System.out.println("staticVar:" + t.staticVar);
            System.out.println("abc:" + t.getAbc());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
