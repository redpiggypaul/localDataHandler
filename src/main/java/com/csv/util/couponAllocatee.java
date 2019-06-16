package com.csv.util;

import com.csv.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class couponAllocatee {
    //private HashMap<Integer, ArrayList<couponRecordByValueFromData>> localRecordGroupMap = new HashMap<Integer, ArrayList<couponRecordByValueFromData>>();

    // private ArrayList<customerItem4Coupon> localCouponList4CustomerGroup = new ArrayList<customerItem4Coupon>();

    public static void main(String[] args) {
        try {
            couponRecordByValueFromData test1 = new couponRecordByValueFromData();
            test1.setCouponID(new StringBuilder("123"));
            test1.setCouponValue(50);

            couponRecordByValueFromData test2 = new couponRecordByValueFromData();
            test2.setCouponID(new StringBuilder("124"));
            test2.setCouponValue(50);

            couponRecordByValueFromData test3 = new couponRecordByValueFromData();
            test3.setCouponID(new StringBuilder("125"));
            test3.setCouponValue(50);

            couponRecordByValueFromData test4 = new couponRecordByValueFromData();
            test4.setCouponID(new StringBuilder("126"));
            test4.setCouponValue(20);

            couponRecordByValueFromData test5 = new couponRecordByValueFromData();
            test5.setCouponID(new StringBuilder("127"));
            test5.setCouponValue(20);

            ArrayList<couponRecordByValueFromData> testList = new ArrayList<couponRecordByValueFromData>();
            testList.add(test1);
            testList.add(test2);
            testList.add(test3);
            //      testList.add(test4);
            //    testList.add(test5);

            HashMap<Integer, ArrayList<couponRecordByValueFromData>> sampleGroup = generateCouponRecord(testList);
            //localRecordGroupMap = sampleGroup;
            System.out.println("Size for HashMap is " + sampleGroup.size());

            System.out.println("Ammount value " + caculateAmount4CouponRecordMap(sampleGroup));

            ArrayList<customerItem4Coupon> tempCuList = new ArrayList<customerItem4Coupon>();
            customerItem4Coupon tempC_1 = new customerItem4Coupon();
            tempC_1.setCustomerIDstr(new StringBuilder("a01"));
            tempC_1.setAmount4matche(50);
            tempCuList.add(tempC_1);

            customerItem4Coupon tempC_2 = new customerItem4Coupon();
            tempC_2.setCustomerIDstr(new StringBuilder("a02"));
            tempC_2.setAmount4matche(50);
            tempCuList.add(tempC_2);

            customerItem4Coupon tempC_3 = new customerItem4Coupon();
            tempC_3.setCustomerIDstr(new StringBuilder("a03"));
            tempC_3.setAmount4matche(50);
            tempCuList.add(tempC_3);

            System.out.println("Customer Coupon SUM value : " + caculateAmount4Cutomer(tempCuList));

            if (findMIN4CouponRecordMap(sampleGroup) > findMinAmount4Cutomer(tempCuList)) {
                throw new Exception("MIN Coupon Record is larger than MIN Customer Amount need");
            }
            if (findMax4CouponRecordMap(sampleGroup) > findMAXAmount4Cutomer(tempCuList)) {
                throw new Exception("MAX Coupon Record is larger than MAX Customer Amount need");
            }

            ArrayList<couponItem> couponItemsList4assign = new ArrayList<couponItem>();
            for (couponRecordByValueFromData couItem4Data : testList) {
                couponItemsList4assign.add(new couponItem(couItem4Data));
            }


            try {
                couponMatch(sampleGroup, tempCuList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            couponItemGroup couponGroup4Match = new couponItemGroup(couponItemsList4assign);

            customerGroup4Coupon customerGroup4Match = new customerGroup4Coupon(tempCuList);

            customerGroup4Match = allocatee(customerGroup4Match, couponGroup4Match);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static customerGroup4Coupon allocatee(customerGroup4Coupon cusGroup, couponItemGroup couGroup) throws Exception {
        customerGroup4Coupon result = cusGroup;
        while ((cusGroup.remainCustomerItemNumber() != 0) && (couGroup.remainCouponItemNumber() != 0)) {
            if (cusGroup.getMaxCouponAmount() == couGroup.getMaxCouponValue()) {
                System.out.println("MAX Coupon Amount is " + cusGroup.getMaxCouponAmount());
                System.out.println("MAX Coupon Value is " + couGroup.getMaxCouponValue());
                int currentMAXamount = result.getMaxCouponAmount();
                for (customerItem4Coupon cusItem : result.getMap4Amount2Customer().get(currentMAXamount)) {
                    couponItem tempCouItem = couGroup.getCouponWithMatchAmount(cusItem.getAmount4match());
                    cusItem.addCouponItem(tempCouItem);
                    cusGroup.renewCustomerList4Match(cusItem);
                }

            } else {
                System.out.println("MAX failed to match ");
            }

            if (cusGroup.getMinCouponAmount() == couGroup.getMinCouponValue()) {
                System.out.println("MIN Coupon Amount is " + cusGroup.getMinCouponAmount());
                System.out.println("MIN Coupon Value is " + couGroup.getMinCouponValue());
                int currentMINamount = result.getMinCouponAmount();
                for (customerItem4Coupon cusItem : result.getMap4Amount2Customer().get(currentMINamount)) {
                    couponItem tempCouItem = couGroup.getCouponWithMatchAmount(cusItem.getAmount4match());
                    cusItem.addCouponItem(tempCouItem);
                    cusGroup.renewCustomerList4Match(cusItem);
                }
            } else {
                System.out.println("MIN failed to match ");
            }


        }

        return result;
    }

    public static void couponMatch(HashMap<Integer, ArrayList<couponRecordByValueFromData>> extGroup, ArrayList<customerItem4Coupon> extCuList) {
        try {
            if (caculateAmount4CouponRecordMap(extGroup) == caculateAmount4Cutomer(extCuList)) {

            } else {
                throw new Exception("Coupon Record Amount mismatch Customer Coupon need");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static int findMax4CouponRecordMap(HashMap<Integer, ArrayList<couponRecordByValueFromData>> extRecordGroup) throws Exception {
        int result = 0;
        try {
            for (Iterator it = extRecordGroup.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry tempEntry = (Map.Entry) it.next();
                if ((Integer.valueOf(String.valueOf(tempEntry.getKey())) > result) || (result == 0)) {
                    result = Integer.valueOf(String.valueOf(tempEntry.getKey()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MAX Coupon Record is " + result);
            return result;
        }
    }

    public static int findMIN4CouponRecordMap(HashMap<Integer, ArrayList<couponRecordByValueFromData>> extRecordGroup) throws Exception {
        int result = 0;
        try {
            for (Iterator it = extRecordGroup.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry tempEntry = (Map.Entry) it.next();
                if ((Integer.valueOf(String.valueOf(tempEntry.getKey())) < result) || (result == 0)) {
                    result = Integer.valueOf(String.valueOf(tempEntry.getKey()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MIN Coupon Record is " + result);
            return result;
        }
    }

    public static int caculateAmount4CouponRecordMap(HashMap<Integer, ArrayList<couponRecordByValueFromData>> extRecordGroup) throws Exception {
        int result = 0;
        try {
            for (Iterator it = extRecordGroup.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry tempEntry = (Map.Entry) it.next();
                result = result + (Integer.parseInt(tempEntry.getKey().toString()) * (((ArrayList<couponRecordByValueFromData>) tempEntry.getValue()).size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public static HashMap<Integer, ArrayList<couponRecordByValueFromData>> generateCouponRecord(ArrayList<couponRecordByValueFromData> extRecordList) {
        HashMap<Integer, ArrayList<couponRecordByValueFromData>> result = new HashMap<Integer, ArrayList<couponRecordByValueFromData>>();
        for (couponRecordByValueFromData rowCouponRecord : extRecordList) {
            if (result.containsKey(rowCouponRecord.getCouponValue())) {
                result.get(rowCouponRecord.getCouponValue()).add(rowCouponRecord);
            } else {
                ArrayList<couponRecordByValueFromData> tempList = new ArrayList<couponRecordByValueFromData>();
                tempList.clear();
                tempList.add(rowCouponRecord);
                result.put(rowCouponRecord.getCouponValue(), tempList);
            }
        }
        return result;

    }

    public static int findMAXAmount4Cutomer(ArrayList<customerItem4Coupon> extCouponList4CustomerGroup) throws Exception {
        int result = 0;
        try {
            for (customerItem4Coupon item : extCouponList4CustomerGroup) {
                if ((item.getAmount4match() > result) || (result == 0)) {
                    result = item.getAmount4match();
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MAX Customer Amount is " + result);
            return result;
        }
    }

    public static int findMinAmount4Cutomer(ArrayList<customerItem4Coupon> extCouponList4CustomerGroup) throws Exception {
        int result = 0;
        try {
            for (customerItem4Coupon item : extCouponList4CustomerGroup) {
                if ((item.getAmount4match() < result) || (result == 0)) {
                    result = item.getAmount4match();
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MIN Customer Amount is " + result);
            return result;
        }
    }

    public static int caculateAmount4Cutomer(ArrayList<customerItem4Coupon> extCouponList4CustomerGroup) throws Exception {
        int result = 0;
        try {
            for (customerItem4Coupon item : extCouponList4CustomerGroup) {
                result = result + item.getAmount4match();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

}
