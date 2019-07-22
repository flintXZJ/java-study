package com.xzj.stu.java.thread.notify;

import lombok.Getter;
import lombok.Setter;

/**
 * 一个java bean类，线程将会使用它并调用wait和notify方法。
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
@Setter
@Getter
public class Message {
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }
}
