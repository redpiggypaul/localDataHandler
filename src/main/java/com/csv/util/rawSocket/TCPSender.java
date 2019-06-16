package concurrent.rawSocket;
import java.net.*;
import java.io.*;

public class TCPSender {


    public static void main(String[] args) throws IOException
    {
   /*     if (args.length!=4)
        {
            System.out.println("���������е������ʽ�����밴�����¸�ʽ�������룡");
            System.out.println("ԴIP��ַ  Դ�˿�  Ŀ��IP��ַ Ŀ�Ķ˿�");
            System.exit(0);
        }
        */
        String source_ip="127.0.0.1";
        int source_port= 1234;
        String dest_ip="127.0.0.1";
        int dest_port= 9994;

      /*  if (dest_port!=9994)
        {
            System.out.println("�뱣��Ŀ�Ķ˿�Ϊ10000!");
            System.exit(0);
        }
*/

        OutputStream os=null;
        PrintStream ps=null;
        BufferedReader bf=null;
        String message="This is my homework of network!";
        Socket soc=null;
        try{
            soc=new Socket(dest_ip,dest_port);
            System.out.println("***************************************************");
            System.out.println("Connect to server......");
            System.out.println("***************************************************");
            System.out.println();
            bf=new BufferedReader(
                    new InputStreamReader(System.in));
            System.out.print("Please input the message: ");
            message=bf.readLine();
            os=soc.getOutputStream();
            ps=new PrintStream(os);
            ps.println(message);


            bf.close();
            ps.close();
            os.close();
            System.out.println();
            System.out.println("***************************************************");
            System.out.println("Send OK !");
            System.out.println("The message was send to the address: "+dest_ip+"["+dest_port+"]");
            System.out.println("***************************************************");
            soc.close();
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
