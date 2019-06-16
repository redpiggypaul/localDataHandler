package com.csv.util;

public class MQTTencode {

    public static void main(String[] args) {
        int X = 128;

        while (X > 0) {
            int digit = X % 128;
            System.out.println("after % 128, digit is : " + digit);
            X = X/128;
            System.out.println("after / 128, X is : " + X);
            // if there are more digits to encode, set the top bit of this digit
            if (X > 0)
                digit = (digit|0x80);
                System.out.println("after | 0x80, digit is : " + digit);

            System.out.println(digit);
        }
    }
}
