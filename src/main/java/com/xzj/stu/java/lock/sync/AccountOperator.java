package com.xzj.stu.java.lock.sync;

/**
 * 账户操作类
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class AccountOperator implements Runnable {
    private Account account;
    public AccountOperator(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        synchronized (account) {
            account.deposit(400);
            account.withdraw(500);
            System.out.println(Thread.currentThread().getName() + ":" + account.getBalance());
        }
    }
}
