package geekJavaConCodePrastic.prodConsMode_36;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;


public class Logger {
    //flush 批量
    static final int batchSize = 500;
    // 任务队列
  //  final BlockingQueue<LogMsg> bq = new BlockingQueue<>();
    //ConcurrentLinkedQueue
   // final LinkedBlockingQueue<LogMsg> localLBQ = new LinkedBlockingQueue<LogMsg>(1024);
    final ArrayBlockingQueue<LogMsg>  localABQ = new ArrayBlockingQueue<LogMsg> (1024);
    // 只需要一个线程写日志
    ExecutorService es =
            Executors.newFixedThreadPool(1);

    // 启动写日志线程
    void start() throws IOException {

        File file = File.createTempFile("foo", ".log");
        final FileWriter writer;

        writer = new FileWriter(file);

        this.es.execute(() -> {
            try {
                // 未刷盘日志数量
                int curIdx = 0;
                long preFT = System.currentTimeMillis();
                while (true) {
                    LogMsg log = localABQ.poll(5, TimeUnit.SECONDS);
                    // 写日志
                    if (log != null) {
                        writer.write(log.toString());
                        ++curIdx;
                    }
                    // 如果不存在未刷盘数据，则无需刷盘
                    if (curIdx <= 0) {
                        continue;
                    }
                    // 根据规则刷盘
                    if (log != null && log.level == logLevel.ERROR ||
                            curIdx == batchSize ||
                            System.currentTimeMillis() - preFT > 5000) {
                        writer.flush();
                        curIdx = 0;
                        preFT = System.currentTimeMillis();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // 写 INFO 级别日志
    void info(String msg) throws InterruptedException {
        localABQ.put(new LogMsg(logLevel.INFO, msg));
    }

    // 写 ERROR 级别日志
    void error(String msg) throws InterruptedException {
        localABQ.put(new LogMsg(logLevel.ERROR, msg));
    }
}

/*
volatile flag4stop;

while(!flag4stop || bq.size() >0)
｛｝

    public void shutdown（）{
        flag4stop =true;
        Es.shutdown（）；
        while（es.awaitUtilTime（5，timeutil.seconds）｛
        es.shutdownNow（）；
｝
 */


/*
public class Logger {
...

    volatile boolean flag4stop;

    // 启动写日志线程
    void start() throws IOException {
...
        this.es.execute(() -> {
            try {
                // 未刷盘日志数量
                int curIdx = 0;
                long preFT = System.currentTimeMillis();
                while (!stop) {
                   	...
                }
            } catch (InterruptedException e) {
                // 重新设置线程中断状态
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
...
            }
        });
    }
...
    void stop(){
        flag4stop = true;
        es.shutdown();
    }
}
 */


