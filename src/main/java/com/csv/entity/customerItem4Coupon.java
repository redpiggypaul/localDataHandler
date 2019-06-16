package com.csv.entity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class customerItem4Coupon {
    private StringBuilder customerIDstr;
    private int amount4match;
    private ArrayList<couponItem> couponList;
    private Boolean flag4matchDone;

    public customerItem4Coupon(){
        this.customerIDstr= new StringBuilder();
        this.amount4match=-1;
        this.couponList = new ArrayList<>();
        this.couponList.clear();
        this.flag4matchDone=false;
    }

    public int getAmount4match() {
        return amount4match;
    }

    public int getCouponValue() throws Exception{
        int result = 0;
        for(couponItem item: couponList){
            result = result + item.getCouponValue();
        }
        if(result==0){
            throw new Exception(String.valueOf(new StringBuilder("SUM for "+ customerIDstr +" is empty")));
        }
        return result;
    }

    public void setCustomerIDstr(StringBuilder customerIDstr){
        this.customerIDstr = customerIDstr;
    }

    public void setAmount4matche(int extValue){
        this.amount4match = extValue;
    }

    public StringBuilder getCustomerIDstr() {
        return customerIDstr;
    }

    public Boolean getFlag4matchDone() {
        return flag4matchDone;
    }

    public void setFlag4matchDone(Boolean flag4matchDone) {
        this.flag4matchDone = flag4matchDone;
    }

    public void setCouponList(ArrayList<couponItem> couponList) {
        this.couponList = couponList;
    }

    public void addCouponItem(couponItem couponItem) throws Exception {
        if(this.amount4match!=0) {
            if(this.amount4match>=couponItem.getCouponValue()) {
                this.amount4match = this.amount4match - couponItem.getCouponValue();
                this.couponList.add(couponItem);
            }
            else{
                throw new Exception("The Coupon Value is " + couponItem.getCouponValue()+ ", which is Lager than this Customer's Amount " + this.amount4match);
            }
        }
        else{
            throw new Exception("This Customer's Amount is 0 ZERO");
        }
    }

    public ArrayList<couponItem> getCouponList() {
        return couponList;
    }
}
