package com.csv.util;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zhuhaoran26 on 17/1/26.
 */
public class ttt {
    public static void main(String[] args) throws Exception {
    try {
        StringBuilder org = new StringBuilder("<1>23</1><b>ishagi</b><g>dsufihbr</g><f>sfadf</f>");
        String[] aBList = org.toString().split("><");
        ArrayList<StringBuilder> sbList = new ArrayList<StringBuilder>();
        for (int ind = 0; ind < aBList.length; ind++) {
            sbList.add(new StringBuilder(aBList[ind]));
        }
        Iterator iter = sbList.iterator();
        while(iter.hasNext()){
           // System.out.println(iter.toString());
            StringBuilder itValue = (StringBuilder) iter.next();
            if(itValue.toString().startsWith("g")){
                String temp = itValue.toString();
                System.out.println("1 : " +temp.indexOf(">"));
                System.out.println("2 : " +temp.lastIndexOf("<"));
                        System.out.println(temp.substring(temp.indexOf(">")+1,temp.lastIndexOf("<")));
               // sbList.remove(itValue);
                itValue.delete(0,itValue.length()).append(new StringBuilder(temp.substring(temp.indexOf(">")+1,temp.lastIndexOf("<"))));

            }
            else
            {
                System.out.println("get");
            }
         //   iter.next();
            System.out.println("loop");
        }
        System.out.println(sbList.size());
        for(StringBuilder x : sbList)
        {
            System.out.println(x);
        }

        System.out.println("end");
    }catch (Exception e)
    {
        e.printStackTrace();
    }
}
}
