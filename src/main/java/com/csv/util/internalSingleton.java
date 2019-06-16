package com.csv.util;

import java.lang.reflect.Constructor;

public class internalSingleton {
    private static class LazyHolder {
        private static final internalSingleton INSTANCE = new internalSingleton();
    }

    private internalSingleton() {
    }

    public static internalSingleton getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void main(String[] args) {
        try {
            //获得构造器
            Constructor con = internalSingleton.class.getDeclaredConstructor();
            //设置为可访问
            con.setAccessible(true);
            //构造两个不同的对象
            internalSingleton singleton1 = (internalSingleton) con.newInstance();
            internalSingleton singleton2 = (internalSingleton) con.newInstance();
            //验证是否是不同对象
            System.out.println(singleton1.equals(singleton2));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
