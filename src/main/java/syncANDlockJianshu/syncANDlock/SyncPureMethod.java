package syncANDlockJianshu.syncANDlock;

public class SyncPureMethod {
    public synchronized void test() {
        System.out.println("test开始..");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test结束..");
    }
}

class MyThread extends Thread {
    public void run() {
        SyncPureMethod sync = new SyncPureMethod();
        sync.test();
    }
}



