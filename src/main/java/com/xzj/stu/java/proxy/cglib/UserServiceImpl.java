package com.xzj.stu.java.proxy.cglib;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhijunxie
 * @date 2019/8/8 20:28
 */
public class UserServiceImpl implements UserService {
    @Override
    public void save(UserPO userPO) {
        System.out.println("save user success...");
    }

    @Override
    public UserPO getUser(String id) {
        UserPO userPO = new UserPO("test", 20);
        userPO.setId(id);
        System.out.println("getUser="+JSONObject.toJSONString(userPO));
        return userPO;
    }
}
