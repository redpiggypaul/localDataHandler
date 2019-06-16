package concurrent.nettyDemo.nettyTCP01;




import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/** 
  * Netty 服务端代码 
  *  
  */
public class MyServer {
    public static void main(String[] args) throws IOException {
        ServerSocket svrSocket = new ServerSocket(8082);
        while(true){
            Socket socket = svrSocket.accept();
            //足够大的一个缓冲区
            byte[] buf = new byte[1024*1024];
            InputStream in = socket.getInputStream();
            int byteRead = in.read(buf, 0, 1024*1024);
            String dataString = new String(buf, 0, byteRead);
            System.out.println(dataString);
            in.close();
            socket.close();
        }
    }
}
