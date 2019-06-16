package com.csv.util;


public class test4sub_2_1 {
    public static void main(String args[]) {

        try {
            StringBuilder src = new StringBuilder("test4sub_1_2");
            StringBuilder fileName = new StringBuilder(src.substring(src.toString().split("_")[0].length()+1, src.length()) + ".json");
            System.out.println(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}

