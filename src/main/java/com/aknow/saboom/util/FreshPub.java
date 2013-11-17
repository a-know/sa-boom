package com.aknow.saboom.util;

/**
 * FreshPub
 * 新譜情報クラス
 *
 * @author a-know
 *
 */
public class FreshPub {

	private String artist;
	private String url;
	private String imageUrl;

	/**
	 * FreshPub コンストラクタ
	 * @param title
	 * @param artist
	 * @param url
	 * @param salesDate
	 * @param imageUrl
	 */
	public FreshPub(String artist, String url, String imageUrl){
		this.artist = artist;
		this.url = url;
		this.imageUrl = imageUrl;
	}

	/**
	 *
	 * @return artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 *
	 * @param artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 *
	 * @return url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 *
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 *
	 * @return imageUrl
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}

	/**
	 *
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
