package com.csv.effCodePrastic.reference;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class SoftReferenceHouse {
    public static void main(String[] args){
         List<House> houses = new ArrayList<House>();   //  Normal Strong reference
        //List<SoftReference> houses = new ArrayList<SoftReference>();  // Soft reference

        int i=0;
        while(true){
            houses.add(new House());   //  Normal Strong reference


            //change
           // SoftReference<House> buyer2 = new SoftReference<House>(new House());  // Soft reference

            //change
           // houses.add(buyer2);   // Soft reference

            System.out.println("i=" + (++i));

        }

    }

    static class House  {
        private static final Integer DOOR_NUMBER = 2000;
        public Door[] doors = new Door[DOOR_NUMBER];


        class Door{}
    }

}
