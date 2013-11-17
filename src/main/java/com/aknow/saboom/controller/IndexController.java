package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.User;
import com.aknow.saboom.service.IndexService;
import com.aknow.saboom.util.Consts;

public class IndexController extends Controller {

    @SuppressWarnings("unchecked")
    @Override
    public Navigation run() throws Exception {
        try{
            IndexService service = new IndexService();

            /*新着アクティビティ一覧を非同期で取得*/
            Future<List<Activity>> activityFuture = service.getActivity();
            
            /*アップロード回数順ユーザーリストの非同期取得*/
            List<User> userRankingOfUploadCountList = Memcache.get(Consts.UserRankingOfUploadCount_KEY);
            Future<List<User>> userRankingOfUploadFuture = null;
            if(userRankingOfUploadCountList == null){
            	userRankingOfUploadFuture = service.userRankingOfUploadCount();
            }
            /*アクセス回数順ユーザーリストの非同期取得*/
            Future<List<User>> userRankingOfAccessCountFuture = service.userRankingOfAccessCount();
            /*日記登録数順ユーザーリストの非同期取得*/
            List<User> userRankingOfDiaryCountList = Memcache.get(Consts.UserRankingOfDiaryCount_KEY);
            Future<List<User>> userRankingOfDiaryCountFuture = null;
            if(userRankingOfDiaryCountList == null){
            	userRankingOfDiaryCountFuture = service.userRankingOfDiaryCount();
            }

            /*ランダムアーティストランキングのアーティストを決定*/
            //String artistName = service.getRandomArtist();
            //requestScope("randomArtist", artistName);
            //ランダムアーティストに関するimageURL,AmazonURLを、まずはdatastoreから取得。なければAPIで取得
            //ArrayList<List<String>> datastoreData = service.getApiData(artistName);
            //ArrayList<List<String>> apiData = service.getDataFromApi(5, artistName, datastoreData.get(0), datastoreData.get(1));
            //List<String> urlRandomArtist = apiData.get(0);
            //List<String> imagesRandomArtist = apiData.get(1);
            //requestScope("urlRandomArtist", urlRandomArtist);
            //requestScope("imagesRandomArtist", imagesRandomArtist);


            /*総再生回数ランキング＜アーティスト＞TOP10に関連する処理*/
            //まずmemcacheからの取得を試みる。
            List<String> top10ArtistDataList = (List<String>) Memcache.get(Consts.Top10ArtistDataListOfTotalPlayCount_KEY);
            HashMap<String, Integer> totalPlayCountTop10Artist = (HashMap<String, Integer>) Memcache.get(Consts.Top10ArtistPlayCountDataListOfTotalPlayCount_KEY);
            HashMap<String, String> urlTop10Artist = (HashMap<String, String>) Memcache.get(Consts.Top10ArtistUrlDataListOfTotalPlayCount_KEY);
            HashMap<String, String> imagesTop10Artist = (HashMap<String, String>) Memcache.get(Consts.Top10ArtistImageUrlDataListOfTotalPlayCount_KEY);

            //グラフデータのmemcache取得状況と同期を取る
            if(!Memcache.contains(Consts.ArtistRankingGraphData_KEY)){
                top10ArtistDataList = null;
                totalPlayCountTop10Artist = null;
                urlTop10Artist = null;
                imagesTop10Artist = null;
            }


            if(top10ArtistDataList == null || totalPlayCountTop10Artist == null || urlTop10Artist == null || imagesTop10Artist == null){

                top10ArtistDataList = service.getTop10ArtistDataList();
                totalPlayCountTop10Artist = service.getTotalPlayCountTop10Artist(top10ArtistDataList);
                //imageURL,AmazonURLを、まずはdatastoreから取得。なければAPIで取得
                ArrayList<HashMap<String, String>> datastoreDataTop10Artist = service.getApiData(totalPlayCountTop10Artist);
                ArrayList<HashMap<String, String>> apiDataTop10Artist = service.getDataFromApi(datastoreDataTop10Artist.get(0), datastoreDataTop10Artist.get(1));

                urlTop10Artist = apiDataTop10Artist.get(0);
                imagesTop10Artist = apiDataTop10Artist.get(1);

                Memcache.put(Consts.Top10ArtistDataListOfTotalPlayCount_KEY, top10ArtistDataList);
                Memcache.put(Consts.Top10ArtistPlayCountDataListOfTotalPlayCount_KEY, totalPlayCountTop10Artist);
                Memcache.put(Consts.Top10ArtistUrlDataListOfTotalPlayCount_KEY, urlTop10Artist);
                Memcache.put(Consts.Top10ArtistImageUrlDataListOfTotalPlayCount_KEY, imagesTop10Artist);
            }

            requestScope("top10ArtistDataList", top10ArtistDataList);
            requestScope("playCountTop10Artist", totalPlayCountTop10Artist);
            requestScope("imagesTop10Artist", imagesTop10Artist);
            requestScope("urlTop10Artist", urlTop10Artist);


            /*アーティスト別再生回数ランキング・差分情報取得*/
            //first
            List<Object> rankingList_first = service.getRankingDataByArtistFirst();
            requestScope("year_first", rankingList_first.get(0));
            requestScope("month_first", rankingList_first.get(1));
            requestScope("top10ArtistDataList_first", rankingList_first.get(2));
            requestScope("playCountTop10Artist_first", rankingList_first.get(3));
            requestScope("imagesTop10Artist_first", rankingList_first.get(4));
            requestScope("urlTop10Artist_first", rankingList_first.get(5));


//            //second
//            List<Object> rankingList_second = (List<Object>) Memcache.get(Consts.ArtistRankingData_second_KEY);
//
//
//            //グラフデータと同期を取る
//            if(!Memcache.contains(Consts.ArtistRankingGraphData_second_KEY)){
//                rankingList_second = null;
//            }
//
//            if(rankingList_second == null) {
//                rankingList_second = service.getRankingDataByArtistSecond();
//                Memcache.put(Consts.ArtistRankingData_second_KEY, rankingList_second);
//            }
//            requestScope("year_second", rankingList_second.get(0));
//            requestScope("month_second", rankingList_second.get(1));
//            requestScope("top10ArtistDataList_second", rankingList_second.get(2));
//            requestScope("playCountTop10Artist_second", rankingList_second.get(3));
//            requestScope("imagesTop10Artist_second", rankingList_second.get(4));
//            requestScope("urlTop10Artist_second", rankingList_second.get(5));

//            //third
//            List<Object> rankingList_third = (List<Object>) Memcache.get(Consts.ArtistRankingData_third_KEY);
//
//
//            //グラフデータと同期を取る
//            if(!Memcache.contains(Consts.ArtistRankingGraphData_third_KEY)){
//                rankingList_third = null;
//            }
//
//            if(rankingList_third == null) {
//                rankingList_third = service.getRankingDataByArtistThird();
//                Memcache.put(Consts.ArtistRankingData_third_KEY, rankingList_third);
//            }
//            requestScope("year_third", rankingList_third.get(0));
//            requestScope("month_third", rankingList_third.get(1));
//            requestScope("top10ArtistDataList_third", rankingList_third.get(2));
//            requestScope("playCountTop10Artist_third", rankingList_third.get(3));
//            requestScope("imagesTop10Artist_third", rankingList_third.get(4));
//            requestScope("urlTop10Artist_third", rankingList_third.get(5));





            


            /*登録済みユーザー数の取得*/
            Integer userCount = Memcache.get(Consts.TotalUserCount_KEY);
            if(userCount == null){
                userCount = service.getUserCount();
            }
            requestScope("userCount", userCount);
            
            // // 各エンティティを非同期で取得
            // activity
            requestScope("activityList", activityFuture.get());
            // user
            if(userRankingOfUploadCountList == null){
            	userRankingOfUploadCountList = userRankingOfUploadFuture.get();
                Memcache.put(Consts.UserRankingOfUploadCount_KEY, userRankingOfUploadCountList);
            }
            requestScope("userRankingOfUploadCount", userRankingOfUploadCountList);
            requestScope("userRankingOfAccessCount", userRankingOfAccessCountFuture.get());
            // 日記登録数順ユーザーリストの取得
            if(userRankingOfDiaryCountList == null){
            	userRankingOfDiaryCountList = userRankingOfDiaryCountFuture.get();
                Memcache.put(Consts.UserRankingOfDiaryCount_KEY, userRankingOfDiaryCountList);
            }
            requestScope("userRankingOfDiaryCount", userRankingOfDiaryCountList);
            
        }catch(Exception e){
            e.printStackTrace();

            throw e;
        }


        return forward("index.jsp");
    }
}
