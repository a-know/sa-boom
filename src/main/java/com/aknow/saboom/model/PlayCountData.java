package com.aknow.saboom.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class PlayCountData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<User> userRef = new ModelRef<User>(User.class);

    private String loginId;
    private String yyMMddHHmmss;

    @Attribute(unindexed = true)
    private String infoName;

    @Attribute(unindexed = true)
    private Integer totalPlayCount;

    @Attribute(unindexed = true)
    private BlobKey blobListKey;

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

    public ModelRef<User> getUserRef() {
        return this.userRef;
    }

    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getYyMMddHHmmss() {
        return this.yyMMddHHmmss;
    }

    public void setYyMMddHHmmss(String yyMMddHHmmss) {
        this.yyMMddHHmmss = yyMMddHHmmss;
    }

    public String getInfoName() {
        return this.infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public Integer getTotalPlayCount() {
        return this.totalPlayCount;
    }

    public void setTotalPlayCount(Integer totalPlayCount) {
        this.totalPlayCount = totalPlayCount;
    }

    public BlobKey getBlobListKey() {
        return this.blobListKey;
    }

    public void setBlobListKey(BlobKey blobListKey) {
        this.blobListKey = blobListKey;
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
        PlayCountData other = (PlayCountData) obj;
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
