package cdit;

public class ElementForWeb implements java.io.Serializable {
    private String trackId = null;
    private String songName = "No Name Song";
    private String artistName = "No Name Artist";
    private String albumName = "No Name Album";
    private Integer playCount = new Integer("0");
    private Integer rating = new Integer("0");

    public String getAlbumName() {
        return this.albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public String getArtistName() {
        return this.artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getPlayCount() {
        return this.playCount;
    }
    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }
    public Integer getRating() {
        return this.rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getSongName() {
        return this.songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public String getTrackId() {
        return this.trackId;
    }
    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

}
