## synchronized
> 参考：  
> [深入理解多线程系列](https://www.hollischuang.com/archives/1883)  
> [JVM源码分析之Object.wait/notify实现](https://www.jianshu.com/p/f4454164c017)  
> [从jvm源码看synchronized](https://www.cnblogs.com/kundeg/p/8422557.html)  
> [Java Synchronized实现原理](http://bigdatadecode.club/JavaSynchronizedTheory.html)  
> [《Java多线程基础》](http://www.cnblogs.com/hanganglin/articles/3517178.html)  


[toc]

### 1、synchronized的用法
> 见com.xzj.stu.multithread.lock.sync.SyncTest.java
> 1) synchronized {代码块}：对代码块执行线程同步，效率要高于对整个函数执行同步，推荐使用这种方法
> 2) synchronized {普通方法}：同一时间只能有一个线程访问同一个对象的该方法。缺点：同步整个方法效率不高。 synchronized void method() { ... }相当于void method( synchronized(this) { ... } )
> 3) synchronized {static方法}：加锁的对象是类，同一时间，该类的所有对象中的synchronized static方法只能有一个线程访问。 class Foo { public synchronized static fun(){...}}等价于在class Foo { public static fun(){ synchronized(Foo.class){ ... } }}
> 4) synchronized {run方法}：此时为同步普通方法的特殊情况，由于在线程的整个生命期内run方法一直在运行，因此同一个Runnable对象的多个线程只能串行运行。
> 重点理解以下细节:
> 1) 当多个并发线程访问同一个对象的同步代码块时，一段时间内只能有一个线程得到执行，其他线程必须等待当前线程执行完代码块后再执行代码；  
> 2) 当一个线程访问一个对象的同步代码块时，其他线程可以访问该对象的中的非同步代码块；  
> 3) 当一个线程访问一个对象的同步代码块时，其他线程对该对象中的所有同步代码块均不能访问

#### a、修饰普通方法
```
public synchronized void fun1() {
    count++;
}
```
javap对代码反编译: 
```
  public synchronized void fun1();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: dup
         2: getfield      #2                  // Field count:I
         5: iconst_1
         6: iadd
         7: putfield      #2                  // Field count:I
        10: return 
```
#### b、修饰静态方法
``` 
public static synchronized void fun2() {
    sum = sum + 100L;
}
```
javap对代码反编译: 
```
  public static synchronized void fun2();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
    Code:
      stack=4, locals=0, args_size=0
         0: getstatic     #3                  // Field sum:J
         3: ldc2_w        #4                  // long 100l
         6: ladd
         7: putstatic     #3                  // Field sum:J
        10: return 
```
#### c、修饰代码块
```
public void fun4() {
    synchronized (SynchronizedDemo.class) {
        sum = sum +200L;
    }
} 
```
javap对代码反编译
```
  public void fun4();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=4, locals=3, args_size=1
         0: ldc           #6                  // class com/xzj/stu/java/lock/sync/SynchronizedDemo
         2: dup
         3: astore_1
         4: monitorenter
         5: getstatic     #3                  // Field sum:J
         8: ldc2_w        #7                  // long 200l
        11: ladd
        12: putstatic     #3                  // Field sum:J
        15: aload_1
        16: monitorexit
        17: goto          25
        20: astore_2
        21: aload_1
        22: monitorexit
        23: aload_2
        24: athrow
        25: return 
```
从代码反编译之后可以看出jvm对于同步方法和同步代码块的处理方式不同。

对于同步方法，jvm采用ACC_SYNCHRONIZED标识符来实现同步。  
对于同步代码块，jvm采用monitorenter、monitorexit指令来实现同步。
具体内容参见[The Java® Virtual Machine Specification 2.11.10 Synchronization](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.11.10)的介绍。

因此：   
同步方法通过ACC_SYNCHRONIZED标识符隐式的对方法进行加锁。当线程要执行的方法被标注上ACC_SYNCHRONIZED时，执行线程将先获取monitor，获取成功之后才能执行方法体，方法执行完后再释放monitor。
同步代码块通过monitorenter和monitorexit执行来进行加锁。当线程执行到monitorenter的时候要先获得所锁，才能执行后面的方法。当线程执行到monitorexit的时候则要释放锁。moniterenter和moniterexit指令是通过monitor对象实现的。
*每个对象自身维护这一个被加锁次数的计数器，当计数器数字为0时表示可以被任意线程获得锁。当计数器不为0时，只有获得锁的线程才能再次获得锁。即可重入锁。*对吗？是对象维护的锁计数器吗？还是monitor？
错，是monitor维护的锁计数器。

synchronized是如何对对象加锁的呢？
首先需要了解对象、类是如何在jvm中实现的。

1、java对象模型

2、java对象头

3、monitor的实现

### 2、synchronized的jvm实现
#### 2.1、java对象模型
> 在面向对象的软件中，对象（Object）是某一个类（Class）的实例  
    
那对象在jvm中是什么结构？  
HotSpot是基于c++实现，而c++是一门面向对象的语言，本身是具备面向对象基本特征的，所以Java中的对象表示，最简单的做法是为每个Java类生成一个c++类与之对应。
但HotSpot JVM并没有这么做，而是设计了一个OOP-Klass Model。OOP（Ordinary Object Pointer）指的是普通对象指针，而Klass用来描述对象实例的具体类型。

oops模块可以分成两个相对独立的部分：OOP框架和Klass框架。  
在[oopsHierarchy.hpp](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/oopsHierarchy.hpp)里定义了oop和klass各自的体系。

**OOP**
OOP体系：  
``` 
//定义了oops共同基类
typedef class   oopDesc*                            oop;
//表示一个Java类型实例
typedef class   instanceOopDesc*            instanceOop;
//表示一个Java方法
typedef class   methodOopDesc*                    methodOop;
//表示一个Java方法中的不变信息
typedef class   constMethodOopDesc*            constMethodOop;
//记录性能信息的数据结构
typedef class   methodDataOopDesc*            methodDataOop;
//定义了数组OOPS的抽象基类
typedef class   arrayOopDesc*                    arrayOop;
//表示持有一个OOPS数组
typedef class   objArrayOopDesc*            objArrayOop;
//表示容纳基本类型的数组
typedef class   typeArrayOopDesc*            typeArrayOop;
//表示在Class文件中描述的常量池
typedef class   constantPoolOopDesc*            constantPoolOop;
//常量池告诉缓存
typedef class   constantPoolCacheOopDesc*   constantPoolCacheOop;
//描述一个与Java类对等的C++类
typedef class   klassOopDesc*                    klassOop;
//表示对象头
typedef class   markOopDesc*                    markOop;
```
在Java程序运行过程中，每创建一个新的对象，在JVM内部就会相应地创建一个对应类型的OOP对象。在HotSpot中，根据JVM内部使用的对象业务类型，具有多种oopDesc的子类。除了oppDesc类型外，opp体系中还有很多instanceOopDesc、arrayOopDesc 等类型的实例，他们都是oopDesc的子类。

![](https://www.hollischuang.com/wp-content/uploads/2017/12/OOP%E7%BB%93%E6%9E%84.png)  

这些OOPS在JVM内部有着不同的用途，例如，instanceOopDesc表示类实例，arrayOopDesc表示数组。也就是说，当我们使用new创建一个Java对象实例的时候，JVM会创建一个instanceOopDesc对象来表示这个Java对象。同理，当我们使用new创建一个Java数组实例的时候，JVM会创建一个arrayOopDesc对象来表示这个数组对象。

在HotSpot中，oopDesc类定义在[oop.hpp](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/oop.hpp)
中，instanceOopDesc定义在[instanceOop.hpp](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/instanceOop.hpp)中，
arrayOopDesc定义在[arrayOop.hpp](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/arrayOop.hpp)中。  
``` 
class oopDesc {
  friend class VMStructs;
  private:
      volatile markOop  _mark;
      union _metadata {
        wideKlassOop    _klass;
        narrowOop       _compressed_klass;
      } _metadata;

  private:
      // field addresses in oop
      void*     field_base(int offset)        const;

      jbyte*    byte_field_addr(int offset)   const;
      jchar*    char_field_addr(int offset)   const;
      jboolean* bool_field_addr(int offset)   const;
      jint*     int_field_addr(int offset)    const;
      jshort*   short_field_addr(int offset)  const;
      jlong*    long_field_addr(int offset)   const;
      jfloat*   float_field_addr(int offset)  const;
      jdouble*  double_field_addr(int offset) const;
      address*  address_field_addr(int offset) const;
}

class instanceOopDesc : public oopDesc {
}
class arrayOopDesc : public oopDesc {
}
```

HotSpot虚拟机中，对象在内存中存储的布局可以分为三块区域：对象头、实例数据和对齐填充。
在虚拟机内部，一个Java对象对应一个instanceOopDesc的对象。
其中对象头包含了两部分内容：_mark和_metadata，而实例数据则保存在oopDesc中定义的各种field中。
关于锁标记、GC分代等信息均保存在_mark中。_metadata是一个共用体，其中_klass是普通指针，_compressed_klass是压缩类指针。指针执行的就是该对象的类型信息klass对象instanceKlass

**Klass**
klass体系
``` 
//klassOop的一部分，用来描述语言层的类型
class  Klass;
//在虚拟机层面描述一个Java类
class   instanceKlass;
//专有instantKlass，表示java.lang.Class的Klass
class     instanceMirrorKlass;
//专有instantKlass，表示java.lang.ref.Reference的子类的Klass
class     instanceRefKlass;
//表示methodOop的Klass
class   methodKlass;
//表示constMethodOop的Klass
class   constMethodKlass;
//表示methodDataOop的Klass
class   methodDataKlass;
//最为klass链的端点，klassKlass的Klass就是它自身
class   klassKlass;
//表示instanceKlass的Klass
class     instanceKlassKlass;
//表示arrayKlass的Klass
class     arrayKlassKlass;
//表示objArrayKlass的Klass
class       objArrayKlassKlass;
//表示typeArrayKlass的Klass
class       typeArrayKlassKlass;
//表示array类型的抽象基类
class   arrayKlass;
//表示objArrayOop的Klass
class     objArrayKlass;
//表示typeArrayOop的Klass
class     typeArrayKlass;
//表示constantPoolOop的Klass
class   constantPoolKlass;
//表示constantPoolCacheOop的Klass
class   constantPoolCacheKlass;
```
![](https://www.hollischuang.com/wp-content/uploads/2017/12/klass.png)  
Klass向JVM提供两个功能：
* 实现语言层面的Java类（在Klass基类中已经实现）
* 实现Java对象的分发功能（由Klass的子类提供虚函数实现）

**instanceKlass**  
JVM在运行时，需要一种用来标识Java内部类型的机制。在HotSpot中的解决方案是：为每一个已加载的Java类创建一个instanceKlass对象，用来在JVM层表示Java类。
    
[instanceKlass](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/instanceKlass.hpp)  
``` 
 //类拥有的方法列表
  objArrayOop     _methods;
  //描述方法顺序
  typeArrayOop    _method_ordering;
  //实现的接口
  objArrayOop     _local_interfaces;
  //继承的接口
  objArrayOop     _transitive_interfaces;
  //域
  typeArrayOop    _fields;
  //常量
  constantPoolOop _constants;
  //类加载器
  oop             _class_loader;
  //protected域
  oop             _protection_domain;
      ....
```
可以看到，一个类该具有的东西，这里面基本都包含了。

这里还有个点需要简单介绍一下。

在JVM中，对象在内存中的基本存在形式就是oop。那么，对象所属的类，在JVM中也是一种对象，因此它们实际上也会被组织成一种oop，即klassOop。同样的，对于klassOop，也有对应的一个klass来描述，它就是klassKlass，也是klass的一个子类。klassKlass作为oop的klass链的端点。关于对象和数组的klass链大致如下图：  

![](https://www.hollischuang.com/wp-content/uploads/2017/12/400_ac3_932.png)  

在这种设计下，JVM对内存的分配和回收，都可以采用统一的方式来管理。oop-klass-klassKlass关系如图：

![](http://www.hollischuang.com/wp-content/uploads/2017/12/2579123-5b117a7c06e83d84.png)  

**内存存储**  
关于一个Java对象，他的存储是怎样的？  
对象的实例（instantOopDesc)保存在堆上，对象的元数据（instantKlass）保存在方法区，对象的引用保存在栈上。  
``` 
class Model
{
    public static int a = 1;
    public int b;

    public Model(int b) {
        this.b = b;
    }
}

public static void main(String[] args) {
    int c = 10;
    Model modelA = new Model(2);
    Model modelB = new Model(3);
}
```
存储结构如下：  

![](http://www.hollischuang.com/wp-content/uploads/2017/12/20170615230126453.jpeg)  

**java总结**

每一个Java类，在被JVM加载的时候，JVM会给这个类创建一个instanceKlass，保存在方法区，用来在JVM层表示该Java类。当我们在Java代码中，使用new创建一个对象的时候，JVM会创建一个instanceOopDesc对象，这个对象中包含了两部分信息，对象头以及元数据。对象头中有一些运行时数据，其中就包括和多线程相关的锁的信息。元数据其实维护的是指针，指向的是对象所属的类的instanceKlass。

 
#### 2.2、对象头
根据上文，我们知道对象头包含_mark和_metadata两部分。其中_metadata存储的是对象的类数据信息(元数据)。_mark ，即mark word， 与java的并发有关。  
考虑到虚拟机的空间效率，Mark Word被设计成一个非固定的数据结构以便在极小的空间内存储尽量多的信息，它会根据对象的状态复用自己的存储空间。  
下图描述了在32位虚拟机上，在对象不同状态时 mark word各个比特位区间的含义。  
![](https://www.hollischuang.com/wp-content/uploads/2018/01/ObjectHead-1024x329.png)  
在HotSpot的源码中我们可以找到关于对象头对象的定义，会一一印证上图的描述。对应与[markOop.hpp](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/oops/markOop.hpp)类。
``` 
enum { age_bits                 = 4,
      lock_bits                = 2,
      biased_lock_bits         = 1,
      max_hash_bits            = BitsPerWord - age_bits - lock_bits - biased_lock_bits,
      hash_bits                = max_hash_bits > 31 ? 31 : max_hash_bits,
      cms_bits                 = LP64_ONLY(1) NOT_LP64(0),
      epoch_bits               = 2
};
```

从上面的枚举定义中可以看出，对象头中主要包含了GC分代年龄、锁状态标记、哈希码、epoch等信息。  

#### 2.3、Moniter的实现原理

上文提过介绍过关于Synchronize的实现原理，无论是同步方法还是同步代码块，无论是ACC_SYNCHRONIZED还是monitorenter、monitorexit都是基于Monitor实现的。 接下来就主要介绍一下jvm中Monitor的实现。  

**Java线程同步相关的Moniter**  
在多线程访问共享资源的时候，经常会带来可见性和原子性的安全问题。为了解决这类线程安全的问题，Java提供了同步机制、互斥锁机制，这个机制保证了在同一时刻只有一个线程能访问共享资源。这个机制的保障来源于监视锁Monitor，每个对象都拥有自己的监视锁Monitor。

**监视器的实现**  
在Java虚拟机(HotSpot)中，Monitor是基于C++实现的，由[ObjectMonitor](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/runtime/objectMonitor.hpp#L193)实现的，其主要数据结构如下：
```
 ObjectMonitor() {
    _header       = NULL;
    _count        = 0;
    _waiters      = 0,
    _recursions   = 0;
    _object       = NULL;
    _owner        = NULL;
    _WaitSet      = NULL;
    _WaitSetLock  = 0 ;
    _Responsible  = NULL ;
    _succ         = NULL ;
    _cxq          = NULL ;
    FreeNext      = NULL ;
    _EntryList    = NULL ;
    _SpinFreq     = 0 ;
    _SpinClock    = 0 ;
    OwnerIsThread = 0 ;
  }
```
ObjectMonitor中有几个关键属性：

> _owner：指向持有ObjectMonitor对象的线程  
_WaitSet：存放处于wait状态的线程队列  
_EntryList：存放处于等待锁block状态的线程队列  
_recursions：锁的重入次数  
_count：用来记录该线程获取锁的次数

当多个线程同时访问一段同步代码时，首先会进入_EntryList队列中，当某个线程获取到对象的monitor后进入_Owner区域并把monitor中的_owner变量设置为当前线程，同时monitor中的计数器_count加1。即获得对象锁。  
若持有monitor的线程调用wait()方法，将释放当前持有的monitor，_owner变量恢复为null，_count自减1，同时该线程进入_WaitSet集合中等待被唤醒。若当前线程执行完毕也将释放monitor(锁)并复位变量的值，以便其他线程进入获取monitor(锁)。如下图所示：  
![](https://www.hollischuang.com/wp-content/uploads/2017/12/monitor.png)  


ObjectMonitor类中提供了几个方法：  

**获得锁**  
``` 
void ATTR ObjectMonitor::enter(TRAPS) {
  Thread * const Self = THREAD ;
  void * cur ;
  //通过CAS尝试把monitor的`_owner`字段设置为当前线程
  cur = Atomic::cmpxchg_ptr (Self, &_owner, NULL) ;
  //获取锁失败
  if (cur == NULL) {         assert (_recursions == 0   , "invariant") ;
     assert (_owner      == Self, "invariant") ;
     // CONSIDER: set or assert OwnerIsThread == 1
     return ;
  }
  // 如果旧值和当前线程一样，说明当前线程已经持有锁，此次为重入，_recursions自增，并获得锁。
  if (cur == Self) { 
     // TODO-FIXME: check for integer overflow!  BUGID 6557169.
     _recursions ++ ;
     return ;
  }

  // 如果当前线程是第一次进入该monitor，设置_recursions为1，_owner为当前线程
  if (Self->is_lock_owned ((address)cur)) { 
    assert (_recursions == 0, "internal state error");
    _recursions = 1 ;
    // Commute owner from a thread-specific on-stack BasicLockObject address to
    // a full-fledged "Thread *".
    _owner = Self ;
    OwnerIsThread = 1 ;
    return ;
  }

  // 省略部分代码。
  // 通过自旋执行ObjectMonitor::EnterI方法等待锁的释放
  for (;;) {
  jt->set_suspend_equivalent();
  // cleared by handle_special_suspend_equivalent_condition()
  // or java_suspend_self()

  EnterI (THREAD) ;

  if (!ExitSuspendEquivalent(jt)) break ;

  //
  // We have acquired the contended monitor, but while we were
  // waiting another thread suspended us. We don't want to enter
  // the monitor while suspended because that would surprise the
  // thread that suspended us.
  //
      _recursions = 0 ;
  _succ = NULL ;
  exit (Self) ;

  jt->java_suspend_self();
}
```
![](https://www.hollischuang.com/wp-content/uploads/2018/02/lockenter.png)  

**释放锁**  
``` 
void ATTR ObjectMonitor::exit(TRAPS) {
   Thread * Self = THREAD ;
   //如果当前线程不是Monitor的所有者
   if (THREAD != _owner) { 
     if (THREAD->is_lock_owned((address) _owner)) { // 
       // Transmute _owner from a BasicLock pointer to a Thread address.
       // We don't need to hold _mutex for this transition.
       // Non-null to Non-null is safe as long as all readers can
       // tolerate either flavor.
       assert (_recursions == 0, "invariant") ;
       _owner = THREAD ;
       _recursions = 0 ;
       OwnerIsThread = 1 ;
     } else {
       // NOTE: we need to handle unbalanced monitor enter/exit
       // in native code by throwing an exception.
       // TODO: Throw an IllegalMonitorStateException ?
       TEVENT (Exit - Throw IMSX) ;
       assert(false, "Non-balanced monitor enter/exit!");
       if (false) {
          THROW(vmSymbols::java_lang_IllegalMonitorStateException());
       }
       return;
     }
   }
    // 如果_recursions次数不为0.自减
   if (_recursions != 0) {
     _recursions--;        // this is simple recursive enter
     TEVENT (Inflated exit - recursive) ;
     return ;
   }

   //省略部分代码，根据不同的策略（由QMode指定），从cxq或EntryList中获取头节点，通过ObjectMonitor::ExitEpilog方法唤醒该节点封装的线程，唤醒操作最终由unpark完成。
```
![](https://www.hollischuang.com/wp-content/uploads/2018/02/lockexit.png)  

**wait()**  
``` 

```

**notify()**  
``` 

```

**notifyAll()**  
``` 

```



### 3、synchronized的优化
在JDK1.6之前，synchronized的实现会直接调用ObjectMonitor的enter和exit，这种锁被称之为重量级锁。为什么说这种方式操作锁很重呢？  

> Java的线程是映射到操作系统原生线程之上的，如果要阻塞或唤醒一个线程就需要操作系统的帮忙，这就要从用户态转换到核心态，这种切换会消耗大量的系统资源，因为用户态与内核态都有各自专用的内存空间，专用的寄存器等，用户态切换至内核态需要传递给许多变量、参数给内核，内核也需要保护好用户态在切换时的一些寄存器值、变量等。因此状态转换需要花费很多的处理器时间，对于代码简单的同步块（如被synchronized修饰的get 或set方法）状态转换消耗的时间有可能比用户代码执行的时间还要长，所以说synchronized是java语言中一个重量级的操纵。

到了Java1.6，synchronized进行了很多的优化，有适应自旋、锁消除、锁粗化、轻量级锁及偏向锁等，效率有了本质上的提高。在之后推出的Java1.7与1.8中，均对该关键字的实现机理做了优化。

很显然我们应该改正monitorenter指令就是获取对象重量级锁的错误认识，优化之后，锁的获取判断次序是偏向锁->轻量级锁->重量级锁。
注意：轻量级锁膨胀为重量级锁之前，是没有初始化ObjectMonitor，即没有使用jvm的monitor机制

在介绍synchronized的优化之前，首先需要了解线程的状态：  

对于线程来说，一共有五种状态，分别为：初始状态(New) 、就绪状态(Runnable) 、运行状态(Running) 、阻塞状态(Blocked) 和死亡状态(Dead) 。
![](https://www.hollischuang.com/wp-content/uploads/2018/04/thread.png)  
如图所示：
* 1. 当初始化一个线程之后，线程状态为new ，此时仅由JVM为其分配内存，并初始化其成员变量。由上文可知，相当于在heap中创建instanceOopDesc对象;
* 2. 当调用start()方法之后，现在状态为runable，此时JVM会为其创建方法调用栈和程序计数器，等待OS调度选中;
* 3. OS调度选中，即获得CPU之后，开始执行run()方法中的代码，此时线程状态为running;
* 4. run() 方法中有io操作、sleep()、wait()、t.join()等，让出了CPU时间片，此时线程状态为block；阻塞分三种：等待阻塞、同步阻塞、其他阻塞；
* 5. 线程结束，状态为dead.


![](https://www.hollischuang.com/wp-content/uploads/2017/12/monitor.png)  
**线程获取锁：**
* 1. 当线程竞争锁，如果竞争成功，对象的monitor中owner设置为当前线程，线程处于running状态；如何竞争失败，线程进入对象的monitor的entrySet队列，线程处于block状态，同步阻塞；entrySet队列中线程竞争到锁之后，进入running状态；
* 2. running线程调用o.wait()方法，对象monitor中owner设置为null，当前线程进入waitSet队列，线程处于等待阻塞状态；
* 3. 当其他线程调用o.notify()后，并退出当前对象锁的占用之后，将waitSet队列中第一个线程取出放入entrySet队列；entrySet队列中线程竞争锁；o.notifyAll()，将waitSet队列中线程根据不同的策略加入到entrySet队列。


**锁优化**
#### 3.1 偏向锁
引入偏向锁的主要原因是，经过研究发现，在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，因此为了减少同一线程获取锁(会涉及到一些CAS操作,耗时)的代价而引入偏向锁。  

引入的主要目的是，为了在无多线程竞争的情况下尽量减少不必要的轻量级锁执行路径。因为轻量级锁的获取及释放依赖多次CAS原子指令，而偏向锁只需要在置换ThreadID的时候依赖一次CAS原子指令（由于一旦出现多线程竞争的情况就必须撤销偏向锁，所以偏向锁的撤销操作的性能损耗必须小于节省下来的CAS原子指令的性能消耗）。  

偏向锁的核心思想是，如果一个线程获得了锁，那么锁就进入偏向模式，此时Mark Word 的结构也变为偏向锁结构，当这个线程再次请求锁时，无需再做任何同步操作，即获取锁的过程，这样就省去了大量有关锁申请的操作，从而也就提升程序的性能。所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，毕竟极有可能连续多次是同一个线程申请相同的锁。


其执行流程为：
获取锁:  
* 1. 检测Mark Word是否为可偏向状态，即是否为偏向锁1，锁标识位为01；  
* 2. 若为可偏向状态，则测试线程ID是否为当前线程ID，如果是，则执行步骤(5)，否则执行步骤(3)；  
* 3. 如果线程ID不为当前线程ID，则通过CAS操作竞争锁，竞争成功，则将Mark Word的线程ID替换为当前线程ID，否则执行线程(4)；
* 4. 通过CAS竞争锁失败，证明当前存在多线程竞争情况，当到达全局安全点，获得偏向锁的线程被挂起，偏向锁升级为轻量级锁，然后被阻塞在安全点的线程继续往下执行同步代码块；
* 5. 执行同步代码块

源码：
```
//偏向锁入口
void ObjectSynchronizer::fast_enter(Handle obj, BasicLock* lock, bool attempt_rebias, TRAPS) {
 //UseBiasedLocking判断是否开启偏向锁
 if (UseBiasedLocking) {
    if (!SafepointSynchronize::is_at_safepoint()) {
      //获取偏向锁的函数调用
      BiasedLocking::Condition cond = BiasedLocking::revoke_and_rebias(obj, attempt_rebias, THREAD);
      if (cond == BiasedLocking::BIAS_REVOKED_AND_REBIASED) {
        return;
      }
    } else {
      assert(!attempt_rebias, "can not rebias toward VM thread");
      BiasedLocking::revoke_at_safepoint(obj);
    }
 }
 //不能偏向，就获取轻量级锁
 slow_enter (obj, lock, THREAD) ;
}
```

BiasedLocking::revoke_and_rebias调用过程如下流程图：  
![revoke_and_rebias](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206160234638-1005593403.png)  



释放锁:  
偏向锁的释放采用了一种只有竞争才会释放锁的机制，线程是不会主动去释放偏向锁，需要等待其他线程来竞争。偏向锁的撤销需要等待全局安全点（这个时间点是上没有正在执行的代码）。其步骤如下：
* 1. 暂停拥有偏向锁的线程，判断锁对象石是否还处于被锁定状态；
* 2. 撤销偏向锁，恢复到无锁状态(01)或者轻量级锁的状态；  

![撤销偏向锁](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155000185-749526156.png)

> 那么轻量级锁和偏向锁的使用场景为轻量级锁是为了在线程交替执行同步块时提高性能，而偏向锁则是在只有一个线程执行同步块时进一步提高性能。

#### 3.2 轻量级锁
引入轻量级锁的主要原因是，对绝大部分的锁，在整个同步周期内都不存在竞争，可能是交替获取锁然后执行。(与偏向锁的区别是，引入偏向锁是假设同一个锁都是由同一线程多次获得，而轻量级锁是假设同一个锁是由n个线程交替获得；相同点是都是假设不存在多线程竞争)


引入轻量级锁的主要目的是，在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗(多指时间消耗)。


触发轻量级锁的条件是当关闭偏向锁功能或者多个线程竞争偏向锁导致偏向锁升级为轻量级锁，则会尝试获取轻量级锁，此时Mark Word的结构也变为轻量级锁的结构。如果存在多个线程同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁。

获取锁:  
* 1. 判断当前对象是否处于无锁状态（hashcode、0、01），若是，则JVM首先将在当前线程的栈帧中建立一个名为锁记录（Lock Record）的空间，用于存储锁对象目前的Mark Word的拷贝（官方把这份拷贝加了一个Displaced前缀，即Displaced Mark Word）；否则执行步骤（3）；
* 2. JVM利用CAS操作尝试将对象的Mark Word更新为指向Lock Record的指针，如果成功表示竞争到锁，则将锁标志位变成00（表示此对象处于轻量级锁状态），执行同步操作；如果失败则执行步骤（3）；
* 3. 判断当前对象的Mark Word是否指向当前线程的栈帧，如果是则表示当前线程已经持有当前对象的锁，则直接执行同步代码块；否则只能说明该锁对象已经被其他线程抢占了，这时轻量级锁需要膨胀为重量级锁，锁标志位变成10，后面等待的线程将会进入阻塞状态；
* 4. 轻量级锁失败后，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为自旋锁的优化手段。如果自旋之后依然没有获取到锁，也就只能升级为重量级锁了。看下文的自旋锁。

源码：  
```
//轻量级锁入口
void ObjectSynchronizer::slow_enter(Handle obj, BasicLock* lock, TRAPS) {
  markOop mark = obj->mark();  //获得Mark Word
  assert(!mark->has_bias_pattern(), "should not see bias pattern here");
  //是否无锁不可偏向，标志001
  if (mark->is_neutral()) {
    //图A步骤1
    lock->set_displaced_header(mark);
    //图A步骤2
    if (mark == (markOop) Atomic::cmpxchg_ptr(lock, obj()->mark_addr(), mark)) {
      TEVENT (slow_enter: release stacklock) ;
      return ;
    }
    // Fall through to inflate() ...
  } else if (mark->has_locker() && THREAD->is_lock_owned((address)mark->locker())) { //如果Mark Word指向本地栈帧，线程重入
    assert(lock != mark->locker(), "must not re-lock the same lock");
    assert(lock != (BasicLock*)obj->mark(), "don't relock with same BasicLock");
    lock->set_displaced_header(NULL);//header设置为null
    return;
  }
  lock->set_displace

  d_header(markOopDesc::unused_mark());
  //轻量级锁膨胀，膨胀完成之后尝试获取重量级锁
  ObjectSynchronizer::inflate(THREAD, obj())->enter(THREAD);
}
```
轻量级锁获取流程如下：  
![轻量级锁获取过程](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206160930795-1885559642.png)  
![轻量级锁对象头设置示意图](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206161340904-1290962758.png)


释放锁:  
轻量级锁的释放也是通过CAS操作来进行的，主要步骤如下：
* 1. 取出在获取轻量级锁保存在Displaced Mark Word中的数据；
* 2. 用CAS操作将取出的数据替换当前对象的Mark Word中，如果成功，则说明释放锁成功，否则执行（3）；
* 3. 如果CAS操作替换失败，说明有其他线程尝试获取该锁，则需要在释放锁的同时需要唤醒被挂起的线程。

源码：
```
void ObjectSynchronizer::fast_exit(oop object, BasicLock* lock, TRAPS) {
  assert(!object->mark()->has_bias_pattern(), "should not see bias pattern here");
  markOop dhw = lock->displaced_header();
  markOop mark ;
  if (dhw == NULL) {//如果header为null，说明这是线程重入的栈帧，直接返回，不用回写
     mark = object->mark() ;
     assert (!mark->is_neutral(), "invariant") ;
     if (mark->has_locker() && mark != markOopDesc::INFLATING()) {
        assert(THREAD->is_lock_owned((address)mark->locker()), "invariant") ;
     }
     if (mark->has_monitor()) {
        ObjectMonitor * m = mark->monitor() ;
     }
     return ;
  }

  mark = object->mark() ;
  if (mark == (markOop) lock) {
     assert (dhw->is_neutral(), "invariant") ;
     //CAS将Mark Word内容写回
     if ((markOop) Atomic::cmpxchg_ptr (dhw, object->mark_addr(), mark) == mark) {
        TEVENT (fast_exit: release stacklock) ;
        return;
     }
  }
  //CAS操作失败，轻量级锁膨胀，为什么在撤销锁的时候会有失败的可能？
   ObjectSynchronizer::inflate(THREAD, object)->exit (THREAD) ;
}
```
轻量级锁撤销流程如下：  
![轻量级锁撤销流程](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155028388-78760140.png)

#### 3.2.1 轻量级锁膨胀
源码：
```
ObjectMonitor * ATTR ObjectSynchronizer::inflate (Thread * Self, oop object) {
  assert (Universe::verify_in_progress() ||
          !SafepointSynchronize::is_at_safepoint(), "invariant") ;
  for (;;) { // 为后面的continue操作提供自旋
      const markOop mark = object->mark() ; //获得Mark Word结构
      assert (!mark->has_bias_pattern(), "invariant") ;

      //Mark Word可能有以下几种状态:
      // *  Inflated(膨胀完成)     - just return
      // *  Stack-locked(轻量级锁) - coerce it to inflated
      // *  INFLATING(膨胀中)     - busy wait for conversion to complete
      // *  Neutral(无锁)        - aggressively inflate the object.
      // *  BIASED(偏向锁)       - Illegal.  We should never see this

      if (mark->has_monitor()) {//判断是否是重量级锁
          ObjectMonitor * inf = mark->monitor() ;
          assert (inf->header()->is_neutral(), "invariant");
          assert (inf->object() == object, "invariant") ;
          assert (ObjectSynchronizer::verify_objmon_isinpool(inf), "monitor is invalid");
          //Mark->has_monitor()为true，说明已经是重量级锁了，膨胀过程已经完成，返回
          return inf ;
      }
      if (mark == markOopDesc::INFLATING()) { //判断是否在膨胀
         TEVENT (Inflate: spin while INFLATING) ;
         ReadStableMark(object) ;
         continue ; //如果正在膨胀，自旋等待膨胀完成
      }

      if (mark->has_locker()) { //如果当前是轻量级锁
          ObjectMonitor * m = omAlloc (Self) ;//返回一个对象的内置ObjectMonitor对象
          m->Recycle();
          m->_Responsible  = NULL ;
          m->OwnerIsThread = 0 ;
          m->_recursions   = 0 ;
          m->_SpinDuration = ObjectMonitor::Knob_SpinLimit ;//设置自旋获取重量级锁的次数
          //CAS操作标识Mark Word正在膨胀
          markOop cmp = (markOop) Atomic::cmpxchg_ptr (markOopDesc::INFLATING(), object->mark_addr(), mark) ;
          if (cmp != mark) {
             omRelease (Self, m, true) ;
             continue ;   //如果上述CAS操作失败，自旋等待膨胀完成
          }
          m->set_header(dmw) ;
          m->set_owner(mark->locker());//设置ObjectMonitor的_owner为拥有对象轻量级锁的线程，而不是当前正在inflate的线程
          m->set_object(object);
          /**
          *省略了部分代码
          **/
          return m ;
      }
  }
}
```

轻量级锁膨胀流程图：  
![轻量级锁膨胀流程图](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155041482-253349254.png)

#### 3.2.2 重量级锁
重量级锁的获取入口：  
```
void ATTR ObjectMonitor::enter(TRAPS) {
  Thread * const Self = THREAD ;
  void * cur ;
  cur = Atomic::cmpxchg_ptr (Self, &_owner, NULL) ;
  if (cur == NULL) {
     assert (_recursions == 0   , "invariant") ;
     assert (_owner      == Self, "invariant") ;
     return ;
  }

  if (cur == Self) {
     _recursions ++ ;
     return ;
  }

  if (Self->is_lock_owned ((address)cur)) {
    assert (_recursions == 0, "internal state error");
    _recursions = 1 ;
    // Commute owner from a thread-specific on-stack BasicLockObject address to
    // a full-fledged "Thread *".
    _owner = Self ;
    OwnerIsThread = 1 ;
    return ;
  }
  /**
  *上述部分在前面已经分析过，不再累述
  **/

  Self->_Stalled = intptr_t(this) ;
  //TrySpin是一个自旋获取锁的操作，此处就不列出源码了
  if (Knob_SpinEarly && TrySpin (Self) > 0) {
     Self->_Stalled = 0 ;
     return ;
  }
  /*
  *省略部分代码
  */
    for (;;) {
      EnterI (THREAD) ;
      /**
      *省略了部分代码
      **/
  }
}
```

进入EnterI (TRAPS)方法
```
void ATTR ObjectMonitor::EnterI (TRAPS) {
    Thread * Self = THREAD ;
    if (TryLock (Self) > 0) {
        //这下不自旋了，我就默默的TryLock一下
        return ;
    }

    DeferredInitialize () ;
    //此处又有自旋获取锁的操作
    if (TrySpin (Self) > 0) {
        return ;
    }
    /**
    *到此，自旋终于全失败了，要入队挂起了
    **/
    ObjectWaiter node(Self) ; //将Thread封装成ObjectWaiter结点
    Self->_ParkEvent->reset() ;
    node._prev   = (ObjectWaiter *) 0xBAD ; 
    node.TState  = ObjectWaiter::TS_CXQ ; 
    ObjectWaiter * nxt ;
    for (;;) { //循环，保证将node插入队列
        node._next = nxt = _cxq ;//将node插入到_cxq队列的首部
        //CAS修改_cxq指向node
        if (Atomic::cmpxchg_ptr (&node, &_cxq, nxt) == nxt) break ;
        if (TryLock (Self) > 0) {//我再默默的TryLock一下，真的是不想挂起呀！
            return ;
        }
    }
    if ((SyncFlags & 16) == 0 && nxt == NULL && _EntryList == NULL) {
        // Try to assume the role of responsible thread for the monitor.
        // CONSIDER:  ST vs CAS vs { if (Responsible==null) Responsible=Self }
        Atomic::cmpxchg_ptr (Self, &_Responsible, NULL) ;
    }
    TEVENT (Inflated enter - Contention) ;
    int nWakeups = 0 ;
    int RecheckInterval = 1 ;

    for (;;) {
        if (TryLock (Self) > 0) break ;//临死之前，我再TryLock下

        if ((SyncFlags & 2) && _Responsible == NULL) {
           Atomic::cmpxchg_ptr (Self, &_Responsible, NULL) ;
        }
        if (_Responsible == Self || (SyncFlags & 1)) {
            TEVENT (Inflated enter - park TIMED) ;
            Self->_ParkEvent->park ((jlong) RecheckInterval) ;
            RecheckInterval *= 8 ;
            if (RecheckInterval > 1000) RecheckInterval = 1000 ;
        } else {
            TEVENT (Inflated enter - park UNTIMED) ;
            Self->_ParkEvent->park() ; //终于挂起了
        }

        if (TryLock(Self) > 0) break ;
        /**
        *后面代码省略
        **/
}
```

try了那么多次lock，接下来看下TryLock
```
int ObjectMonitor::TryLock (Thread * Self) {
   for (;;) {
      void * own = _owner ;
      if (own != NULL) return 0 ;//如果有线程还拥有着重量级锁，退出
      //CAS操作将_owner修改为当前线程，操作成功return>0
      if (Atomic::cmpxchg_ptr (Self, &_owner, NULL) == NULL) {
         return 1 ;
      }
      //CAS更新失败return<0
      if (true) return -1 ;
   }
}
```
重量级锁获取入口流程图：  
![重量级锁获取流程](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155616826-1212580866.png)


重量级锁的出口：
```
void ATTR ObjectMonitor::exit(TRAPS) {
   Thread * Self = THREAD ;
   if (THREAD != _owner) {
     if (THREAD->is_lock_owned((address) _owner)) {
       _owner = THREAD ;
       _recursions = 0 ;
       OwnerIsThread = 1 ;
     } else {
       TEVENT (Exit - Throw IMSX) ;
       if (false) {
          THROW(vmSymbols::java_lang_IllegalMonitorStateException());
       }
       return;
     }
   }
   if (_recursions != 0) {
     _recursions--;        // 如果_recursions次数不为0.自减
     TEVENT (Inflated exit - recursive) ;
     return ;
   }
   if ((SyncFlags & 4) == 0) {
      _Responsible = NULL ;
   }

   for (;;) {
      if (Knob_ExitPolicy == 0) {
         OrderAccess::release_store_ptr (&_owner, NULL) ;   // drop the lock
         OrderAccess::storeload() ;                         
         if ((intptr_t(_EntryList)|intptr_t(_cxq)) == 0 || _succ != NULL) {
            TEVENT (Inflated exit - simple egress) ;
            return ;
         }
         TEVENT (Inflated exit - complex egress) ;
         if (Atomic::cmpxchg_ptr (THREAD, &_owner, NULL) != NULL) {
            return ;
         }
         TEVENT (Exit - Reacquired) ;
      } else {
         if ((intptr_t(_EntryList)|intptr_t(_cxq)) == 0 || _succ != NULL) {
            OrderAccess::release_store_ptr (&_owner, NULL) ;  
            OrderAccess::storeload() ;
            if (_cxq == NULL || _succ != NULL) {
                TEVENT (Inflated exit - simple egress) ;
                return ;
            }
            if (Atomic::cmpxchg_ptr (THREAD, &_owner, NULL) != NULL) {
               TEVENT (Inflated exit - reacquired succeeded) ;
               return ;
            }
            TEVENT (Inflated exit - reacquired failed) ;
         } else {
            TEVENT (Inflated exit - complex egress) ;
         }
      }
      ObjectWaiter * w = NULL ;
      int QMode = Knob_QMode ;
      if (QMode == 2 && _cxq != NULL) {
          /**
          *模式2:cxq队列的优先权大于EntryList，直接从cxq队列中取出一个线程结点，准备唤醒
          **/
          w = _cxq ;
          ExitEpilog (Self, w) ;
          return ;
      }

      if (QMode == 3 && _cxq != NULL) {
          /**
          *模式3:将cxq队列插入到_EntryList尾部
          **/
          w = _cxq ;
          for (;;) {
             //CAS操作取出cxq队列首结点
             ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
             if (u == w) break ;
             w = u ; //更新w，自旋
          }
          ObjectWaiter * q = NULL ;
          ObjectWaiter * p ;
          for (p = w ; p != NULL ; p = p->_next) {
              guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
              p->TState = ObjectWaiter::TS_ENTER ; //改变ObjectWaiter状态
              //下面两句为cxq队列反向构造一条链，即将cxq变成双向链表
              p->_prev = q ;
              q = p ;
          }
          ObjectWaiter * Tail ;
          //获得_EntryList尾结点
          for (Tail = _EntryList ; Tail != NULL && Tail->_next != NULL ; Tail = Tail->_next) ;
          if (Tail == NULL) {
              _EntryList = w ;//_EntryList为空，_EntryList=w
          } else {
              //将w插入_EntryList队列尾部
              Tail->_next = w ;
              w->_prev = Tail ;
          }
   }

      if (QMode == 4 && _cxq != NULL) {
         /**
         *模式四：将cxq队列插入到_EntryList头部
         **/
          w = _cxq ;
          for (;;) {
             ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
             if (u == w) break ;
             w = u ;
          }
          ObjectWaiter * q = NULL ;
          ObjectWaiter * p ;
          for (p = w ; p != NULL ; p = p->_next) {
              guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
              p->TState = ObjectWaiter::TS_ENTER ;
              p->_prev = q ;
              q = p ;
          }
          if (_EntryList != NULL) {
            //q为cxq队列最后一个结点
              q->_next = _EntryList ;
              _EntryList->_prev = q ;
          }
          _EntryList = w ;
       }

      w = _EntryList  ;
      if (w != NULL) {
          ExitEpilog (Self, w) ;//从_EntryList中唤醒线程
          return ;
      }
      w = _cxq ;
      if (w == NULL) continue ; //如果_cxq和_EntryList队列都为空，自旋

      for (;;) {
          //自旋再获得cxq首结点
          ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
          if (u == w) break ;
          w = u ;
      }
      /**
      *下面执行的是：cxq不为空，_EntryList为空的情况
      **/
      if (QMode == 1) {//结合前面的代码，如果QMode == 1，_EntryList不为空，直接从_EntryList中唤醒线程
         // QMode == 1 : drain cxq to EntryList, reversing order
         // We also reverse the order of the list.
         ObjectWaiter * s = NULL ;
         ObjectWaiter * t = w ;
         ObjectWaiter * u = NULL ;
         while (t != NULL) {
             guarantee (t->TState == ObjectWaiter::TS_CXQ, "invariant") ;
             t->TState = ObjectWaiter::TS_ENTER ;
             //下面的操作是双向链表的倒置
             u = t->_next ;
             t->_prev = u ;
             t->_next = s ;
             s = t;
             t = u ;
         }
         _EntryList  = s ;//_EntryList为倒置后的cxq队列
      } else {
         // QMode == 0 or QMode == 2
         _EntryList = w ;
         ObjectWaiter * q = NULL ;
         ObjectWaiter * p ;
         for (p = w ; p != NULL ; p = p->_next) {
             guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
             p->TState = ObjectWaiter::TS_ENTER ;
             //构造成双向的
             p->_prev = q ;
             q = p ;
         }
      }
      if (_succ != NULL) continue;
      w = _EntryList  ;
      if (w != NULL) {
          ExitEpilog (Self, w) ; //从_EntryList中唤醒线程
          return ;
      }
   }
}
```

ExitEpilog用来唤醒线程，代码如下：  
```
void ObjectMonitor::ExitEpilog (Thread * Self, ObjectWaiter * Wakee) {
   assert (_owner == Self, "invariant") ;
   _succ = Knob_SuccEnabled ? Wakee->_thread : NULL ;
   ParkEvent * Trigger = Wakee->_event ;
   Wakee  = NULL ;
   OrderAccess::release_store_ptr (&_owner, NULL) ;
   OrderAccess::fence() ;                            
   if (SafepointSynchronize::do_call_back()) {
      TEVENT (unpark before SAFEPOINT) ;
   }
   DTRACE_MONITOR_PROBE(contended__exit, this, object(), Self);
   Trigger->unpark() ; //唤醒线程
   // Maintain stats and report events to JVMTI
   if (ObjectMonitor::_sync_Parks != NULL) {
      ObjectMonitor::_sync_Parks->inc() ;
   }
}
```

重量级锁出口流程图：  
![重量级锁出口流程](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155148216-1870687856.png)


#### 3.3 自旋锁/适应性自旋锁
线程的阻塞和唤醒需要CPU从用户态转为核心态，频繁的阻塞和唤醒对CPU来说是一件负担很重的工作，势必会给系统的并发性能带来很大的压力。同时我们发现在许多应用上面，对象锁的锁状态只会持续很短一段时间，为了这一段很短的时间频繁地阻塞和唤醒线程是非常不值得的。所以引入自旋锁。

所谓自旋锁，就是让该线程等待一段时间，不会被立即挂起，看持有锁的线程是否会很快释放锁。怎么等待呢？执行一段无意义的循环即可（自旋）。

自旋等待不能替代阻塞，虽然它可以避免线程切换带来的开销，但是它占用了处理器的时间。如果持有锁的线程很快就释放了锁，那么自旋的效率就非常好，反之，自旋的线程就会白白消耗掉处理的资源，它不会做任何有意义的工作，这样反而会带来性能上的浪费。所以说，自旋等待的时间（自旋的次数）必须要有一个限度，如果自旋超过了定义的时间仍然没有获取到锁，则应该被挂起。

自旋锁在JDK 1.4.2中引入，默认关闭，但是可以使用-XX:+UseSpinning开开启，在JDK1.6中默认开启。同时自旋的默认次数为10次，可以通过参数-XX:PreBlockSpin来调整；

如果通过参数-XX:preBlockSpin来调整自旋锁的自旋次数，会带来诸多不便。假如我将参数调整为10，但是很多线程都是等你刚刚退出自旋的时候就释放了锁（假如你再多自旋一两次就可以获取锁），你是不是很尴尬。于是JDK1.6引入自适应的自旋锁，让虚拟机会变得越来越聪明。

JDK 1.6引入了更加聪明的自旋锁，即自适应自旋锁。所谓自适应就意味着自旋的次数不再是固定的，它是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。它怎么做呢？线程如果自旋成功了，那么下次自旋的次数会更加多，因为虚拟机认为既然上次成功了，那么此次自旋也很有可能会再次成功，那么它就会允许自旋等待持续的次数更多。反之，如果对于某个锁，很少有自旋能够成功的，那么在以后要或者这个锁的时候自旋的次数会减少甚至省略掉自旋过程，以免浪费处理器资源。

轻量级锁失败后，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为自旋锁的优化手段。如果自旋之后依然没有获取到锁，也就只能升级为重量级锁了。

自旋锁与重量级锁最大的区别是，到底要不要放弃CPU 时间片。对于阻塞锁和自旋锁来说，都是要等待获得共享资源。但是阻塞锁是放弃了CPU时间，进入了等待区，等待被唤醒。而自旋锁是一直“自旋”在那里，时刻的检查共享资源是否可以被访问。

#### 3.4 锁消除
自旋锁之后，JDK中还有一种锁的优化被称之为锁消除，是JIT编译器对内部锁的具体实现所做的一种优化。

在动态编译同步块的时候，JIT编译器可以借助一种被称为逃逸分析（Escape Analysis）的技术来判断同步块所使用的锁对象是否只能够被一个线程访问而没有被发布到其他线程。（发布到其他线程称为**线程逃逸**， 用于是否锁消除。另外逃逸分析还有一种**方法逃逸**，用于判断局部变量是否逃逸出方法，如果未出现方法逃逸将**有可能**直接将变量实例直接分配到栈中，即实例不在heap堆中分配内存了。）

如果同步块所使用的锁对象通过这种分析被证实只能够被一个线程访问，那么JIT编译器在编译这个同步块的时候就会取消对这部分代码的同步。
如：
```
public void f() {
    Object object = new Object();
    synchronized(object) {
        System.out.println(object);
    }}
```
代码中对object这个对象进行加锁，但是object对象的生命周期只在f()方法中，并不会被其他线程所访问到，所以在JIT编译阶段就会被优化掉。优化成：
```
public void f() {
    Object object = new Object();
    System.out.println(object);}
```
在实际开发过程中不会有这么明显的错误使用情况出现的。但是当我们在代码中使用StringBuffer作为局部变量，而StringBuffer中的append是线程安全的，有synchronized修饰的。这种情况开发者可能会忽略。这是JIT就可以帮忙进行锁消除优化。

#### 3.5 锁粗化
在代码中，需要加锁的时候，我们提倡尽量减小锁的粒度，这样可以避免不必要的阻塞。  
如果在一段代码中连续的对同一个对象反复加锁解锁，其实是相对耗费资源的，这种情况可以适当放宽加锁的范围，减少性能消耗。
当JIT发现一系列连续的操作都对同一个对象反复加锁和解锁，甚至加锁操作出现在循环体中的时候，会将加锁同步的范围扩散（粗化）到整个操作序列的外部。  

如下：
```
for(int i=0;i<100000;i++){  
    synchronized(this){  
        do();  
} 
```
会被粗化为：
```
synchronized(this){  
    for(int i=0;i<100000;i++){  
        do();  
}  
```

总结
引入**偏向锁**的目的：在只有单线程执行情况下，尽量减少不必要的轻量级锁执行路径，轻量级锁的获取及释放依赖多次CAS原子指令，而偏向锁只依赖一次CAS原子指令置换ThreadID，之后只要判断线程ID为当前线程即可，偏向锁使用了一种等到竞争出现才释放锁的机制，消除偏向锁的开销还是蛮大的。如果同步资源或代码一直都是多线程访问的，那么消除偏向锁这一步骤对你来说就是多余的，可以通过-XX:-UseBiasedLocking=false来关闭
引入**轻量级锁**的目的：在多线程交替执行同步块的情况下，尽量避免重量级锁引起的性能消耗(用户态和核心态转换)，但是如果多个线程在同一时刻进入临界区，会导致轻量级锁膨胀升级重量级锁，所以轻量级锁的出现并非是要替代重量级锁
**重入**:对于不同级别的锁都有重入策略，偏向锁:单线程独占，重入只用检查threadId等于该线程；轻量级锁：重入将栈帧中lock record的header设置为null，重入退出，只用弹出栈帧，直到最后一个重入退出CAS写回数据释放锁；重量级锁：重入_recursions++，重入退出_recursions--，_recursions=0时释放锁

![synchronized原理](https://images2017.cnblogs.com/blog/1053518/201802/1053518-20180206155203420-628607348.jpg)

### 4、Object.wait()、notify()、notifyAll()、Thread.yield()方法做了什么操作
```
public static void main(String[] args) {
    final Object lock = new Object();
    new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("thread A is waiting to get lock");
            synchronized (lock) {
                try {
                    System.out.println("thread A get lock");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("thread A do wait method");
                    lock.wait();
                    System.out.println("wait end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }).start();
    new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("thread B is waiting to get lock");
            synchronized (lock) {
                System.out.println("thread B get lock");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.notify();
                System.out.println("thread B do notify method");
            }
        }
    }).start();
}
```
执行结果：
thread A is waiting to get lock
thread A get lock
thread B is waiting to get lock
thread A do wait method
thread B get lock
thread B do notify method
wait end


问题：
1、进入wait/notify方法之前，为什么要获取synchronized锁？  
lock.wait()、lock.notify();调用是native方法，在其注释中有【@throws  IllegalMonitorStateException  if the current thread is 
not the owner of this object's monitor.】。如果当前线程不是对象的monitor的持有者，将会报错。而synchronized可以获取到对象的monitor。 

2、线程A获取了synchronized锁，执行wait方法并挂起，线程B又如何再次获取锁？  
[JVM源码分析之Object.wait/notify实现](https://www.jianshu.com/p/f4454164c017)

### 5、注意
1、需要说明的是，当线程通过synchronized等待锁时是不能被Thread.interrupt()中断的，因此程序设计时必须检查确保合理，否则可能会造成线程死锁的尴尬境地。

2、尽管Java实现的锁机制有很多种，并且有些锁机制性能也比synchronized高，但还是强烈推荐在多线程应用程序中使用该关键字，
因为实现方便，后续工作由JVM来完成，可靠性高。只有在确定锁机制是当前多线程程序的性能瓶颈时，才考虑使用其他机制，如ReentrantLock等。
