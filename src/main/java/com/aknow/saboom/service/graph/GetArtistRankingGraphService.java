package com.aknow.saboom.service.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.TotalPlayCountByArtist;
import com.aknow.saboom.model.graph.ArtistRankingGraphData;
import com.aknow.saboom.service.IndexService;


public class GetArtistRankingGraphService {
    @SuppressWarnings("static-method")
    public List<ArtistRankingGraphData> getData(){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        String current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH) + 1).toString();

        List<ArtistRankingGraphData> returnList = new ArrayList<ArtistRankingGraphData>();

        TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
        List<TotalPlayCountByArtist> datas = Datastore.query(meta).filter(meta.year.equal(current_year),meta.month.equal(current_month)).sort(meta.totalPlayCount.desc).limit(10).asList();

        for(TotalPlayCountByArtist e : datas){
            ArtistRankingGraphData data = new ArtistRankingGraphData();
            int rank = returnList.size() + 1;
            data.setArtistNameWithRank(rank + "位：" + e.getArtistName());
            data.setArtistName(e.getArtistName());
            data.setPlaycount(e.getTotalPlayCount());
            returnList.add(data);
        }

        return returnList;
    }
    @SuppressWarnings({ "unchecked", "static-method" })
    public List<ArtistRankingGraphData> getFirstData(){

        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        List<Object> rankingList_first = (List<Object>) syncCache.get("rankingData_first");
        if(rankingList_first == null){
            IndexService service = new IndexService();
            rankingList_first = service.getRankingDataByArtistFirst();
        }
        List<String> artistNameList = (List<String>) rankingList_first.get(2);
        HashMap<String, Integer> dataMap = (HashMap<String, Integer>) rankingList_first.get(3);
        List<ArtistRankingGraphData> returnList = new ArrayList<ArtistRankingGraphData>();

        for(String name : artistNameList){
            ArtistRankingGraphData data = new ArtistRankingGraphData();
            int rank = returnList.size() + 1;
            data.setArtistNameWithRank(rank + "位：" + name);
            data.setArtistName(name);
            data.setPlaycount(dataMap.get(name));
            returnList.add(data);
        }

        return returnList;
    }
    @SuppressWarnings({ "unchecked", "static-method" })
    public List<ArtistRankingGraphData> getSecondData(){

        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        List<Object> rankingList_second = (List<Object>) syncCache.get("rankingData_second");
        if(rankingList_second == null){
            IndexService service = new IndexService();
            rankingList_second = service.getRankingDataByArtistSecond();
        }
        List<String> artistNameList = (List<String>) rankingList_second.get(2);
        HashMap<String, Integer> dataMap = (HashMap<String, Integer>) rankingList_second.get(3);
        List<ArtistRankingGraphData> returnList = new ArrayList<ArtistRankingGraphData>();

        for(String name : artistNameList){
            ArtistRankingGraphData data = new ArtistRankingGraphData();
            int rank = returnList.size() + 1;
            data.setArtistNameWithRank(rank + "位：" + name);
            data.setArtistName(name);
            data.setPlaycount(dataMap.get(name));
            returnList.add(data);
        }

        return returnList;
    }
    @SuppressWarnings({ "unchecked", "static-method" })
    public List<ArtistRankingGraphData> getThirdData(){

        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        List<Object> rankingList_third = (List<Object>) syncCache.get("rankingData_third");
        if(rankingList_third == null){
            IndexService service = new IndexService();
            rankingList_third = service.getRankingDataByArtistThird();
        }
        List<String> artistNameList = (List<String>) rankingList_third.get(2);
        HashMap<String, Integer> dataMap = (HashMap<String, Integer>) rankingList_third.get(3);
        List<ArtistRankingGraphData> returnList = new ArrayList<ArtistRankingGraphData>();

        for(String name : artistNameList){
            ArtistRankingGraphData data = new ArtistRankingGraphData();
            int rank = returnList.size() + 1;
            data.setArtistNameWithRank(rank + "位：" + name);
            data.setArtistName(name);
            data.setPlaycount(dataMap.get(name));
            returnList.add(data);
        }

        return returnList;
    }
}
