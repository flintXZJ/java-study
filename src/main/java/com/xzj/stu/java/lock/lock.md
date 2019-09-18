## 锁
> 参考：https://www.cnblogs.com/hanganglin/p/3577096.html  
> 参考：https://blog.csdn.net/zqz_zqz/article/details/70233767  
> 参考：https://www.jianshu.com/p/39628e1180a9


## 锁的宏观分类
### 乐观锁

#### CAS
> 参考： https://www.cnblogs.com/xrq730/p/4976007.html 

CAS，Compare and Swap即比较并交换，设计并发算法时常用到的一种技术，java.util.concurrent包全完建立在CAS之上，没有CAS也就没有此包，可见CAS的重要性。    
CAS有三个操作数：内存值V、旧的预期值A、要修改的值B，当且仅当预期值A和内存值V相同时，将内存值修改为B并返回true，否则什么都不做并返回false。   

JDK中有一个类Unsafe，它提供了硬件级别的原子操作。CAS也是通过Unsafe实现的，看下Unsafe下的三个方法：
``` 
public final native boolean compareAndSwapObject(Object paramObject1, long paramLong, Object paramObject2, Object paramObject3);
public final native boolean compareAndSwapInt(Object paramObject, long paramLong, int paramInt1, int paramInt2);
public final native boolean compareAndSwapLong(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
```
CAS的比较、交换是一组原子操作，不会被外部打断，先根据paramLong/paramLong1获取到内存当中当前的内存值V，在将内存值V和原值A作比较，要是相等就修改为要修改的值B，由于CAS都是硬件级别的操作，因此效率会高一些。  

**CAS的缺点：ABA问题**
CAS看起来很美，但这种操作显然无法涵盖并发下的所有场景，并且CAS从语义上来说也不是完美的，存在这样一个逻辑漏洞：
如果一个变量V初次读取的时候是A值，并且在准备赋值的时候检查到它仍然是A值，那我们就能说明它的值没有被其他线程修改过了吗？
如果在这段期间它的值曾经被改成了B，然后又改回A，那CAS操作就会误认为它从来没有被修改过。这个漏洞称为CAS操作的"ABA"问题。
java.util.concurrent包为了解决这个问题，提供了一个带有标记的原子引用类"AtomicStampedReference"，它可以通过控制变量值的版本来保证CAS的正确性。
不过目前来说这个类比较"鸡肋"，大部分情况下ABA问题并不会影响程序并发的正确性，如果需要解决ABA问题，使用传统的互斥同步可能回避原子类更加高效。


#### 偏向锁、轻量级锁、(适应性)自旋锁
> 详见synchronized.md  

jvm对synchronized重量级锁进行优化之后引入的锁，都属于乐观锁



### 悲观锁

#### synchronized
> synchronized详见synchronized.md
> synchronized锁的获取判断次序是偏向锁->轻量级锁->重量级锁


#### RetreenLock
> ReentrantLock实现的前提就是AbstractQueuedSynchronizer，简称AQS
> AQS框架下的锁则是先尝试cas乐观锁去获取锁，获取不到，才会转换为悲观锁。 
> AQS、RetreenLock: https://www.cnblogs.com/xrq730/p/4979021.html 


### ConcurrentHashMap
java中的ConcurrentHashMap在jdk1.8之前的版本，使用一个Segment 数组

``` 
Segment< K,V >[] segments
```

Segment继承自ReenTrantLock，所以每个Segment就是个可重入锁，每个Segment 有一个HashEntry< K,V >数组用来存放数据，
put操作时，先确定往哪个Segment放数据，只需要锁定这个Segment，执行put，其它的Segment不会被锁定；
所以数组中有多少个Segment就允许同一时刻多少个线程存放数据，这样增加了并发能力。


### LongAdder
LongAdder 实现思路也类似ConcurrentHashMap，LongAdder有一个根据当前并发状况动态改变的Cell数组，Cell对象里面有一个long类型的value用来存储值;
开始没有并发争用的时候或者是cells数组正在初始化的时候，会使用cas来将值累加到成员变量的base上，在并发争用的情况下，LongAdder会初始化cells数组，
在Cell数组中选定一个Cell加锁，数组有多少个cell，就允许同时有多少线程进行修改，最后将数组中每个Cell中的value相加，在加上base的值，就是最终的值；
cell数组还能根据当前线程争用情况进行扩容，初始长度为2，每次扩容会增长一倍，直到扩容到大于等于cpu数量就不再扩容，
这也就是为什么LongAdder比cas和AtomicInteger效率要高的原因，后面两者都是volatile+cas实现的，他们的竞争维度是1，
LongAdder的竞争维度为“Cell个数+1”为什么要+1？因为它还有一个base，如果竞争不到锁还会尝试将数值加到base上；


### LinkedBlockingQueue
LinkedBlockingQueue也体现了这样的思想，在队列头入队，在队列尾出队，入队和出队使用不同的锁，相对于LinkedBlockingArray只有一个锁效率要高；


### ReentrantReadWriteLock 
ReentrantReadWriteLock 是一个读写锁，读操作加读锁，可以并发读，写操作使用写锁，只能单线程写；


### CopyOnWriteArrayList 
CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，
而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。
这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。
所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。  
CopyOnWrite并发容器用于读多写少的并发场景，因为，读的时候没有锁，但是对其进行更改的时候是会加锁的，
否则会导致多个线程同时复制出多个副本，各自修改各自的；


### 消除缓存行的伪共享
> https://blog.csdn.net/ff00yo/article/details/88795635

除了我们在代码中使用的同步锁和jvm自己内置的同步锁外，还有一种隐藏的锁就是缓存行，它也被称为性能杀手。
在多核cup的处理器中，每个cup都有自己独占的一级缓存、二级缓存，甚至还有一个共享的三级缓存，为了提高性能，cpu读写数据是以缓存行为最小单元读写的；32位的cpu缓存行为32字节，64位cup的缓存行为64字节，这就导致了一些问题。
例如，多个不需要同步的变量因为存储在连续的32字节或64字节里面，当需要其中的一个变量时，就将它们作为一个缓存行一起加载到某个cup-1私有的缓存中（虽然只需要一个变量，但是cpu读取会以缓存行为最小单位，将其相邻的变量一起读入），被读入cpu缓存的变量相当于是对主内存变量的一个拷贝，也相当于变相的将在同一个缓存行中的几个变量加了一把锁，这个缓存行中任何一个变量发生了变化，当cup-2需要读取这个缓存行时，就需要先将cup-1中被改变了的整个缓存行更新回主存（即使其它变量没有更改），然后cup-2才能够读取，而cup-2可能需要更改这个缓存行的变量与cpu-1已经更改的缓存行中的变量是不一样的，所以这相当于给几个毫不相关的变量加了一把同步锁；
为了防止伪共享，不同jdk版本实现方式是不一样的：
1. 在jdk1.7之前会 将需要独占缓存行的变量前后添加一组long类型的变量，依靠这些无意义的数组的填充做到一个变量自己独占一个缓存行；
2. 在jdk1.7因为jvm会将这些没有用到的变量优化掉，所以采用继承一个声明了好多long变量的类的方式来实现；
3. 在jdk1.8中通过添加sun.misc.Contended注解来解决这个问题，若要使该注解有效必须在jvm中添加以下参数：
-XX:-RestrictContended

sun.misc.Contended注解会在变量前面添加128字节的padding将当前变量与其他变量进行隔离；


### Java对象的Monitor机制
> https://blog.csdn.net/boyeleven/article/details/81390738   
> https://www.jianshu.com/p/7f8a873d479c

### java对象结构
> https://blog.csdn.net/zqz_zqz/article/details/70246212


## 扩展：分布式锁