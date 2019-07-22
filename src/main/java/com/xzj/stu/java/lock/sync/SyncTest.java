package com.xzj.stu.java.lock.sync;

/**
 * synchronized用法
 *
 * 修饰不同的对象时表现
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class SyncTest {
    public static void main(String[] args) {
        //1、当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，在同一时刻只能有一个线程得到执行，另一个线程受阻塞
//        SyncThread syncThread = new SyncThread();
//        Thread thread1 = new Thread(syncThread, "SyncThread1");
//        Thread thread2 = new Thread(syncThread, "SyncThread2");
//        thread1.start();
//        thread2.start();

        //2、创建了两个SyncThread的对象syncThread1和syncThread2，
        // 线程thread1执行的是syncThread1对象中的synchronized代码(run)，而线程thread2执行的是syncThread2对象中的synchronized代码(run)；
        // 我们知道synchronized锁定的是对象，这时会有两把锁分别锁定syncThread1对象和syncThread2对象，而这两把锁是互不干扰的，不形成互斥，所以两个线程可以同时执行
//        SyncThread syncThread1 = new SyncThread();
//        SyncThread syncThread2 = new SyncThread();
//        Thread thread1 = new Thread(syncThread1, "SyncThread1");
//        Thread thread2 = new Thread(syncThread2, "SyncThread2");
//        thread1.start();
//        thread2.start();

        //3、多个线程访问synchronized和非synchronized代码块
        // 当一个线程访问对象的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块
//        Counter counter = new Counter();
//        Thread thread1 = new Thread(counter, "A");
//        Thread thread2 = new Thread(counter, "B");
//        thread1.start();
//        thread2.start();

        //4、指定要给某个对象加锁
//        Account xzj = new Account("xzj", 10000f);
//        AccountOperator accountOperator = new AccountOperator(xzj);
//        for (int i =0; i < 5; i++) {
//            Thread thread = new Thread(accountOperator, "Thread" + i);
//            thread.start();
//        }

        //4.1、当有一个明确的对象作为锁时，就可以用类似下面这样的方式写程序
//        public void method3(SomeObject obj) {
//           //obj 锁定的对象
//           synchronized(obj){
//            // todo
//           }
//        }
        //4.2、当没有明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的对象来充当锁：
//        class Test implements Runnable {
//            private byte[] lock = new byte[0];  // 特殊的instance变量：
//            public void method() {
//                synchronized(lock) {
//                    // todo 同步代码块
//                }
//            }
//            public void run() {
//            }
//        }
        //零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：
        // 生成零长度的byte[]对象只需3条操作码，而Object lock = new Object()则需要7行操作码

        //5、修饰一个方法
//        SyncThreadTwo syncThread = new SyncThreadTwo("");
//        Thread thread1 = new Thread(syncThread, "SyncThread1");
//        Thread thread2 = new Thread(syncThread, "SyncThread2");
//        thread1.start();
//        thread2.start();

        //6、修饰一个静态的方法
        //syncThread1和syncThread2是SyncThread的两个对象，但在thread1和thread2并发执行时却保持了线程同步。
        //这是因为run中调用了静态方法method，而静态方法是属于类的，所以syncThread1和syncThread2相当于用了同一把锁
//        SyncThreadTwo syncThread1 = new SyncThreadTwo("static");
//        SyncThreadTwo syncThread2 = new SyncThreadTwo("static");
//        Thread thread1 = new Thread(syncThread1, "SyncThread1");
//        Thread thread2 = new Thread(syncThread2, "SyncThread2");
//        thread1.start();
//        thread2.start();

        //7、修饰一个类
        //synchronized作用于一个类T时，是给这个类T加锁，T的所有对象用的是同一把锁，其效果和【6】一样
//        SyncThreadThree syncThreadThree1 = new SyncThreadThree();
//        SyncThreadThree syncThreadThree2 = new SyncThreadThree();
//        Thread thread1 = new Thread(syncThreadThree1, "SyncThread1");
//        Thread thread2 = new Thread(syncThreadThree2, "SyncThread2");
//        thread1.start();
//        thread2.start();
//修饰类形如：
//        class ClassName {
//            public void method() {
//                synchronized(ClassName.class) {
//                    // todo
//                }
//            }
//        }

        //8、修饰Runnable实现类的run()
        //如果SyncRunnable的run方法不加synchronized的话，则四个线程几乎同时执行，交替混乱的打印数字
        //如果SyncRunnable的run方法添加synchronized同步，则四个线程同时只能有一个获得t的对象锁，四个线程交替执行，最后打印结果为4个0...9
//        SyncRunnable syncRunnable = new SyncRunnable();
//        new Thread(syncRunnable).start();
//        new Thread(syncRunnable).start();
//        new Thread(syncRunnable).start();
//        new Thread(syncRunnable).start();

        //
        final SyncEntity syncEntity = new SyncEntity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncEntity.fun1();
            }
        },"thread1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                syncEntity.fun2();
            }
        },"thread2").start();


//        总结：
//        A. 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；
//              如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。
//        B. 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。
//        C. 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制。
    }
}
