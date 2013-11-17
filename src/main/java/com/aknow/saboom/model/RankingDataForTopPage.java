package com.aknow.saboom.model;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class RankingDataForTopPage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Key createKey(String fromYear, String fromMonth, String toYear, String toMonth){
    	return Datastore.createKey(RankingDataForTopPage.class, fromYear + fromMonth + toYear + toMonth);
    }

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    @Attribute(unindexed = true, lob = true)
    private List<TotalPlayCountByArtist> sortedList;

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
    

    public List<TotalPlayCountByArtist> getSortedList() {
		return sortedList;
	}

	public void setSortedList(List<TotalPlayCountByArtist> sortedList) {
		this.sortedList = sortedList;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        return result;
    }
}
