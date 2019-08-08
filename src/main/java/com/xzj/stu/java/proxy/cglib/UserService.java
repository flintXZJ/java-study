package com.xzj.stu.java.proxy.cglib;

/**
 * @author zhijunxie
 * @date 2019/8/8 20:26
 */
public interface UserService {
    void save(UserPO userPO);

    UserPO getUser(String id);
}
