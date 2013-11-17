package com.aknow.saboom.controller.graph;

import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.model.graph.ArtistRankingGraphData;
import com.aknow.saboom.model.graph.NormalPlayCountGraphByUser;
import com.aknow.saboom.model.graph.RandomArtistRankingGraphData;
import com.aknow.saboom.model.graph.SaboomPlayCountGraphByUser;
import com.aknow.saboom.model.graph.TotalPlayCountByUser;
import com.aknow.saboom.model.graph.UserRankingGraphData;
import com.aknow.saboom.service.graph.GetArtistRankingGraphService;
import com.aknow.saboom.service.graph.GetGraphDataForTotalPlayCountByUserService;
import com.aknow.saboom.service.graph.GetNormalPlayCountGraphDataByUserService;
import com.aknow.saboom.service.graph.GetRandomArtistRankingGraphService;
import com.aknow.saboom.service.graph.GetSaboomPlayCountGraphDataByUserService;
import com.aknow.saboom.service.graph.GetUserRankingGraphService;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.UtilityMethods;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class GetGraphDataController extends Controller {
	
	public static GetArtistRankingGraphService service = new GetArtistRankingGraphService();

    @SuppressWarnings("unchecked")
    @Override
    protected Navigation run() throws Exception {

        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String functionCode = this.request.getParameter("functionCode");
            String targetLoginID = this.request.getParameter("targetLoginID");


            if("1".equals(functionCode)){//ユーザー毎・総再生回数推移グラフ
                //まずはmamcacheからの読み込み
                List<TotalPlayCountByUser> graphDatas = Memcache.get(Consts.TotalPlayCountByUser_KEY + targetLoginID);

                if(graphDatas == null){
                    GetGraphDataForTotalPlayCountByUserService service = new GetGraphDataForTotalPlayCountByUserService();
                    graphDatas = service.getData(targetLoginID);
                    Memcache.put(Consts.TotalPlayCountByUser_KEY + targetLoginID, graphDatas);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("2".equals(functionCode)){//ユーザー毎・再生回数グラフ（通常）
                String normalViewValue = this.request.getParameter("normalViewValue");
                ArrayList<NormalPlayCountGraphByUser> graphDatas = Memcache.get(Consts.NormalPlayCountGraphDataByUser_KEY + targetLoginID + normalViewValue);
                if(graphDatas == null){
                    GetNormalPlayCountGraphDataByUserService service = new GetNormalPlayCountGraphDataByUserService();
                    graphDatas = service.getData(targetLoginID, normalViewValue);
                    graphDatas = service.getAmazonData(graphDatas);
                    Memcache.put(Consts.NormalPlayCountGraphDataByUser_KEY + targetLoginID + normalViewValue, graphDatas);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("3".equals(functionCode)){//ユーザー毎・再生回数グラフ（サブーン）
                String from = this.request.getParameter("from");
                String to = this.request.getParameter("to");
                ArrayList<SaboomPlayCountGraphByUser> graphDatas = Memcache.get(Consts.SaboomPlayCountGraphDataByUser_KEY + targetLoginID + from + to);
                if(graphDatas == null){
                    GetSaboomPlayCountGraphDataByUserService service = new GetSaboomPlayCountGraphDataByUserService();
                    graphDatas = service.getData(targetLoginID, from, to);
                    Memcache.put(graphDatas, Consts.SaboomPlayCountGraphDataByUser_KEY + targetLoginID + from + to);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("4".equals(functionCode)){//ランダムアーティストランキンググラフ
                String randomArtist = this.request.getParameter("randomArtist");
                //まずはmamcacheからの読み込み
                MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
                List<RandomArtistRankingGraphData> graphDatas = (List<RandomArtistRankingGraphData>) syncCache.get("RandomArtistRankingGraphData" + randomArtist);

                if(graphDatas == null){
                    GetRandomArtistRankingGraphService service = new GetRandomArtistRankingGraphService();
                    graphDatas = service.getData(randomArtist);
                    syncCache.put("RandomArtistRankingGraphData" + randomArtist, graphDatas);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("5".equals(functionCode)){//総再生回数ランキング（ユーザー別）グラフ
                //まずはmamcacheからの読み込み
                List<UserRankingGraphData> graphDatas = Memcache.get(Consts.UserRankingGraphData_KEY);

                if(graphDatas == null){
                    GetUserRankingGraphService service = new GetUserRankingGraphService();
                    graphDatas = service.getData();
                    Memcache.put(Consts.UserRankingGraphData_KEY, graphDatas);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("6".equals(functionCode)){//総再生回数ランキング（アーティスト別）グラフ
                //まずはmamcacheからの読み込み
                List<ArtistRankingGraphData> graphDatas = Memcache.get(Consts.ArtistRankingGraphData_KEY);

                //付随データのmemcache状況と同期を取る
                if(!Memcache.contains(Consts.Top10ArtistDataListOfTotalPlayCount_KEY) || !Memcache.contains(Consts.Top10ArtistPlayCountDataListOfTotalPlayCount_KEY) || !Memcache.contains(Consts.Top10ArtistUrlDataListOfTotalPlayCount_KEY) || !Memcache.contains(Consts.Top10ArtistImageUrlDataListOfTotalPlayCount_KEY)){
                    graphDatas = null;
                }

                if(graphDatas == null){
                    graphDatas = service.getData();
                    Memcache.put(Consts.ArtistRankingGraphData_KEY, graphDatas);
                }
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("7".equals(functionCode)){//総再生回数ランキング・最新月（アーティスト別）グラフ
            	List<ArtistRankingGraphData> graphDatas = service.getFirstData();
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("8".equals(functionCode)){//総再生回数ランキング・最新月（アーティスト別）グラフ
                //まずはmamcacheからの読み込み
                List<ArtistRankingGraphData> graphDatas = service.getSecondData();
                JSON.encode(graphDatas, this.response.getOutputStream());
            }else if("9".equals(functionCode)){//総再生回数ランキング・最新月（アーティスト別）グラフ
                //まずはmamcacheからの読み込み
                List<ArtistRankingGraphData> graphDatas = service.getThirdData();
                JSON.encode(graphDatas, this.response.getOutputStream());
            }
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
