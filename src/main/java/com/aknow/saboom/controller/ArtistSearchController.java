package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.model.User;
import com.aknow.saboom.service.ArtistSearchService;
import com.aknow.saboom.service.IndexService;
import com.aknow.saboom.service.UserViewService;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.UtilityMethods;

public class ArtistSearchController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            String artistName = (String) this.request.getAttribute("artistName");
            if(artistName == null || "".equals(artistName)){
                artistName = "　";
            }
            requestScope("artistName", artistName);

            /*指定されたアーティストを聴いているユーザーを再生回数順のリストとして取得*/
            ArtistSearchService service = new ArtistSearchService();
            List<Object> returnList = Memcache.get(Consts.UserListByArtist_KEY);
            if(returnList == null){
                returnList = service.getUserListByArtist(artistName);
            }
            requestScope("userList", returnList.get(0));
            requestScope("countList", returnList.get(1));


            /*上位3位のユーザーに関しては、よく聞くアーティスト上位10位のアートワークを表示。*/
            int index = 0;
            UserViewService userViewService = new UserViewService();
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) returnList.get(0);
            for(User user : userList){
                HashMap<String, Integer> playCountTop10Artist = Memcache.get(Consts.PlayCountTop10ArtistByUser_KEY + user.getLoginId());
                ArrayList<LinkedHashMap<String, String>> datastoreData = Memcache.get(Consts.ApiDataByUser_KEY + user.getLoginId());
                ArrayList<LinkedHashMap<String, String>> apiData = Memcache.get(Consts.DataFromApiByUser_KEY + user.getLoginId());

                if(playCountTop10Artist == null || datastoreData == null || apiData == null){
                    playCountTop10Artist = userViewService.getPlayCountTop10Artist(user);
                    datastoreData = userViewService.getApiData(user);
                    apiData = userViewService.getDataFromApi(datastoreData.get(0), datastoreData.get(1));
                }

                HashMap<String, String> urlTop10Artist = apiData.get(0);
                HashMap<String, String> imagesTop10Artist = apiData.get(1);

                requestScope("playCountTop10Artist" + index, playCountTop10Artist);
                requestScope("imagesTop10Artist" + index, imagesTop10Artist);
                requestScope("urlTop10Artist" + index, urlTop10Artist);

                index++;

                if(index > 2) break;
            }


            /*ページ下部に表示するアートワークの取得*/
            IndexService indexService = new IndexService();
            ArrayList<List<String>> datastoreData = Memcache.get(Consts.ApiDataByArtist + artistName);
            ArrayList<List<String>> apiData = Memcache.get(Consts.DataFromApiByArtist + artistName);

            if(datastoreData == null || apiData == null){
                datastoreData = indexService.getApiData(artistName);
                apiData = indexService.getDataFromApi(9, artistName, datastoreData.get(0), datastoreData.get(1));
            }

            List<String> urlRandomArtist = apiData.get(0);
            List<String> imagesRandomArtist = apiData.get(1);

            requestScope("urlThisArtist", urlRandomArtist);
            requestScope("imagesThisArtist", imagesRandomArtist);
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return forward("/artistSearch.jsp");

    }
}
