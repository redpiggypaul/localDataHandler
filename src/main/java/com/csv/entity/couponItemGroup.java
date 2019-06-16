package com.csv.entity;

import java.util.*;

public class couponItemGroup {
    private int maxCouponValue;

    private int minCouponValue;

    private ArrayList<couponItem> couponList4All;

    private ArrayList<couponItem> couponList4Match;

    private ArrayList<Integer> valueListMax2Min;

    private HashMap<Integer, ArrayList<couponItem>> map4Amount2Coupon;

    private HashMap<Integer, Integer> map4Amount2CouponNumber;

    public couponItemGroup(ArrayList<couponItem> couponList) throws Exception {

        this.couponList4All = new ArrayList<couponItem>(couponList);
        this.couponList4Match = new ArrayList<couponItem> (couponList);
        this.maxCouponValue = findMAXAmount();
        this.minCouponValue = findMINAmount();
        this.valueListMax2Min = setAmountListMax2Min();
        this.map4Amount2Coupon = orgnizeCustomerByAmount();
        this.map4Amount2CouponNumber = countCouponNumByAmount();

    }

    public int getMaxCouponValue() {
        return maxCouponValue;
    }

    public int getMinCouponValue() {
        return minCouponValue;
    }

    private void renewGroup() throws Exception{
        this.maxCouponValue = findMAXAmount();
        this.minCouponValue = findMINAmount();
        this.valueListMax2Min = setAmountListMax2Min();
        this.map4Amount2Coupon = orgnizeCustomerByAmount();
        this.map4Amount2CouponNumber = countCouponNumByAmount();
    }

    public static void main(String[] args) {
        try {

            couponItem test1 = new couponItem(10, new StringBuilder("123"), new StringBuilder("new"));

            couponItem test2 = new couponItem(20, new StringBuilder("124"), new StringBuilder("new"));

            couponItem test3 = new couponItem(10, new StringBuilder("125"), new StringBuilder("new"));

            couponItem test4 = new couponItem(10, new StringBuilder("126"), new StringBuilder("new"));

            couponItem test5 = new couponItem(30, new StringBuilder("126"), new StringBuilder("new"));
            couponItem test6 = new couponItem(40, new StringBuilder("127"), new StringBuilder("new"));
            couponItem test7 = new couponItem(50, new StringBuilder("128"), new StringBuilder("new"));
            couponItem test8 = new couponItem(60, new StringBuilder("129"), new StringBuilder("new"));
            couponItem test9 = new couponItem(100, new StringBuilder("130"), new StringBuilder("new"));

            ArrayList<couponItem> testCouponList = new ArrayList<couponItem>();
            testCouponList.add(test1);
            testCouponList.add(test2);
            testCouponList.add(test3);
            testCouponList.add(test4);
            testCouponList.add(test5);
            testCouponList.add(test6);
            testCouponList.add(test7);
            testCouponList.add(test8);
            testCouponList.add(test9);

            couponItemGroup testGroup = new couponItemGroup(testCouponList);
            System.out.println("");
            testGroup.getCouponWithMatchAmount(100);

            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public int findMAXAmount() throws Exception {
        int result = 0;
        try {
            for (couponItem item : this.couponList4Match) {
                if ((item.getCouponValue() > result) || (result == 0)) {
                    result = item.getCouponValue();
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MAX Coupon Amount is " + result);
            return result;
        }
    }

    public int findMINAmount() throws Exception {
        int result = 0;
        try {
            for (couponItem item : this.couponList4Match) {
                if ((item.getCouponValue() < result) || (result == 0)) {
                    result = item.getCouponValue();
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MIN Coupon Amount is " + result);
            return result;
        }
    }

    public ArrayList<Integer> setAmountListMax2Min() throws Exception {
        Set<Integer> set = new HashSet<Integer>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();

        for (couponItem curCum : this.couponList4Match) {
            if (curCum.getCouponValue() >= 0) {
                set.add(curCum.getCouponValue());
            } else {
                throw new Exception("Coupon Amount MUST NOT negative");
            }
        }
        resultList.addAll(set);
        System.out.println("Remove duplicated item by same Amount： " + resultList);
        Collections.sort(resultList);
        System.out.println("After Sort： " + resultList);
        Collections.reverse(resultList);
        System.out.println("After Reverse： " + resultList);

        return resultList;

        //   this.amountListMax2Min.clear();
        //   this.amountListMax2Min.addAll(newList);
    }

    public HashMap<Integer, Integer> countCouponNumByAmount() {
        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Iterator it =  this.map4Amount2Coupon .entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry tempEntry = (Map.Entry) it.next();
            if (!result.containsKey(tempEntry.getKey())) {
                ArrayList<couponItem> tempList = (ArrayList<couponItem>) tempEntry.getValue();
                int size4cus = tempList.size();
                result.put(Integer.valueOf(tempEntry.getKey().toString()), size4cus);
            }
        }
        return result;
    }

    public HashMap<Integer, ArrayList<couponItem>> orgnizeCustomerByAmount() {
        HashMap<Integer, ArrayList<couponItem>> result = new HashMap<Integer, ArrayList<couponItem>>();
        for (int amountItem : this.valueListMax2Min) {
            for (couponItem couItem : this.couponList4Match) {
                if (couItem.getCouponValue() == amountItem) {
                    if (result.containsKey(amountItem)) {
                        result.get(amountItem).add(couItem);
                    } else {
                        ArrayList<couponItem> tempList = new ArrayList<couponItem>();
                        tempList.clear();
                        tempList.add(couItem);
                        result.put(amountItem, tempList);
                    }
                } else {
                    continue;
                }
            }
        }
        return result;
    }

    public couponItem getCouponWithMatchAmount(int extAmount) throws Exception {
        couponItem result = new couponItem();

        if (this.map4Amount2Coupon.containsKey(extAmount)) {
            if (this.map4Amount2Coupon.get(extAmount).size() != 0) {
                result = this.map4Amount2Coupon.get(extAmount).get(0);
                result.setCouponStatus(new StringBuilder("assigned"));
                this.couponList4Match.remove(result);
                renewGroup();
            } else {
                throw new Exception("map4Amount2Coupon for speical amount : " + extAmount + " is empty");
            }
        } else {
            throw new Exception("map4Amount2Coupon Don't including speical amount : " + extAmount);
        }


        return result;
    }

    public int remainCouponItemNumber() {
        int result = 0;

        Iterator iter = this.map4Amount2CouponNumber.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            result = result + Integer.valueOf(entry.getValue().toString());
        }

        return result;
    }
}
