package com.aknow.saboom.model.graph;

import java.io.Serializable;


@SuppressWarnings("serial")
public class UserRankingGraphData implements Serializable {

    String loginID;
    String loginIDwithRank;
    Integer playcount;


    public String getLoginID() {
        return this.loginID;
    }
    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }
    public Integer getPlaycount() {
        return this.playcount;
    }
    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
    }
    public String getLoginIDwithRank() {
        return this.loginIDwithRank;
    }
    public void setLoginIDwithRank(String loginIDwithRank) {
        this.loginIDwithRank = loginIDwithRank;
    }
}
