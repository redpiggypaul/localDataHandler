package com.csv.loader;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by appledev131 on 7/16/16.
 */
public class FolderList {
    private String dir_name = null;
    private String list_name = null;
    private BufferedWriter out = null;
    Vector<String> ver = null;

    public FolderList(String dir_name, String list_name) throws IOException {
        this.dir_name = dir_name;    //文件夹地址
        this.list_name = list_name;    //保存文件列表的文件地址
        ver = new Vector<String>();    //用做堆栈
    }

    public void getList() throws Exception {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list_name, true)));    //以追加的方式写入到指定的文件
        ver.add(dir_name);
        while (ver.size() > 0) {
            File[] files = new File(ver.get(0).toString()).listFiles();    //获取该文件夹下所有的文件(夹)名
            ver.remove(0);

            int len = files.length;
            for (int i = 0; i < len; i++) {
                String tmp = files[i].getAbsolutePath();
                if (files[i].isDirectory())    //如果是目录，则加入队列。以便进行后续处理
                { // ver.add(tmp);
                } else {
                    out.write(tmp + "\r\n");        //如果是文件，则直接输出文件名到指定的文件。
                }
            }
        }
        out.close();
    }

    public ArrayList<String> getFolderList2Arrary() throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        // out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list_name, true)));    //以追加的方式写入到指定的文件
        ver.add(dir_name);
        while (ver.size() > 0) {
            File[] files = new File(ver.get(0).toString()).listFiles();    //获取该文件夹下所有的文件(夹)名
            ver.remove(0);

            int len = files.length;
            for (int i = 0; i < len; i++) {
                String tmp = files[i].getAbsolutePath();
                if (files[i].isDirectory())    //如果是目录，则加入队列。以便进行后续处理
                {
                    result.add(files[i].getName());
                } else {
                    //  result.add(files[i].getName());
                    // out.write(tmp + "\r\n");        //如果是文件，则直接输出文件名到指定的文件。
                }
            }
        }
        // out.close();
        return result;
    }

    public ArrayList<String> getList2ArraryModify() throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        // out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list_name, true)));    //以追加的方式写入到指定的文件
        ver.add(dir_name);
        while (ver.size() > 0) {
            File[] files = new File(ver.get(0).toString()).listFiles();    //获取该文件夹下所有的文件(夹)名
            ver.remove(0);

            int len = files.length;
            for (int i = 0; i < len; i++) {
                String tmp = files[i].getAbsolutePath();
                if (files[i].isDirectory())    //如果是目录，则加入队列。以便进行后续处理
                { // ver.add(tmp);
                } else {
                    result.add(files[i].getName().replaceFirst("Action_", "").replaceFirst(".xml", ""));
                    // out.write(tmp + "\r\n");        //如果是文件，则直接输出文件名到指定的文件。
                }
            }
        }
        // out.close();
        return result;
    }
}
