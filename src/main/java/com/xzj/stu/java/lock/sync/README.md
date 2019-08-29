## synchronized

##### 几乎每一个Java开发人员都认识synchronized，使用它来实现多线程的同步操作是非常简单的，只要在需要同步的对方的方法、类或代码块中加入该关键字，它能够保证在同一个时刻最多只有一个线程执行同一个对象的同步代码，可保证修饰的代码在执行过程中不会被其他线程干扰。使用synchronized修饰的代码具有原子性和可见性，在需要进程同步的程序中使用的频率非常高，可以满足一般的进程同步要求（详见[《Java多线程基础》](http://www.cnblogs.com/hanganglin/articles/3517178.html)）。

##### synchronized实现的机理依赖于软件层面上的JVM，因此其性能会随着Java版本的不断升级而提高。事实上，在Java1.5中，synchronized是一个重量级操作，需要调用操作系统相关接口，性能是低效的，有可能给线程加锁消耗的时间比有用操作消耗的时间更多。到了Java1.6，synchronized进行了很多的优化，有适应自旋、锁消除、锁粗化、轻量级锁及偏向锁等，效率有了本质上的提高。在之后推出的Java1.7与1.8中，均对该关键字的实现机理做了优化。

#### 需要说明的是，当线程通过synchronized等待锁时是不能被Thread.interrupt()中断的，因此程序设计时必须检查确保合理，否则可能会造成线程死锁的尴尬境地。

##### 最后，尽管Java实现的锁机制有很多种，并且有些锁机制性能也比synchronized高，但还是强烈推荐在多线程应用程序中使用该关键字，因为实现方便，后续工作由JVM来完成，可靠性高。只有在确定锁机制是当前多线程程序的性能瓶颈时，才考虑使用其他机制，如ReentrantLock等。

### 1. synchronized可修饰的对象
> 见com.xzj.stu.multithread.lock.sync.SyncTest.java
#### 1) synchronized {代码块}：对代码块执行线程同步，效率要高于对整个函数执行同步，推荐使用这种方法
#### 2) synchronized {普通方法}：同一时间只能有一个线程访问同一个对象的该方法。缺点：同步整个方法效率不高。 synchronized void method() { ... }相当于void method( synchronized(this) { ... } )
#### 3) synchronized {static方法}：加锁的对象是类，同一时间，该类的所有对象中的synchronized static方法只能有一个线程访问。 class Foo { public synchronized static fun(){...}}等价于在class Foo { public static fun(){ synchronized(Foo.class){ ... } }}
#### 4) synchronized {run方法}：此时为同步普通方法的特殊情况，由于在线程的整个生命期内run方法一直在运行，因此同一个Runnable对象的多个线程只能串行运行。
> 重点理解以下细节:
> 1) 当多个并发线程访问同一个对象的同步代码块时，一段时间内只能有一个线程得到执行，其他线程必须等待当前线程执行完代码块后再执行代码；  
> 2) 当一个线程访问一个对象的同步代码块时，其他线程可以访问该对象的中的非同步代码块；  
> 3) 当一个线程访问一个对象的同步代码块时，其他线程对该对象中的所有同步代码块均不能访问


### 实现原理
> 见com.xzj.stu.java.lock.sync.SynchronizedDemo  
使用javap -c -v SynchronizedDemo.class反汇编字节码
```
Classfile /D:/mycode/java-study/target/classes/com/xzj/stu/java/lock/sync/SynchronizedDemo.class
  Last modified 2019-8-29; size 942 bytes
  MD5 checksum c25d8e54543c5994a9c7d9df37df3f89
  Compiled from "SynchronizedDemo.java"
public class com.xzj.stu.java.lock.sync.SynchronizedDemo
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #9.#32         // java/lang/Object."<init>":()V
   #2 = Fieldref           #6.#33         // com/xzj/stu/java/lock/sync/SynchronizedDemo.count:I
   #3 = Fieldref           #6.#34         // com/xzj/stu/java/lock/sync/SynchronizedDemo.sum:J
   #4 = Long               100l
   #6 = Class              #35            // com/xzj/stu/java/lock/sync/SynchronizedDemo
   #7 = Long               200l
   #9 = Class              #36            // java/lang/Object
  #10 = Utf8               count
  #11 = Utf8               I
  #12 = Utf8               sum
  #13 = Utf8               J
  #14 = Utf8               <init>
  #15 = Utf8               ()V
  #16 = Utf8               Code
  #17 = Utf8               LineNumberTable
  #18 = Utf8               LocalVariableTable
  #19 = Utf8               this
  #20 = Utf8               Lcom/xzj/stu/java/lock/sync/SynchronizedDemo;
  #21 = Utf8               fun1
  #22 = Utf8               fun2
  #23 = Utf8               fun3
  #24 = Utf8               StackMapTable
  #25 = Class              #35            // com/xzj/stu/java/lock/sync/SynchronizedDemo
  #26 = Class              #36            // java/lang/Object
  #27 = Class              #37            // java/lang/Throwable
  #28 = Utf8               fun4
  #29 = Utf8               <clinit>
  #30 = Utf8               SourceFile
  #31 = Utf8               SynchronizedDemo.java
  #32 = NameAndType        #14:#15        // "<init>":()V
  #33 = NameAndType        #10:#11        // count:I
  #34 = NameAndType        #12:#13        // sum:J
  #35 = Utf8               com/xzj/stu/java/lock/sync/SynchronizedDemo
  #36 = Utf8               java/lang/Object
  #37 = Utf8               java/lang/Throwable
{
  public com.xzj.stu.java.lock.sync.SynchronizedDemo();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: iconst_0
         6: putfield      #2                  // Field count:I
         9: return
      LineNumberTable:
        line 15: 0
        line 11: 4
        line 17: 9
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Lcom/xzj/stu/java/lock/sync/SynchronizedDemo;

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
      LineNumberTable:
        line 20: 0
        line 21: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Lcom/xzj/stu/java/lock/sync/SynchronizedDemo;

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
      LineNumberTable:
        line 24: 0
        line 25: 10

  public void fun3();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=3, args_size=1
         0: aload_0
         1: dup
         2: astore_1
         3: monitorenter
         4: aload_0
         5: dup
         6: getfield      #2                  // Field count:I
         9: iconst_1
        10: iadd
        11: putfield      #2                  // Field count:I
        14: aload_1
        15: monitorexit
        16: goto          24
        19: astore_2
        20: aload_1
        21: monitorexit
        22: aload_2
        23: athrow
        24: return
      Exception table:
         from    to  target type
             4    16    19   any
            19    22    19   any
      LineNumberTable:
        line 28: 0
        line 29: 4
        line 30: 14
        line 31: 24
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      25     0  this   Lcom/xzj/stu/java/lock/sync/SynchronizedDemo;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 19
          locals = [ class com/xzj/stu/java/lock/sync/SynchronizedDemo, class java/lang/Object ]
          stack = [ class java/lang/Throwable ]
        frame_type = 250 /* chop */
          offset_delta = 4

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
      Exception table:
         from    to  target type
             5    17    20   any
            20    23    20   any
      LineNumberTable:
        line 34: 0
        line 35: 5
        line 36: 15
        line 37: 25
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      26     0  this   Lcom/xzj/stu/java/lock/sync/SynchronizedDemo;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 20
          locals = [ class com/xzj/stu/java/lock/sync/SynchronizedDemo, class java/lang/Object ]
          stack = [ class java/lang/Throwable ]
        frame_type = 250 /* chop */
          offset_delta = 4

  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=2, locals=0, args_size=0
         0: lconst_0
         1: putstatic     #3                  // Field sum:J
         4: return
      LineNumberTable:
        line 13: 0
}
SourceFile: "SynchronizedDemo.java"
```
从上述反汇编结果可以看出，jvm对于同步方法和同步代码块的处理方式不同。
对于同步方法，jvm采用ACC_SYNCHRONIZED标识符来实现同步。对于同步代码块，jvm采用monitorenter、monitorexit指令来实现同步。
具体内容参见[The Java® Virtual Machine Specification 2.11.10 Synchronization](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.11.10)的介绍。

因此：   
同步方法通过ACC_SYNCHRONIZED标识符隐式的对方法进行加锁。当线程要执行的方法被标注上ACC_SYNCHRONIZED时，需要先获得锁才能执行该方法。  

同步代码块通过monitorenter和monitorexit执行来进行加锁。当线程执行到monitorenter的时候要先获得所锁，才能执行后面的方法。当线程执行到monitorexit的时候则要释放锁。
  
每个对象自身维护这一个被加锁次数的计数器，当计数器数字为0时表示可以被任意线程获得锁。当计数器不为0时，只有获得锁的线程才能再次获得锁。即可重入锁。