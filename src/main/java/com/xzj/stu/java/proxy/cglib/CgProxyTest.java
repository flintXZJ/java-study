package com.xzj.stu.java.proxy.cglib;

/**
 * @author zhijunxie
 * @date 2019/8/8 20:29
 */
public class CgProxyTest {
    public static void main(String[] args) {
        //被代理的对象
//        UserService userService = new UserServiceImpl();
        CgProxy cgProxy = new CgProxy(UserServiceImpl.class);

        UserService proxyObject = (UserService) cgProxy.getProxyObject();

        proxyObject.save(new UserPO("xzj", 28));
        proxyObject.getUser("15679812570247");

        //与jdk的动态代理不同，cglib可以代理未实现接口的类。
        //另外CGLIB是通过继承的方式做的动态代理，因此如果某个类被标记为final，那么它是无法使用CGLIB做动态代理的
        CgProxy personCg = new CgProxy(Person.class);
        Person person = (Person)personCg.getProxyObject();
        person.talk();
        person.eat();
    }
}
