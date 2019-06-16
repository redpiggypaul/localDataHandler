package concurrent.compareIOmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) {
        server1();
    }

    /**
     * 传统，只能是1对1
     */
    public static void server1() {
        ServerSocket server = null;
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            server = new ServerSocket(8000);
            System.out.println("服务端启动成功，监听端口为8000，等待客户端连接...");

            socket = server.accept(); //阻塞 客户端
            in = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            //读取客户端的数据
            while ((len = in.read(buffer)) > 0) {
                System.out.println(new String(buffer, 0, len));
            }
            //向客户端写数据
            out = socket.getOutputStream();
            out.write("hello everybody!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多线程
     */
    public static void server2() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(8000);
            System.out.println("服务端启动成功，监听端口为8000，等待客户端连接...");
            while (true) {
                Socket socket = new Socket();
                //针对每个连接创建一个线程，去处理io操作
                //socket来了之后就创建线程  不合理
                new Thread(new BIOServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池
     */
    public static void server3() {
        ServerSocket server = null;
        ExecutorService executorService = Executors.newFixedThreadPool(60);
        try {
            server = new ServerSocket(8000);
            System.out.println("服务端启动成功，监听端口为8000，等待客户端连接...");
            while (true) {
                Socket socket = new Socket();
                //使用线程池中的线程去执行每个对应的任务
                executorService.execute(new BIOServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
