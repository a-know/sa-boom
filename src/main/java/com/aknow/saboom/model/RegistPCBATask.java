package com.aknow.saboom.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Persistent;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.aknow.saboom.util.PlayCountByArtistForTask;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class RegistPCBATask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return this.key;
    }

    private String loginIDyyMMddHHmmssi;

    @Attribute(lob = true, unindexed = true)
    @Persistent(defaultFetchGroup = "true", serialized = "true")
    private ArrayList<PlayCountByArtistForTask> playCountByArtistList;

    @Persistent
    private String yyMMddHHmmss;

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

    public String getLoginIDyyMMddHHmmssi() {
        return this.loginIDyyMMddHHmmssi;
    }

    public void setLoginIDyyMMddHHmmssi(String loginIDyyMMddHHmmssi) {
        this.loginIDyyMMddHHmmssi = loginIDyyMMddHHmmssi;
    }

    public ArrayList<PlayCountByArtistForTask> getPlayCountByArtistList() {
        return this.playCountByArtistList;
    }

    public void setPlayCountByArtistList(
            ArrayList<PlayCountByArtistForTask> playCountByArtistList) {
        this.playCountByArtistList = playCountByArtistList;
    }

    public String getYyMMddHHmmss() {
        return this.yyMMddHHmmss;
    }

    public void setYyMMddHHmmss(String yyMMddHHmmss) {
        this.yyMMddHHmmss = yyMMddHHmmss;
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
        RegistPCBATask other = (RegistPCBATask) obj;
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
