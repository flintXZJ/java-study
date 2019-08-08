package com.xzj.stu.java.proxy.proxypattern;

/**
 * @author zhijunxie
 * @date 2019/3/18
 */
public class FoodServiceImpl implements FoodService {
    @Override
    public void makeChicken() {
        System.out.println("make chicken");
    }

    @Override
    public void makeNoodle() {
        System.out.println("make noodle");
    }
}
