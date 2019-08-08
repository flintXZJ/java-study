package com.xzj.stu.java.proxy.cglib;

/**
 * @author zhijunxie
 * @date 2019/8/8 20:29
 */
public class CgProxyTest {
    public static void main(String[] args) {
        //被代理的对象
        UserService userService = new UserServiceImpl();
        CgProxy cgProxy = new CgProxy(userService);

        UserService proxyObject = (UserService) cgProxy.getProxyObject();

        proxyObject.save(new UserPO("xzj", 28));
        proxyObject.getUser("15679812570247");
    }
}
