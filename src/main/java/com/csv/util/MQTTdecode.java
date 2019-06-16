package com.csv.util;

import java.util.ArrayList;

public class MQTTdecode {

    public static void main(String[] args) {
        ArrayList<Integer> digit = new ArrayList<Integer>();
        digit.add(1);
        digit.add(127);

        int multiplier = 1;
        int value = 0;
        for(int i=0;i<= digit.size()-1;i++) {
            while ((digit.get(i) & 128) != 0) {

                value += (digit.get(i+1) & 127) * multiplier;
                multiplier *= 128;
            }
        }
        System.out.println(multiplier);

    }
}
