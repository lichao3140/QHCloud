package com.quhwa.cloudintercom.bean;

import java.io.Serializable;

/**
 * Created by lxz on 2017/8/31 0031.
 */

public class SmartProduce implements Serializable{
    private String spName;
    private int spRes;

    public SmartProduce(String spName, int spRes) {
        this.spName = spName;
        this.spRes = spRes;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public int getSpRes() {
        return spRes;
    }

    public void setSpRes(int spRes) {
        this.spRes = spRes;
    }

    @Override
    public String toString() {
        return "SmartProduce{" +
                "spName='" + spName + '\'' +
                ", spRes=" + spRes +
                '}';
    }
}
