package com.aknow.saboom.model.graph;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ArtistRankingGraphData implements Serializable {

    String artistName;
    String artistNameWithRank;
    Integer playcount;


    public String getArtistName() {
        return this.artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public String getArtistNameWithRank() {
        return this.artistNameWithRank;
    }
    public void setArtistNameWithRank(String artistNameWithRank) {
        this.artistNameWithRank = artistNameWithRank;
    }
    public Integer getPlaycount() {
        return this.playcount;
    }
    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
    }


}
