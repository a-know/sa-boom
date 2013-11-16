package com.aknow.saboom.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.meta.ActivityMeta;
import com.aknow.saboom.meta.AmazonApiDataByArtistMeta;
import com.aknow.saboom.meta.AmazonApiDataMeta;
import com.aknow.saboom.meta.ArtistNameMeta;
import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.AmazonApiData;
import com.aknow.saboom.model.AmazonApiDataByArtist;
import com.aknow.saboom.model.ArtistName;
import com.aknow.saboom.model.TotalPlayCountByArtist;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.AmazonHelper;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.DatastoreCacheUtility;
import com.aknow.saboom.util.FreshPub;
import com.google.appengine.api.datastore.Key;


public class IndexService {
	
    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
    private static final String separater = "@@##@@";
    private static final Logger logger = Logger.getLogger(IndexService.class.getName());

    @SuppressWarnings("static-method")
    public Future<List<Activity>> getActivity() {
        ActivityMeta meta = ActivityMeta.get();
        List<Key> keyList = Datastore.query(meta).filter(meta.viewable.equal(Boolean.TRUE)).sort(meta.activityDate.desc).limit(10).asKeyList();
        return (Future<List<Activity>>) Datastore.getAsync(Activity.class, keyList);
    }

    @SuppressWarnings("static-method")
    public String getRandomArtist(){
        ArtistNameMeta meta = new ArtistNameMeta();
        List<ArtistName> list = Datastore.query(meta).asList();

        int random = (int) Math.floor(Math.random() * list.size());

        return list.get(random).getArtistName();
    }

    @SuppressWarnings("static-method")
    public ArrayList<List<String>> getApiData(String artistName){
        List<String> imagesRandomArtist = new ArrayList<String>();
        List<String> urlRandomArtist = new ArrayList<String>();

        AmazonApiDataByArtistMeta meta = new AmazonApiDataByArtistMeta();

        List<AmazonApiDataByArtist> data = Datastore.query(meta).filter(meta.artistName.equal(artistName)).asList();

        for(AmazonApiDataByArtist e : data){
            imagesRandomArtist.add(e.getImageUrl());
            urlRandomArtist.add(e.getUrl());
        }

        ArrayList<List<String>> list = new ArrayList<List<String>>();
        list.add(urlRandomArtist);
        list.add(imagesRandomArtist);

        return list;
    }

    @SuppressWarnings("static-method")
    public ArrayList<List<String>> getDataFromApi(int limit, String artistName, List<String> urlList, List<String> imageUrlList){

        StringBuffer artistBuffer = new StringBuffer(separater);
        artistBuffer.append(artistName);
        artistBuffer.append(separater);

        int needCount = limit - urlList.size();//あと残りいくつの情報が必要か

        AmazonHelper helper = new AmazonHelper(artistBuffer.toString());
        List<FreshPub> freshPubs = null;

        try{
            freshPubs = helper.getFreshPubsForRandomRanking(needCount);
        }catch(Exception e){
            throw new RuntimeException(e);//TODO 回避手段
        }


        for(FreshPub e : freshPubs){
            urlList.add(e.getUrl());
            imageUrlList.add(e.getImageUrl());
            if(!e.getUrl().equals("")){
                //取得できたデータのdatastoreへの登録
                AmazonApiDataByArtist data = new AmazonApiDataByArtist();
                data.setArtistName(artistName);
                data.setUrl(e.getUrl());
                data.setImageUrl(e.getImageUrl());
                Datastore.put(data);
            }
        }

        ArrayList<List<String>> list = new ArrayList<List<String>>();
        list.add(urlList);
        list.add(imageUrlList);

        return list;
    }

    @SuppressWarnings("static-method")
    public Integer getUserCount(){
        UserMeta meta = new UserMeta();
        int count = Datastore.query(meta).count();

        return Integer.valueOf(count);
    }

    @SuppressWarnings("static-method")
    public List<String> getTop10ArtistDataList(){

        String current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH) + 1).toString();

        List<String> top10ArtistDataList = new ArrayList<String>();

        TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
        List<Key> keyList = Datastore.query(meta).filter(meta.year.equal(current_year), meta.month.equal(current_month)).sort(meta.totalPlayCount.desc).limit(10).asKeyList();
        List<TotalPlayCountByArtist> datas = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), keyList);

        for(TotalPlayCountByArtist e : datas){
            top10ArtistDataList.add(e.getArtistName());
        }

        return top10ArtistDataList;
    }

    @SuppressWarnings("static-method")
    public HashMap<String, Integer> getTotalPlayCountTop10Artist(List<String> top10ArtistDataList){

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();

        for(String e : top10ArtistDataList){
        	List<Key> keyList = Datastore.query(meta).filter(meta.artistName.equal(e)).sort(meta.year.desc, meta.month.desc).asKeyList();
            List<TotalPlayCountByArtist> datas = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), keyList);
            map.put(e, datas.get(0).getTotalPlayCount());
        }

        return map;
    }

    @SuppressWarnings("static-method")
    public ArrayList<HashMap<String, String>> getApiData(HashMap<String, Integer> totalPlayCountTop10Artist){
        HashMap<String, String> imagesTop10Artist = new HashMap<String, String>();
        HashMap<String, String> urlTop10Artist = new HashMap<String, String>();
        AmazonApiDataMeta meta = new AmazonApiDataMeta();


        for(Map.Entry<String, Integer> e : totalPlayCountTop10Artist.entrySet()){
        	List<AmazonApiData> data = Memcache.get(Consts.GetAmazonApiData_KEY + e.getKey());
        	if(data == null){
        		data = Datastore.query(meta).filter(meta.artistName.equal(e.getKey())).asList();
        		Memcache.put(Consts.GetAmazonApiData_KEY + e.getKey(), data);
        	}
            if(data.size() == 0){
                imagesTop10Artist.put(e.getKey(), null);
                urlTop10Artist.put(e.getKey(),null);
            }else{
                imagesTop10Artist.put(e.getKey(), data.get(0).getImageUrl() != null ? data.get(0).getImageUrl() : "");
                urlTop10Artist.put(e.getKey(),data.get(0).getUrl() != null ? data.get(0).getUrl() : "");
            }
        }

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        list.add(urlTop10Artist);
        list.add(imagesTop10Artist);

        return list;
    }

    @SuppressWarnings("static-method")
    public ArrayList<HashMap<String, String>> getDataFromApi(HashMap<String, String> urlMap, HashMap<String, String> imageUrlMap){
        //TODO 全く同じメソッドがUserViewServiceにもあり。共通化要
        StringBuffer artistBuffer = new StringBuffer(separater);

        boolean apiCallNeed = false;
        
        for(Map.Entry<String, String> e : urlMap.entrySet()) {
            if(e.getValue() == null){
                artistBuffer.append(e.getKey());
                artistBuffer.append(separater);
            	apiCallNeed = true;
            }
        }

        List<FreshPub> freshPubs = new ArrayList<>();
        if(apiCallNeed){
        	AmazonHelper helper = new AmazonHelper(artistBuffer.toString());
            try{
                freshPubs = helper.getFreshPubs();
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }



        for(FreshPub e : freshPubs){
            if(urlMap.get(e.getArtist()) == null){
                urlMap.put(e.getArtist(), e.getUrl());
                imageUrlMap.put(e.getArtist(), e.getImageUrl());
                //取得できたデータのdatastoreへの登録
                AmazonApiData data = new AmazonApiData();
                data.setArtistName(e.getArtist());
                data.setUrl(e.getUrl());
                data.setImageUrl(e.getImageUrl());
                Datastore.put(data);
            }
        }

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        list.add(urlMap);
        list.add(imageUrlMap);

        return list;
    }

    @SuppressWarnings("static-method")
    public Future<List<User>> userRankingOfUploadCount(){
        UserMeta meta = new UserMeta();
        List<Key> keyList = Datastore.query(meta).filter(meta.isPrivate.equal(Boolean.FALSE)).sort(meta.uploadCount.desc).limit(10).asKeyList();

        return Datastore.getAsync(User.class, keyList);
    }

    @SuppressWarnings("static-method")
    public Future<List<User>> userRankingOfAccessCount(){
        UserMeta meta = new UserMeta();
        List<Key> keyList = Datastore.query(meta).filter(meta.isPrivate.equal(Boolean.FALSE)).sort(meta.accessCount.desc).limit(10).asKeyList();

        return Datastore.getAsync(User.class, keyList);
    }

    @SuppressWarnings("static-method")
    public Future<List<User>> userRankingOfDiaryCount(){
        UserMeta meta = new UserMeta();
        List<Key> keyList = Datastore.query(meta).filter(meta.isPrivate.equal(Boolean.FALSE)).sort(meta.diaryCount.desc).limit(10).asKeyList();

        return Datastore.getAsync(User.class, keyList);
    }

    public List<Object> getRankingDataByArtistFirst(){
    	
    	logger.severe("@@@getRankingDataByArtistFirst start " + System.currentTimeMillis());

        String current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH) + 1).toString();

        //差分元となる前月情報を特定
        String pre_year = null;
        String pre_month = Integer.valueOf(calendar.get(Calendar.MONTH)).toString();
        if("0".equals(pre_month)){
            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
            pre_month = "12";
        }else{
            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
        }
    	logger.severe("@@@end dicide param " + System.currentTimeMillis());

        return getRankingDataByArtist(current_year, current_month, pre_year, pre_month);

    }

    private List<Object> getRankingDataByArtist(String saki_year, String saki_month, String moto_year, String moto_month){

    	List<TotalPlayCountByArtist> sorted_list = Memcache.get(Consts.SabunArtistList_KEY + saki_year + saki_month + moto_year + moto_month);
    	
    	if(sorted_list == null){
    		TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
//    		List<Key> sakiKeyList = Datastore.query(meta).filter(meta.year.equal(saki_year), meta.month.equal(saki_month)).asKeyList();
//    		List<TotalPlayCountByArtist> saki_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), sakiKeyList);
//    		List<Key> motoKeyList = Datastore.query(meta).filter(meta.year.equal(moto_year), meta.month.equal(moto_month)).asKeyList();
//    		List<TotalPlayCountByArtist> moto_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), motoKeyList);
    		List<TotalPlayCountByArtist> saki_list = Datastore.query(meta).filter(meta.year.equal(saki_year), meta.month.equal(saki_month)).asList();
    		List<TotalPlayCountByArtist> moto_list = Datastore.query(meta).filter(meta.year.equal(moto_year), meta.month.equal(moto_month)).asList();

    		List<String> saki_artistList = new ArrayList<String>();
    		List<String> moto_artistList = new ArrayList<String>();

    		for(TotalPlayCountByArtist e1 : saki_list){
    			saki_artistList.add(e1.getArtistName());
    		}
    		for(TotalPlayCountByArtist e2 : moto_list){
    			moto_artistList.add(e2.getArtistName());
    		}
    		logger.severe("@@@end query " + System.currentTimeMillis());

    		//差分取得
    		List<TotalPlayCountByArtist> sabun_list = new ArrayList<TotalPlayCountByArtist>();

    		for(TotalPlayCountByArtist e1 : saki_list){
    			TotalPlayCountByArtist tempElement = new TotalPlayCountByArtist();

    			if(moto_artistList.contains(e1.getArtistName())){
    				TotalPlayCountByArtist e2 = moto_list.get(moto_artistList.indexOf(e1.getArtistName()));
    				tempElement.setArtistName(e1.getArtistName());
    				tempElement.setYear(saki_year);
    				tempElement.setMonth(saki_month);
    				tempElement.setTotalPlayCount(Integer.valueOf(e1.getTotalPlayCount().intValue() - e2.getTotalPlayCount().intValue()));
    				sabun_list.add(tempElement);
    			}
    		}
    		logger.severe("@@@end sabun " + System.currentTimeMillis());

    		//再生回数でソート
    		sorted_list = new ArrayList<TotalPlayCountByArtist>();

    		while(sorted_list.size() < 10){

    			int max = 0;
    			int maxIndex = 0;
    			TotalPlayCountByArtist tempElement = null;

    			for(int i = 0; i < sabun_list.size(); i++){
    				TotalPlayCountByArtist e = sabun_list.get(i);
    				if(e.getTotalPlayCount().intValue() > max){
    					tempElement = new TotalPlayCountByArtist();
    					tempElement.setArtistName(e.getArtistName());
    					tempElement.setTotalPlayCount(e.getTotalPlayCount());
    					max = e.getTotalPlayCount().intValue();
    					maxIndex = i;
    				}
    			}

    			sorted_list.add(tempElement);
    			sabun_list.remove(maxIndex);
    		}
    		Memcache.put(Consts.SabunArtistList_KEY + saki_year + saki_month + moto_year + moto_month, sorted_list);
    		logger.severe("@@@end sort " + System.currentTimeMillis());
    	}

        //出力情報の生成
        List<String> artistNameList = new ArrayList<String>();
        HashMap<String, Integer> dataMap = new HashMap<String, Integer>();
        for(TotalPlayCountByArtist e : sorted_list){
            artistNameList.add(e.getArtistName());
            dataMap.put(e.getArtistName(), e.getTotalPlayCount());
        }
        //imageURL,AmazonURLを、まずはdatastoreから取得。なければAPIで取得
        ArrayList<HashMap<String, String>> datastoreData = getApiData(dataMap);
        ArrayList<HashMap<String, String>> apiData = getDataFromApi(datastoreData.get(0), datastoreData.get(1));

        HashMap<String, String> urlList = apiData.get(0);
        HashMap<String, String> imageList = apiData.get(1);

        List<Object> returnList = new ArrayList<Object>();
        returnList.add(saki_year);
        returnList.add(saki_month);
        returnList.add(artistNameList);
        returnList.add(dataMap);
        returnList.add(imageList);
        returnList.add(urlList);
    	logger.severe("@@@end output " + System.currentTimeMillis());

        return returnList;
    }

//    public List<Object> getRankingDataByArtistSecond(){
//
//        String current_year = null;
//        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH)).toString();
//        if("0".equals(current_month)){
//            current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            current_month = "12";
//        }else{
//            current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
//        }
//
//        //差分元となる前月情報を特定
//        String pre_year = null;
//        String pre_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 1).toString();
//        if("0".equals(pre_month)){
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            pre_month = "12";
//        }else{
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
//        }
//
//        return getRankingDataByArtist(current_year, current_month, pre_year, pre_month);
//
//    }
//
//    public List<Object> getRankingDataByArtistThird(){
//
//        String current_year = null;
//        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 1).toString();
//        if("0".equals(current_month)){
//            current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            current_month = "12";
//        }else if("-1".equals(current_month)){
//            current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            current_month = "11";
//        }else{
//            current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
//        }
//
//        //差分元となる前月情報を特定
//        String pre_year = null;
//        String pre_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 2).toString();
//        if("0".equals(pre_month)){
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            pre_month = "12";
//        }else if("-1".equals(pre_month)){
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            pre_month = "11";
//        }else if("-2".equals(pre_month)){
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
//            pre_month = "10";
//        }else{
//            pre_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
//        }
//
//        return getRankingDataByArtist(current_year, current_month, pre_year, pre_month);
//
//    }
}
