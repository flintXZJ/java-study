#### Java中的阻塞队列
1. ArrayBlockingQueue ：由数组结构组成的有界阻塞队列。  
一个锁ReentrantLock lock来控制所有操作的并发。
此队列按照先进先出（FIFO）的原则对元素进行排序。默认情况下不保证访问者公平的访问队列。


2. LinkedBlockingQueue ：由链表结构组成的单向有界阻塞队列。  
两个独立锁(putlock、takelock)提高并发。2个锁分别操作队头队尾，因此生产者和消费者可以并行地操作队列中的数据。
又因为元素个数使用AtomicInteger来记录，保证了生产者和消费者并发处理时的线程安全。


3. PriorityBlockingQueue ：支持优先级排序的无界阻塞队列。   
默认情况下元素采取自然顺序升序排列。可以自定义实现compareTo()方法来指定元素进行排序规则，或者初始化PriorityBlockingQueue时，
指定构造参数Comparator来对元素进行排序。需要注意的是不能保证同优先级元素的顺序。


4. DelayQueue：使用优先级队列实现的无界阻塞队列。   
是一个支持延时获取元素的无界阻塞队列。队列使用PriorityQueue来实现。队列中的元素必须实现Delayed接口，
在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。  
> 应用场景：  
> 1. 缓存系统的设计：可以用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，表示缓存有效期到了。
> 2. 定时任务调度：使用DelayQueue保存当天将会执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，从比如TimerQueue就是使用DelayQueue实现的。


5. SynchronousQueue：不存储元素的阻塞队列。   
是一个不存储元素的阻塞队列。每一个put操作必须等待一个take操作，否则不能继续添加元素。SynchronousQueue可以看成是一个传球手，
负责把生产者线程处理的数据直接传递给消费者线程。队列本身并不存储任何元素，非常适合于传递性场景,比如在一个线程中使用的数据，传递给另外一个线程使用，
SynchronousQueue的吞吐量高于LinkedBlockingQueue 和 ArrayBlockingQueue。


6. LinkedTransferQueue：由链表结构组成的无界阻塞队列。   
是一个由链表结构组成的无界阻塞TransferQueue队列。相对于其他阻塞队列，LinkedTransferQueue多了tryTransfer和transfer方法。 
> 1. transfer方法：如果当前有消费者正在等待接收元素（消费者使用take()方法或带时间限制的poll()方法时），
transfer方法可以把生产者传入的元素立刻transfer（传输）给消费者。如果没有消费者在等待接收元素，transfer方法会将元素存放在队列的tail节点，
并等到该元素被消费者消费了才返回。 
> 2. tryTransfer方法。则是用来试探下生产者传入的元素是否能直接传给消费者。如果没有消费者等待接收元素，则返回false。
和transfer方法的区别是tryTransfer方法无论消费者是否接收，方法立即返回。而transfer方法是必须等到消费者消费了才返回。
对于带有时间限制的tryTransfer(E e, long timeout, TimeUnit unit)方法，则是试图把生产者传入的元素直接传给消费者，
但是如果没有消费者消费该元素则等待指定的时间再返回，如果超时还没消费元素，则返回false，如果在超时时间内消费了元素，则返回true。


7. LinkedBlockingDeque：由链表结构组成的双向有界阻塞队列。
是一个由链表结构组成的双向阻塞队列。所谓双向队列指的你可以从队列的两端插入和移出元素。
双端队列因为多了一个操作队列的入口，在多线程同时入队时，也就减少了一半的竞争。
相比其他的阻塞队列，LinkedBlockingDeque多了addFirst，addLast，offerFirst，offerLast，peekFirst，peekLast等方法，
以First单词结尾的方法，表示插入，获取（peek）或移除双端队列的第一个元素。
以Last单词结尾的方法，表示插入，获取或移除双端队列的最后一个元素。
另外插入方法add等同于addLast，移除方法remove等效于removeFirst。
但是take方法却等同于takeFirst，不知道是不是Jdk的bug，使用时还是用带有First和Last后缀的方法更清楚。 
在初始化LinkedBlockingDeque时可以设置容量防止其过渡膨胀。另外双向阻塞队列可以运用在“工作窃取”模式中。

