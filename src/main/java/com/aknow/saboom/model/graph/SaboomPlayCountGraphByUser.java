package com.aknow.saboom.model.graph;

import java.io.Serializable;


public class SaboomPlayCountGraphByUser implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Integer rank_bySong;
    String songName_bySong;
    String artist_bySong;
    String rate_bySong;
    Integer playcount_bySong;

    Integer rank_byArtist;
    String artistName_byArtist;
    String rate_byArtist;
    Integer playcount_byArtist;

    Integer rank_hikakuBySong_now;
    Integer rank_hikakuBySong_pre;
    String songName_hikakuBySong;
    String artistName_hikakuBySong;
    String rate_hikakuBySong;
    Integer playcount_hikakuBySong;
    Integer sabuncount_hikakuBySong;
    Integer motocount_hikakuBySong;
    String rankImage_hikakuBySong;

    Integer rank_hikakuByArtist_now;
    Integer rank_hikakuByArtist_pre;
    String artistName_hikakuByArtist;
    String rate_hikakuByArtist;
    Integer playcount_hikakuByArtist;
    Integer sabuncount_hikakuByArtist;
    Integer motocount_hikakuByArtist;
    String rankImage_hikakuByArtist;




    public Integer getRank_bySong() {
        return this.rank_bySong;
    }
    public void setRank_bySong(Integer rank_bySong) {
        this.rank_bySong = rank_bySong;
    }
    public String getSongName_bySong() {
        return this.songName_bySong;
    }
    public void setSongName_bySong(String songName_bySong) {
        this.songName_bySong = songName_bySong;
    }
    public String getArtist_bySong() {
        return this.artist_bySong;
    }
    public void setArtist_bySong(String artist_bySong) {
        this.artist_bySong = artist_bySong;
    }
    public String getRate_bySong() {
        return this.rate_bySong;
    }
    public void setRate_bySong(String rate_bySong) {
        this.rate_bySong = rate_bySong;
    }
    public Integer getPlaycount_bySong() {
        return this.playcount_bySong;
    }
    public void setPlaycount_bySong(Integer playcount_bySong) {
        this.playcount_bySong = playcount_bySong;
    }
    public Integer getRank_byArtist() {
        return this.rank_byArtist;
    }
    public void setRank_byArtist(Integer rank_byArtist) {
        this.rank_byArtist = rank_byArtist;
    }
    public String getArtistName_byArtist() {
        return this.artistName_byArtist;
    }
    public void setArtistName_byArtist(String artistName_byArtist) {
        this.artistName_byArtist = artistName_byArtist;
    }
    public String getRate_byArtist() {
        return this.rate_byArtist;
    }
    public void setRate_byArtist(String rate_byArtist) {
        this.rate_byArtist = rate_byArtist;
    }
    public Integer getPlaycount_byArtist() {
        return this.playcount_byArtist;
    }
    public void setPlaycount_byArtist(Integer playcount_byArtist) {
        this.playcount_byArtist = playcount_byArtist;
    }
    public Integer getRank_hikakuBySong_now() {
        return this.rank_hikakuBySong_now;
    }
    public void setRank_hikakuBySong_now(Integer rank_hikakuBySong_now) {
        this.rank_hikakuBySong_now = rank_hikakuBySong_now;
    }
    public Integer getRank_hikakuBySong_pre() {
        return this.rank_hikakuBySong_pre;
    }
    public void setRank_hikakuBySong_pre(Integer rank_hikakuBySong_pre) {
        this.rank_hikakuBySong_pre = rank_hikakuBySong_pre;
    }
    public String getSongName_hikakuBySong() {
        return this.songName_hikakuBySong;
    }
    public void setSongName_hikakuBySong(String songName_hikakuBySong) {
        this.songName_hikakuBySong = songName_hikakuBySong;
    }
    public String getArtistName_hikakuBySong() {
        return this.artistName_hikakuBySong;
    }
    public void setArtistName_hikakuBySong(String artistName_hikakuBySong) {
        this.artistName_hikakuBySong = artistName_hikakuBySong;
    }
    public String getRate_hikakuBySong() {
        return this.rate_hikakuBySong;
    }
    public void setRate_hikakuBySong(String rate_hikakuBySong) {
        this.rate_hikakuBySong = rate_hikakuBySong;
    }
    public Integer getPlaycount_hikakuBySong() {
        return this.playcount_hikakuBySong;
    }
    public void setPlaycount_hikakuBySong(Integer playcount_hikakuBySong) {
        this.playcount_hikakuBySong = playcount_hikakuBySong;
    }
    public Integer getSabuncount_hikakuBySong() {
        return this.sabuncount_hikakuBySong;
    }
    public void setSabuncount_hikakuBySong(Integer sabuncount_hikakuBySong) {
        this.sabuncount_hikakuBySong = sabuncount_hikakuBySong;
    }
    public String getRankImage_hikakuBySong() {
        return this.rankImage_hikakuBySong;
    }
    public void setRankImage_hikakuBySong(String rankImage_hikakuBySong) {
        this.rankImage_hikakuBySong = rankImage_hikakuBySong;
    }
    public Integer getRank_hikakuByArtist_now() {
        return this.rank_hikakuByArtist_now;
    }
    public void setRank_hikakuByArtist_now(Integer rank_hikakuByArtist_now) {
        this.rank_hikakuByArtist_now = rank_hikakuByArtist_now;
    }
    public Integer getRank_hikakuByArtist_pre() {
        return this.rank_hikakuByArtist_pre;
    }
    public void setRank_hikakuByArtist_pre(Integer rank_hikakuByArtist_pre) {
        this.rank_hikakuByArtist_pre = rank_hikakuByArtist_pre;
    }
    public String getArtistName_hikakuByArtist() {
        return this.artistName_hikakuByArtist;
    }
    public void setArtistName_hikakuByArtist(String artistName_hikakuByArtist) {
        this.artistName_hikakuByArtist = artistName_hikakuByArtist;
    }
    public String getRate_hikakuByArtist() {
        return this.rate_hikakuByArtist;
    }
    public void setRate_hikakuByArtist(String rate_hikakuByArtist) {
        this.rate_hikakuByArtist = rate_hikakuByArtist;
    }
    public Integer getPlaycount_hikakuByArtist() {
        return this.playcount_hikakuByArtist;
    }
    public void setPlaycount_hikakuByArtist(Integer playcount_hikakuByArtist) {
        this.playcount_hikakuByArtist = playcount_hikakuByArtist;
    }
    public Integer getSabuncount_hikakuByArtist() {
        return this.sabuncount_hikakuByArtist;
    }
    public void setSabuncount_hikakuByArtist(Integer sabuncount_hikakuByArtist) {
        this.sabuncount_hikakuByArtist = sabuncount_hikakuByArtist;
    }
    public String getRankImage_hikakuByArtist() {
        return this.rankImage_hikakuByArtist;
    }
    public void setRankImage_hikakuByArtist(String rankImage_hikakuByArtist) {
        this.rankImage_hikakuByArtist = rankImage_hikakuByArtist;
    }
    public Integer getMotocount_hikakuBySong() {
        return this.motocount_hikakuBySong;
    }
    public void setMotocount_hikakuBySong(Integer motocount_hikakuBySong) {
        this.motocount_hikakuBySong = motocount_hikakuBySong;
    }
    public Integer getMotocount_hikakuByArtist() {
        return this.motocount_hikakuByArtist;
    }
    public void setMotocount_hikakuByArtist(Integer motocount_hikakuByArtist) {
        this.motocount_hikakuByArtist = motocount_hikakuByArtist;
    }
}
