package com.xzj.stu.java.thread.thdlocal;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 多线程共享一个数据库连接会引发很多问题，我们可以为每个线程单独建立一个连接资源。
 * 可以结合单例模式与ThreadLocal创建“线程内部的单例模式”，每个线程都拥有一个实例
 *
 * @author zhijunxie
 * @date 2019/5/15
 */
public class ConnectionManager {
    private static ThreadLocal<Connection> maps = new ThreadLocal<>();

    public Connection getInstance() {
        Connection connection = maps.get();
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("");
                maps.set(connection);
            } catch (Exception e) {
            }
        }
        return connection;
    }
}
