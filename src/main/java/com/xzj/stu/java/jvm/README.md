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

栈stack 实例引用指针--> 堆实例对象instanceOopDesc --> 方法区instanceKlass对象，获取方法信息，加载到栈中？？？



> 参考[JVM内存结构 VS Java内存模型 VS Java对象模型](http://www.hollischuang.com/archives/2509)


#### jvm类加载过程
![类加载过程](https://wx4.sinaimg.cn/large/005UybFhly1g71kxu96isj30w00asdiu.jpg)  

Java 虚拟机规范没有强制约束类加载过程的第一阶段（即：加载）什么时候开始，但对于“初始化”阶段，有着严格的规定。有且仅有 5 种情况必须立即对类进行“初始化”：

* 在遇到 new、putstatic、getstatic、invokestatic 字节码指令时，如果类尚未初始化，则需要先触发其初始化。
* 对类进行反射调用时，如果类还没有初始化，则需要先触发其初始化。
* 初始化一个类时，如果其父类还没有初始化，则需要先初始化父类。
* 虚拟机启动时，用于需要指定一个包含 main() 方法的主类，虚拟机会先初始化这个主类。
* 当使用 JDK 1.7 的动态语言支持时，如果一个 java.lang.invoke.MethodHandle 实例最后的解析结果为 REF_getStatic、REF_putStatic、REF_invokeStatic 的方法句柄，并且这个方法句柄所对应的类还没初始化，则需要先触发其初始化。

这 5 种场景中的行为称为对一个类进行主动引用，除此之外，其它所有引用类的方式都不会触发初始化，称为被动引用。

被动引用示例：
**PassiveReferenceDemo**

##### “非数组类”与“数组类”加载比较
* 非数组类加载阶段可以使用系统提供的引导类加载器，也可以由用户自定义的类加载器完成，开发人员可以通过定义自己的类加载器控制字节流的获取方式（如重写一个类加载器的 loadClass() 方法）  
* 数组类本身不通过类加载器创建，它是由 Java 虚拟机直接创建的，再由类加载器创建数组中的元素类。

##### 接口加载过程
接口加载过程与类加载过程稍有不同。

当一个类在初始化时，要求其父类全部都已经初始化过了，但是一个接口在初始化时，并不要求其父接口全部都完成了初始化，当真正用到父接口的时候才会初始化。

##### 1 加载
##### 2 验证
##### 3 准备
准备阶段是正式为类变量（或称“静态成员变量”）分配内存并设置初始值的阶段。这些变量（不包括实例变量）所使用的内存都在方法区中进行分配。  

初始值“通常情况下”是数据类型的零值（0, null...），假设一个类变量的定义为：
```
public static int value = 123;
```
那么变量 value 在准备阶段过后的初始值为 0 而不是 123，因为这时候尚未开始执行任何 Java 方法。

但是如果类字段的字段属性表中存在 ConstantValue 属性，那么在准备阶段 value 就会被初始化为 ConstantValue 属性所指定的值，假设上面类变量 value 的定义变为：  
``` 
public static final int value = 123;
```
那么在准备阶段虚拟机会根据 ConstantValue 的设置将 value 赋值为 123。


##### 4 解析
解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。


##### 5 初始化
