package com.aknow.saboom.util;

@SuppressWarnings("serial")
public class HikakuInfo implements java.io.Serializable {
    private Integer preRank = new Integer(0);
    private Integer preCnt = new Integer(0);

	public void setPreCnt(Integer preCnt) {
		this.preCnt = preCnt;
	}
	public void setPreRank(Integer preRank) {
		this.preRank = preRank;
	}
	public Integer getPreCnt() {
		return this.preCnt;
	}
	public Integer getPreRank() {
		return this.preRank;
	}
}