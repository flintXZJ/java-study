
#### 代理模式（Proxy Pattern）
代理是一种常用的设计模式，其目的就是为其他对象提供一个代理以控制对某个对象的访问。代理类负责为委托类预处理消息，过滤消息并转发消息，以及进行消息被委托类执行后的后续处理。

![UML图](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1552910182076&di=bed17367981b921d975500389dcd2bde&imgtype=0&src=http%3A%2F%2Fww1.sinaimg.cn%2Flarge%2F006rMFVegy1fdpnfxbh3oj30j60ayglq.jpg)

为了保持行为的一致性，代理类和委托类通常会实现相同的接口，所以在访问者看来两者没有丝毫的区别。


通过代理类这中间一层，能有效控制对委托类对象的直接访问，也可以很好地隐藏和保护委托类对象，同时也为实施不同控制策略预留了空间，从而在设计上获得了更大的灵活性。

Java 动态代理机制以巧妙的方式近乎完美地实践了代理模式的设计理念。

#### Java 动态代理

##### InvocationHandler
> public object invoke(Object obj,Method method, Object[] args)  
> 第一个参数obj一般是指代理类，method是被代理的方法，如上例中的request()，args为该方法的参数数组
##### Proxy 动态代理类
![Proxy](https://ws4.sinaimg.cn/large/005UybFhly1g5sgpsvddsj30eb0di3yv.jpg)
 

##### Proxy的使用
com.xzj.stu.java.proxy.dynamic中代码实现


#### CGLIB
com.xzj.stu.java.proxy.cglib中代码实现

```
public Object getProxyObject() {
    Enhancer enhancer = new Enhancer();
    //设置父类
    enhancer.setSuperclass(this.target.getClass());
    //设置回调，在调用父类方法时，回调 this.intercept()
    enhancer.setCallback(this);
    //创建代理对象
    return enhancer.create();
}
```

它的原理是：生成一个父类 enhancer.setSuperclass(this.target.getClass()) 的子类enhancer.create(),
然后对父类的方法进行拦截enhancer.setCallback(this)



从以上两种代理方式可以看出，实现AOP的关键是：
动态代理，即将需要用的接口、类再包装一层，
通过动态修改字节码文件实现各种拦截与通知


两者(JDK动态代理、CGLIB)都需要：要代理真实对象的实例。

比如：在Spring MVC的Controller层一般@Autowired是Service接口，但带有@Service标识的却是实现Service接口的实体类，
这样对于JDK动态代理来说已经足以生成代理类了
(其实，不过是cglib还是jdk的动态代理，你直接@Autowired Service接口实现类，也是可以注入成功的，
但不如注入Service接口灵活)，
大家在跟踪代码的时候可以看一下Spring注入的bean真正的类型，你就可以发现它是代理生成的实例。

带有注解标识的接口或者在Spring.XML中配置的bean会在Spring初始化的时候，被Spring通过反射加载实例化到Spring容器中。


参考：  
[Java JDK 动态代理（AOP）使用及实现原理分析[精品长文]](https://juejin.im/post/5d31c1b7f265da1b9421845d)  
[关于Spring AOP与IOC的个人思考[精品长文]](https://juejin.im/post/5d31d192e51d45105d63a5ea)