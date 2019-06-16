package syncANDlockJianshu.syncANDlock_3;

public class SyncThis {
    public void test() {
        synchronized(this) {
            System.out.println("test开始..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test结束..");
        }
    }
}

class MyThread extends Thread {
    public void run() {
        SyncThis sync = new SyncThis();
        sync.test();
    }
}



