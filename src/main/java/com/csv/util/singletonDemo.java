package com.csv.util;

public class singletonDemo {

    private static volatile singletonDemo singletonInstance = null;

    private singletonDemo() {
    }

    public static singletonDemo getSingleton() {
        if (singletonInstance == null) { // 尽量避免重复进入同步块
            synchronized (singletonDemo.class) { // 同步.class，意味着对同步类方法调用
                if (singletonInstance == null) {
                    singletonInstance = new singletonDemo();
                }
            }
        }
        return singletonInstance;
    }


}
