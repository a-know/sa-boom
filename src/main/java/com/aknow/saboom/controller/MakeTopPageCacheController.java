package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.RankingDataForTopPage;
import com.aknow.saboom.model.TotalPlayCountByArtist;
import com.aknow.saboom.util.DatastoreCacheUtility;
import com.google.appengine.api.datastore.Key;

public class MakeTopPageCacheController extends Controller {

	private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));

	@Override
	public Navigation run() throws Exception {

		// // first
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

		RankingDataForTopPage e = getRankingDataByArtist(current_year, current_month, pre_year, pre_month);

		// put cache entity
		DatastoreCacheUtility.put(e, e.getKey());
		
		// // second
		current_month = Integer.valueOf(calendar.get(Calendar.MONTH)).toString();
		if("0".equals(current_month)){
			current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			current_month = "12";
		}else{
			current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
		}
		//差分元となる前月情報を特定
		pre_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 1).toString();
		if("0".equals(pre_month)){
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			pre_month = "12";
		}else if("-1".equals(pre_month)){
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			pre_month = "11";
		}else{
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
		}
		if(DatastoreCacheUtility.getOrNull(new RankingDataForTopPage(), RankingDataForTopPage.createKey(pre_year, pre_month, current_year, current_month)) == null){
			// データが存在しない（初めて集計対象月になった）ときだけ取得＆登録
			e = getRankingDataByArtist(current_year, current_month, pre_year, pre_month);
			// put cache entity
			DatastoreCacheUtility.put(e, e.getKey());
		}
		
		// // third
		current_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 1).toString();
		if("0".equals(current_month)){
			current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			current_month = "12";
		}else if("-1".equals(current_month)){
			current_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			current_month = "11";
		}else{
			current_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
		}
		//差分元となる前月情報を特定
		pre_month = Integer.valueOf(calendar.get(Calendar.MONTH) - 2).toString();
		if("0".equals(pre_month)){
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			pre_month = "12";
		}else if("-1".equals(pre_month)){
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			pre_month = "11";
		}else if("-2".equals(pre_month)){
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR) - 1).toString();
			pre_month = "10";
		}else{
			pre_year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();
		}
		if(DatastoreCacheUtility.getOrNull(new RankingDataForTopPage(), RankingDataForTopPage.createKey(pre_year, pre_month, current_year, current_month)) == null){
			// データが存在しない（初めて集計対象月になった）ときだけ取得＆登録
			e = getRankingDataByArtist(current_year, current_month, pre_year, pre_month);
			// put cache entity
			DatastoreCacheUtility.put(e, e.getKey());
		}

		return null;
	}

	private RankingDataForTopPage getRankingDataByArtist(String saki_year, String saki_month, String moto_year, String moto_month) throws Exception{

		List<TotalPlayCountByArtist> sorted_list = new ArrayList<TotalPlayCountByArtist>();

		TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
		List<Key> sakiKeyList = Datastore.query(meta).filter(meta.year.equal(saki_year), meta.month.equal(saki_month)).asKeyList();
		List<TotalPlayCountByArtist> saki_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), sakiKeyList);
		List<Key> motoKeyList = Datastore.query(meta).filter(meta.year.equal(moto_year), meta.month.equal(moto_month)).asKeyList();
		List<TotalPlayCountByArtist> moto_list = DatastoreCacheUtility.get(new TotalPlayCountByArtist(), motoKeyList);

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
		
		// cache entity
		RankingDataForTopPage e = new RankingDataForTopPage();
		e.setKey(RankingDataForTopPage.createKey(moto_year, moto_month, saki_year, saki_month));
		e.setSortedList(sorted_list);
		
		return e;
	}
}
