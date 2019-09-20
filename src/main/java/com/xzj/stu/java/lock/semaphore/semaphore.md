## Semaphore信号量

#### synchronized与ReentrantLock两种锁机制类型都是“互斥锁”
> 互斥是进程同步关系的一种特殊情况，相当于只存在一个临界资源，因此同时最多只能给一个线程提供服务。  
> 但是，在实际复杂的多线程应用程序中，可能存在多个临界资源，这时候我们可以借助Semaphore信号量来完成多个临界资源的访问。

### Semaphore使用方法
#### Semaphore基本能完成ReentrantLock的所有工作，使用方法也与之类似，通过acquire()与release()方法来获得和释放临界资源。
#### Semaphore.acquire()方法默认为可响应中断锁，与ReentrantLock.lockInterruptibly()作用效果一致，也就是说在等待临界资源的过程中可以被Thread.interrupt()方法中断。
#### Semaphore的锁释放操作也由手动进行，因此与ReentrantLock一样，为避免线程因抛出异常而无法正常释放锁的情况发生，释放锁的操作也必须在finally代码块中完成。
#### Semaphore也实现了可轮询的锁请求与定时锁的功能，除了方法名tryAcquire与tryLock不同，其使用方法与ReentrantLock几乎一致。
#### Semaphore也提供了公平与非公平锁的机制，也可在构造函数中进行设定。默认非公平锁
#### Semaphore支持多个临界资源，而ReentrantLock只支持一个临界资源，所以可以认为ReentrantLock是Semaphore的一种特殊情况。

Semaphore类中比较重要的几个方法： 
1. public void acquire(): 用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
2. public void acquire(int permits):获取permits个许可 
3. public void release() { } :释放许可。注意，在释放许可之前，必须先获获得许可。 
4. public void release(int permits) { }:释放permits个许可

1. public boolean tryAcquire():尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false 
2. public boolean tryAcquire(long timeout, TimeUnit unit):尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
3. public boolean tryAcquire(int permits):尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false 
4. public boolean tryAcquire(int permits, long timeout, TimeUnit unit): 尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false 
5. 还可以通过availablePermits()方法得到可用的许可数目。