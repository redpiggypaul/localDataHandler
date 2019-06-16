package concurrent.rawSocket;

import java.net.*;
import java.io.*;

public class  TCPListener	//����TCP���ݰ��ķ���˳���
{
    public static void main(String[] args)
    {
        ServerSocket listen=null; //����һ��ServerScoket���͵�����
        Socket soc=null; //����Socket��Ӧ�ã������׽�������
        String message=null;
        BufferedReader bf=null;//����һ��BufferedReader���͵Ķ����ݵ�����
        InputStreamReader isr=null;//����һ���ɶ�������

        try{
            listen=new ServerSocket(9994); //����һ������1000�˿ڵĶ���
            soc=listen.accept();//����socket�����������Ķ˿�ʱ�������ϸ�socket
            System.out.println("*******************************************");
            System.out.println("server ok");
            System.out.println("*******************************************");
            System.out.println("");
            isr=new InputStreamReader(soc.getInputStream());//����һ�������׽���soc�Ŀɶ�������
            bf=new BufferedReader(isr);//��soc�Ŀɶ���������Ϊ��������һ��BufferedReader
            message=bf.readLine();//��ÿ��Ϊ��λ��ȡ�ӿͻ��˷���������
            System.out.println("Socket:"+soc);//��ʾ�����ͷ���IP��ַ�Ͷ˿ں�
            System.out.println("Receive the message from :"+message); //��ʾ���յ�������
            System.out.println("");
            System.out.println("*******************************************");
            isr.close();//�ر�������isr
            bf.close(); //�ر��ַ���bf
            soc.close(); //�ر�Socket�׽���
        }
        catch(Exception e)//�쳣����
        {
            System.out.println("Error:"+e);
        }

    }

}


