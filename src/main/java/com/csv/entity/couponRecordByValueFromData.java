package com.csv.entity;

import java.util.ArrayList;

public class couponRecordByValueFromData {
    private int couponValue;
    private StringBuilder couponID;



    public int getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(int extValue){
        this.couponValue = extValue;
    }

    public StringBuilder getCouponID(){
        return couponID;
    }

    public void setCouponID(StringBuilder extID){
        this.couponID = extID;
    }

}
