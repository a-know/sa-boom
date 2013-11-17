package com.aknow.saboom.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.PlayCountByArtist;
import com.aknow.saboom.model.TotalPlayCountByArtist;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.DatastoreCacheUtility;


public class CalcTotalPlayCountByArtistService {
    @SuppressWarnings("static-method")
    public void calc(){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        String current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
        String current_month = Integer.valueOf(calendar.get(Calendar.MONTH) + 1).toString();

        PlayCountByArtistMeta pcbaMeta = new PlayCountByArtistMeta();
        List<PlayCountByArtist> pcbaList = Datastore.query(pcbaMeta).asList();

        Map<String, Integer> totalPlayCountByArtistMap = new HashMap<String, Integer>();

        for(PlayCountByArtist e : pcbaList){
            //client 3.0.1-3.0.2のバグ対応-start
            if(e.getArtistName().contains("|")){//本来ふさわしくない情報をPCBAListとして渡されていたら、集計無視（「さざなみCD|スピッツ」がアーティスト名として集計されているバグ）
                //no operation
            }
            //client 3.0.1-3.0.2のバグ対応-end
            else if(totalPlayCountByArtistMap.containsKey(e.getArtistName())){
                Integer currentCount = totalPlayCountByArtistMap.get(e.getArtistName());
                currentCount = Integer.valueOf(currentCount.intValue() + e.getPlayCount().intValue());
                totalPlayCountByArtistMap.put(e.getArtistName(), currentCount);
            }else{
                totalPlayCountByArtistMap.put(e.getArtistName(), e.getPlayCount());
            }
        }


        TotalPlayCountByArtistMeta tmeta = new TotalPlayCountByArtistMeta();

        for(Map.Entry<String, Integer> m : totalPlayCountByArtistMap.entrySet()){

            TotalPlayCountByArtist model = Datastore.query(tmeta).filter(tmeta.year.equal(current_year), tmeta.month.equal(current_month), tmeta.artistName.equal(m.getKey())).asSingle();

            if(model == null){
                model = new TotalPlayCountByArtist();
                model.setYear(current_year);
                model.setMonth(current_month);
                model.setArtistName(m.getKey());
                model.setTotalPlayCount(m.getValue());
            }else{
                model.setTotalPlayCount(m.getValue());
            }

            DatastoreCacheUtility.put(model, model.getKey());
        }

        //delete memcache
        Memcache.delete(Consts.ArtistRankingGraphData_KEY);

        Memcache.delete(Consts.Top10ArtistDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistPlayCountDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistUrlDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistImageUrlDataListOfTotalPlayCount_KEY);

        Memcache.delete(Consts.ArtistRankingData_first_KEY);
        Memcache.delete(Consts.ArtistRankingData_second_KEY);
        Memcache.delete(Consts.ArtistRankingData_third_KEY);
    }
}
