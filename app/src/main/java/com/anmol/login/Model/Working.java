package com.anmol.login.Model;

/**
 * Created by anmol on 2017-08-10.
 */

public class Working {
    Boolean mon,tue,wed,thurs,fri,sat,sun;

    public Working(Boolean mon, Boolean tue, Boolean wed, Boolean thurs, Boolean fri, Boolean sat, Boolean sun) {
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thurs = thurs;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }
    public Working(){

    }

    public Boolean getMon() {
        return mon;
    }

    public void setMon(Boolean mon) {
        this.mon = mon;
    }

    public Boolean getTue() {
        return tue;
    }

    public void setTue(Boolean tue) {
        this.tue = tue;
    }

    public Boolean getWed() {
        return wed;
    }

    public void setWed(Boolean wed) {
        this.wed = wed;
    }

    public Boolean getThurs() {
        return thurs;
    }

    public void setThurs(Boolean thurs) {
        this.thurs = thurs;
    }

    public Boolean getFri() {
        return fri;
    }

    public void setFri(Boolean fri) {
        this.fri = fri;
    }

    public Boolean getSat() {
        return sat;
    }

    public void setSat(Boolean sat) {
        this.sat = sat;
    }

    public Boolean getSun() {
        return sun;
    }

    public void setSun(Boolean sun) {
        this.sun = sun;
    }
}
