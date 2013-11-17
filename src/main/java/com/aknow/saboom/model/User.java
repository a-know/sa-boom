package com.aknow.saboom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.Sort;

import com.aknow.saboom.meta.PlayCountDataMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    @Attribute(persistent = false)
    private InverseModelListRef<PlayCountData, User> playCountDataRefs =
        new InverseModelListRef<PlayCountData, User>(PlayCountData.class, PlayCountDataMeta.get().userRef.getName(),
        this, new Sort(PlayCountDataMeta.get().yyMMddHHmmss.getName(), SortDirection.ASCENDING));

    private String loginId;

    @Attribute(unindexed = true)
    private String pass;

    private Boolean isPrivate;

    @Attribute(unindexed = true)
    private Date firstRegistDate;

    @Attribute(unindexed = true)
    private ArrayList<String> registedDataLabel;

    @Attribute(unindexed = true)
    private Date lastUploadDate;

    @Attribute(unindexed = true)
    private String lastUploadDataYyMMddHHmmss;

    private Integer totalPlayCount;

    @Attribute(unindexed = true)
    private ArrayList<String> top10ArtistDataList;

    private Integer uploadCount;

    private Integer accessCount;

    private Integer diaryCount;

    @Attribute(unindexed = true, lob = true)
    private String introduction;

    @Attribute(unindexed = true)
    private String url;

    @Attribute(unindexed = true)
    private Boolean isTweetOption1;

    @Attribute(unindexed = true)
    private Boolean isTweetOption2;

    @Attribute(unindexed = true)
    private Boolean isTweetOption3;

    @Attribute(unindexed = true)
    private Boolean isTweetOption4;

    @Attribute(unindexed = true)
    private Boolean isTweetOption5;

    @Attribute(unindexed = true)
    private String twitterAccessToken;

    @Attribute(unindexed = true)
    private String twitterAccessTokenSecret;

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return this.version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    public InverseModelListRef<PlayCountData, User> getPlayCountDataRefs() {
        return this.playCountDataRefs;
    }

    /**
     * Returns the loginID.
     *
     * @return the loginID
     */
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * Sets the loginID.
     *
     * @param loginID
     *            the loginID
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Returns the pass(hashed).
     *
     * @return the pass(hashed)
     */
    public String getPass() {
        return this.pass;
    }

    /**
     * Sets the pass(hashed).
     *
     * @param pass(hashed)
     *            the pass(hashed)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Returns the isPrivate.
     *
     * @return the isPrivate
     */
    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    /**
     * Sets the isPrivate.
     *
     * @param isPrivate
     *            the isPrivate
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * Returns the firstRegistDate.
     *
     * @return the firstRegistDate
     */
    public Date getFirstRegistDate() {
        return this.firstRegistDate;
    }

    /**
     * Sets the firstRegistDate.
     *
     * @param firstRegistDate
     *            the firstRegistDate
     */
    public void setFirstRegistDate(Date firstRegistDate) {
        this.firstRegistDate = firstRegistDate;
    }

    public Date getLastUploadDate() {
        return this.lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public String getLastUploadDataYyMMddHHmmss() {
        return this.lastUploadDataYyMMddHHmmss;
    }

    public void setLastUploadDataYyMMddHHmmss(String lastUploadDataYyMMddHHmmss) {
        this.lastUploadDataYyMMddHHmmss = lastUploadDataYyMMddHHmmss;
    }

    public Integer getTotalPlayCount() {
        return this.totalPlayCount;
    }

    public void setTotalPlayCount(Integer totalPlayCount) {
        this.totalPlayCount = totalPlayCount;
    }

    public ArrayList<String> getTop10ArtistDataList() {
        return this.top10ArtistDataList;
    }

    public void setTop10ArtistDataList(ArrayList<String> top10ArtistDataList) {
        this.top10ArtistDataList = top10ArtistDataList;
    }

    public Integer getUploadCount() {
        return this.uploadCount;
    }

    public void setUploadCount(Integer uploadCount) {
        this.uploadCount = uploadCount;
    }

    public Integer getAccessCount() {
        return this.accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    public Integer getDiaryCount() {
        return this.diaryCount;
    }

    public void setDiaryCount(Integer diaryCount) {
        this.diaryCount = diaryCount;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsTweetOption1() {
        return this.isTweetOption1;
    }

    public void setIsTweetOption1(Boolean isTweetOption1) {
        this.isTweetOption1 = isTweetOption1;
    }

    public Boolean getIsTweetOption2() {
        return this.isTweetOption2;
    }

    public void setIsTweetOption2(Boolean isTweetOption2) {
        this.isTweetOption2 = isTweetOption2;
    }

    public Boolean getIsTweetOption3() {
        return this.isTweetOption3;
    }

    public void setIsTweetOption3(Boolean isTweetOption3) {
        this.isTweetOption3 = isTweetOption3;
    }

    public Boolean getIsTweetOption4() {
        return this.isTweetOption4;
    }

    public void setIsTweetOption4(Boolean isTweetOption4) {
        this.isTweetOption4 = isTweetOption4;
    }

    public Boolean getIsTweetOption5() {
        return this.isTweetOption5;
    }

    public void setIsTweetOption5(Boolean isTweetOption5) {
        this.isTweetOption5 = isTweetOption5;
    }

    public String getTwitterAccessToken() {
        return this.twitterAccessToken;
    }

    public void setTwitterAccessToken(String twitterAccessToken) {
        this.twitterAccessToken = twitterAccessToken;
    }

    public String getTwitterAccessTokenSecret() {
        return this.twitterAccessTokenSecret;
    }

    public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
        this.twitterAccessTokenSecret = twitterAccessTokenSecret;
    }

    public ArrayList<String> getRegistedDataLabel() {
        return this.registedDataLabel;
    }

    public void setRegistedDataLabel(ArrayList<String> registedDataLabel) {
        this.registedDataLabel = registedDataLabel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!this.key.equals(other.key)) {
            return false;
        }
        return true;
    }
}
