package com.xzj.stu.java.proxy.proxypattern;

/**
 * 代理要表现得“就像是”真实实现类，所以需要实现 FoodService
 *
 * @author zhijunxie
 * @date 2019/3/18
 */
public class FoodServiceProxy implements FoodService {
    /**
     * 内部一定要有一个真实的实现类，当然也可以通过构造方法注入
     */
    private FoodService foodService = new FoodServiceImpl();

    @Override
    public void makeChicken() {
        System.out.println("我们马上要开始制作鸡肉了");
        // 如果我们定义这句为核心代码的话，那么，核心代码是真实实现类做的，
        // 代理只是在核心代码前后做些“无足轻重”的事情
        foodService.makeChicken();
        System.out.println("鸡肉制作完成啦");
    }

    @Override
    public void makeNoodle() {
        System.out.println("准备制作拉面~");
        foodService.makeNoodle();
        System.out.println("制作完成啦");
    }
}
