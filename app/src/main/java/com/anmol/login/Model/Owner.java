package com.anmol.login.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-09.
 */

public class Owner implements Serializable{
    String mname,mownname,mcont,est,mwrkhrs,madd,mlandphone,token;
    int minimum_order,delivery_radius,discount;
    float average_time;

  public Owner(String mname, String mownname, String mcont,String mlandphone, int minimum_order, String est, int delivery_radius, String mwrkhrs, int discount, String madd, float average_time,String token) {
        this.mname = mname;
        this.mownname = mownname;
        this.mcont = mcont;
        this.mlandphone = mlandphone;
        this.minimum_order = minimum_order;
        this.est = est;
        this.delivery_radius = delivery_radius;
        this.mwrkhrs = mwrkhrs;
        this.discount = discount;
        this.madd = madd;
        this.average_time = average_time;
        this.token = token;
    }
    public Owner() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public float getAverage_time() {
        return average_time;
    }

    public void setAverage_time(float average_time) {
        this.average_time = average_time;
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }

    public int getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(int minimum_order) {
        this.minimum_order = minimum_order;
    }

    public String getMlandphone() {
        return mlandphone;
    }

    public void setMlandphone(String mlandphone) {
        this.mlandphone = mlandphone;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMownname() {
        return mownname;
    }

    public void setMownname(String mownname) {
        this.mownname = mownname;
    }

    public String getMcont() {
        return mcont;
    }

    public void setMcont(String mcont) {
        this.mcont = mcont;
    }

    public int getDelivery_radius() {
        return delivery_radius;
    }

    public void setDelivery_radius(int delivery_radius) {
        this.delivery_radius = delivery_radius;
    }

    public String getMwrkhrs() {
        return mwrkhrs;
    }

    public void setMwrkhrs(String mwrkhrs) {
        this.mwrkhrs = mwrkhrs;
    }



    public String getMadd() {
        return madd;
    }

    public void setMadd(String madd) {
        this.madd = madd;
    }
}
