package com.csv.entity;

import java.util.*;

public class customerGroup4Coupon {
    private int maxCouponAmount;

    private int minCouponAmount;

    private ArrayList<customerItem4Coupon> customerList4All;

    private ArrayList<customerItem4Coupon> customerList4Match;

    private ArrayList<Integer> amountListMax2Min;

    private HashMap<Integer, ArrayList<customerItem4Coupon>> map4Amount2Customer;

    private HashMap<Integer, Integer> map4Amount2CustomerNumber;

    public customerGroup4Coupon(ArrayList<customerItem4Coupon> extcustomerList4Match) {
        try {
            this.customerList4All = new ArrayList<customerItem4Coupon>(extcustomerList4Match);
            this.customerList4Match = new ArrayList<customerItem4Coupon>(extcustomerList4Match);
            this.maxCouponAmount = findMAXAmount4Cutomer();
            this.minCouponAmount = findMINAmount4Cutomer();
            this.amountListMax2Min = setAmountListMax2Min();
            this.map4Amount2Customer = orgnizeCustomerByAmount();
            this.map4Amount2CustomerNumber = countCustomerNumByAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ArrayList<customerItem4Coupon> tempCuList = new ArrayList<customerItem4Coupon>();
            customerItem4Coupon tempC_1 = new customerItem4Coupon();
            tempC_1.setCustomerIDstr(new StringBuilder("a01"));
            tempC_1.setAmount4matche(30);
            tempCuList.add(tempC_1);

            customerItem4Coupon tempC_2 = new customerItem4Coupon();
            tempC_2.setCustomerIDstr(new StringBuilder("a02"));
            tempC_2.setAmount4matche(40);
            tempCuList.add(tempC_2);

            customerItem4Coupon tempC_3 = new customerItem4Coupon();
            tempC_3.setCustomerIDstr(new StringBuilder("a03"));
            tempC_3.setAmount4matche(10);
            tempCuList.add(tempC_3);

            customerItem4Coupon tempC_4 = new customerItem4Coupon();
            tempC_4.setCustomerIDstr(new StringBuilder("a04"));
            tempC_4.setAmount4matche(10);
            tempCuList.add(tempC_4);

            //    setAmountListMax2Min(tempCuList);

            //      HashMap<Integer, ArrayList<customerItem4Coupon>> map4Amount2Customer444 = orgnizeCustomerByAmount(setAmountListMax2Min(tempCuList), tempCuList);

            //  HashMap<Integer, Integer> map4Amount2CustomerNumber445 = countCustomerNumByAmount(map4Amount2Customer444);

            customerGroup4Coupon localGroup = new customerGroup4Coupon(tempCuList);


            couponItem test1 = new couponItem(10, new StringBuilder("123"), new StringBuilder("new"));

            couponItem test2 = new couponItem(10, new StringBuilder("124"), new StringBuilder("new"));

            couponItem test3 = new couponItem(10, new StringBuilder("125"), new StringBuilder("new"));

            couponItem test4 = new couponItem(10, new StringBuilder("126"), new StringBuilder("new"));

            //      couponItem test5 = new couponItem(10, new StringBuilder("127"), new StringBuilder("new"));

            ArrayList<couponItem> testCouponList = new ArrayList<couponItem>();
            testCouponList.add(test1);
            testCouponList.add(test2);
            testCouponList.add(test3);
            testCouponList.add(test4);
            //    testCouponList.add(test5);

            localGroup.matchCustomerWithCouponList(tempC_2, testCouponList);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public int findMAXAmount4Cutomer() throws Exception {
        int result = 0;
        try {
            for (customerItem4Coupon item : this.customerList4Match) {
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

    public int findMINAmount4Cutomer() throws Exception {
        int result = 0;
        try {
            for (customerItem4Coupon item : this.customerList4Match) {
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

    public HashMap<Integer, Integer> countCustomerNumByAmount() {
        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Iterator it = this.map4Amount2Customer.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry tempEntry = (Map.Entry) it.next();
            if (!result.containsKey(tempEntry.getKey())) {
                ArrayList<customerItem4Coupon> tempList = (ArrayList<customerItem4Coupon>) tempEntry.getValue();
                int size4cus = tempList.size();
                result.put(Integer.valueOf(tempEntry.getKey().toString()), size4cus);
            }
        }
        return result;
    }

    public HashMap<Integer, ArrayList<customerItem4Coupon>> orgnizeCustomerByAmount() {
        HashMap<Integer, ArrayList<customerItem4Coupon>> result = new HashMap<Integer, ArrayList<customerItem4Coupon>>();
        for (int amountItem : this.amountListMax2Min) {
            for (customerItem4Coupon cumItem : this.customerList4Match) {
                if (cumItem.getAmount4match() == amountItem) {
                    if (result.containsKey(amountItem)) {
                        result.get(amountItem).add(cumItem);
                    } else {
                        ArrayList<customerItem4Coupon> tempList = new ArrayList<customerItem4Coupon>();
                        tempList.clear();
                        tempList.add(cumItem);
                        result.put(amountItem, tempList);
                    }
                } else {
                    continue;
                }
            }
        }

        return result;
    }

    public ArrayList<Integer> setAmountListMax2Min() throws Exception {
        Set<Integer> set = new HashSet<Integer>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();

        for (customerItem4Coupon curCum : this.customerList4Match) {
            if (curCum.getAmount4match() >= 0) {
                set.add(curCum.getAmount4match());
            } else {
                throw new Exception("Customer Coupon Amount MUST NOT negative");
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

    public Boolean checkAllCustomerMatchDone() {
        Boolean result = true;
        if (this.customerList4Match.size() == 0) {
            for (customerItem4Coupon item : this.customerList4All) {
                if (item.getFlag4matchDone() == false) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    public int getMaxCouponAmount() {
        return maxCouponAmount;
    }

    public int getMinCouponAmount() {
        return minCouponAmount;
    }

    public ArrayList<customerItem4Coupon> getcustomerList4Match() {
        return customerList4Match;
    }

    public ArrayList<Integer> getAmountListMax2Min() {
        return amountListMax2Min;
    }

    public HashMap<Integer, ArrayList<customerItem4Coupon>> getMap4Amount2Customer() {
        return map4Amount2Customer;
    }

    public HashMap<Integer, Integer> getMap4Amount2CustomerNumber() {
        return map4Amount2CustomerNumber;
    }

    public int remainCustomerItemNumber() {
        int result = 0;

        Iterator iter = this.map4Amount2CustomerNumber.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            result = result + Integer.valueOf(entry.getValue().toString());
        }

        return result;
    }

    // public HashMap<Integer, customerItem4Coupon> classCumstorByAmount(int currentMax, ArrayList<customerItem4Coupon> extcustomerList4Match) {

    // }

    public void matchCustomerWithCouponList(customerItem4Coupon cust4Remove, ArrayList<couponItem> extCouList) throws Exception {
        int couponListAmountSum = 0;
        StringBuilder targetCustID = new StringBuilder();
        targetCustID = cust4Remove.getCustomerIDstr();
        for (couponItem item : extCouList) {
            couponListAmountSum = couponListAmountSum + item.getCouponValue();
        }
        if (cust4Remove.getAmount4match() == couponListAmountSum) {
            for (customerItem4Coupon tempCust : this.customerList4Match) {
                if (tempCust.getCustomerIDstr().toString().equals(targetCustID.toString())) {
                    this.customerList4Match.remove(tempCust);
                    break;
                }
            }
            for (customerItem4Coupon tempCust : this.customerList4All) {
                if (tempCust.getCustomerIDstr().toString().equals(targetCustID.toString())) {
                    tempCust.setCouponList(extCouList);
                    tempCust.setFlag4matchDone(true);
                    tempCust.setAmount4matche(0);
                    break;
                }
            }
            renewCustomerList4Match();
        } else {
            throw new Exception("Customer Amount mismatch Coupon List SUM");
        }
        //   System.out.println("");
    }

    public void renewCustomerList4Match() throws Exception {

        this.maxCouponAmount = findMAXAmount4Cutomer();
        this.amountListMax2Min = setAmountListMax2Min();
        this.map4Amount2Customer = orgnizeCustomerByAmount();
        this.map4Amount2CustomerNumber = countCustomerNumByAmount();
    }

    public void renewCustomerList4Match(customerItem4Coupon extItem) throws Exception {

        this.customerList4Match.remove(extItem);

        this.maxCouponAmount = findMAXAmount4Cutomer();
        this.amountListMax2Min = setAmountListMax2Min();
        this.map4Amount2Customer = orgnizeCustomerByAmount();
        this.map4Amount2CustomerNumber = countCustomerNumByAmount();
    }

}
