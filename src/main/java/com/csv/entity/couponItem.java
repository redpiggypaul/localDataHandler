package com.csv.entity;

public class couponItem {
    private int couponValue;
    private StringBuilder couponID;
    private StringBuilder couponStatus;
    private StringBuilder customerIDstr;

    public couponItem(){

    }

    public couponItem(couponRecordByValueFromData extCoupon){
        this.couponValue = extCoupon.getCouponValue();
        this.couponID = extCoupon.getCouponID();
        this.couponStatus = new StringBuilder("unassigned");
    }

    public couponItem(int extValue, StringBuilder cID, StringBuilder cStatus){
        this.couponValue = extValue;
        this.couponID = cID;
        this.couponStatus = cStatus;
    }

    public void setCouponValue(int couponValue) {
        this.couponValue = couponValue;
    }

    public int getCouponValue() {
        return couponValue;
    }

    public void setCouponID(StringBuilder couponID) {
        this.couponID = couponID;
    }

    public StringBuilder getCouponID() {
        return couponID;
    }

    public void setCouponStatus(StringBuilder couponStatus) {
        this.couponStatus = couponStatus;
    }

    public StringBuilder getCouponStatus()
    {
        return couponStatus;
    }

    public void setCustomerIDstr(StringBuilder customerIDstr) {
        this.customerIDstr = customerIDstr;
    }

    public StringBuilder getCustomerIDstr() {
        return customerIDstr;
    }

}
