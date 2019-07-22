package com.xzj.stu.java.lock.semaphore;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class SemapWriteThread implements Runnable {
    private XzjBuffer xzjBuffer;

    public SemapWriteThread(XzjBuffer xzjBuffer) {
        this.xzjBuffer = xzjBuffer;
    }

    @Override
    public void run() {
        xzjBuffer.write();
    }
}
