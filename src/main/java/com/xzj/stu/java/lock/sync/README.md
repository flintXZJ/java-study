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