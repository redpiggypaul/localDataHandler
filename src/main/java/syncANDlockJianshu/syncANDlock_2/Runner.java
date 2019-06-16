package syncANDlockJianshu.syncANDlock_2;

public class Runner {


    public static void main(String[] args) {
        //System.out.println(
        long startTime = System.currentTimeMillis();
        //);
        //    System.out.println(Calendar.getInstance().getTimeInMillis());
        //    System.out.println(new Date().getTime());


        for (int i = 0; i < 20; i++) {
            Thread thread = new MyThread();
            thread.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime - 3000);

    }
}
