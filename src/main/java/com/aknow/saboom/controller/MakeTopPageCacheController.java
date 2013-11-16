package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.TotalPlayCountByArtist;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.DatastoreCacheUtility;
import com.google.appengine.api.datastore.Key;

public class MakeTopPageCacheController extends Controller {
	
    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));

    @Override
    public Navigation run() throws Exception {
    	

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
        
        getRankingDataByArtist(current_year, current_month, pre_year, pre_month);

        return null;
    }

    private void getRankingDataByArtist(String saki_year, String saki_month, String moto_year, String moto_month){

    	List<TotalPlayCountByArtist> sorted_list = Memcache.get(Consts.SabunArtistList_KEY + saki_year + saki_month + moto_year + moto_month);
    	
    	if(sorted_list == null){
    		TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
    		List<Key> sakiKeyList = Datastore.query(meta).filter(meta.year.equal(saki_year), meta.month.equal(saki_month)).asKeyList();
    		List<TotalPlayCountByArtist> saki_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), sakiKeyList);
    		List<Key> motoKeyList = Datastore.query(meta).filter(meta.year.equal(moto_year), meta.month.equal(moto_month)).asKeyList();
    		List<TotalPlayCountByArtist> moto_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), motoKeyList);
//    		List<TotalPlayCountByArtist> saki_list = Datastore.query(meta).filter(meta.year.equal(saki_year), meta.month.equal(saki_month)).asList();
//    		List<TotalPlayCountByArtist> moto_list = Datastore.query(meta).filter(meta.year.equal(moto_year), meta.month.equal(moto_month)).asList();

    		List<String> saki_artistList = new ArrayList<String>();
    		List<String> moto_artistList = new ArrayList<String>();

    		for(TotalPlayCountByArtist e1 : saki_list){
    			saki_artistList.add(e1.getArtistName());
    		}
    		for(TotalPlayCountByArtist e2 : moto_list){
    			moto_artistList.add(e2.getArtistName());
    		}

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
    	}
    }
}
