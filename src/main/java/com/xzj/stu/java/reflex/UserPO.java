package com.xzj.stu.java.reflex;

import java.io.Serializable;

/**
 * @author zhijunxie
 * @date 2019/7/30 18:31
 */
public class UserPO implements Serializable {
    private static final long serialVersionUID = 6703216809599385427L;

    private UserPO() {

    }

    public UserPO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private String name;

    private String address;

    private Integer age;

    private Integer sex;

    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private void sayHello(String content, Integer age) {
        System.out.println(this.name + " is " + age + " old, say: " + content);
    }
}
