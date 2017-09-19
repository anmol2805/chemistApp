package com.anmol.login.Model;

/**
 * Created by anmol on 9/18/2017.
 */

public class Neworder {
    String date,address,oid;

    public Neworder() {
    }

    public Neworder(String date, String address,String oid) {
        this.date = date;
        this.address = address;
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
