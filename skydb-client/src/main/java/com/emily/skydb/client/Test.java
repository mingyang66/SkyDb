package com.emily.skydb.client;

/**
 * @Description :
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/7 5:46 PM
 */
public class Test {
    private TestUserSub userSub = new TestUserSub();
    private TestUserSub userSub1 = new TestUserSub();
    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        Thread thread = test.thread("Thread-0：");
        Thread thread1 = test.thread1("Thread-1：");
        thread.start();
        Thread.sleep(100);
        thread1.start();
        System.out.println("情动完成---");
        Thread.sleep(200*1000);
        System.out.println("---休眠结束");
    }
    public Thread thread(String threadName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (userSub.getObject()){
                    try {
                        userSub.getObject().wait();
                        System.out.println(threadName+"----------已释放");
                    } catch (InterruptedException e) {
                        System.out.println("----------中断------------");
                    }
                }
            }
        });
        thread.setName(threadName);
        return thread;
    }

    public Thread thread1(String threadName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (userSub1.getObject()){
                    userSub1.getObject().notify();
                    System.out.println(threadName+"--------通知");
                }
            }
        });
        thread.setName(threadName);
        return thread;
    }
}
