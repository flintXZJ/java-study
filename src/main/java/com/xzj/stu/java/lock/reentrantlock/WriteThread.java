package com.xzj.stu.java.lock.reentrantlock;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class WriteThread implements Runnable {
    private MyBuffer myBuffer;

    public WriteThread(MyBuffer myBuffer) {
        this.myBuffer = myBuffer;
    }

    @Override
    public void run() {
        myBuffer.write();
    }
}
