package com.sharmas.golf_android.models;

/**
 * Created by Admin on 2017-03-24.
 */

public class GolfStatesModel {

    String stateId;
    String stateName;
    String golfId;
    String expdate;
    String OfferId;

    public String getOfferId() {
        return OfferId;
    }

    public void setOfferId(String offerId) {
        OfferId = offerId;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String desc;

    public String getStateId() {
        return stateId;
    }

    public String getGolfId() {
        return golfId;
    }

    public void setGolfId(String golfId) {
        this.golfId = golfId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
