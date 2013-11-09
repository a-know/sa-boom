package cdit;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PlayCountByArtist implements java.io.Serializable{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String loginIDArtistName;

    @Persistent
    private String artistName;

    @Persistent
    private String loginID;

    @Persistent
    private Integer playCount;

    @Persistent
    private String yyMMddHHmmss;

    public PlayCountByArtist(String loginIDArtistName, String artistName, String loginID, Integer playCount, String yyMMddHHmmss){
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