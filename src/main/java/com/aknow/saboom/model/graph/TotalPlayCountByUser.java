package com.aknow.saboom.model.graph;

import java.io.Serializable;


@SuppressWarnings("serial")
public class TotalPlayCountByUser implements Serializable {
    String yyyymmdd;
    Integer playCount;

    public TotalPlayCountByUser(String yyyymmdd, Integer playCount){
        this.yyyymmdd = yyyymmdd;
        this.playCount = playCount;
    }

    public String getYyyymmdd() {
        return this.yyyymmdd;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public Integer getPlayCount() {
        return this.playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }
}
