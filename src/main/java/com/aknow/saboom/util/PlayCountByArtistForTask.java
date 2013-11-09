package com.aknow.saboom.util;

@SuppressWarnings("serial")
public class PlayCountByArtistForTask implements java.io.Serializable{

    private String loginIDArtistName;

    private String artistName;

    private String loginID;


    private Integer playCount;

    private String yyMMddHHmmss;

    public PlayCountByArtistForTask(String loginIDArtistName, String artistName, String loginID, Integer playCount, String yyMMddHHmmss){
    	this.loginIDArtistName = loginIDArtistName;
    	this.artistName = artistName;
    	this.loginID = loginID;
    	this.playCount = playCount;
    	this.yyMMddHHmmss = yyMMddHHmmss;
    }

	public String getloginIDArtistName() {
		return this.loginIDArtistName;
	}

	public void setloginIDArtistName(String loginIDArtistName) {
		this.loginIDArtistName = loginIDArtistName;
	}

	public String getArtistName() {
		return this.artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getLoginID() {
		return this.loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public Integer getPlayCount() {
		return this.playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public String getYyMMddHHmmss() {
		return this.yyMMddHHmmss;
	}

	public void setYyMMddHHmmss(String yyMMddHHmmss) {
		this.yyMMddHHmmss = yyMMddHHmmss;
	}

}