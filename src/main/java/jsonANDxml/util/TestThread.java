package jsonANDxml.util;

public class TestThread {

    public static void main(String[] args) throws InterruptedException {

        Worker t = new Worker();
        t.start();

        Thread.sleep(2000);

        System.out.println("-1-1-1-");
        t.interrupt();
        System.out.println("000000");
        Thread.sleep(2000);
        t.stop();
        System.out.println("000111");
        Thread.sleep(2000);
        t.join();
        System.out.println("111111");
    }

}



