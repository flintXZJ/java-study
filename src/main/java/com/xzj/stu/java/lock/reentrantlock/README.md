### ReentantLock

####　　可重入锁，顾名思义，这个锁可以被线程多次重复进入进行获取操作。ReentantLock继承接口Lock并实现了接口中定义的方法，除了能完成synchronized所能完成的所有工作外，还提供了诸如可响应中断锁、可轮询锁请求、定时锁等避免多线程死锁的方法。
####　　Lock实现的机理依赖于特殊的CPU指定，可以认为不受JVM的约束，并可以通过其他语言平台来完成底层的实现。在并发量较小的多线程应用程序中，ReentrantLock与synchronized性能相差无几，但在高并发量的条件下，synchronized性能会迅速下降几十倍，而ReentrantLock的性能却能依然维持一个水准，因此我们建议在高并发量情况下使用ReentrantLock。
####　　ReentrantLock引入两个概念：公平锁与非公平锁。公平锁指的是锁的分配机制是公平的，通常先对锁提出获取请求的线程会先被分配到锁。反之，JVM按随机、就近原则分配锁的机制则称为不公平锁。ReentrantLock在构造函数中提供了是否公平锁的初始化方式，默认为非公平锁。这是因为，非公平锁实际执行的效率要远远超出公平锁，除非程序有特殊需要，否则最常用非公平锁的分配机制。
####　　ReentrantLock通过方法lock()与unlock()来进行加锁与解锁操作，与synchronized会被JVM自动解锁机制不同，ReentrantLock加锁后需要手动进行解锁。为了避免程序出现异常而无法正常解锁的情况，使用ReentrantLock必须在finally控制块中进行解锁操作。通常使用方式如下所示：
> Lock lock = new ReentrantLock();  
> 　　try {  
> 　　　　lock.lock();  
> 　　　　//...进行任务操作  
> 　　} finally {  
> 　　　　lock.unlock();  
> 　　}

#### ReentrantLock提供的可响应中断锁、可轮询锁请求、定时锁等机制与操作方式。

* 1、线程在等待资源过程中需要中断
> ReentrantLock在获取锁的过程中有2种锁机制，忽略中断锁和响应中断锁。
>> 当等待线程A或其他线程尝试中断线程A时，  
>> a. 忽略中断锁机制则不会接收中断，而是继续处于等待状态；  
>> b. 响应中断锁则会处理这个中断请求，并将线程A由阻塞状态唤醒为就绪状态，不再请求和等待资源。
  
> lock.lock() : 可设置锁机制为忽略中断锁  
> lock.lockInterruptibly() : 可设置锁机制为响应中断锁

##### 下述例子描述了，一个写线程和一个读线程分别操作同一个对象的写方法和读方法，写方法需要执行10秒时间，主线程中在启动写线程writer和读线程reader后，启动了第三个线程，这个线程判断当程序执行5秒后，如果读线程依然处于等待状态，就将他中断，不再继续等待资源。
> 见【ReentrantLockInterruptTest.java】  
>> 由例子可知，ReentrantLock.lockInterruptibly()方法可设置线程在获取锁的时候响应其他线程对当前线程发出的中断请求。  
>> 注意，此处响应中断锁是指正在获取锁的过程中，如果线程此时并非处于获取锁的状态，通过此方法设置是无法中断线程的。（写线程设置为4秒，读线程设置为5秒。第三方线程在5秒后中断，实验能否中断已获取锁的线程。）  

* 2、实现可轮询的锁请求
> 在synchronized中，一旦发生死锁，唯一能够恢复的办法只能重新启动程序，唯一的预防方法是在设计程序时考虑完善不要出错。而有了Lock以后，死锁问题就有了新的预防办法，它提供了tryLock()轮询方法来获得锁，如果锁可用则获取锁，如果锁不可用，则此方法返回false，并不会为了等待锁而阻塞线程，这极大地降低了死锁情况的发生。典型使用语句如下:  
>> Lock lock = ...;  
   if(lock.tryLock()){  
   　　//锁可用，则成功获取锁  
   　　try {  
   　　　　//获取锁后进行处理  
   　　} finally {  
   　　　　lock.unlock();  
   　　}  
   } else {  
   　　//锁不可用，其他处理方法  
   }    
>> 详细见【ReentrantLockPollingTest.java、MyBuffer.readPolling()】

* 3、定时锁请求
> 在synchronized中，一旦发起锁请求，该请求就不能停止了，如果不能获得锁，则当前线程会阻塞并等待获得锁。在某些情况下，你可能需要让线程在一定时间内去获得锁，如果在指定时间内无法获取锁，则让线程放弃锁请求，转而执行其他的操作。Lock就提供了定时锁的机制，使用Lock.tryLock(long timeout, TimeUnit unit)来指定让线程在timeout单位时间内去争取锁资源，如果超过这个时间仍然不能获得锁，则放弃锁请求，定时锁可以避免线程陷入死锁的境地  
>> 详细见【ReentrantLockPollingTest.java、MyBuffer.readPolling()】

#### ReentrantReadWriteLock
#### ReentantLock实现原理