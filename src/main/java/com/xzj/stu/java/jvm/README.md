#### jvm内存结构
![jvm内存结构](http://www.hollischuang.com/wp-content/uploads/2018/06/QQ20180624-150918.png)

#### java内存模型-jmm
> 参考[深入理解Java内存模型](https://www.jianshu.com/p/15106e9c4bf3)
![jmm](http://www.hollischuang.com/wp-content/uploads/2018/06/11.png)



#### java对象模型
> 参考[深入理解多线程（二）—— Java的对象模型](https://www.hollischuang.com/archives/1910)
![java对象模型](http://www.hollischuang.com/wp-content/uploads/2018/06/20170615230126453.jpeg)

Class类加载时方法区创建instanceKlass对象；类实例化时（new、反射、clone、反序列化）在堆(heap)中创建instanceOopDesc对象，
instanceOopDesc对象头中—_metadata._klass指向方法区的instanceKlass对象；线程栈(stack)中引用指针指向堆中的instanceOopDesc对象。
那么java对象模型中，线程执行实例的方法时，是怎么找到方法的？（已知instanceKlass对象中存储着类方法列表）



> 参考[JVM内存结构 VS Java内存模型 VS Java对象模型](http://www.hollischuang.com/archives/2509)