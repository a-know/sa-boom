package com.aknow.saboom.model.graph;

import java.io.Serializable;


public class NormalPlayCountGraphByUser implements Serializable{
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

    String url_byArtist;
    String imageUrl_byArtist;





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
    public String getUrl_byArtist() {
        return this.url_byArtist;
    }
    public void setUrl_byArtist(String url_byArtist) {
        this.url_byArtist = url_byArtist;
    }
    public String getImageUrl_byArtist() {
        return this.imageUrl_byArtist;
    }
    public void setImageUrl_byArtist(String imageUrl_byArtist) {
        this.imageUrl_byArtist = imageUrl_byArtist;
    }
}
