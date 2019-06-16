package concurrent.compareIOmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BIOServerHandler implements Runnable {
    private Socket socket;

    public BIOServerHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * 当前线程要执行的任务
     */
    @Override
    public void run() {
        InputStream in = null;
        try {
            in = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            //读取客户端的数据
            while ((len = in.read(buffer)) > 0) {
                System.out.println(new String(buffer, 0, len));
            }
            //向客户端写数据
            OutputStream out = socket.getOutputStream();
            out.write("hello everybody!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
