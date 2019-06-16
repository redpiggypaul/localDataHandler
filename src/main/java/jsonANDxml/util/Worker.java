package jsonANDxml.util;

class Worker extends Thread {

    @Override
    public void run() {
        int i = 0;
        while (i<20) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            ++i;
            System.out.println(Thread.currentThread().getName() + "i: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
