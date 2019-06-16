package syncANDlockJianshu.syncANDlock_4;

public class SyncBySyncObject {
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
    private SyncBySyncObject sync;
    public MyThread(SyncBySyncObject sync) {
        this.sync = sync;
    }
    public void run() {
        sync.test();
    }
}




