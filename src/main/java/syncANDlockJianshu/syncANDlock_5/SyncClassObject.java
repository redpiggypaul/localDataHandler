package syncANDlockJianshu.syncANDlock_5;

public class SyncClassObject {
    public void test() {
        synchronized (SyncClassObject.class) {
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
        SyncClassObject sync = new SyncClassObject();
        sync.test();
    }
}
