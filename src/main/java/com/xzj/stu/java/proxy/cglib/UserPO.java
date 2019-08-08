package com.xzj.stu.java.proxy.cglib;

/**
 * @author zhijunxie
 * @date 2019/7/30 18:31
 */
public class UserPO{
    public UserPO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private String id;

    private String name;

    private Integer age;

    private Integer sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
