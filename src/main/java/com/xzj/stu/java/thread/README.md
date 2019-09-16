## 线程
> 参考：https://www.cnblogs.com/hanganglin/articles/3517178.html
> 参考：https://blog.csdn.net/luoweifu/article/details/46664809


### 一、java线程的生命周期
* 1 new：new语句创建时，线程状态
* 2 runable：线程运行状态，start()方法启动之后。线程只是在jvm中处于执行状态，但是在操作系统中可能还在等待cpu分片。或者操作系统中该线程在等待io结束
* 3 block：等待获取锁，或获取锁之后调用object.wait()之后等待获取锁
* 4 waiting：调用object.wait()、Thread.join、LockSupport.park之后
* 5 timed_waiting：thread.sleep()、object.wait(timeouot)、thread.join(timeout)等，进入此状态
* 6 terminated：线程结束

操作系统中线程生命周期
* 1、新建状态（NEW）：用Thread的new语句创建了线程对象，此时对象只在对内存中分配了内存，并初始化其成员变量的值。
* 2、就绪状态（RUNNABLE）：当新建状态下的线程对象调用了start()方法后，该线程就进入了就绪状态。Java虚拟机会为其创建方法调用栈和程序计数器，等待调度运行，处于这个状态的线程位于可运行池中，等待获得CPU使用权。
* 3、运行状态（RUNNING）：正在被CPU执行的线程状态：如果处于就绪状态的线程获得了CPU，开始执行run()方法的线程执行体。
* 4、阻塞状态（BLOCKED）：是指线程因为某种原因放弃了cpu 使用权，也即让出了cpu timeslice，暂时停止运行。直到线程进入可运行(runnable)状态，才有机会再次获得cpu timeslice 转到运行(running)状态。
> 阻塞分三种情况：  
> 1、等待阻塞（o.wait->等待对列）：当线程运行获取对象锁后，运行(running)的线程执行Object.wait()方法，JVM就会把该线程加入对象的等待池（waitting queue）中，这种状态必须等待其他线程调用同个对象的notify()或notifyAll()方法时才有可能激活为就绪状态；  
> 2、同步阻塞(lock->锁池)：运行(running)的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入该对象的锁池(lock pool)中，当对象的同步锁被释放后，JVM就会根据一定的调度算法，将处于对象锁中阻塞状态的某个线程激活为就绪状态；  
> 3、其他阻塞(sleep/join)：运行(running)的线程执行Thread.sleep(long ms)或t.join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入可运行(runnable)状态。

* 5、线程死亡（DEAD）：线程会以下面三种方式结束，结束后就是死亡状态
> 1、正常结束：run()或call()方法执行完成，线程正常结束  
> 2、异常结束：线程抛出一个未捕获的Exception或Error  
> 3、调用stop：直接调用该线程的stop()方法来结束该线程—该方法通常容易导致死锁，不推荐使用  

![线程周期](http://images.cnitblog.com/i/426802/201406/232002051747387.jpg)

#### 终止线程的四种方式
> 1、正常运行结束  
> 2、使用退出标志退出线程：伺服线程  
> 3、Interrupt方法结束线程：A. 线程处于阻塞状态; B. 线程未处于阻塞状态  
> 4、stop方法终止线程（线程不安全）  


### 二、Java创建多线程的几种方式
> com.xzj.stu.java.thread.create  

#### 1、通过继承Thread类创建线程（了解即可，不常用）
> 这种方式创建多线程的缺点：① Java是单继承的，继承Thead类后不能继承其他类，可能不满足开发需求；
  
#### 2、通过实现Runnable接口创建线程（推荐使用）
> 这种方式创建多线程的优点：① 避免了Java但继承带来的局限性

#### 3、通过FutureTask类创建带返回值的线程（需要获得线程返回值时使用）
> FutureTask implements RunnableFuture extends Runnable,Future
> * CountDownLatch 

#### 4、通过TimerTask类创建计划任务类线程
> Timer和TimerTask是用来创建定时任务，定时任务实质就是一个线程.  
> TimerTask继承了Runnable接口，通过Timer启动计划任务入口有schedule和scheduleAtFixedRate两种方法


### 线程的基本方法
#### Thread.sleep(long millis)和Thread.yield()
##### 这两个方法都会让当前正在执行的线程处于暂时停止执行的状态，交出CPU的使用权一段时间。
> 与Object.wait()方法不同的是，Thread.sleep(long millis)和Thread.yield()在暂停线程的同时不会释放已获得的对象锁，而Object.wait()会暂停线程并且释放对象锁。

##### 两者的区别如下：
* 1、Thread.sleep方法必须带一个时间参数，单位毫秒，当线程执行sleep后，在指定时间内，将转为阻塞状态；Thread.yield方法不带参数，当线程执行yield后，线程将进入就绪状态。
* 2、Thread.sleep会抛出InterruptedException异常，而Thread.yield方法不会抛出异常。
* 3、sleep()方法比yield()方法具有更好的移植性。？？
* 4、sleep()方法会给其他线程运行的机会，而不考虑其他线程的优先级，因此会给较低线程一个运行的机会；yield()方法只会给相同优先级或者更高优先级的线程一个运行的机会。 ？？
##### 实际上，yield()方法对应了如下操作： 先检测当前是否有相同优先级的线程处于同可运行状态，如有，则把 CPU 的占有权交给此线程，否则继续运行原来的线程。所以yield()方法称为“退让”，它把运行机会让给了同等优先级的其他线程。yield()只是提前结束当前线程占用CPU的时间，线程转为就绪状态，等待下一个时间片再继续获得CPU并执行


#### Thread.join()
##### Thread.join()可以将多线程的异步变为同步，在父线程调用子线程的join方法后，必须等待子线程执行结束，父线程才会继续执行下去。Thread.join()方法会抛出InterruptedException异常。
     

#### Thread.start()和Thread.run() 
##### 启动线程应该使用Thread.start()，Thread.run()只是调用Runnable中的run方法，并没有启动线程，此时整个程序还是只有一个线程， 顺序执行。


#### Thread.setDaemon()设置守护线程
##### Java中有两类线程，分别是用户线程(User Thread)和守护线程(Daemon Thread)
##### 守护线程是指在程序运行时后台提供一种通用服务的线程，如垃圾回收线程就是一个守护线程，当所有的非守护线程结束时，程序也就终止了，同时会结束所有的守护进程
##### 用户线程和守护线程唯一的区别就是，当程序中只剩守护线程时，程序就会结束，而只要程序中还存在一个非守护线程，程序就不会终止。
> 1) 普通线程转换为守护线程。Thread.SetDaemon(true)可以将普通线程转换为守护线程，但是设置必须在Thread.start()之前，否则会报IllegalThreadStateException异常。  
> 2) Daemon线程产生的子线程也是Daemon的。  
> 3) 守护线程应该永远不去访问固有资源，如文件和数据库等，因为他可能随时会中断。


#### Thread.interrput()中断线程(参考：http://jiangzhengjun.iteye.com/blog/652269)
##### 调用线程的Thread.interrupt方法中断线程时，JVM将会将对应线程内的中断状态位设置为true，可以在线程执行的方法中调用Thread.interrupted()或Thread.currentThread().isInterrupted()来检测中断位是否为true，至于线程下一步是死亡还是继续执行完全取决于程序本身，这一点与强制结束线程的已废弃的方法stop不同
##### 通过Thread.interrupt请求中断当前线程时，当前线程可能正处于非阻塞状态、阻塞状态或请求锁临时状态
* 1、被请求中断时，当前线程处于非阻塞状态
##### 此时可以在程序中调用方法获取线程内中断状态位的值，并根据该值自由决定是否结束当前线程。获取中断位值建议有两种方法：①Thread.currentThread().isInterrupted()方法，返回状态值；②Thread.interrupted()静态方法，返回状态值，并将中断位状态值重置为false。也就是说，此时若调用两次Thread.interrupted()方法，第二次方法返回值为false。因此建议使用方法Thread.currentThread().isInterrupted()来获取中断位状态值
* 2、被请求中断时，当前线程处于阻塞状态
> 1、对于由于调用Object.wait()、Thread.sleep()或Thread.join()等方式而处于阻塞状态的线程，被请求中断时会抛出异常InterruptException，使当前线程从阻塞状态激活进入异常代码块，便于结束。注意，在抛出异常InterruptException后，中断状态位会被重置为false，因此在使用Thread.currentThread().isInterrupted()检测循环是否中断的代码块内，若捕捉到此异常，必须将状态位重新设置为true，否则线程循环将永不停止  
> 2、对由于请求I/O操作而处于阻塞状态的线程，被请求中断时，I/O通道会立即被关闭，并抛出异常ClosedByInterruptException，处理方式与上述一致  
* 3、被请求中断时，当前线程正处于获取锁
##### 被请求中断时，当前线程正处于获取锁的过程中，这时候线程是无法响应中断的，也就是说，当线程采用synchronized争夺锁资源而发生死锁时，使用Thread.interrupt是无法使线程中断的。补充一点，当使用Lock锁并且通过方法lockInterruptibly()设置响应中断锁时，线程可以被中断（详细参考博文http://www.cnblogs.com/hanganglin/articles/3577096.html）  

##### 结合上述说明，处理线程中断的方法应该同时适用于阻塞和非阻塞线程，一个常用方法架构如下所示：
> public void run() {  
      try {  
          ...  
          /* 
           * 不管循环里是否调用过线程阻塞的方法如sleep、join、wait，这里还是需要加上 
           * !Thread.currentThread().isInterrupted()条件，虽然抛出异常后退出了循环，显 
           * 得用阻塞的情况下是多余的，但如果调用了阻塞方法但没有阻塞时，这样会更安全、更及时。 
           */  
          while (!Thread.currentThread().isInterrupted()&& more work to do) {  
              do more work   
          }  
      } catch (InterruptedException e) {  
          //线程在wait或sleep期间被中断了  
      } finally {  
          //线程结束前做一些清理工作  
      }  
  }


### ThreadLocal
#### ThreadLocal主要用来更方便地访问线程内部变量，提供了保持对象的方法和避免参数传递的方便的对象访问方式  
#### 当线程调用ThreadLocal.set()方法时，具体的值是保存在线程内部的ThreadLocal.ThreadLocalMap对象中，也就是说，每个线程设值的变量都只限于本线程访问，对于其他线程是隔离的
#### ThreadLocal不是用来解决对象共享访问问题的，也不是为线程提供共享变量的副本，而仅仅是为线程隔离对象。
#### 此外，线程的数据是保存在线程内部的变量中的，而非保存在ThreadLocal对象中，ThreadLocal的get与set方法可以直接操作线程Thread内部ThreadLocal.ThreadLocalMap对象。在ThreadLocal.ThreadLocalMap中保存了一个Entry数组，通过ThreadLocal对象作为数组下标操作数据。
#### 在ThreadLocal.ThreadLocalMap中，使用了弱引用WeakReference，避免了内存泄露
#### 多线程共享一个数据库连接会引发很多问题，我们可以为每个线程单独建立一个连接资源。可以结合单例模式与ThreadLocal创建“线程内部的单例模式”，每个线程都拥有一个实例。 


### TimeUnit枚举工具
#### TimeUnit是一个枚举，可以使用它来简化某些操作，如让线程休眠5分钟，可以写成：TimeUnit.MINUTES.sleep(5)，相当于Thread.sleep(5*60*1000)，时间会在TimeUnit内部自动转化。


### Object.wait()、Object.notify()和Object.notifyAll()
#### Object.wait(): 是指线程在获取对象锁后，由于某些条件的不成立，主动释放对象锁，同时本线程进入对象等待池中处于阻塞状态.
> Object.wait() ：线程调用此方法后，只有当其他线程调用同个对象的notify()或notifyAll()方法后，才可能激活为就绪状态  
> Object.wait(long timeout) ：线程调用此方法后，当其他线程调用同个对象的notify()或notifyAll()方法，或者超过时间timeout，线程都可能激活为就绪状态
#### Object.notify(): JVM会根据调度策略调取一个对象等待池中的线程，将其从阻塞状态激活为就绪状态，当此线程再次获得对象锁和CPU后，就可以进入执行状态
> 方法Object.notify()和方法Object.notifyAll()用于将处于wait等待状态的线程激活为就绪状态，notify()是根据调度策略激活某一个线程，notifyAll()是将所有处于等待线程池中的线程全部激活为就绪状态，但是激活后就绪状态的线程要想重新执行，必须再次获得对象锁

> 在synchronized同步块中，一旦线程捕获某个对象的同步锁，系统就很难控制线程，必须等待线程主动释放对象锁，这时候，在同步块内，使用Object.wait()可以使线程在进入synchronized同步块后主动释放对象锁。因此Object.wait()、Object.notify()和Object.notifyAll()方法必须在synchronized同步块内使用，否则会抛出IllegalMonitorStateException异常


### Condition.await()、Condition.signal()和Condition.signalAll()
#### 同Object.wait()、Object.notify()和Object.notifyAll()对应功能一致，Object的方法用于synchronized同步块中，而Condition的方法用于ReentrantLock的lock()与unlock()之间。
#### 可用Reentrant.newCondition()来产生一个新的Condition。
