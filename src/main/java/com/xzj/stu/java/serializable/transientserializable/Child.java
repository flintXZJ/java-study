package com.xzj.stu.java.serializable.transientserializable;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 1. 序列化并不保存静态变量
 * 2. 要想将父类对象也序列化，就需要让父类也实现Serializable 接口
 * 3. Transient 关键字阻止该变量被序列化到文件中
 *  在变量声明前加上Transient 关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null
 *
 * @author zhijunxie
 * @date 2019/3/6
 */
@Setter
@Getter
public class Child extends Parent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String attr4;

    private transient String attr5;

    public Child(String attr1, String attr2, String attr3, String attr4, String attr5) {
        super(attr1, attr2, attr3);
        this.attr4 = attr4;
        this.attr5 = attr5;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file = new File("D:" + File.separator + "s.txt");
        OutputStream os = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(new Child("str1", "str2", "str3", "str4", "str5"));
        oos.close();

        InputStream is = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(is);
        Child so = (Child) ois.readObject();
        System.out.println("str1 = " + so.getAttr1());
        System.out.println("str2 = " + so.getAttr2());
        System.out.println("str3 = " + so.getAttr3());
        System.out.println("str4 = " + so.getAttr4());
        System.out.println("str5 = " + so.getAttr5());
        ois.close();
    }
}
