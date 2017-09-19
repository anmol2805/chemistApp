package com.anmol.login.Model;

/**
 * Created by anmol on 9/18/2017.
 */

public class Oldorder {
    String date,name,address,phone1,phone2,oid;

    public Oldorder() {
    }

    public Oldorder(String date, String name, String address, String phone1, String phone2,String oid) {
        this.date = date;
        this.name = name;
        this.address = address;
        this.phone1 = phone1;
        this.phone2 = phone2;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
}
