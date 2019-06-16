package com.csv.effCodePrastic.reference;

import java.lang.ref.SoftReference;

public class SoftReferenceWhenIdle {

    public static void main(String[] args){
        House seller = new House();

        SoftReference<House> buyer2 = new SoftReference<House>(seller);
        seller = null;

        while(true){

            System.gc();
            System.runFinalization();

            if (buyer2.get() == null){
                System.out.println("house is null.");
                break;
            }
            else{
                System.out.println("still there.");
            }
        }
    }

    static class House  {
        private static final Integer DOOR_NUMBER = 2000;
        public SoftReferenceHouse.House.Door[] doors = new SoftReferenceHouse.House.Door[DOOR_NUMBER];


        class Door{}
    }
}
