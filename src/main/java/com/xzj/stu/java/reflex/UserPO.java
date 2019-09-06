package com.xzj.stu.java.reflex;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhijunxie
 * @date 2019/7/30 18:31
 */
@Data
public class UserPO extends BasePO implements Serializable {
    private static final long serialVersionUID = 6703216809599385427L;

    private UserPO() {

    }

    public UserPO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String publicName;

    protected String protectedName;

    private String name;

    private String address;

    private Integer age;

    private Integer sex;

    private String city;

    private void sayHello(String content, Integer age) {
        System.out.println(this.name + " is " + age + " old, say: " + content);
    }
}
