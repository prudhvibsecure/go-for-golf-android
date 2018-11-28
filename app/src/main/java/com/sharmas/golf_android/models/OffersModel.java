package com.sharmas.golf_android.models;

/**
 * Created by Admin on 2017-11-07.
 */

public class OffersModel {

    String specialid,
            golfid, specialname,
            sdescription;

    public String getSpecialid() {
        return specialid;
    }

    public void setSpecialid(String specialid) {
        this.specialid = specialid;
    }

    public String getGolfid() {
        return golfid;
    }

    public void setGolfid(String golfid) {
        this.golfid = golfid;
    }

    public String getSpecialname() {
        return specialname;
    }

    public void setSpecialname(String specialname) {
        this.specialname = specialname;
    }

    public String getSdescription() {
        return sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    @Override
    public String toString() {
        return "OffersModel{" +
                "specialname='" + specialname + '\'' +
                '}';
    }
}
