package concurrent.compareIOmodel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    private int port;
    private InetSocketAddress address = null;
    private Selector selector;

    public NIOServer(int port) {
        try {
            this.port = port;
            address = new InetSocketAddress(this.port);
            ServerSocketChannel server = ServerSocketChannel.open();
            //服务器通道设置成非阻塞的模式
            //server.configureBlocking(false);
            server.socket().bind(address);
            selector = Selector.open();
            //每当有客户端连接上来的时候，默认它已经连接上来了
            //而这个连接我需要记录一个它的状态  connected
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动成功：" + this.port);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Selector开始轮询
    public void listen() {
        try {
            while (true) {
                //accept()阻塞的,select()也是阻塞的
                int wait = this.selector.select();
                if (wait == 0) {
                    continue;
                }
                //SelectionKey代表的是客户端和服务端连接的一个关键
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    //针对每一个客户端进行相应的操作
                    process(key);
                    i.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //处理每一个客户端 key
    private void process(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            //客户端一旦连接上来  读写
            //往这个selector上注册key  read   接下来可以读
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            int len = client.read(buffer);
            //读取完成了
            if (len > 0) {
                buffer.flip(); //固定
                String content = new String(buffer.array(), 0, len);
                client.register(selector, SelectionKey.OP_WRITE);
                System.out.println(content);
            }
            buffer.clear();
        } else if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();
            client.write(buffer.wrap("hello world".getBytes()));
            client.close();
        }
    }

    public static void main(String[] args) {
        new NIOServer(8000).listen();
    }
}
